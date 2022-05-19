package com.example.myapplication

import kotlin.random.Random

// Определяет позицию поля
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
    var tie: Boolean
    fun actGameOnOneDevice(row: Int, col: Int): Boolean
    fun actMultiplayerGame(row: Int, col: Int, mark: Boolean): Boolean
    fun actSingleGameEasy(row: Int, col: Int): Boolean
    fun actSingleGameNormal(row: Int, col: Int): Boolean
    fun actSingleGameHard(row: Int, col: Int): Boolean
}

interface Field {
    val size: Int
    fun get(row: Int, col: Int): Boolean?
}

interface MutableField : Field {
    fun set(row: Int, col: Int, value: Boolean)
}

class GameImp : Game {
    override var tie: Boolean = false
    override var isFinished: Boolean = false
    override var winner: Boolean? = null
    override val field: MutableField = ArrayField(3)
    override var userMark = true
    private var move = 0

    // Внутренняя логика одиночной игры на высокой сложности
    override fun actSingleGameHard(row: Int, col: Int): Boolean {
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
                actAiHard()
            }
        }
        if (field.get(row, col) != null)
            return false
        field.set(row, col, userMark)
        checkWinner()
        checkEnd()
        if (!isFinished) {
            actAiHard()
            checkWinner()
            checkEnd()
        }

        return true
    }

    // Внутренняя логика одиночной игры на нормальной сложности
    override fun actSingleGameNormal(row: Int, col: Int): Boolean {
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
                val n = Random.nextBoolean()
                if (n)
                    actAiEasy()
                else
                    actAiHard()
            }
        }
        if (field.get(row, col) != null)
            return false
        field.set(row, col, userMark)
        checkWinner()
        checkEnd()
        if (!isFinished) {
            val n = Random.nextBoolean()
            if (n)
                actAiEasy()
            else
                actAiHard()
            checkWinner()
            checkEnd()
        }

        return true
    }

    // Внутренняя логика одиночной игры на лёгкой сложности
    override fun actSingleGameEasy(row: Int, col: Int): Boolean {
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
                actAiEasy()
            }
        }
        if (field.get(row, col) != null)
            return false
        field.set(row, col, userMark)
        checkWinner()
        checkEnd()
        if (!isFinished) {
            actAiEasy()
            checkWinner()
            checkEnd()
        }

        return true
    }

    // Внутренняя логика для игры на одном устройстве
    override fun actGameOnOneDevice(row: Int, col: Int): Boolean {
        if (field.get(row, col) != null)
            return false
        field.set(row, col, userMark)
        checkWinner()
        checkEnd()
        userMark = !userMark
        return true
    }

    // Внутренняя логика для онлайн игры
    override fun actMultiplayerGame(row: Int, col: Int, mark: Boolean): Boolean {
        if (field.get(row, col) != null)
            return false
        field.set(row, col, mark)
        checkWinner()
        checkEnd()
        return true
    }

    // Проверка на конец игры
    private fun checkEnd() {
        repeat(field.size) { row ->
            repeat(field.size) { col ->
                if (field.get(row, col) == null) {
                    return
                }
            }
        }
        tie = true
        isFinished = true
    }

    // Проверка на победителя
    private fun checkWinner() {
        if (field.get(0, 0) == userMark && field.get(0, 1) == userMark && field.get(
                0,
                2
            ) == userMark
        ) {
            winner = userMark
            isFinished = true
        } else if (field.get(1, 0) == userMark && field.get(1, 1) == userMark && field.get(
                1,
                2
            ) == userMark
        ) {
            winner = userMark
            isFinished = true
        } else if (field.get(2, 0) == userMark && field.get(2, 1) == userMark && field.get(
                2,
                2
            ) == userMark
        ) {
            winner = userMark
            isFinished = true
        } else if (field.get(0, 0) == userMark && field.get(1, 0) == userMark && field.get(
                2,
                0
            ) == userMark
        ) {
            winner = userMark
            isFinished = true
        } else if (field.get(0, 1) == userMark && field.get(1, 1) == userMark && field.get(
                2,
                1
            ) == userMark
        ) {
            winner = userMark
            isFinished = true
        } else if (field.get(0, 2) == userMark && field.get(1, 2) == userMark && field.get(
                2,
                2
            ) == userMark
        ) {
            winner = userMark
            isFinished = true
        } else if (field.get(0, 0) == userMark && field.get(1, 1) == userMark && field.get(
                2,
                2
            ) == userMark
        ) {
            winner = userMark
            isFinished = true
        } else if (field.get(0, 2) == userMark && field.get(1, 1) == userMark && field.get(
                2,
                0
            ) == userMark
        ) {
            winner = userMark
            isFinished = true
        }

        if (field.get(0, 0) == !userMark && field.get(0, 1) == !userMark && field.get(
                0,
                2
            ) == !userMark
        ) {
            winner = !userMark
            isFinished = true
        } else if (field.get(1, 0) == !userMark && field.get(1, 1) == !userMark && field.get(
                1,
                2
            ) == !userMark
        ) {
            winner = !userMark
            isFinished = true
        } else if (field.get(2, 0) == !userMark && field.get(2, 1) == !userMark && field.get(
                2,
                2
            ) == !userMark
        ) {
            winner = !userMark
            isFinished = true
        } else if (field.get(0, 0) == !userMark && field.get(1, 0) == !userMark && field.get(
                2,
                0
            ) == !userMark
        ) {
            winner = !userMark
            isFinished = true
        } else if (field.get(0, 1) == !userMark && field.get(1, 1) == !userMark && field.get(
                2,
                1
            ) == !userMark
        ) {
            winner = !userMark
            isFinished = true
        } else if (field.get(0, 2) == !userMark && field.get(1, 2) == !userMark && field.get(
                2,
                2
            ) == !userMark
        ) {
            winner = !userMark
            isFinished = true
        } else if (field.get(0, 0) == !userMark && field.get(1, 1) == !userMark && field.get(
                2,
                2
            ) == !userMark
        ) {
            winner = !userMark
            isFinished = true
        } else if (field.get(0, 2) == !userMark && field.get(1, 1) == !userMark && field.get(
                2,
                0
            ) == !userMark
        ) {
            winner = !userMark
            isFinished = true
        }

        return
    }

    // Проверка на выйгрыш на следующем ходу для бота
    private fun checkNextWinAi(): Pair<Int, Int> {
        if (field.get(0, 0) == !userMark && field.get(0, 1) == !userMark && field.get(
                0,
                2
            ) == null
        ) {
            return Pair(0, 2)
        }
        if (field.get(0, 0) == !userMark && field.get(0, 2) == !userMark && field.get(
                0,
                1
            ) == null
        ) {
            return Pair(0, 1)
        }
        if (field.get(0, 1) == !userMark && field.get(0, 2) == !userMark && field.get(
                0,
                0
            ) == null
        ) {
            return Pair(0, 0)
        }
        if (field.get(1, 0) == !userMark && field.get(1, 1) == !userMark && field.get(
                1,
                2
            ) == null
        ) {
            return Pair(1, 2)
        }
        if (field.get(1, 0) == !userMark && field.get(1, 2) == !userMark && field.get(
                1,
                1
            ) == null
        ) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == !userMark && field.get(1, 2) == !userMark && field.get(
                1,
                0
            ) == null
        ) {
            return Pair(1, 0)
        }
        if (field.get(2, 0) == !userMark && field.get(2, 1) == !userMark && field.get(
                2,
                2
            ) == null
        ) {
            return Pair(2, 2)
        }
        if (field.get(2, 0) == !userMark && field.get(2, 2) == !userMark && field.get(
                2,
                1
            ) == null
        ) {
            return Pair(2, 1)
        }
        if (field.get(2, 1) == !userMark && field.get(2, 2) == !userMark && field.get(
                2,
                0
            ) == null
        ) {
            return Pair(2, 0)
        }
        if (field.get(0, 0) == !userMark && field.get(1, 0) == !userMark && field.get(
                2,
                0
            ) == null
        ) {
            return Pair(2, 0)
        }
        if (field.get(0, 0) == !userMark && field.get(2, 0) == !userMark && field.get(
                1,
                0
            ) == null
        ) {
            return Pair(1, 0)
        }
        if (field.get(1, 0) == !userMark && field.get(2, 0) == !userMark && field.get(
                0,
                0
            ) == null
        ) {
            return Pair(0, 0)
        }
        if (field.get(0, 1) == !userMark && field.get(1, 1) == !userMark && field.get(
                2,
                1
            ) == null
        ) {
            return Pair(2, 1)
        }
        if (field.get(0, 1) == !userMark && field.get(2, 1) == !userMark && field.get(
                1,
                1
            ) == null
        ) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == !userMark && field.get(2, 1) == !userMark && field.get(
                0,
                1
            ) == null
        ) {
            return Pair(0, 1)
        }
        if (field.get(0, 2) == !userMark && field.get(1, 2) == !userMark && field.get(
                2,
                2
            ) == null
        ) {
            return Pair(2, 2)
        }
        if (field.get(0, 2) == !userMark && field.get(2, 2) == !userMark && field.get(
                1,
                2
            ) == null
        ) {
            return Pair(1, 2)
        }
        if (field.get(1, 2) == !userMark && field.get(2, 2) == !userMark && field.get(
                0,
                2
            ) == null
        ) {
            return Pair(0, 2)
        }
        if (field.get(0, 0) == !userMark && field.get(1, 1) == !userMark && field.get(
                2,
                2
            ) == null
        ) {
            return Pair(2, 2)
        }
        if (field.get(0, 0) == !userMark && field.get(2, 2) == !userMark && field.get(
                1,
                1
            ) == null
        ) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == !userMark && field.get(2, 2) == !userMark && field.get(
                0,
                0
            ) == null
        ) {
            return Pair(0, 0)
        }
        if (field.get(0, 2) == !userMark && field.get(1, 1) == !userMark && field.get(
                2,
                0
            ) == null
        ) {
            return Pair(2, 0)
        }
        if (field.get(0, 2) == !userMark && field.get(2, 0) == !userMark && field.get(
                1,
                1
            ) == null
        ) {
            return Pair(1, 1)
        }
        if (field.get(1, 1) == !userMark && field.get(2, 0) == !userMark && field.get(
                0,
                2
            ) == null
        ) {
            return Pair(0, 2)
        }

        return Pair(-1, -1)
    }

    // Проверка на выйгрыш на следующем ходу для игрока
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

    // Внутренняя логика лёгкого бота
    private fun actAiEasy() {
        randomSetField()
    }

    // Внутренняя логика сложного бота
    private fun actAiHard() {
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
                } else {
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
                    } else
                        randomSetField()
                } else if ((checkNextWinPlayer().first == -1))
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

    // Рандомное заполнения клетки
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

}

class ArrayField(override val size: Int) : MutableField {

    private val points: Array<Array<Boolean?>> = Array(size) { arrayOfNulls(size) }
    override fun set(row: Int, col: Int, value: Boolean) {
        points[row][col] = value
    }

    override fun get(row: Int, col: Int): Boolean? = points[row][col]
}