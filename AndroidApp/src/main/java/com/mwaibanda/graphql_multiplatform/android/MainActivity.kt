package com.mwaibanda.graphql_multiplatform.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.mwaibanda.GetAllTodosQuery
import com.mwaibanda.graphql_multiplatform.addTodo
import com.mwaibanda.graphql_multiplatform.deleteTodo
import com.mwaibanda.graphql_multiplatform.getAllTodos
import com.mwaibanda.graphql_multiplatform.updateTodo
import com.mwaibanda.type.DeleteTodo
import com.mwaibanda.type.NewTodo
import com.mwaibanda.type.UpdateTodo
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var todos: List<GetAllTodosQuery.Todo> by remember {
                mutableStateOf(emptyList())
            }
            var showAddTodo by remember {
                mutableStateOf(false)
            }

            var addTodoText by remember {
                mutableStateOf("")
            }
            var coroutineScope = rememberCoroutineScope()
            LaunchedEffect(key1 = todos) {
                if (todos.isEmpty()) {
                    val remoteTodos = getAllTodos()
                    todos = remoteTodos.data?.todos ?: emptyList()
                }
            }
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Column {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Todo List", style = MaterialTheme.typography.titleLarge)
                                TextButton(onClick = { showAddTodo = !showAddTodo }) {
                                    Text(text = "ADD TODO")
                                }
                            }
                            Divider()
                            todos.forEach {
                                TodoCard(it) {
                                    todos = emptyList()
                                }
                            }
                        }
                        AnimatedVisibility(visible = showAddTodo, enter = fadeIn(), exit = fadeOut()) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(0.35f)), contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    Modifier
                                        .fillMaxWidth(0.75f)
                                        .clip(RoundedCornerShape(percent = 10))
                                        .background(Color.White)
                                        .padding(20.dp)
                                ) {
                                    Text(text = "Add Todo", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 20.dp))
                                    OutlinedTextField(value = addTodoText, onValueChange = {
                                        addTodoText = it
                                    })
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Button(onClick = { showAddTodo = !showAddTodo }) {
                                            Text(text = "Cancel")
                                        }
                                        Button(onClick = {
                                            showAddTodo = !showAddTodo
                                            coroutineScope.launch {
                                                addTodo(NewTodo(text = addTodoText, userId = "Android")){
                                                    addTodoText = ""
                                                    todos = emptyList()
                                                }
                                            }
                                        }) {
                                            Text(text = "Upload")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodoCard(todo: GetAllTodosQuery.Todo, onUpdateTodos: () -> Unit) {
    var text by remember {
        mutableStateOf(todo.text)
    }
    var canEdit by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    Card(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        Column(Modifier.padding(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    BasicTextField(
                        value = text,
                        onValueChange = { text = it },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                        ),
                        modifier = Modifier.focusRequester(focusRequester),
                        textStyle = MaterialTheme.typography.titleMedium,
                        readOnly = canEdit.not(),
                        keyboardActions = KeyboardActions(onDone = {
                        canEdit = canEdit.not()
                            coroutineScope.launch {
                                updateTodo(UpdateTodo(id = todo.id, text = text)) {
                                    onUpdateTodos()
                                }
                            }
                    }))

                }
                Row {
                    IconButton(onClick = {
                        canEdit = !canEdit
                        if (canEdit) focusRequester.requestFocus() else {
                            focusRequester.freeFocus()
                            text = todo.text
                        }
                    }) {
                        Icon(imageVector = if (canEdit) Icons.Default.List else Icons.Default.Edit, contentDescription = "")
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            deleteTodo(DeleteTodo(id = todo.id)) {
                                onUpdateTodos()
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                    }
                }
            }
            BasicTextField(value = "Created by: ${todo.user.name}", onValueChange = {

            }, readOnly = true)

        }
    }
}

