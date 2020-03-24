package com.zahid.coolshoptest.local_db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class UserModelRoom(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var email: String?,
    var password: String?,
    var avatar: String?,
    var token: String?
)
