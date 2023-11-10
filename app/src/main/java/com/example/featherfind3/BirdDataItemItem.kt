package com.example.featherfind3

data class BirdDataItemItem(
    val abundance_mean: Double,
    val common_name: String,
    val end_date: String,
    val prediction_year: Int,
    val range_days_occupation: Int,
    val range_percent_occupied: Double,
    val range_total_percent: Double,
    val region_code: String,
    val region_name: String,
    val region_type: String,
    val scientific_name: String,
    val season: String,
    val species_code: String,
    val start_date: String,
    val total_pop_percent: Double
)