package constant.felipe.weather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ForecastAdapter(context: Context, val data: List<Forecast>) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getItemId(position: Int): Long = getItem(position).code.toLong()
    override fun getCount(): Int = data.size
    override fun getItem(position: Int): Forecast = data[position]
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        val view = if (convertView == null) {
            val inflated = inflater.inflate(layoutResource, parent, false)
            holder = ViewHolder(inflated)
            inflated.tag = holder
            inflated
        } else {
            holder = convertView.tag as ViewHolder
            convertView
        }

        val item = getItem(position)
        holder.day.text = item.date
        holder.maxTemp.text = "${item.high}°"
        holder.minTemp.text = "${item.low}°"

        return view
    }

    private class ViewHolder(
        view: View
    ) {
        val day: TextView = view.findViewById(R.id.day)
        val minTemp: TextView = view.findViewById(R.id.min_temp)
        val maxTemp: TextView = view.findViewById(R.id.max_temp)
    }

    companion object {
        const val layoutResource = R.layout.list_item
    }
}