package com.example.onlineshop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemsModel(
    val title: String = "",
    val categoryId: String = "",
    val description: String = "",
    val price: Int = 0,
    val rating: Double = 0.0,
    val showRecommended: Boolean = false,
    val model: ArrayList<String> = ArrayList(),
    val picUrl: ArrayList<String> = ArrayList(),
    var numberInCart: Int = 0
): Parcelable
