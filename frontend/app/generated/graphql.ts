import { GraphQLClient } from 'graphql-request';
import { RequestInit } from 'graphql-request/dist/types.dom';
import { useQuery, useInfiniteQuery, UseQueryOptions, UseInfiniteQueryOptions } from '@tanstack/react-query';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };

function fetcher<TData, TVariables extends { [key: string]: any }>(client: GraphQLClient, query: string, variables?: TVariables, requestHeaders?: RequestInit['headers']) {
  return async (): Promise<TData> => client.request({
    document: query,
    variables,
    requestHeaders
  });
}
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: { input: string; output: string; }
  String: { input: string; output: string; }
  Boolean: { input: boolean; output: boolean; }
  Int: { input: number; output: number; }
  Float: { input: number; output: number; }
};

export type Query = {
  __typename?: 'Query';
  user?: Maybe<User>;
};


export type QueryUserArgs = {
  id: Scalars['ID']['input'];
};

export type User = {
  __typename?: 'User';
  id: Scalars['ID']['output'];
};

export type FetchUserQueryVariables = Exact<{
  id: Scalars['ID']['input'];
}>;


export type FetchUserQuery = { __typename?: 'Query', user?: { __typename?: 'User', id: string } | null };



export const FetchUserDocument = `
    query fetchUser($id: ID!) {
  user(id: $id) {
    id
  }
}
    `;

export const useFetchUserQuery = <
      TData = FetchUserQuery,
      TError = unknown
    >(
      client: GraphQLClient,
      variables: FetchUserQueryVariables,
      options?: UseQueryOptions<FetchUserQuery, TError, TData>,
      headers?: RequestInit['headers']
    ) => {
    
    return useQuery<FetchUserQuery, TError, TData>(
      ['fetchUser', variables],
      fetcher<FetchUserQuery, FetchUserQueryVariables>(client, FetchUserDocument, variables, headers),
      options
    )};

export const useInfiniteFetchUserQuery = <
      TData = FetchUserQuery,
      TError = unknown
    >(
      client: GraphQLClient,
      variables: FetchUserQueryVariables,
      options?: UseInfiniteQueryOptions<FetchUserQuery, TError, TData>,
      headers?: RequestInit['headers']
    ) => {
    
    return useInfiniteQuery<FetchUserQuery, TError, TData>(
      ['fetchUser.infinite', variables],
      (metaData) => fetcher<FetchUserQuery, FetchUserQueryVariables>(client, FetchUserDocument, {...variables, ...(metaData.pageParam ?? {})}, headers)(),
      options
    )};
