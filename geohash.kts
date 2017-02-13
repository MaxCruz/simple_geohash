val bitLength = 5
val precision = 9
val characterMap = charArrayOf (
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
        'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
)

println(encode(42.6, -5.6))

fun encode(latitude: Double, longitude: Double): String {
    val latitudeBits = getBits(latitude, -90.0, 90.0)
    val longitudeBits = getBits(longitude, -180.0, 180.0)
    val mixLists = longitudeBits.mapIndexed { i, b -> listOf(b, latitudeBits[i]) }.flatten()
    val chars = mixLists.batch(5).map { it.toBooleanArray().toInt() }.map { characterMap[it] }
    return String(chars.toCharArray())
}

fun getBits(value: Double, low: Double, high: Double): BooleanArray {
    val length = (precision * bitLength) / 2
    var currentLow = low
    var currentHigh = high
    return (0..length - 1)
            .map {
                val division = (currentLow + currentHigh) / 2
                val current = (value >= division)
                if (current) currentLow = division
                else currentHigh = division
                current
            }
            .toBooleanArray()
}

fun BooleanArray.toInt(): Int {
    var n: Int = 0
    this.forEach { n = (n shl 1) or if (it) 1 else 0 }
    return n
}

fun <T> Iterable<T>.batch(chunkSize: Int): List<List<T>> {
    return mapIndexed { i, item -> i to item }
            .groupBy { it.first / chunkSize }
            .map { it.value.map { it.second } }
}
