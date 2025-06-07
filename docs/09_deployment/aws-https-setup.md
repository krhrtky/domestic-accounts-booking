# AWS環境でのHTTPS設定と動作確認

## 概要

このドキュメントでは、AWS環境での家計簿システムのHTTPS設定について説明します。
AWS ALBでSSL終端を行い、Spring BootアプリケーションはHTTPで動作する構成となります。

## 1. AWS構成要件

### 1.1 Route53設定
- ドメインの登録・管理
- ALBへのAレコード設定

### 1.2 ALB (Application Load Balancer) 設定

**リスナー設定:**
```
HTTPS (443) → Target Group (HTTP:8080)
- SSL Policy: ELBSecurityPolicy-TLS13-1-2-2021-06
- 証明書: AWS Certificate Manager (ACM)
```

**Target Group設定:**
```
Protocol: HTTP
Port: 8080
Health Check:
  - Path: /actuator/health  
  - Protocol: HTTP
  - Port: 8080
  - Healthy threshold: 2
  - Unhealthy threshold: 3
  - Timeout: 5 seconds
  - Interval: 30 seconds
```

### 1.3 Security Group設定

**ALB Security Group:**
- Inbound: 443 (HTTPS) from 0.0.0.0/0
- Outbound: 8080 (HTTP) to ECS Task Security Group

**ECS Task Security Group:**
- Inbound: 8080 (HTTP) from ALB Security Group
- Outbound: All traffic

## 2. Spring Boot設定

### 2.1 application.yml設定

```yaml
server:
  port: 8080
  forward-headers-strategy: framework

spring:
  web:
    trust-proxy: true

management:
  endpoints:
    web:
      exposure:
        include: health,info
      base-path: /actuator
```

### 2.2 SecurityConfiguration

- ALBプロキシヘッダー対応
- セキュリティヘッダー設定（HSTS、CSP、X-Frame-Options等）
- ヘルスチェックエンドポイントのパブリックアクセス

## 3. 動作確認方法

### 3.1 開発環境での確認

**ローカル起動:**
```bash
cd backend
./gradlew bootRun
```

**ヘルスチェック確認:**
```bash
curl http://localhost:8080/actuator/health
# 期待する応答: {"status":"UP"}
```

**セキュリティヘッダー確認:**
```bash
curl -I http://localhost:8080/actuator/health
# 確認項目:
# - X-Frame-Options: DENY
# - X-Content-Type-Options: nosniff
# - Content-Security-Policy: default-src 'self'; ...
```

### 3.2 AWS環境での確認

**ALB経由のHTTPS確認:**
```bash
# ALBのDNS名を使用
curl -I https://your-alb-dns-name.elb.amazonaws.com/actuator/health

# Route53ドメインを使用
curl -I https://yourdomain.com/actuator/health
```

**期待するレスポンスヘッダー:**
```
HTTP/2 200
strict-transport-security: max-age=31536000; includeSubDomains; preload
x-frame-options: DENY
x-content-type-options: nosniff
referrer-policy: strict-origin-when-cross-origin
content-security-policy: default-src 'self'; ...
permissions-policy: geolocation=(), camera=(), microphone=()...
```

### 3.3 GraphQL エンドポイント確認

**GraphQL Playground/Introspection:**
```bash
curl -X POST https://yourdomain.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ __schema { types { name } } }"}'
```

### 3.4 SSL証明書確認

**証明書情報確認:**
```bash
openssl s_client -connect yourdomain.com:443 -servername yourdomain.com < /dev/null 2>/dev/null | openssl x509 -text -noout

# 確認項目:
# - TLS 1.3対応
# - 証明書有効期限
# - Subject Alternative Names
```

### 3.5 セキュリティテスト

**SSL Labs テスト:**
- https://www.ssllabs.com/ssltest/
- A+グレード取得を目標

**HSTS確認:**
```bash
curl -I https://yourdomain.com
# strict-transport-security ヘッダーの存在確認
```

## 4. トラブルシューティング

### 4.1 よくある問題

**1. ALBヘルスチェック失敗**
```bash
# ECS Taskログ確認
aws logs get-log-events --log-group-name /ecs/your-task-definition

# ポート8080での接続確認
telnet your-ecs-task-ip 8080
```

**2. プロキシヘッダー問題**
```yaml
# application.yml に追加
server:
  forward-headers-strategy: framework
logging:
  level:
    org.springframework.web: DEBUG
```

**3. CORS問題**
```yaml
# 開発環境でのCORS設定
spring:
  web:
    cors:
      allowed-origins: "https://yourdomain.com"
```

### 4.2 デバッグ用エンドポイント

**リクエストヘッダー確認用:**
```kotlin
@RestController
class DebugController {
    @GetMapping("/debug/headers")
    fun headers(request: HttpServletRequest): Map<String, String> {
        return request.headerNames.asSequence()
            .associateWith { request.getHeader(it) }
    }
}
```

## 5. 本番環境移行チェックリスト

- [ ] Route53ドメイン設定完了
- [ ] ACM証明書発行・検証完了
- [ ] ALB設定完了（HTTPS → HTTP転送）
- [ ] Target Group設定完了
- [ ] Security Group設定完了
- [ ] ECS Task定義更新
- [ ] ヘルスチェック正常動作確認
- [ ] SSL Labs A+グレード確認
- [ ] GraphQL API動作確認
- [ ] セキュリティヘッダー動作確認

## 関連ドキュメント

- [セキュリティ要件定義](../11_security/security-requirements.md)
- [バックエンドアーキテクチャ](../03_architecture/backend-architecture.md)
- [セキュリティテスト計画](../11_security/security-testing.md)