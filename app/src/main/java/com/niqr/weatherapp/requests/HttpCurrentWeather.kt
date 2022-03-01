package com.niqr.weatherlisttest.requests

import io.ktor.client.*
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
        val timezone: Int,
        @SerialName("id")
        val cityId: Int,
        @SerialName("name")
        val cityName: String,
        @SerialName("cod")
        val code: Int
    ) {
        internal fun toCurrentWeather() = CurrentWeather(
            icon = this.weather[0].icon,
            temperature = this.mainInfo.temp.roundToInt(),
            mainWeather = this.weather[0].mainWeather,

            humidity = this.mainInfo.humidity,
            pressure = this.mainInfo.pressure,
            windSpeed = this.wind.speed
        )

        @Serializable
        data class System(
            val type: Int? = null,
            val id: Int? = null,
            val country: String? = null,
            @SerialName("sunrise")
            val sunriseTime: Int = 0,
            @SerialName("sunset")
            val sunsetTime: Int = 0
        )
    }
}

data class CurrentWeather(
    val icon: String,
    val temperature: Int,
    val mainWeather: String,

    val humidity: Int,
    val pressure: Int,
    val windSpeed: Float
)

