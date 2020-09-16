package processor


import java.util.*
import kotlin.ArithmeticException
import kotlin.math.pow

val input = Scanner(System.`in`)
var exit = false
fun main() {
    while (!exit) {
        try {
            choiceParser()
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
        }
    }

}
fun choiceParser() {
    println("1. Add matrices")
    println("2. Multiply matrix to a constant")
    println("3. Multiply matrices")
    println("4. Transpose matrix")
    println("5. Calculate a determinant")
    println("6. Inverse matrix")
    println("0. Exit")
    print("Your choice:")
    val choice = input.nextInt()
    when (choice) {
        0 -> { exit = true; return }
        2 -> {
            print("Enter size of matrix:")
            val rows = input.nextInt()
            val cols = input.nextInt()
            println("Enter matrix:")
            val matrix = scanMatrix(rows, cols)
            print("Enter constant:")
            val scalar = input.nextDouble()
            println("The multiplication result is:")
            println(scalar * matrix)
            return
        }
        4 -> {
            println("1. Main diagonal")
            println("2. Side diagonal")
            println("3. Vertical line")
            println("4. Horizontal line")
            print("Your choice:")
            val type = input.nextInt()
            print("Enter size of matrix:")
            val rows = input.nextInt()
            val cols = input.nextInt()
            println("Enter matrix:")
            val matrix = scanMatrix(rows, cols)
            print(matrix.transpose(type))
            return
        }
        5 , 6 ->  {
            print("Enter size of matrix:")
            val rows = input.nextInt()
            val cols = input.nextInt()
            println("Enter matrix:")
            val matrix = scanMatrix(rows, cols)
            println("The result is:")
            println( if (choice== 5) matrix.determinant() else matrix.inverse())
            return
        }
    }
    print("Enter size of first matrix:")
    val rowA = input.nextInt()
    val colA = input.nextInt()
    println("Enter first matrix:")
    val matA = scanMatrix(rowA, colA)
    print("Enter size of second matrix:")
    val rowB = input.nextInt()
    val colB = input.nextInt()
    println("Enter second matrix:")
    val matB = scanMatrix(rowB, colB)
    if (choice == 1) {
        println("The addition result is:")
        println(matA + matB)
        return
    }
    println("The multiplication result is:")
    println(matA * matB)
    return
}

fun scanMatrix(row: Int, col: Int): Matrix {
    return Matrix(Array(row) { Array(col) { input.nextDouble() }})
}

class  Matrix(private val entries: Array<Array<Double>>) {
    private val rows = entries.size
    private val cols = entries[0].size
    operator fun plus(other: Matrix): Matrix {
        if (rows != other.rows || cols != other.cols) throw ArithmeticException("Matrices must have the same dimension.")
        return Matrix(Array(rows) { i -> Array(cols) { j -> entries[i][j] + other.entries[i][j]} })
    }
    operator fun times(scalar: Double): Matrix {
        return Matrix(Array(rows) { i -> Array(cols) { j -> entries[i][j] * scalar }})
    }
    operator fun times(other: Matrix): Matrix {
        if (cols != other.rows) throw ArithmeticException("Matrix multiplication not defined for these dimensions.")
        return Matrix(Array(rows) {
            i -> Array(other.cols) {
                k -> entries[i].zip(other.entries.map { it[k]}) { a, b -> a * b }.sum()
        }})

    }
    override fun toString(): String {
        var string = ""
        for (row in entries) {
            string += row.joinToString(" ")
            string += "\n"
        }
        return string
    }
    fun transpose(type: Int): Matrix {
        return when (type) {
            4 -> Matrix(Array(rows) { i -> Array(cols) { j -> entries[rows - i - 1][j]}})
            3 -> Matrix(Array(rows) { i -> Array(cols) { j -> entries[i][cols - j - 1]}})
            2 -> Matrix(Array(cols) { j -> Array(rows) { i -> entries[rows - i - 1][cols - j - 1]}})
            else -> Matrix(Array(cols) { j -> Array(rows) { i -> entries[i][j]}})
        }
    }
    fun determinant(): Double {
        if(rows != cols) throw ArithmeticException("Determinant only defined for Square matricies.")
        return when (rows) {
            0 -> 0.0
            1 -> entries[0][0]
            2 -> entries[0][0] * entries[1][1] - entries[0][1] * entries[1][0]
            else -> {
                var det = 0.0
                for (j in 0 until cols) {
                    det += entries[0][j] * coFactor(0, j)
                }
                det
            }
        }
    }
    fun coFactor(i: Int, j: Int): Double {
        return (-1.0).pow( i + j ) * Matrix(Array(rows - 1) {
            r -> Array( cols - 1) { c -> when {
                r < i && c < j -> entries[r][c]
                r < i -> entries[r][c + 1]
                c < j -> entries[r + 1][c]
                else -> entries[r + 1][c + 1]
        }}}).determinant()
    }
    fun adjoint(): Matrix {
        return  Matrix(Array(rows) {i -> Array(cols) { j -> coFactor(i, j) }})
    }
    fun inverse(): Matrix {
        return this.adjoint().transpose(1) * (1 / this.determinant())
    }
}

operator fun Double.times(matrix: Matrix) = matrix * this