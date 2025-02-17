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

    private var extend = false

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

    fun updateDirection(direction: Direction) {
        if (randomDirection != direction) {
            if (randomDirection == Direction.UP && direction != Direction.DOWN) {
                randomDirection = direction
            }
            if (randomDirection == Direction.DOWN && direction != Direction.UP) {
                randomDirection = direction
            }
            if (randomDirection == Direction.LEFT && direction != Direction.RIGHT) {
                randomDirection = direction
            }
            if (randomDirection == Direction.RIGHT && direction != Direction.LEFT) {
                randomDirection = direction
            }
        }
    }

    private fun updateSnake(): List<Offset> {

        Log.d("Ajay", "ViewModel:: snake Alive state.")

        val state = (_snake.value as SnakeState.Alive)

        val list = state.snake
        val update = mutableListOf<Offset>()

        var head = list[0]

        var x = head.x
        var y = head.y

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
        for (i in 1..<list.size) {
            update.add(head)
            head = list[i]
        }

        return update
    }


    private fun generateSnakePoints() {
        val list = mutableListOf<Offset>()
        var x = Random.nextInt(0, 20)
        var y = Random.nextInt(0, 20)

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
        //Log.d("Ajay", "snake points $list")
        _snake.update { SnakeState.Alive(list, randomDirection) }
    }

    fun storeBoard(columns: Int, rows: Int) {
        val offset = Offset(columns.toFloat(), rows.toFloat())
        board = offset
    }

    private fun updateSnakeState(state: SnakeState) {
        _snake.update {
            state
        }
    }

    fun extendSnake(b: Boolean) {
        extend = b
        if (extend) {
            when (snake.value) {
                is SnakeState.Alive -> {

                    val snake = (snake.value as SnakeState.Alive).snake
                    val update = mutableListOf<Offset>()
                    for (i in snake) {
                        update.add(i)
                    }

                    val head = snake[snake.size - 1]

                    if (randomDirection == Direction.LEFT || randomDirection == Direction.RIGHT) {
                        update.add(Offset(head.x + 1, head.y))
                    } else {
                        update.add(Offset(head.x, head.y + 1))
                    }

                    val state = SnakeState.Alive(update, randomDirection)
                    updateSnakeState(state)
                    extend = false
                }

                is SnakeState.Dead -> {

                }

                SnakeState.Init -> {

                }
            }
        }
    }
}


