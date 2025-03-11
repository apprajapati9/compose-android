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

sealed class SnakeState {
    data class Alive(val snake: List<Offset>, val direction: Direction) : SnakeState()
    data object Init : SnakeState()
    data class Dead(val snake: List<Offset>) : SnakeState()
}

class SnakeViewModel : ViewModel() {

    private val _snake = MutableStateFlow<SnakeState>(SnakeState.Init)
    val snake: StateFlow<SnakeState> get() = _snake.asStateFlow()

    private val defaultDelay = 110L
    private var delay = defaultDelay

    private var extend = false

    private var snakeManager = SnakeManager()

    fun moveSnake() {
        viewModelScope.launch {
            while (true) {

                when (snake.value) {
                    is SnakeState.Alive -> {
                        snakeManager.moveSnake()
                        _snake.update {
                            SnakeState.Alive(snakeManager.getSnake(), snakeManager.randomDirection)
                        }
                    }

                    is SnakeState.Dead -> {
                    }

                    SnakeState.Init -> {
                        _snake.update {
                            SnakeState.Init
                        }
                    }
                }
                delay(delay)
            }
        }
    }

    fun updateDirection(direction: Direction) {
        snakeManager.updateDirection(direction)
    }

    private fun resetDelay() {
        if (delay != defaultDelay) delay = defaultDelay
    }

    fun storeBoard(columns: Int, rows: Int) {
        val offset = Offset(columns.toFloat(), rows.toFloat())
        snakeManager.setBoard(offset)
    }

    fun restartSnake() {
        resetDelay()
        snakeManager.generateSnakePoints()
        _snake.update {
            SnakeState.Alive(snake = snakeManager.getSnake(), snakeManager.randomDirection)
        }
    }

    fun updateSnakeState(state: SnakeState) {
        _snake.update {
            state
        }
    }

    fun extendSnake(b: Boolean) {
        extend = b
        if (extend) {
            snakeManager.extendSnake()
            if (delay > 85) {
                delay--
            }
            extend = false
        }
    }

    fun getDirection(): Direction {
        return snakeManager.randomDirection
    }
}


