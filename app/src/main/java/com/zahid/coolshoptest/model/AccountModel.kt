package com.zahid.coolshoptest.model

import com.google.gson.annotations.SerializedName


data class AccountModel(
    @SerializedName("avatar_url")
    var avatarUrl: String?,
    var email: String?,
    var password: String?
)