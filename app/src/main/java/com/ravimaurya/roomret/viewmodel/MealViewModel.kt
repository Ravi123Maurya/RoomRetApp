package com.ravimaurya.roomret.viewmodel

import android.content.ClipDescription
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravimaurya.roomret.MainApplication
import com.ravimaurya.roomret.approom.Category
import com.ravimaurya.roomret.approom.mealApiService
import kotlinx.coroutines.launch

class MealViewModel: ViewModel() {

    val mealDao = MainApplication.mealDatabase.mealDao()
    private var _mealState = mutableStateOf(MealState())
    val mealState: State<MealState> = _mealState
    private var _mealRoomState = mutableStateOf(MealRoomState())
    val mealRoomState: State<MealRoomState> = _mealRoomState

    init {
        getMealsFromInternet()
    }

    fun getMealsFromInternet(){
        viewModelScope.launch {
            try {
                val response = mealApiService.getMeals()

                _mealState.value = mealState.value.copy(
                    meal = response.categories,
                    error = null,
                    loading = false
                )
            }
            catch (e: Exception){
                _mealState.value = mealState.value.copy(
                    error = "Check your internet connection!",
                    loading = false
                )
            }

        }

    }

    fun insertMealToRoom(id: String, category: String, thumb: String, description: String){
        viewModelScope.launch {
            mealDao.insertMeal(Category(id,category,thumb,description))

        }
    }

    fun getMealsFromRoom(){
        viewModelScope.launch {

           _mealRoomState.value = _mealRoomState.value.copy(
               meal = mealDao.getAllMeal()
           )
        }
    }
    fun deleteMealFromRoom(meal: Category){
        viewModelScope.launch {
            mealDao.deleteMeal(meal)
        }
    }

}




/// Meal State
data class  MealState(
    val meal: List<Category> = emptyList(),
    val error: String? = null,
    val loading:Boolean = true
)
data class  MealRoomState(
    val meal: List<Category> = emptyList(),
)