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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.weather_view.view.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.round

class WeatherActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        window.navigationBarColor = getColor(R.color.appAccent)

        city_name_forecast.text=intent.getStringExtra("city")
        weather.text=intent.getStringExtra("weather_img")
        when(intent.getStringExtra("weather_img")){
            "Снег" -> weather_img.setImageResource(R.drawable.ic_snow)
            "Дождь со снегом" -> weather_img.setImageResource(R.drawable.ic_rainwithsnow)
            "Град" -> weather_img.setImageResource(R.drawable.ic_hail)
            "Гроза" -> weather_img.setImageResource(R.drawable.ic_thunderstorm)
            "Сильный дождь" -> weather_img.setImageResource(R.drawable.ic_heavyrain)
            "Лёгкий дождь" -> weather_img.setImageResource(R.drawable.ic_lightrain)
            "Ливни" -> weather_img.setImageResource(R.drawable.ic_showers)
            "Пасмурно" -> weather_img.setImageResource(R.drawable.ic_cloud)
            "Облачно" -> weather_img.setImageResource(R.drawable.ic_cloud)
            "Солнечно" -> weather_img.setImageResource(R.drawable.ic_clear)
        }
        wind.text = (round(((JSONObject(intent.getStringExtra("weather")!!).get("consolidated_weather")as JSONArray)[0] as JSONObject).get("wind_speed").toString().toDouble()*10)/10).toString()+" м/с"
        temperature.text = round(((JSONObject(intent.getStringExtra("weather")!!).get("consolidated_weather")as JSONArray)[0] as JSONObject).get("the_temp").toString().toDouble()).toString()+"°C"
        humidity.text = ((JSONObject(intent.getStringExtra("weather")!!).get("consolidated_weather") as JSONArray)[0] as JSONObject).get("humidity").toString()+"%"
        pressure.text = ((JSONObject(intent.getStringExtra("weather")!!).get("consolidated_weather") as JSONArray)[0] as JSONObject).get("air_pressure").toString()+" кПа"

    }

    override fun onResume() {
        super.onResume()
        val info=JSONObject(intent.getStringExtra("weather")!!)
        val lst= mutableListOf(((info.get("consolidated_weather") as JSONArray)[0] as JSONObject).toString(), ((info.get("consolidated_weather") as JSONArray)[1] as JSONObject).toString(), ((info.get("consolidated_weather") as JSONArray)[2] as JSONObject).toString(), ((info.get("consolidated_weather") as JSONArray)[3] as JSONObject).toString(), ((info.get("consolidated_weather") as JSONArray)[4] as JSONObject).toString(), ((info.get("consolidated_weather") as JSONArray)[5] as JSONObject).toString())
        val layoutManager =
            LinearLayoutManager(this@WeatherActivity, LinearLayoutManager.HORIZONTAL, false)
        forecast.layoutManager = layoutManager
        forecast.adapter=ForecastAdapter(this, lst)
    }
}