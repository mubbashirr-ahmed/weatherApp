package com.example.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.models.Hour

class HoursAdapter(private val ctx: Context, private val list: List<Hour>) :
    RecyclerView.Adapter<HoursAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_day, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.t1.text = list[position].time.drop(10)
        holder.t2.text = list[position].temp_c.toString() + "°"
        val url = list[position].condition.icon
            url.let {
                val imgUri = url.toUri().buildUpon().scheme("https").build()
                    holder.images.load(imgUri)
            }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var images: ImageView  = itemView.findViewById(R.id.ivDay)
        var ll: LinearLayout = itemView.findViewById(R.id.ll)
        var t1: TextView = itemView.findViewById(R.id.tvtime)
        var t2: TextView = itemView.findViewById(R.id.tvtemp)


    }
}
