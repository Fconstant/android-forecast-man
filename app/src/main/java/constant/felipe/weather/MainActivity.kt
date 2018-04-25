package constant.felipe.weather

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ForecastMan.OnForecastListener {

    lateinit var forecastMan: ForecastMan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        forecastMan = ForecastMan.getInstance(this.applicationContext)
        forecastMan.fetchForecasts(this)
    }

    override fun onFetchForecast(forecasts: List<Forecast>?) {
        if (forecasts != null) {
            val (todayForecast) = forecasts
            today_date.text = todayForecast.date
            today_min_temp.text = "Mínimo\n${todayForecast.low}°"
            today_max_temp.text = "Máximo\n${todayForecast.high}°"
        } else {
            print("Forecasts is null")
        }
    }
}
