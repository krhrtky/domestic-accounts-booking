import type { CodegenConfig } from '@graphql-codegen/cli'

const config: CodegenConfig = {
  schema: '../interface/schema.graphql',
  documents: '../interface/src/graphql/**/*.graphql',
  generates: {
    './app/generated/graphql.ts': {
      plugins: [
        'typescript',
        'typescript-operations',
        'typescript-react-query'
      ],
      config: {
        fetcher: 'graphql-request',
        legacyMode: false,
        addInfiniteQuery: true
      }
    }
  }
}

export default config