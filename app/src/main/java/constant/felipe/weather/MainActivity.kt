package constant.felipe.weather

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ForecastMan.ForecastListener {

    private lateinit var forecastMan: ForecastMan
    private var forecasts: List<Forecast>? = null

    private val forecastListHeader: View by lazy {
        val header = this.layoutInflater.inflate(ForecastAdapter.layoutResource, forecast_list, false)
        header.findViewById<TextView>(R.id.day).typeface = Typeface.DEFAULT_BOLD
        header.findViewById<TextView>(R.id.max_temp).typeface = Typeface.DEFAULT_BOLD
        header.findViewById<TextView>(R.id.min_temp).typeface = Typeface.DEFAULT_BOLD
        header
    }

    private fun setLoading(loading: Boolean): Unit {
        loading_frame.visibility = if (loading) View.VISIBLE else View.GONE
        content_panel.visibility = if (loading) View.GONE else View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        forecastMan = ForecastMan.getInstance(this.applicationContext)
        val forecasts = savedInstanceState?.getSerializable(FORECASTS_KEY) as List<Forecast>?
        if (forecasts != null) {
            setLoading(false)
            this.onFetchForecast(forecasts)
        } else {
            setLoading(true)
            forecastMan.fetchForecasts(this)
        }

        forecast_list.addHeaderView(this.forecastListHeader)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putSerializable(FORECASTS_KEY, forecasts as ArrayList<Forecast>)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onFetchForecast(forecasts: List<Forecast>?) {
        if (forecasts != null) {
            this.forecasts = forecasts
            val (todayForecast) = forecasts

            // Setup today
            today_date.text = todayForecast.date
            today_min_temp.text = "Mínimo\n${todayForecast.low}°"
            today_max_temp.text = "Máximo\n${todayForecast.high}°"

            // Prepare ListAdapter
            forecast_list.adapter = ForecastAdapter(this, forecasts.withoutFirst())

        }

        // Stop loading, Show results
        setLoading(false)
    }

    companion object {
        private const val FORECASTS_KEY = "@Forecasts"
    }
}
