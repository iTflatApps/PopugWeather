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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_tab.view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.math.roundToInt

class TSJFragment(private val activity: Activity, private val mContext: Context) : Fragment() {

    private var adapter:RecyclerViewAdapter?=null
    private var list= mutableListOf<WeatherModel>()
    private var fullList= mutableListOf<String>()
    private var mView:View? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.fragment_tab, null)
        mView!!.swipe_refresh.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.progress_1), ContextCompat.getColor(mContext,R.color.progress_2), ContextCompat.getColor(mContext,R.color.background_accent))

        val llm = GridLayoutManager(mContext,1)
        mView!!.city_list.layoutManager=llm
        if (adapter==null){
            adapter = RecyclerViewAdapter(mContext, Intent(mContext, WeatherActivity::class.java), activity, mutableListOf(), mutableListOf())
        }

        mView!!.city_list.adapter=adapter

        mView!!.swipe_refresh.isRefreshing = true
        getWeather("2457170/", "Нижний Ква")
        getWeather("116545/", "Болотово")
        getWeather("8775/", "Узкоротовск")
        getWeather("2383489/", "Водоносинск")
        getWeather("2402292/", "Срединск")
        getWeather("2449808/", "Озёрск")
        getWeather("3534/", "Мухаедово")
        getWeather("2475687/", "Жабнаул")
        getWeather("2364559/", "Орал-На-Болоте")
        getWeather("1099805/", "Мухаедово")

        mView!!.swipe_refresh.setOnRefreshListener {
            mView!!.city_list.suppressLayout(true)
            list.clear()
            fullList.clear()
            getWeather("2457170/", "Нижний Ква")
            getWeather("116545/", "Болотово")
            getWeather("8775/", "Узкоротовск")
            getWeather("2383489/", "Водоносинск")
            getWeather("2402292/", "Срединск")
            getWeather("2449808/", "Озёрск")
            getWeather("3534/", "Мухаедово")
            getWeather("2475687/", "Жабнаул")
            getWeather("2364559/", "Орал-На-Болоте")
            getWeather("1099805/", "Мухаедово")
        }
        return mView!!
    }
    private fun getWeather(cityCode:String, cityName:String) {
        val url = "https://www.metaweather.com/api/location/$cityCode"
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    val fullInfo= JSONObject(response.body!!.string())
                    val info= ((fullInfo.get("consolidated_weather") as JSONArray)[0] as JSONObject)
                    val weather=WeatherModel(cityName, info.get("weather_state_name").toString(), info.get("the_temp").toString().toDouble().roundToInt().toString())
                    when(weather.cityWeather){
                        "Snow" -> weather.cityWeather="Снег"
                        "Sleet" -> weather.cityWeather= "Дождь со снегом"
                        "Hail" -> weather.cityWeather= "Град"
                        "Thunderstorm" -> weather.cityWeather= "Гроза"
                        "Heavy Rain" -> weather.cityWeather= "Сильный дождь"
                        "Light Rain" -> weather.cityWeather= "Лёгкий дождь"
                        "Showers" -> weather.cityWeather= "Ливни"
                        "Heavy Cloud" -> weather.cityWeather= "Пасмурно"
                        "Light Cloud" -> weather.cityWeather= "Облачно"
                        "Clear" -> weather.cityWeather= "Солнечно"
                    }
                    activity.runOnUiThread {
                        list.add(weather)
                        fullList.add(fullInfo.toString())
                        if (list.size == 10){
                            if (adapter?.weatherList?.isEmpty() == true){
                                mView!!.swipe_refresh.isRefreshing = false
                                adapter?.weatherList=list
                                adapter?.fullWeatherList=fullList
                                for (i in list.indices){
                                    adapter?.notifyItemInserted(i)
                                }
                            }
                            else{
                                mView!!.swipe_refresh.isRefreshing = false
                                adapter?.weatherList=list
                                adapter?.fullWeatherList=fullList
                                for (i in list.indices){
                                    adapter?.notifyItemChanged(i)
                                }
                            }
                            mView!!.city_list.suppressLayout(false)
                        }
                    }
                }
            }
        })
    }
}