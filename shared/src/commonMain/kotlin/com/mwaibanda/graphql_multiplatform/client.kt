package com.mwaibanda.graphql_multiplatform

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.mwaibanda.GetAllTodosQuery

val apolloClient = ApolloClient.Builder()
    .serverUrl("http://192.168.1.66:8080/query")
    .build()


suspend fun getAllTodos(): ApolloResponse<GetAllTodosQuery.Data> {
    return apolloClient.query(GetAllTodosQuery()).execute()
}

