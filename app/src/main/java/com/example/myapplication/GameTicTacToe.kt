package com.example.myapplication

import kotlin.random.Random

fun Boolean?.toMark(): String = when (this) {
    true -> "X"
    false -> "O"
    null -> " "
}

interface Game {
    val isFinished: Boolean
    val winner: Boolean?
    val field: Field
    var userMark: Boolean
    fun actGameOnOneDevice(row: Int, col: Int): Boolean
    fun actSingleGame(row: Int, col: Int): Boolean
    fun actMultiplayerGame(row: Int, col: Int, mark: Boolean): Boolean
}

interface Field {
    val size: Int
    fun get(row: Int, col: Int): Boolean?
}

interface MutableField : Field {
    fun set(row: Int, col: Int, value: Boolean)
}

class GameImp : Game {

    override var isFinished: Boolean = false
    override var winner: Boolean? = null
    override val field: MutableField = ArrayField(3)
    override var userMark = true
    private var move = 0

    override fun actSingleGame(row: Int, col: Int): Boolean {
        var count = 0
        for (i in 0..2) {
            for (j in 0..2) {
                if (field.get(i, j) != null)
                    count++
            }
        }
        if (count == 0) {
            userMark = Random.nextBoolean()
            if (!userMark) {
                actAi()
            }
        }
        if (field.get(row, col) != null)
            return false
        field.set(row, col, userMark)
        checkWinner()
        checkEnd()
        if (!isFinished) {
            actAi()
            checkWinner()
            checkEnd()
        }

        return true
    }

    override fun actGameOnOneDevice(row: Int, col: Int): Boolean {
        if (field.get(row, col) != null)
            return false
        field.set(row, col, userMark)

        userMark = !userMark
        return true
    }

    override fun actMultiplayerGame(row: Int, col: Int, mark: Boolean): Boolean {
        if (field.get(row, col) != null)
            return false
        field.set(row, col, userMark)
        checkWinner()
        checkEnd()
        return true
    }

    private fun checkEnd() {
        repeat(field.size) { row ->
            repeat(field.size) { col ->
                if (field.get(row, col) == null) {
                    return
                }
            }
        }
        isFinished = true
    }

    private fun checkWinner() {
        if (field.get(0, 0) == userMark && field.get(0, 1) == userMark && field.get(0, 2) == userMark) {
            winner = userMark
            isFinished = true
        } else if (field.get(1, 0) == userMark && field.get(1, 1) == userMark && field.get(1, 2) == userMark) {
            winner = userMark
            isFinished = true
        } else if (field.get(2, 0) == userMark && field.get(2, 1) == userMark && field.get(2, 2) == userMark) {
            winner = userMark
            isFinished = true
        } else if (field.get(0, 0) == userMark && field.get(1, 0) == userMark && field.get(2, 0) == userMark) {
            winner = userMark
            isFinished = true
        } else if (field.get(0, 1) == userMark && field.get(1, 1) == userMark && field.get(2, 1) == userMark) {
            winner = userMark
            isFinished = true
        } else if (field.get(0, 2) == userMark && field.get(1, 2) == userMark && field.get(2, 2) == userMark) {
            winner = userMark
            isFinished = true
        } else if (field.get(0, 0) == userMark && field.get(1, 1) == userMark && field.get(2, 2) == userMark) {
            winner = userMark
            isFinished = true
        } else if (field.get(0, 2) == userMark && field.get(1, 1) == userMark && field.get(2, 0) == userMark) {
            winner = userMark
            isFinished = true
        }

        if (field.get(0, 0) == !userMark && field.get(0, 1) == !userMark && field.get(0, 2) == !userMark) {
            winner = !userMark
            isFinished = true
        } else if (field.get(1, 0) == !userMark && field.get(1, 1) == !userMark && field.get(1, 2) == !userMark) {
            winner = !userMark
            isFinished = true
        } else if (field.get(2, 0) == !userMark && field.get(2, 1) == !userMark && field.get(2, 2) == !userMark) {
            winner = !userMark
            isFinished = true
        } else if (field.get(0, 0) == !userMark && field.get(1, 0) == !userMark && field.get(2, 0) == !userMark) {
            winner = !userMark
            isFinished = true
        } else if (field.get(0, 1) == !userMark && field.get(1, 1) == !userMark && field.get(2, 1) == !userMark) {
            winner = !userMark
            isFinished = true
        } else if (field.get(0, 2) == !userMark && field.get(1, 2) == !userMark && field.get(2, 2) == !userMark) {
            winner = !userMark
            isFinished = true
        } else if (field.get(0, 0) == !userMark && field.get(1, 1) == !userMark && field.get(2, 2) == !userMark) {
            winner = !userMark
            isFinished = true
        } else if (field.get(0, 2) == !userMark && field.get(1, 1) == !userMark && field.get(2, 0) == !userMark) {
            winner = !userMark
            isFinished = true
        }

        return
    }

    private fun actAi() {
        when {
            move == 0 -> {
                if (field.get(1, 1) == null)
                    field.set(1, 1, !userMark)
                else
                    field.set(0, 0, !userMark)
                move++
            }
            move == 1 -> {
                if (field.get(1, 1) == !userMark || field.get(1, 1) == userMark) {
                    when {
                        field.get(0, 0) == null -> field.set(0, 0, !userMark)
                        field.get(0, 2) == null -> field.set(0, 2, !userMark)
                        else -> field.set(
                            checkNextWinPlayer().first,
                            checkNextWinPlayer().second,
                            !userMark
                        )
                    }
                }
                else {
                    if (checkNextWinPlayer().first != -1) {
                        field.set(
                            checkNextWinPlayer().first,
                            checkNextWinPlayer().second,
                            !userMark
                        )
                    } else
                        randomSetField()
                }
                move++
            }
            move == 2 -> {
                if (checkNextWinAi().first == -1) {
                    if (checkNextWinPlayer().first != -1) {
                        field.set(
                            checkNextWinPlayer().first,
                            checkNextWinPlayer().second,
                            !userMark
                        )
                    }
                    else
                        randomSetField()
                }
                else if ((checkNextWinPlayer().first == -1))
                    field.set(checkNextWinAi().first, checkNextWinAi().second, !userMark)
                else
                    randomSetField()

                move++
            }
            move > 2 -> {
                randomSetField()
            }
        }

    }

    private fun randomSetField() {
        var n = 0
        var randCountOne = -1
        var randCountTwo = -1
        while (n == 0) {
            randCountOne = (0..2).random()
            randCountTwo = (0..2).random()
            if (field.get(randCountOne, randCountTwo) == null)
                n++
        }
        field.set(randCountOne, randCountTwo, !userMark)
    }

    private fun checkNextWinAi(): Pair<Int, Int> {
        if (field.get(0, 0) == !userMark && field.get(0, 1) == !userMark && field.get(0, 2) == null) {
            return Pair(0, 2)
        }
        if (field.get(0, 0) == !userMark && field.get(0, 2) == !userMark && field.get(0, 1) == null) {
            return Pair(0, 1)
        }
        if (field.get(0, 1) == !userMark && field.get(0, 2) == !userMark && field.get(0, 0) == null) {
            return Pair(0, 0)
        }
        if (field.get(1, 0) == !userMark && field.get(1, 1) == !userMark && field.get(1, 2) == null) {
            return Pair(1, 2)
        }
        if (field.get(1, 0) == !userMark && field.get(1, 2) == !userMark && field.get(1, 1) == null) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == !userMark && field.get(1, 2) == !userMark && field.get(1, 0) == null) {
            return Pair(1, 0)
        }
        if (field.get(2, 0) == !userMark && field.get(2, 1) == !userMark && field.get(2, 2) == null) {
            return Pair(2, 2)
        }
        if (field.get(2, 0) == !userMark && field.get(2, 2) == !userMark && field.get(2, 1) == null) {
            return Pair(2, 1)
        }
        if (field.get(2, 1) == !userMark && field.get(2, 2) == !userMark && field.get(2, 0) == null) {
            return Pair(2, 0)
        }
        if (field.get(0, 0) == !userMark && field.get(1, 0) == !userMark && field.get(2, 0) == null) {
            return Pair(2, 0)
        }
        if (field.get(0, 0) == !userMark && field.get(2, 0) == !userMark && field.get(1, 0) == null) {
            return Pair(1, 0)
        }
        if (field.get(1, 0) == !userMark && field.get(2, 0) == !userMark && field.get(0, 0) == null) {
            return Pair(0, 0)
        }
        if (field.get(0, 1) == !userMark && field.get(1, 1) == !userMark && field.get(2, 1) == null) {
            return Pair(2, 1)
        }
        if (field.get(0, 1) == !userMark && field.get(2, 1) == !userMark && field.get(1, 1) == null) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == !userMark && field.get(2, 1) == !userMark && field.get(0, 1) == null) {
            return Pair(0, 1)
        }
        if (field.get(0, 2) == !userMark && field.get(1, 2) == !userMark && field.get(2, 2) == null) {
            return Pair(2, 2)
        }
        if (field.get(0, 2) == !userMark && field.get(2, 2) == !userMark && field.get(1, 2) == null) {
            return Pair(1, 2)
        }
        if (field.get(1, 2) == !userMark && field.get(2, 2) == !userMark && field.get(0, 2) == null) {
            return Pair(0, 2)
        }
        if (field.get(0, 0) == !userMark && field.get(1, 1) == !userMark && field.get(2, 2) == null) {
            return Pair(2, 2)
        }
        if (field.get(0, 0) == !userMark && field.get(2, 2) == !userMark && field.get(1, 1) == null) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == !userMark && field.get(2, 2) == !userMark && field.get(0, 0) == null) {
            return Pair(0, 0)
        }
        if (field.get(0, 2) == !userMark && field.get(1, 1) == !userMark && field.get(2, 0) == null) {
            return Pair(2, 0)
        }
        if (field.get(0, 2) == !userMark && field.get(2, 0) == !userMark && field.get(1, 1) == null) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == !userMark && field.get(2, 0) == !userMark && field.get(0, 2) == null) {
            return Pair(0, 2)
        }

        return Pair(-1, -1)
    }

    private fun checkNextWinPlayer(): Pair<Int, Int> {
        if (field.get(0, 0) == userMark && field.get(0, 1) == userMark && field.get(0, 2) == null) {
            return Pair(0, 2)
        }
        if (field.get(0, 0) == userMark && field.get(0, 2) == userMark && field.get(0, 1) == null) {
            return Pair(0, 1)
        }
        if (field.get(0, 1) == userMark && field.get(0, 2) == userMark && field.get(0, 0) == null) {
            return Pair(0, 0)
        }
        if (field.get(1, 0) == userMark && field.get(1, 1) == userMark && field.get(1, 2) == null) {
            return Pair(1, 2)
        }
        if (field.get(1, 0) == userMark && field.get(1, 2) == userMark && field.get(1, 1) == null) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == userMark && field.get(1, 2) == userMark && field.get(1, 1) == null) {
            return Pair(1, 0)
        }
        if (field.get(2, 0) == userMark && field.get(2, 1) == userMark && field.get(2, 2) == null) {
            return Pair(2, 2)
        }
        if (field.get(2, 0) == userMark && field.get(2, 2) == userMark && field.get(2, 1) == null) {
            return Pair(2, 1)
        }
        if (field.get(2, 1) == userMark && field.get(2, 2) == userMark && field.get(2, 0) == null) {
            return Pair(2, 0)
        }
        if (field.get(0, 0) == userMark && field.get(1, 0) == userMark && field.get(2, 0) == null) {
            return Pair(2, 0)
        }
        if (field.get(0, 0) == userMark && field.get(2, 0) == userMark && field.get(1, 0) == null) {
            return Pair(1, 0)
        }
        if (field.get(1, 0) == userMark && field.get(2, 0) == userMark && field.get(0, 0) == null) {
            return Pair(0, 0)
        }
        if (field.get(0, 1) == userMark && field.get(1, 1) == userMark && field.get(2, 1) == null) {
            return Pair(2, 1)
        }
        if (field.get(0, 1) == userMark && field.get(2, 1) == userMark && field.get(1, 1) == null) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == userMark && field.get(2, 1) == userMark && field.get(0, 1) == null) {
            return Pair(0, 1)
        }
        if (field.get(0, 2) == userMark && field.get(1, 2) == userMark && field.get(2, 2) == null) {
            return Pair(2, 2)
        }
        if (field.get(0, 2) == userMark && field.get(2, 2) == userMark && field.get(1, 2) == null) {
            return Pair(1, 2)
        }
        if (field.get(1, 2) == userMark && field.get(2, 2) == userMark && field.get(0, 2) == null) {
            return Pair(0, 2)
        }
        if (field.get(0, 0) == userMark && field.get(1, 1) == userMark && field.get(2, 2) == null) {
            return Pair(2, 2)
        }
        if (field.get(0, 0) == userMark && field.get(2, 2) == userMark && field.get(1, 1) == null) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == userMark && field.get(2, 2) == userMark && field.get(0, 0) == null) {
            return Pair(0, 0)
        }
        if (field.get(0, 2) == userMark && field.get(1, 1) == userMark && field.get(2, 0) == null) {
            return Pair(2, 0)
        }
        if (field.get(0, 2) == userMark && field.get(2, 0) == userMark && field.get(1, 1) == null) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == userMark && field.get(2, 0) == userMark && field.get(0, 2) == null) {
            return Pair(0, 2)
        }

        return Pair(-1, -1)
    }

}


class ArrayField(override val size: Int) : MutableField {

    private val points: Array<Array<Boolean?>> = Array(size) { arrayOfNulls(size) }
    override fun set(row: Int, col: Int, value: Boolean) {
        points[row][col] = value
    }

    override fun get(row: Int, col: Int): Boolean? = points[row][col]
}