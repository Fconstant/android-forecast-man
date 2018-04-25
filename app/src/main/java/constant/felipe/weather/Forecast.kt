package constant.felipe.weather

import java.io.Serializable

data class Forecast(
    val code: String,
    val date: String,
    val high: Int,
    val low: Int
) : Serializable