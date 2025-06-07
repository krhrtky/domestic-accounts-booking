# セキュリティ要件定義

このドキュメントでは、家計簿システムの包括的なセキュリティ要件を定義します。

## 概要

家計簿システムは個人の金融情報を扱うため、高いセキュリティレベルが求められます。本仕様では、データ保護、通信セキュリティ、認証・認可、および各種攻撃対策を定義します。

## 1. データ暗号化要件

### 1.1 個人情報の暗号化

**対象データ:**
- ユーザーのメールアドレス
- 金額情報
- 取引先情報
- メモ・説明文（個人情報が含まれる可能性）

**暗号化方式:**
- **データベース内暗号化**: AES-256-GCM
- **パスワード**: bcrypt (cost factor: 12)
- **機密性の高いデータ**: Spring Security Crypto による透過的暗号化

**キー管理:**
- 環境変数による暗号化キー管理
- 本番環境: AWS Secrets Manager / HashiCorp Vault の使用を推奨
- 開発環境: `.env` ファイル（Git管理外）

### 1.2 データベースセキュリティ

**MySQL設定:**
```sql
-- TLS接続の強制
REQUIRE SSL;

-- データ暗号化
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    email VARCHAR(255) ENCRYPTED,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 2. 通信セキュリティ

### 2.1 HTTPS/TLS (AWS ALB終端)

**AWS構成要件:**
- **Route53**: ドメイン管理
- **ALB (Application Load Balancer)**: SSL終端処理
- **TLS 1.3** 必須（ALB設定）
- **証明書**: AWS Certificate Manager (ACM) による自動管理

**Spring Boot設定（ALBプロキシ対応）:**
```yaml
server:
  # HTTPで動作（ALBがHTTPS終端）
  port: 8080
  forward-headers-strategy: framework
  
spring:
  # プロキシヘッダー信頼設定
  web:
    trust-proxy: true
```

**ALB設定要件:**
- SSL Policy: ELBSecurityPolicy-TLS13-1-2-2021-06
- HTTPS Listener (443) → HTTP Target (8080)
- Health Check: HTTP:/target:8080/actuator/health

**セキュリティヘッダー（Spring Boot側で設定）:**
```yaml
# HSTS等のセキュリティヘッダーは引き続きアプリケーションで設定
management:
  server:
    add-application-context-header: false
```

### 2.2 GraphQL セキュリティ

**認証:**
- JWT (JSON Web Token) による認証
- Access Token（15分）+ Refresh Token（7日）方式

**認可:**
- GraphQL スキーマレベルでの認可チェック
- フィールドレベルでのアクセス制御

**クエリ制限:**
- Query depth 制限（最大10レベル）
- Query complexity 制限（最大1000ポイント）
- Query timeout（30秒）

## 3. 認証・認可

### 3.1 認証方式

**JWT設定:**
```yaml
security:
  jwt:
    secret: ${JWT_SECRET}  # 最低256bit
    access-token-expiry: 900  # 15分
    refresh-token-expiry: 604800  # 7日
```

**Spring Security設定:**
- `/graphql` エンドポイントは認証必須
- `/health`, `/metrics` エンドポイントは認証不要

### 3.2 認可ポリシー

**ユーザー別データアクセス:**
- ユーザーは自分の作成したExpenseのみアクセス可能
- 管理者権限は当面不要

## 4. 入力値検証・SQL インジェクション対策

### 4.1 バックエンド検証

**Kotlin Bean Validation:**
```kotlin
data class ExpenseInput(
    @field:NotBlank
    @field:Size(max = 255)
    val description: String,
    
    @field:Positive
    @field:Digits(integer = 10, fraction = 2)
    val amount: BigDecimal,
    
    @field:Pattern(regexp = "^[a-zA-Z0-9\\s\\-_]+$")
    val category: String
)
```

**GraphQL Validation:**
- Spring Boot Validation との統合
- カスタムバリデーターによる業務ルール検証

### 4.2 JOOQ による SQL インジェクション対策

**現在の構成（JOOQ）での対策:**
- Prepared Statement の自動使用
- 動的クエリ構築時のパラメータバインディング強制

```kotlin
// 安全なクエリ例
context.select()
    .from(EXPENSE)
    .where(EXPENSE.USER_ID.eq(userId))  // パラメータバインディング
    .and(EXPENSE.AMOUNT.greaterThan(amount))
    .fetch()
```

## 5. フロントエンド（Remix）セキュリティ

### 5.1 CSRF 対策

**Remix CSRF Token:**
```typescript
// app/utils/csrf.server.ts
import { createCookie } from "@remix-run/node";

export const csrfToken = createCookie("csrf", {
  httpOnly: true,
  secure: process.env.NODE_ENV === "production",
  sameSite: "strict",
});
```

### 5.2 XSS 対策

**Content Security Policy (CSP):**
```typescript
// app/root.tsx
export const headers = {
  "Content-Security-Policy": 
    "default-src 'self'; " +
    "script-src 'self' 'unsafe-inline'; " +
    "style-src 'self' 'unsafe-inline'; " +
    "img-src 'self' data: https:; " +
    "connect-src 'self' https://api.example.com"
};
```

**入力値サニタイゼーション:**
- DOMPurify によるHTMLサニタイゼーション
- GraphQL レスポンスの自動エスケープ

### 5.3 セキュリティヘッダー

```typescript
// セキュリティヘッダーの設定
export const headers = {
  "X-Frame-Options": "DENY",
  "X-Content-Type-Options": "nosniff",
  "Referrer-Policy": "strict-origin-when-cross-origin",
  "Permissions-Policy": "geolocation=(), camera=(), microphone=()"
};
```

## 6. レート制限

### 6.1 API レート制限

**GraphQL エンドポイント:**
- 一般ユーザー: 100 requests/分
- 認証失敗: 5 attempts/15分（アカウントロック）

**Spring Boot レート制限:**
```yaml
spring:
  security:
    rate-limit:
      graphql:
        requests-per-minute: 100
      auth:
        failed-attempts: 5
        lockout-duration: 900  # 15分
```

### 6.2 Redis による分散レート制限

**実装:**
- Redis Sliding Window アルゴリズム
- ユーザーID + IPアドレス による制限

## 7. ログ・監査

### 7.1 セキュリティログ

**記録対象:**
- 認証成功/失敗
- 不正なアクセス試行
- データアクセスパターンの異常

**ログフォーマット:**
```json
{
  "timestamp": "2024-01-01T00:00:00Z",
  "level": "WARN",
  "event": "AUTHENTICATION_FAILURE",
  "userId": null,
  "ipAddress": "192.168.1.1",
  "userAgent": "Mozilla/5.0...",
  "details": "Invalid password attempt"
}
```

### 7.2 個人情報ログ除外

- パスワード、金額、個人情報をログに出力しない
- ログ構造化による機密情報フィルタリング

## 8. 開発・運用セキュリティ

### 8.1 依存関係管理

**自動脆弱性チェック:**
- `npm audit` （フロントエンド）
- Gradle dependency vulnerability scan
- GitHub Dependabot によるアップデート

### 8.2 CI/CD セキュリティ

**GitHub Actions:**
- Secrets による機密情報管理
- OIDC による AWS認証（長期的なアクセスキー排除）

## 関連ドキュメント

- [認証・認可詳細設計](./auth-design.md)
- [暗号化実装ガイド](./encryption-guide.md)
- [セキュリティテスト計画](./security-testing.md)