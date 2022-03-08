package com.niqr.weatherlisttest.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

//-------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------
@Serializable
sealed class HttpCurrentWeather: HttpApiShared() {

    @Serializable
    data class Body(
        @SerialName("coord")
        val coordinates: Coordinates,
        @Serializable(with = Weather.ListSerializer::class)
        val weather: List<Weather>,
        val base: String,
        @SerialName("main")
        val mainInfo: MainInfo,
        val visibility: Int,
        val wind: Wind = Wind(),
        val clouds: Clouds = Clouds(),
        val rain: Rain = Rain(),
        val snow: Snow = Snow(),
        @SerialName("dt")
        val date: Long,
        @SerialName("sys")
        val system: System,
        val timezone: Long,
        @SerialName("id")
        val cityId: Int,
        @SerialName("name")
        val cityName: String?,
        @SerialName("cod")
        val code: Int
    ) {
        internal fun toCurrentWeather() = CurrentWeather(
            icon = this.weather[0].icon,
            temperature = this.mainInfo.temp.roundToInt(),
            feelsLike = this.mainInfo.feelsLike.roundToInt(),
            mainWeather = this.weather[0].mainWeather,
            cityName = this.cityName,
            country = this.system.country,

            humidity = this.mainInfo.humidity,
            pressure = this.mainInfo.pressure,
            windSpeed = this.wind.speed,

            sunriseTime = "${ if (this.system.sunriseTime.toLocalDateTime().hour < 10) "0" else ""}${this.system.sunriseTime.toLocalDateTime().hour}:" +
                    "${this.system.sunriseTime.toLocalDateTime().minute}${ if (this.system.sunriseTime.toLocalDateTime().minute < 10) "0" else ""}",
            sunsetTime = "${ if (this.system.sunsetTime.toLocalDateTime().hour < 10) "0" else ""}${this.system.sunsetTime.toLocalDateTime().hour}:" +
                    "${this.system.sunsetTime.toLocalDateTime().minute}${ if (this.system.sunsetTime.toLocalDateTime().minute < 10) "0" else ""}",
        )

        @Serializable
        data class System(
            val type: Int? = null,
            val id: Int? = null,
            val country: String? = null,
            @SerialName("sunrise")
            val sunriseTime: Long = 0,
            @SerialName("sunset")
            val sunsetTime: Long = 0
        )
    }
}

data class CurrentWeather(
    val icon: String,
    val temperature: Int,
    val feelsLike: Int,
    val mainWeather: String,
    val cityName: String?,
    val country: String?,

    val humidity: Int,
    val pressure: Int,
    val windSpeed: Float,

    val sunriseTime: String,
    val sunsetTime: String
)

