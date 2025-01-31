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
    LEFT, RIGHT, UP, DOWN
}

data class Snake(val head: Offset = Offset(0f, 0f))

sealed interface SnakeState {
    data class Alive(val snake: List<Offset>, val direction: Direction) : SnakeState
    data object Init : SnakeState
    data class Dead(val snake: List<Offset>) : SnakeState
}

class SnakeViewModel : ViewModel() {

    private val _snake = MutableStateFlow<SnakeState>(SnakeState.Init)
    val snake: StateFlow<SnakeState> get() = _snake.asStateFlow()

    //Accessing direction using indices 0..3
    private var randomDirection = Direction.entries[Random.nextInt(0, 4)]

    private var board: Offset = Offset(0f, 0f)
    private var isDead = false

    init {
        generateSnakePoints()
        moveSnake()
    }

    private fun moveSnake() {
        viewModelScope.launch {
            while (isDead.not()) {
                delay(100)


                _snake.update {
                    SnakeState.Alive(updateSnake(), randomDirection)
                }
            }

        }
    }

    private fun updateSnake(): List<Offset> {

        Log.d("Ajay", "ViewModel:: snake Alive state.")

        val state = (_snake.value as SnakeState.Alive)

        val list = state.snake
        val update = mutableListOf<Offset>()

        list.forEach { offset ->

            var x = offset.x
            var y = offset.y

            when (randomDirection) {
                Direction.LEFT -> {
                    if (x <= 0) {
                        x = board.x
                    } else {
                        x -= 1
                    }
                }

                Direction.RIGHT -> {
                    if (x >= board.x) {
                        x = 0f
                    } else {
                        x += 1
                    }
                }

                Direction.UP -> {
                    if (y <= 0) {
                        y = board.y
                    } else {
                        y -= 1

                    }
                }

                Direction.DOWN -> {
                    if (y >= board.y) {
                        y = 0f
                    } else {
                        y += 1
                    }
                }
            }
            update.add(Offset(x, y))
        }

        return update
    }


    private fun generateSnakePoints() {
        val list = mutableListOf<Offset>()
        var x = Random.nextInt(0, 69)
        var y = Random.nextInt(0, 120)

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
        _snake.update { SnakeState.Alive(list, randomDirection) }
    }

    fun storeBoard(columns: Int, rows: Int) {
        val offset = Offset(columns.toFloat(), rows.toFloat())
        board = offset
    }

    fun updateSnakeState(state: SnakeState) {
        _snake.update {
            state
        }
    }
}


