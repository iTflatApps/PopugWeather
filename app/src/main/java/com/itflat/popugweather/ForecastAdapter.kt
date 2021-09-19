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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast_view.view.*
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class ForecastAdapter(private val activity: Activity, private var ForecastList: MutableList<String>): RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    private var inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private var selectedPosition = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(position: Int) {
            var weather = JSONObject(ForecastList[position]).get("weather_state_name").toString()
            when(weather){
                "Snow" -> weather ="Снег"
                "Sleet" -> weather = "Дождь со снегом"
                "Hail" -> weather = "Град"
                "Thunderstorm" -> weather = "Гроза"
                "Heavy Rain" -> weather = "Сильный дождь"
                "Light Rain" -> weather = "Лёгкий дождь"
                "Showers" -> weather = "Ливни"
                "Heavy Cloud" -> weather = "Пасмурно"
                "Light Cloud" -> weather = "Облачно"
                "Clear" -> weather = "Солнечно"
            }
            itemView.weather_forecast.text = weather
            when(weather){
                "Снег" -> itemView.weather_forecast_img.setImageResource(R.drawable.ic_snow)
                "Дождь со снегом" -> itemView.weather_forecast_img.setImageResource(R.drawable.ic_rainwithsnow)
                "Град" -> itemView.weather_forecast_img.setImageResource(R.drawable.ic_hail)
                "Гроза" -> itemView.weather_forecast_img.setImageResource(R.drawable.ic_thunderstorm)
                "Сильный дождь" -> itemView.weather_forecast_img.setImageResource(R.drawable.ic_heavyrain)
                "Лёгкий дождь" -> itemView.weather_forecast_img.setImageResource(R.drawable.ic_lightrain)
                "Ливни" -> itemView.weather_forecast_img.setImageResource(R.drawable.ic_showers)
                "Пасмурно" -> itemView.weather_forecast_img.setImageResource(R.drawable.ic_cloud)
                "Облачно" -> itemView.weather_forecast_img.setImageResource(R.drawable.ic_cloud)
                "Солнечно" -> itemView.weather_forecast_img.setImageResource(R.drawable.ic_clear)
            }
            var date = JSONObject(ForecastList[position]).get("applicable_date").toString()
            val weekDay = getWeekDay(date)
            date=date.takeLast(date.indexOf("-")+1).replace('-', '.')
            date=date.takeLast(date.indexOf('.'))+"."+date.take(date.indexOf("."))
            itemView.weather_forecast.append("\n$date\n$weekDay")
            itemView.setOnClickListener {
                notifyItemChanged(selectedPosition)
                selectedPosition = layoutPosition
                notifyItemChanged(selectedPosition)
                selectedIsChanged()
            }
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastAdapter.ViewHolder {
        val view=inflater.inflate(R.layout.forecast_view, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastAdapter.ViewHolder, position: Int) {
        holder.itemView.isSelected = selectedPosition == position
        holder.setData(position)
    }

    override fun getItemCount(): Int {
        return ForecastList.size
    }

    @SuppressLint("SetTextI18n")
    private fun selectedIsChanged(){
        activity.weather.animate().alpha(0f).setDuration(200).withStartAction {
            activity.weather_img.animate().alpha(0f).duration = 200
            activity.temperature.animate().alpha(0f).duration = 200
            activity.wind.animate().alpha(0f).duration = 200
            activity.pressure.animate().alpha(0f).duration = 200
            activity.humidity.animate().alpha(0f).duration = 200
        }.withEndAction {
            var newWeather=JSONObject(ForecastList[selectedPosition]).get("weather_state_name").toString()
            when(newWeather){
                "Snow" -> newWeather ="Снег"
                "Sleet" -> newWeather = "Дождь со снегом"
                "Hail" -> newWeather = "Град"
                "Thunderstorm" -> newWeather = "Гроза"
                "Heavy Rain" -> newWeather = "Сильный дождь"
                "Light Rain" -> newWeather = "Лёгкий дождь"
                "Showers" -> newWeather = "Ливни"
                "Heavy Cloud" -> newWeather = "Пасмурно"
                "Light Cloud" -> newWeather = "Облачно"
                "Clear" -> newWeather = "Солнечно"
            }
            activity.weather.text=newWeather
            when(newWeather){
                "Снег" -> activity.weather_img.setImageResource(R.drawable.ic_snow)
                "Дождь со снегом" -> activity.weather_img.setImageResource(R.drawable.ic_rainwithsnow)
                "Град" -> activity.weather_img.setImageResource(R.drawable.ic_hail)
                "Гроза" -> activity.weather_img.setImageResource(R.drawable.ic_thunderstorm)
                "Сильный дождь" -> activity.weather_img.setImageResource(R.drawable.ic_heavyrain)
                "Лёгкий дождь" -> activity.weather_img.setImageResource(R.drawable.ic_lightrain)
                "Ливни" -> activity.weather_img.setImageResource(R.drawable.ic_showers)
                "Пасмурно" -> activity.weather_img.setImageResource(R.drawable.ic_cloud)
                "Облачно" -> activity.weather_img.setImageResource(R.drawable.ic_cloud)
                "Солнечно" -> activity.weather_img.setImageResource(R.drawable.ic_clear)
            }
            activity.wind.text = (round(JSONObject(ForecastList[selectedPosition]).get("wind_speed").toString().toDouble()*10) /10).toString()+" м/с"
            activity.temperature.text = round(JSONObject(ForecastList[selectedPosition]).get("the_temp").toString().toDouble()).toString()+"°C"
            activity.humidity.text = JSONObject(ForecastList[selectedPosition]).get("humidity").toString()+"%"
            activity.pressure.text = JSONObject(ForecastList[selectedPosition]).get("air_pressure").toString()+" кПа"
            activity.weather.animate().alpha(1f).duration = 200
            activity.weather_img.animate().alpha(1f).duration = 200
            activity.temperature.animate().alpha(1f).duration = 200
            activity.wind.animate().alpha(1f).duration = 200
            activity.pressure.animate().alpha(1f).duration = 200
            activity.humidity.animate().alpha(1f).duration = 200
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun getWeekDay(date:String):String{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val dateSource = sdf.parse(date)
        val cal = Calendar.getInstance()
        cal.time = dateSource!!

        val dateInfo = DateFormat.getDateInstance(DateFormat.FULL, Locale("ru","RU")).format(cal.time)
        return dateInfo.take(dateInfo.indexOf(','))
    }
}