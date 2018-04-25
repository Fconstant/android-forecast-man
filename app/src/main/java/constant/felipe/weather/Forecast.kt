package constant.felipe.weather

interface Forecast {
    val code: String
    val date: String
    val high: Int
    val low: Int
}