package ca.apprajapati.composer.snake_game

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random


enum class Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN
}

sealed interface Snake {
    data class Alive(val snake: List<Offset>, val direction: Direction) : Snake
    data object Init : Snake
}

class SnakeViewModel : ViewModel() {

    private val _snake = MutableStateFlow<Snake>(Snake.Init)
    val snake: StateFlow<Snake> get() = _snake.asStateFlow()

    //Accessing direction using indices 0..3
    private var randomDirection = Direction.entries[Random.nextInt(0, 4)]

    init {
        generateSnakePoints()
        moveSnake()
    }

    private fun moveSnake() {
        viewModelScope.launch {
            while (true) {
                delay(500)

                _snake.update {
                    Snake.Alive(updateSnake(), Direction.DOWN)
                }
            }

        }
    }

    private fun updateSnake() =
        when (_snake.value) {
            is Snake.Alive -> {

                val list = (_snake.value as Snake.Alive).snake
                val update = mutableListOf<Offset>()

                list.forEach { offset ->

                    var x = offset.x
                    var y = offset.y

                    when (randomDirection) {
                        Direction.LEFT -> {
                            x -= 1
                        }

                        Direction.RIGHT -> {
                            x += 1
                        }

                        Direction.UP -> {
                            y -= 1
                        }

                        Direction.DOWN -> {
                            y += 1
                        }
                    }
                    update.add(Offset(x, y))
                }
                update
            }

            Snake.Init -> emptyList()
        }


    private fun generateSnakePoints() {
        val list = mutableListOf<Offset>()
        var x = Random.nextInt(20, 50)
        var y = Random.nextInt(20, 50)

        for (i in 1..5) {
            when (randomDirection) {
                Direction.LEFT -> {
                    list.add(Offset(x.toFloat(), y.toFloat()))
                    x++
                }

                Direction.RIGHT -> {
                    list.add(Offset(x.toFloat(), y.toFloat()))
                    x++
                }

                Direction.UP -> {
                    list.add(Offset(x.toFloat(), y.toFloat()))
                    y++
                }

                Direction.DOWN -> {
                    list.add(Offset(x.toFloat(), y.toFloat()))
                    y++
                }
            }
        }
        Log.d("Ajay", "snake points $list")
        _snake.update { Snake.Alive(list, randomDirection) }
    }
}


