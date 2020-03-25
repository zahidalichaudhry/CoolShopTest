package com.zahid.coolshoptest.local_db.dao

import androidx.room.*
import com.zahid.coolshoptest.local_db.entity.UserModelRoom

@Dao
interface UserModelDao {

    @Insert
    fun insert(vararg userModelRoom: UserModelRoom):List<Long>

    @Query("UPDATE UserModelRoom SET token = :newToken  WHERE id=:userId")
    fun updateToken(userId: Long,newToken:String)

    @Query("SELECT * FROM UserModelRoom WHERE id=:userId")
    fun getUser(userId:Long): List<UserModelRoom>

    @Query("SELECT * FROM UserModelRoom WHERE email=:email AND password=:password")
    fun checkUserExist(email:String,password:String):List<UserModelRoom>

    @Query("UPDATE UserModelRoom SET avatar = :avatar  WHERE id=:userId")
    fun updateAvatar(userId: Long,avatar:String)

    @Query("SELECT * FROM UserModelRoom WHERE token=:token")
    fun checkToken(token:String): List<UserModelRoom>

    @Query("SELECT * FROM UserModelRoom")
    fun allusers(): List<UserModelRoom>
}