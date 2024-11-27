package com.example.fitness.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh

import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIScreen(navController: NavHostController? = null) {
    var userInput by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Pair<Boolean, String>>() }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.offset(y = (-60).dp),
                title = { Text(text = "AI assistant", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Return")
                    }
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.Refresh, contentDescription = "refresh")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Please enter your question") },
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (userInput.isNotBlank()) {

                        messages.add(Pair(true, userInput))
                        userInput = ""


                        messages.add(Pair(false, "This is AI respond"))
                    }
                }) {
                    Text("Send")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(messages.size) { index ->
                    val (isUserMessage, message) = messages[index]

                    if (isUserMessage) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = message,
                                modifier = Modifier
                                    .background(Color.LightGray)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = Color.Black,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Right
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = message,
                                modifier = Modifier
                                    .background(Color.White)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = Color.Black,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Left
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}