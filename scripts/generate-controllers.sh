#!/bin/bash

set -e

echo "GraphQLスキーマからKotlinコントローラーを生成中..."

# GraphQLスキーマファイルのパス
SCHEMA_FILE="interface/schema.graphql"
OUTPUT_DIR="backend/presentation/api/src/main/kotlin/graphql"

# 出力ディレクトリが存在しない場合は作成
mkdir -p "$OUTPUT_DIR"

# GraphQLクライアントコード生成
echo "1. GraphQLクライアントコード生成中..."
./gradlew :backend:presentation:api:graphqlGenerateClient

# スキーマから型定義とリゾルバーテンプレートを生成
echo "2. GraphQLサーバーコード生成中..."
node scripts/generate-kotlin-resolvers.js

echo "コントローラー生成完了!"
echo "生成されたファイル: $OUTPUT_DIR/"