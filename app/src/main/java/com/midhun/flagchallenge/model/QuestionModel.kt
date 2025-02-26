package com.midhun.flagchallenge.model

import kotlinx.serialization.Serializable

@Serializable
class QuestionModel {
    var answer_id: Int = 0
    var countries: ArrayList<CountryModel>? = null
    var country_code: String? = null
}