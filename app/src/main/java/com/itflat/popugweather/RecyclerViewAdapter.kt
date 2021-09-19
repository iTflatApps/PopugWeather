/*
 * Copyright 2021 iTFlat Apps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itflat.popugweather

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.weather_view.view.*
import java.lang.Exception

class RecyclerViewAdapter(private val context: Context, private val intent: Intent, activity: Activity, var weatherList: MutableList<WeatherModel>, var fullWeatherList:MutableList<String>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun setData(position: Int) {
            itemView.city_name.text = "${weatherList[position].cityName} - ${weatherList[position].cityWeather}"
            itemView.temperature.text = "${weatherList[position].cityTemperature}°C"
            when(weatherList[position].cityWeather){
                "Снег" -> itemView.city_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_snow, 0, 0, 0)
                "Дождь со снегом" -> itemView.city_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_rainwithsnow, 0, 0, 0)
                "Град" -> itemView.city_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_hail, 0, 0, 0)
                "Гроза" -> itemView.city_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_thunderstorm, 0, 0, 0)
                "Сильный дождь" -> itemView.city_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_heavyrain, 0, 0, 0)
                "Лёгкий дождь" -> itemView.city_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_lightrain, 0, 0, 0)
                "Ливни" -> itemView.city_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_showers, 0, 0, 0)
                "Пасмурно" -> itemView.city_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_cloud, 0, 0, 0)
                "Облачно" -> itemView.city_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_cloud, 0, 0, 0)
                "Солнечно" -> itemView.city_name.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_clear, 0, 0, 0)
            }
            itemView.setOnClickListener {
                try {
                    intent.putExtra("city", weatherList[position].cityName)
                    intent.putExtra("weather_img", weatherList[position].cityWeather)
                    intent.putExtra("weather", fullWeatherList[position])
                    ContextCompat.startActivity(context, intent, null)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
        val view=inflater.inflate(R.layout.weather_view, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        holder.setData(position)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }
}