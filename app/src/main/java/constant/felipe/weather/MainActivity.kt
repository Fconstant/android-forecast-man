package constant.felipe.weather

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ForecastMan.ForecastListener {

    lateinit var forecastMan: ForecastMan

    private val forecastListHeader: View by lazy {
        val header = this.layoutInflater.inflate(ForecastAdapter.layoutResource, forecast_list, false)
        header.findViewById<TextView>(R.id.day).typeface = Typeface.DEFAULT_BOLD
        header.findViewById<TextView>(R.id.max_temp).typeface = Typeface.DEFAULT_BOLD
        header.findViewById<TextView>(R.id.min_temp).typeface = Typeface.DEFAULT_BOLD
        header
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        forecastMan = ForecastMan.getInstance(this.applicationContext)
        forecastMan.fetchForecasts(this)

        forecast_list.addHeaderView(this.forecastListHeader)
    }

    override fun onFetchForecast(forecasts: List<Forecast>?) {
        if (forecasts != null) {
            val (todayForecast) = forecasts

            // Setup today
            today_date.text = todayForecast.date
            today_min_temp.text = "Mínimo\n${todayForecast.low}°"
            today_max_temp.text = "Máximo\n${todayForecast.high}°"

            // Prepare ListAdapter
            forecast_list.adapter = ForecastAdapter(this, forecasts.withoutFirst())

        }

        // Stop loading, Show results
        loading_frame.visibility = View.GONE
        content_panel.visibility = View.VISIBLE
    }
}
