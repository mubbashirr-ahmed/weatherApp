package com.example.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.models.Forecastday

class DetailAdapter(private val ctx: Context, private val forecastday: List<Forecastday>) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_future, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.temp.text = forecastday[position].day.maxtemp_c.toString()
        holder.day.text = forecastday[position].date
        holder.desc.text = forecastday[position].day.condition.text
        val url = forecastday[position].day.condition.icon


        url.let {
            val imgUri = url.toUri().buildUpon().scheme("https").build()
            holder.images.load(imgUri)
        }


    }

    override fun getItemCount(): Int {
        return forecastday.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var images: ImageView = itemView.findViewById(R.id.iv_images)
        var temp: TextView = itemView.findViewById(R.id.tvTemp)
        var day: TextView = itemView.findViewById(R.id.tv_day)
        var desc: TextView = itemView.findViewById(R.id.tv_desc)
    }
}
