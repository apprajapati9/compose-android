package ca.apprajapati.composer.snake_game

import android.util.Log
import androidx.compose.ui.geometry.Offset
import kotlin.random.Random

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

class SnakeManager {

    private var snake: List<Offset> = listOf()
    private var boardOffset: Offset = Offset(0f, 0f)
    private var board: Int = 0 //Used to randomly place a snake position

    //Accessing direction using indices 0..3
    var randomDirection = Direction.entries[Random.nextInt(0, 4)]

    private val defaultCountSnakePoints =
        2 //how many points snake starts with at the start of a game.

    fun setBoard(boardXY: Offset) {
        boardOffset = boardXY
        board = if (boardXY.x < boardXY.y) boardXY.x.toInt() else boardXY.y.toInt()
    }

    fun getSnake(): List<Offset> = snake

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

    fun moveSnake() {
        val list = snake
        val update = mutableListOf<Offset>()

        var head = list[0]

        var x = head.x
        var y = head.y

        when (randomDirection) {

            Direction.LEFT -> {
                if (x <= 0) {
                    x = boardOffset.x
                } else {
                    x -= 1
                }

            }

            Direction.RIGHT -> {
                if (x >= boardOffset.x) {
                    x = 0f
                } else {
                    x += 1
                }
            }

            Direction.UP -> {
                if (y <= 0) {
                    y = boardOffset.y
                } else {
                    y -= 1

                }
            }

            Direction.DOWN -> {
                if (y >= boardOffset.y) {
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
        snake = update
    }


    fun generateSnakePoints() {
        val list = mutableListOf<Offset>()

        var x = 0
        var y = 0

        if (board - defaultCountSnakePoints > defaultCountSnakePoints) {
            x = Random.nextInt(defaultCountSnakePoints, board - defaultCountSnakePoints)
            y = Random.nextInt(defaultCountSnakePoints, board - defaultCountSnakePoints)
        } else {
            x = Random.nextInt(0, board)
            y = Random.nextInt(0, board)
        }

        randomDirection = Direction.entries[Random.nextInt(0, 4)]

        for (i in 1..2) {
            when (randomDirection) {
                Direction.LEFT -> {
                    list.add(Offset(x.toFloat(), y.toFloat()))
                    x++
                }

                Direction.RIGHT -> {
                    list.add(Offset(x.toFloat(), y.toFloat()))
                    x--
                }

                Direction.UP -> {
                    list.add(Offset(x.toFloat(), y.toFloat()))
                    y++
                }

                Direction.DOWN -> {
                    list.add(Offset(x.toFloat(), y.toFloat()))
                    y--
                }
            }
        }
        snake = list
        Log.d("Ajay", "Generating random snake points.. $snake")
    }

    fun extendSnake() {
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

        snake = update
    }

}


