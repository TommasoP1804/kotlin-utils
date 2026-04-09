
import dev.tommasop1804.kutils.classes.measure.MeasureUnit.DataSizeUnit.Companion.TERABYTES
import dev.tommasop1804.kutils.classes.measure.RMeasurement.Companion.ofUnit
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

fun main() {
    val baos = ByteArrayOutputStream()
    ObjectOutputStream(baos).use { oos ->
        oos.writeObject(12 ofUnit TERABYTES)
    }
    println("OK: ${baos.size()} bytes")
}