package com.mwaibanda.graphql_multiplatform.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mwaibanda.GetAllTodosQuery
import com.mwaibanda.graphql_multiplatform.getAllTodos

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var todos: List<GetAllTodosQuery.Todo> by remember {
                mutableStateOf(emptyList())
            }
            LaunchedEffect(key1 = Unit) {
                val remoteTodos = getAllTodos()
                todos = remoteTodos.data?.todos ?: emptyList()
            }
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Todo List", style = MaterialTheme.typography.titleLarge)
                            TextButton(onClick = { /*TODO*/ }) {
                                Text(text = "ADD TODO")
                            }
                        }
                        Divider()
                        todos.forEach { 
                            Card(Modifier.padding(10.dp)) {
                                Column(Modifier.padding(10.dp)) {
                                    Text(text = "Todo: ${it.text}")
                                    Text(text = "User: ${it.user.name}")
                                }
                            }
                        }
                    }
                    
                }
            }
        }
    }
}

