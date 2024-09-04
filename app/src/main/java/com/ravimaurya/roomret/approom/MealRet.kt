package com.ravimaurya.roomret.approom

import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET




private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val mealApiService: MealApiService = retrofit.create(MealApiService::class.java)

interface MealApiService{
    @GET("categories.php")
    suspend fun getMeals(): MealResponse
}