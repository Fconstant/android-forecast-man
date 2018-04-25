package constant.felipe.weather

fun <E> List<E>.withoutFirst() = this.slice(1 until this.size)

// T(°C) = (T(°F) - 32) × 5/9
fun Int.fromFtoC(): Int = (this - 32) * 5 / 9