package com.example.slidepuzzlegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.slidepuzzlegame.ui.theme.SlidePuzzleGameTheme
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SlidePuzzleGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SlidePuzzle()
                }
            }
        }
    }
}

@Composable
fun SlidePuzzle() {
    var board by remember { mutableStateOf(generateShuffledBoard()) }
    var emptyIndex by remember { mutableStateOf(board.indexOf(0)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 0 until 3) {
            Row {
                for (j in 0 until 3) {
                    val index = i * 3 + j
                    val number = board[index]

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                            .background(if (number == 0) Color.LightGray else Color(0xFF64B5F6))
                            .clickable {
                                if (canMove(index, emptyIndex)) {
                                    board = board.toMutableList().also {
                                        it[emptyIndex] = number
                                        it[index] = 0
                                    }
                                    emptyIndex = index
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (number != 0) {
                            Text(
                                text = number.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            board = generateShuffledBoard()
            emptyIndex = board.indexOf(0)
        }) {
            Text("Restart")
        }

        if (isSolved(board)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("🎉 Puzzle Solved!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Green)
        }
    }
}

fun generateShuffledBoard(): List<Int> {
    val list = (0..8).toMutableList()
    do {
        list.shuffle(Random(System.currentTimeMillis()))
    } while (!isSolvable(list) || isSolved(list))
    return list
}

fun isSolved(list: List<Int>): Boolean {
    return list == (1..8).toList() + 0
}

fun canMove(index: Int, emptyIndex: Int): Boolean {
    val r1 = index / 3
    val c1 = index % 3
    val r2 = emptyIndex / 3
    val c2 = emptyIndex % 3
    return abs(r1 - r2) + abs(c1 - c2) == 1
}

fun isSolvable(puzzle: List<Int>): Boolean {
    val list = puzzle.filter { it != 0 }
    var inversions = 0
    for (i in list.indices) {
        for (j in i + 1 until list.size) {
            if (list[i] > list[j]) inversions++
        }
    }
    return inversions % 2 == 0
}
