package com.example.projectocursoscesae.model


data class Course(
    var id: Int = 0,
    var name: String,
    var location: String,
    var startDate: String,
    var endDate: String,
    var price: String,
    var duration: String,
    var edition: String,
    var imagePath: String = ""
)