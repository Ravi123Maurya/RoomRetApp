package com.ravimaurya.roomret

import android.app.Application
import androidx.room.Room
import com.ravimaurya.roomret.approom.MealDatabase
import com.ravimaurya.roomret.approom.UserBioDatabase

class MainApplication: Application() {

    companion object{
        lateinit var userBioDatabase: UserBioDatabase
        lateinit var mealDatabase: MealDatabase
    }

    override fun onCreate() {
            super.onCreate()

        userBioDatabase = Room.databaseBuilder(
            applicationContext,
            UserBioDatabase::class.java,
            UserBioDatabase.DBNAME
        ).build()
        mealDatabase = Room.databaseBuilder(
            applicationContext,
            MealDatabase::class.java,
            MealDatabase.DBNAME
        ).build()
    }
}