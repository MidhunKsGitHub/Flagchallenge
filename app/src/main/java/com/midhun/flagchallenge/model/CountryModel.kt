package com.midhun.flagchallenge.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class CountryModel {
    var country_name: String? = null
    var id: Int = 0
}