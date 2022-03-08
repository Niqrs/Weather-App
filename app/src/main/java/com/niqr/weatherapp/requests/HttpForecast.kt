package com.niqr.weatherlisttest.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import kotlin.math.roundToInt

sealed class HttpForecast: HttpApiShared() {
    @Serializable
    data class Body(
        @SerialName("cod")
        val code: Int,
        val message: String,
        @SerialName("cnt")
        val count: Int, //Count of forecasts
        @SerialName("list")
        val forecasts: List<Forecast>,
        val city: City? = null,
    ) {
        internal fun toForecasts() = this.forecasts.map {
            val currentDayOfWeek = LocalDateTime.now().dayOfWeek.toString()
            val forecastDayOfWeek = if (currentDayOfWeek == it.date.toLocalDateTime().dayOfWeek.toString()) {
                "TODAY"
            } else {
                it.date.toLocalDateTime().dayOfWeek.toString()
            }
            WeatherForecast(
                icon = it.weather[0].icon,
                time = "${ if (it.date.toLocalDateTime().hour < 10) "0" else ""}${it.date.toLocalDateTime().hour}:00",
                weather = it.weather[0].mainWeather,
                temperature = it.mainInfo.temp.roundToInt(),
                dayOfWeek = forecastDayOfWeek
            )
        }
    }

    @Serializable
    data class Forecast(
        @SerialName("dt")
        val date: Long,
        @SerialName("main")
        val mainInfo: HttpApiShared.MainInfo,
        @Serializable(with = HttpApiShared.Weather.ListSerializer::class)
        val weather: List<HttpApiShared.Weather>,
        val clouds: HttpApiShared.Clouds = HttpApiShared.Clouds(),
        val wind: HttpApiShared.Wind = HttpApiShared.Wind(),
        val visibility: Int,
        @SerialName("pop")
        val probabilityOfPrecipitation: Float = 0f,
        val rain: HttpApiShared.Rain = HttpApiShared.Rain(),
        val snow: HttpApiShared.Snow = HttpApiShared.Snow(),
        @SerialName("sys")
        val system: System,
        @SerialName("dt_txt")
        val dateTxt: String,
    ) {

        @Serializable
        data class System(
            @SerialName("pod")
            val parOfDay: String
        )
    }

    @Serializable
    data class City(
        val id: Int,
        val name: String,
        @SerialName("coord")
        val coordinates: HttpApiShared.Coordinates,
        val country: String,
        val population: Int,
        val timezone: Int,
        val sunrise: Int,
        val sunset: Int
    )
}

data class WeatherForecast(
    val icon: String,
    val time: String,
    val weather: String,
    val temperature: Int,
    val dayOfWeek: String
)

