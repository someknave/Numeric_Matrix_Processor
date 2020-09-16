import java.util.Scanner
import kotlin.math.*

val scanner = Scanner(System.`in`)
fun main() {

    println(scanVector().getAngle(scanVector()))
}
class Vector(val x:Double, val y:Double) {
    fun length() = (x.pow(2) + y.pow(2)).pow(.5)
    fun dotProduct(other: Vector) = x * other.x + y * other.y
    fun getAngle(other: Vector) = (180 / PI) * acos(this.dotProduct(other) /(this.length() * other.length()))
}
fun scanVector() = Vector(scanner.nextDouble(), scanner.nextDouble())




