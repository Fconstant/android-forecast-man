package constant.felipe.weather

import android.annotation.SuppressLint
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ForecastMan private constructor(private val context: Context) {

    interface OnForecastListener {
        fun onFetchForecast(forecasts: List<Forecast>?)
    }

    // T(°C) = (T(°F) - 32) × 5/9
    private fun convertFtoC(temperature: Int): Int = (temperature - 32) * 5 / 9

    private fun parseJsonArrayToForecastList(array: JSONArray): List<Forecast> {
        val mappedList = ArrayList<Forecast>()
        for (i in 0 until array.length() - 1) {
            val curJsonObject = array.getJSONObject(i)
            mappedList.add(object : Forecast {
                override val code = curJsonObject.getString("code")
                override val date = curJsonObject.getString("date")
                override val high = convertFtoC(curJsonObject.getInt("high"))
                override val low = convertFtoC(curJsonObject.getInt("low"))
            })
        }
        return mappedList
    }

    fun fetchForecasts(callback: OnForecastListener) {
        val queue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                Response.Listener<JSONObject> {
                    val forecasts = try {
                        print(it.toString(4))
                        parseJsonArrayToForecastList(
                            it
                                .getJSONObject("query")
                                .getJSONObject("results")
                                .getJSONObject("channel")
                                .getJSONObject("item")
                                .getJSONArray("forecast")
                        )
                    } catch(e: TypeCastException) {
                        null
                    }
                    callback.onFetchForecast(forecasts)
                },
                Response.ErrorListener {
                    callback.onFetchForecast(null)
                }
        )
        queue.add(request)
    }

    companion object {
        private const val apiUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text=\"São%20Carlos,%20SP\")&format=json&env=store://datatables.org/alltableswithkeys"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var sInstance: ForecastMan? = null

        fun getInstance(context: Context) =
                sInstance ?: synchronized(this) {
                    sInstance ?: ForecastMan(context)
                }

    }
}