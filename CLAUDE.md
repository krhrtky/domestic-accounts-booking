# CLAUDE.md

このファイルは、Claude Code (claude.ai/code) がこのリポジトリでコードを扱う際のガイダンスを提供します。

## 開発の進め方

指示を受けたら docs ディレクトリを見てガイドラインとなる情報を収集してください。
ガイドラインとなる情報が見つからない場合は、推測で進めずに候補を提案してください。
提案で決まった方針は docs ディレクトリ以下に ADR として記載してください。
ガイドラインとなりうる決定は、 docs ディレクトリ内の適切なディレクトリに Markdown として、次回以降ガイドラインとなるように記録してください。

### 実装の進め方

タスク開始の時点で、 ISSUE の番号からブランチを切って実装に入るようにしてください。
実装したら、必ず対応するテストコードまたは、仕様をパスすることを確認してください。
テストコードに記載する仕様が確認できないときは、まず確認して仕様を定義してください。
試行を3回繰り返してもパスしない場合は一度作業を止めて、何が失敗しているのかを何を実装したのかを合わせて報告して、次の指示を仰いで下さい。
テストが完了したら、 commit してください。 commit message には、どんな指示を受けて何をしたのかを記載してください。

タスクの完了は、GitHub の Pull Request の作成を行い、CI がパスするまでとします。

## プロジェクト構成

これは家計簿システムのフルスタックアプリケーションです。

### バックエンド（Kotlin/Spring Boot）

#### コア技術
- **Kotlin 2.1.20** with **Java 21**
- **Spring Boot 3.2.5** フレームワーク
- **GraphQL API** (graphql-kotlin-spring-server 7.0.2)
- **MySQL 8.0.43** データベース
- **Gradle** ビルドシステム（バージョンカタログ使用）

#### アーキテクチャ
- **ドメイン駆動設計（DDD）** の4層アーキテクチャ
- マルチモジュール構成：
  - `domains/` - ドメインロジック（user, expense）
  - `infrastructure/` - インフラストラクチャ層
  - `presentation/api/` - プレゼンテーション層（GraphQL コントローラー）
  - `shared/` - 共有コンポーネント

#### 開発コマンド
- `./gradlew build` - バックエンドビルド
- `./gradlew test` - テスト実行
- `./gradlew bootRun` - アプリケーション起動

### フロントエンド（TypeScript/Remix）

#### コア技術
- **TypeScript 5.1.6** with **Remix 2.16.5**
- **Vite 6.0.0** ビルドツール
- **Tailwind CSS** ユーティリティファーストCSS
- **TanStack Query** GraphQLデータフェッチング
- **クライアントサイドのみ** （SSR無効化）

#### テスト・開発ツール
- **Playwright** - E2Eテスト（Argos CI でビジュアルリグレッション）
- **Ladle** - コンポーネント開発環境（Storybookの代替）
- **Biome** - コードリンティング・フォーマット

#### 開発コマンド
- `npm run dev` - Remix 開発サーバー起動
- `npm run build` - プロダクションビルド
- `npm start` - プロダクションビルド実行
- `npm run lint` - Biome リンター実行
- `npm run lint:format` - Biome フォーマット実行
- `npm run typecheck` - TypeScript 型チェック
- `npm run ladle` - Ladle コンポーネント開発サーバー起動
- `npx playwright test` - Playwright テスト実行

### GraphQL 統合

#### スキーマファースト設計
- 共有スキーマ: `/interface/schema.graphql`
- バックエンド: Kotlin クラスからスキーマ生成
- フロントエンド: graphql-codegen で TypeScript 型とReact Query フック生成

#### コード生成
- `npx graphql-codegen` - フロントエンド型生成
- バックエンドは Gradle プラグインで自動生成

### 設定の重要なポイント

#### フロントエンド
- `vite.config.ts` で SSR 無効化（`ssr: false`）
- Remix v3 future フラグ有効化（将来の互換性）
- Playwright テストは Ladle サーバー（ポート61000）に対して実行
- Argos CI でスクリーンショット比較によるビジュアルリグレッションテスト

#### バックエンド
- GraphQL サーバー設定のみ（クライアント設定は不要）
- DDD 4層アーキテクチャ
- マルチモジュール構成でドメイン分離

### テスト構成

#### フロントエンド
- `tests/` - Playwright E2E テスト（ビジュアルリグレッション含む）
- `app/routes/*.stories.tsx` - Ladle 用コンポーネントストーリー
- `tests/snapshot.spec.ts-snapshots/` - ビジュアルスナップショット保存

#### バックエンド
- 各モジュールの `src/test/` - ユニット・統合テスト

### CI/CD

- **GitHub Actions** で CI/CD
- フロントエンド・バックエンド別々のワークフロー
- **mise** でツールバージョン管理（Java, Kotlin, Node.js, Gradle）

## 重要な注意事項

- バックエンドはサーバーサイドのみ（GraphQL クライアント設定不要）
- フロントエンドは実質的に SPA（Remix のルーティングと開発メリット活用）
- GraphQL による型安全な通信
- ドメイン駆動設計によるクリーンアーキテクチャ
