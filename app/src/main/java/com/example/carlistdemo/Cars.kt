package com.example.carlistdemo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cars (
    var cars: List<Car>
)
@Serializable
data class Car (
    var id: Long,
    var car: String,

    @SerialName("car_model")
    var car_model: String,

    @SerialName("car_color")
    var car_color: String,

    @SerialName("car_model_year")
    var car_model_year: Long,

    @SerialName("car_vin")
    var car_vin: String,

    var price: String,
    var availability: Boolean
)
