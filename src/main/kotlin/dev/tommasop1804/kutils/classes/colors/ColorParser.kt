package dev.tommasop1804.kutils.classes.colors

import dev.tommasop1804.kutils.classes.numbers.Percentage
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.invoke
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

@Suppress("localVariableName", "kutils_substring_as_get_intprogression", "unused")
internal object ColorParser {
    // ---------- RGB ----------
    fun parseRgb(s: String): Color {
        val regex = Regex("""rgb\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*\)""")
        val (r, g, b) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        return Color(r.toInt(), g.toInt(), b.toInt())
    }

    fun parseRgba(s: String): Color {
        val regex = Regex("""rgba\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]*\.?[0-9]+)\s*\)""")
        val (r, g, b, a) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        return Color(r.toInt(), g.toInt(), b.toInt(), parseAlpha(a))
    }

    // ---------- HSL ----------
    fun parseHsl(s: String): Color {
        val regex = Regex("""hsl\(\s*([0-9]+)\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*\)""")
        val (hStr, sStr, lStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        val h = hStr.toInt()
        val sVal = parsePercentOrFraction(sStr)
        val lVal = parsePercentOrFraction(lStr)
        return hslToRgb(h, sVal, lVal, Percentage.FULL)
    }

    fun parseHsla(s: String): Color {
        val regex = Regex("""hsla\(\s*([0-9]+)\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)\s*\)""")
        val (hStr, sStr, lStr, aStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        val h = hStr.toInt()
        val sVal = parsePercentOrFraction(sStr)
        val lVal = parsePercentOrFraction(lStr)
        return hslToRgb(h, sVal, lVal, parseAlpha(aStr))
    }

    fun hslToRgb(h: Int, s: Int, l: Int, alpha: Percentage): Color {
        val H = h % 360 / 360.0
        val S = s / 100.0
        val L = l / 100.0

        val c = (1 - abs(2 * L - 1)) * S
        val x = c * (1 - abs((H * 6) % 2 - 1))
        val m = L - c / 2

        val (r1, g1, b1) = when ((H * 6).toInt()) {
            0 -> Triple(c, x, 0.0)
            1 -> Triple(x, c, 0.0)
            2 -> Triple(0.0, c, x)
            3 -> Triple(0.0, x, c)
            4 -> Triple(x, 0.0, c)
            else -> Triple(c, 0.0, x)
        }

        val r = ((r1 + m) * 255).roundToInt()
        val g = ((g1 + m) * 255).roundToInt()
        val b = ((b1 + m) * 255).roundToInt()
        return Color(r, g, b, alpha)
    }

    // ---------- HSV ----------
    fun parseHsv(s: String): Color {
        val regex = Regex("""hsv\(\s*([0-9]+)\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*\)""")
        val (hStr, sStr, vStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        val h = hStr.toInt()
        val sVal = parsePercentOrFraction(sStr)
        val vVal = parsePercentOrFraction(vStr)
        return hsvToRgb(h, sVal, vVal, Percentage.FULL)
    }

    fun parseHsva(s: String): Color {
        val regex = Regex("""hsva\(\s*([0-9]+)\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)\s*\)""")
        val (hStr, sStr, vStr, aStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        val h = hStr.toInt()
        val sVal = parsePercentOrFraction(sStr)
        val vVal = parsePercentOrFraction(vStr)
        return hsvToRgb(h, sVal, vVal, parseAlpha(aStr))
    }

    fun hsvToRgb(h: Int, s: Int, v: Int, alpha: Percentage): Color {
        val H = h % 360 / 60.0
        val S = s / 100.0
        val V = v / 100.0

        val c = V * S
        val x = c * (1 - abs(H % 2 - 1))
        val m = V - c

        val (r1, g1, b1) = when (H.toInt()) {
            0 -> Triple(c, x, 0.0)
            1 -> Triple(x, c, 0.0)
            2 -> Triple(0.0, c, x)
            3 -> Triple(0.0, x, c)
            4 -> Triple(x, 0.0, c)
            else -> Triple(c, 0.0, x)
        }

        val r = ((r1 + m) * 255).roundToInt()
        val g = ((g1 + m) * 255).roundToInt()
        val b = ((b1 + m) * 255).roundToInt()
        return Color(r, g, b, alpha)
    }

    // ---------- HSB ----------
    fun parseHsb(s: String): Color {
        val regex = Regex("""hsb\(\s*([0-9]+)\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*\)""")
        val (hStr, sStr, bStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        val h = hStr.toInt()
        val sVal = parsePercentOrFraction(sStr)
        val bVal = parsePercentOrFraction(bStr)
        return hsbToRgb(h, sVal, bVal, Percentage.FULL)
    }

    fun parseHsba(s: String): Color {
        val regex = Regex("""hsba\(\s*([0-9]+)\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)\s*\)""")
        val (hStr, sStr, bStr, aStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        val h = hStr.toInt()
        val sVal = parsePercentOrFraction(sStr)
        val bVal = parsePercentOrFraction(bStr)
        return hsbToRgb(h, sVal, bVal, parseAlpha(aStr))
    }

    fun hsbToRgb(h: Int, s: Int, b: Int, alpha: Percentage): Color {
        val H = h % 360 / 360.0
        val S = s / 100.0
        val B = b / 100.0

        val c = B * S
        val x = c * (1 - abs((H * 6) % 2 - 1))
        val m = B - c

        val (r1, g1, b1) = when ((H * 6).toInt()) {
            0 -> Triple(c, x, 0.0)
            1 -> Triple(x, c, 0.0)
            2 -> Triple(0.0, c, x)
            3 -> Triple(0.0, x, c)
            4 -> Triple(x, 0.0, c)
            else -> Triple(c, 0.0, x)
        }

        val r = ((r1 + m) * 255).roundToInt()
        val g = ((g1 + m) * 255).roundToInt()
        val bl = ((b1 + m) * 255).roundToInt()
        return Color(r, g, bl, alpha)
    }


    // ---------- CMYK ----------
    fun parseCmyk(s: String): Color {
        val regex = Regex("""cmyk\(\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*\)""")
        val (cStr, mStr, yStr, kStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        val c = parsePercentOrFraction(cStr)
        val m = parsePercentOrFraction(mStr)
        val y = parsePercentOrFraction(yStr)
        val k = parsePercentOrFraction(kStr)
        return cmykToRgb(c, m, y, k, Percentage.FULL)
    }

    fun parseCmyka(s: String): Color {
        val regex = Regex("""cmyka\(\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)\s*\)""")
        val (cStr, mStr, yStr, kStr, aStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        val c = parsePercentOrFraction(cStr)
        val m = parsePercentOrFraction(mStr)
        val y = parsePercentOrFraction(yStr)
        val k = parsePercentOrFraction(kStr)
        return cmykToRgb(c, m, y, k, parseAlpha(aStr))
    }

    fun cmykToRgb(c: Int, m: Int, y: Int, k: Int, alpha: Percentage): Color {
        val C = c / 100.0
        val M = m / 100.0
        val Y = y / 100.0
        val K = k / 100.0

        val r = ((1 - C) * (1 - K) * 255).roundToInt()
        val g = ((1 - M) * (1 - K) * 255).roundToInt()
        val b = ((1 - Y) * (1 - K) * 255).roundToInt()

        return Color(r, g, b, alpha)
    }

    // ---------- HWB ----------
    fun parseHwb(s: String): Color {
        val regex = Regex("""hwb\(\s*([0-9]+)\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*\)""")
        val (hStr, wStr, bStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        val h = hStr.toInt()
        val w = parsePercentOrFraction(wStr)
        val bl = parsePercentOrFraction(bStr)
        return hwbToRgb(h, w, bl, Percentage.FULL)
    }

    fun parseHwba(s: String): Color {
        val regex = Regex("""hwba\(\s*([0-9]+)\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)%?\s*,\s*([0-9]*\.?[0-9]+)\s*\)""")
        val (hStr, wStr, bStr, aStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")
        val h = hStr.toInt()
        val w = parsePercentOrFraction(wStr)
        val bl = parsePercentOrFraction(bStr)
        return hwbToRgb(h, w, bl, parseAlpha(aStr))
    }

    fun hwbToRgb(h: Int, w: Int, b: Int, alpha: Percentage): Color {
        val rgb = hsvToRgb(h, 100, 100, Percentage.FULL)
        val ratioW = w / 100.0
        val ratioB = b / 100.0
        val ratioC = 1 - ratioW - ratioB

        val r = (rgb.red * ratioC + 255 * ratioW).roundToInt().coerceIn(0, 255)
        val g = (rgb.green * ratioC + 255 * ratioW).roundToInt().coerceIn(0, 255)
        val bl = (rgb.blue * ratioC + 255 * ratioW).roundToInt().coerceIn(0, 255)

        return Color(r, g, bl, alpha)
    }

    // ---------- HEX ----------
    fun parseHex(s: String): Color {
        val hex = s.removePrefix("#").removePrefix("0x")
        return when (hex.length) {
            3 -> {
                val r = "${hex[0]}${hex[0]}".toInt(16)
                val g = "${hex[1]}${hex[1]}".toInt(16)
                val b = "${hex[2]}${hex[2]}".toInt(16)
                Color(r, g, b)
            }
            4 -> {
                val r = "${hex[0]}${hex[0]}".toInt(16)
                val g = "${hex[1]}${hex[1]}".toInt(16)
                val b = "${hex[2]}${hex[2]}".toInt(16)
                val a = "${hex[3]}${hex[3]}".toInt(16) / 255.0
                Color(r, g, b, Percentage(a, true))
            }
            6 -> {
                val r = 2(hex).toInt(16)
                val g = hex.substring(2, 4).toInt(16)
                val b = hex.substring(4, 6).toInt(16)
                Color(r, g, b)
            }
            8 -> {
                val r = 2(hex).toInt(16)
                val g = hex.substring(2, 4).toInt(16)
                val b = hex.substring(4, 6).toInt(16)
                val a = hex.substring(6, 8).toInt(16) / 255.0
                Color(r, g, b, Percentage(a, true))
            }
            else -> throw MalformedInputException("HEX non valido: $s")
        }
    }

    // ---------- XYZ ----------
    fun parseXyz(s: String): Color {
        val regex = Regex("""xyz\(\s*([0-9]*\.?[0-9]+)\s*,\s*([0-9]*\.?[0-9]+)\s*,\s*([0-9]*\.?[0-9]+)\s*\)""")
        val (xStr, yStr, zStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")

        val X = xStr.toDouble() / 100.0
        val Y = yStr.toDouble() / 100.0
        val Z = zStr.toDouble() / 100.0

        var r = X * 3.2406 + Y * -1.5372 + Z * -0.4986
        var g = X * -0.9689 + Y * 1.8758 + Z * 0.0415
        var b = X * 0.0557 + Y * -0.2040 + Z * 1.0570

        fun comp(c: Double): Double =
            if (c > 0.0031308) 1.055 * c.pow(1 / 2.4) - 0.055 else 12.92 * c

        r = comp(r).coerceIn(0.0, 1.0)
        g = comp(g).coerceIn(0.0, 1.0)
        b = comp(b).coerceIn(0.0, 1.0)

        return Color((r * 255).roundToInt(), (g * 255).roundToInt(), (b * 255).roundToInt())
    }

    // ---------- LAB ----------
    fun parseLab(s: String): Color {
        val regex = Regex("""lab\(\s*(-?[0-9]*\.?[0-9]+)\s*,\s*(-?[0-9]*\.?[0-9]+)\s*,\s*(-?[0-9]*\.?[0-9]+)\s*\)""")
        val (lStr, aStr, bStr) = regex.matchEntire(s)?.destructured
            ?: throw MalformedInputException("The format is not valid:  $s")

        val L = lStr.toDouble()
        val A = aStr.toDouble()
        val B = bStr.toDouble()

        return labToRgb(L, A, B, 1.0)
    }

    // ---------- Helpers for LAB/LCH path ----------
    @Suppress("SameParameterValue")
    private fun labToRgb(L: Double, A: Double, B: Double, alpha: Double): Color {
        val refX = 95.047
        val refY = 100.000
        val refZ = 108.883

        val y = (L + 16) / 116.0
        val x = A / 500.0 + y
        val z = y - B / 200.0

        fun fInv(t: Double): Double = if (t.pow(3) > 0.008856) t.pow(3) else (t - 16 / 116.0) / 7.787

        val X = refX * fInv(x)
        val Y = refY * fInv(y)
        val Z = refZ * fInv(z)

        // Reuse parseXyz to convert to sRGB
        val color = parseXyz("xyz($X,$Y,$Z)")
        return Color(color.red, color.green, color.blue, Percentage(alpha, true))
    }

    // ---------- Helpers ----------
    fun parseAlpha(a: String): Percentage {
        val value = a.toDouble()
        return Percentage(if (value <= 1f) value.coerceIn(0.0, 1.0) else (value / 255f).coerceIn(0.0, 1.0), true)
    }

    private fun parsePercentOrFraction(valueStr: String): Int {
        val v = valueStr.toDouble()
        val pct = if (v <= 1.0) (v * 100.0) else v
        return pct.roundToInt().coerceIn(0, 100)
    }
}
