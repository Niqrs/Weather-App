package com.niqr.weatherlisttest.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.serializer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

sealed class HttpApiShared {
    @Serializable
    data class Coordinates(
        @SerialName("lat")
        val latitude: Float = 0f,
        @SerialName("lon")
        val longitude: Float = 0f
    )

    @Serializable
    data class Weather(
        val id: Int,
        @SerialName("main") val mainWeather: String,
        val description: String,
        val icon: String
    ) {
        object ListSerializer : JsonTransformingSerializer<List<Weather>>(ListSerializer(serializer())) {
            override fun transformSerialize(element: JsonElement): JsonElement {
                require(element is JsonArray) // this serializer is used only with lists
                return element.singleOrNull() ?: element
            }
        }
    }

    @Serializable
    data class MainInfo(
        val temp: Float = 0f,
        @SerialName("feels_like")
        val feelsLike: Float = 0f,
        @SerialName("temp_min")
        val tempMin: Float = 0f,
        @SerialName("temp_max")
        val tempMax: Float = 0f,
        val pressure: Int,
        @SerialName("sea_level")
        val seaLevelPressure: Int = pressure,
        @SerialName("grnd_level")
        val groundLevelPressure: Int = pressure,
        val humidity: Int = 0,
        @SerialName("temp_kf")
        val tempKf: Float = 0f
    )

    @Serializable
    data class Wind(
        val speed: Float = 0f,
        @SerialName("deg")
        val degrees: Int = 0,
        val gust: Float? = 0f
    )

    @Serializable
    data class Clouds(val all: Int = 0)

    @Serializable
    data class Rain(
        @SerialName("1h")
        val oneHour: Float = 0f,
        @SerialName("3h")
        val threeHours: Float = 0f,
    )

    @Serializable
    data class Snow(
        @SerialName("1h")
        val oneHour: Float = 0f,
        @SerialName("3h")
        val threeHours: Float = 0f,
    )
}

fun Long.toLocalDateTime(): LocalDateTime = Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault()).toLocalDateTime()