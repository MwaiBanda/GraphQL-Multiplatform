package com.mwaibanda.graphql_multiplatform

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.mwaibanda.AddTodoMutation
import com.mwaibanda.DeleteTodoMutation
import com.mwaibanda.GetAllTodosQuery
import com.mwaibanda.UpdateTodoMutation
import com.mwaibanda.type.DeleteTodo
import com.mwaibanda.type.NewTodo
import com.mwaibanda.type.UpdateTodo

val apolloClient = ApolloClient.Builder()
    .serverUrl("http://192.168.1.66:8080/query")
    .build()


suspend fun getAllTodos(): ApolloResponse<GetAllTodosQuery.Data> {
    return apolloClient.query(GetAllTodosQuery()).execute()
}

suspend fun updateTodo(input: UpdateTodo, onCompletion: (ApolloResponse<UpdateTodoMutation.Data>) -> Unit) {
    onCompletion(apolloClient.mutation(UpdateTodoMutation(input)).execute())
}

suspend fun deleteTodo(input: DeleteTodo, onCompletion: (ApolloResponse<DeleteTodoMutation.Data>) -> Unit) {
    onCompletion(apolloClient.mutation(DeleteTodoMutation(input)).execute())
}

suspend fun addTodo(input: NewTodo, onCompletion: (ApolloResponse<AddTodoMutation.Data>) -> Unit) {
    onCompletion(apolloClient.mutation(AddTodoMutation(input)).execute())
}