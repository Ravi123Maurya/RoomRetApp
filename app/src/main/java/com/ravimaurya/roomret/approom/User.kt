package com.ravimaurya.roomret.approom

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import retrofit2.http.GET

/// UserBio
@Entity("userbio")
data class UserBio(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var age: String,
    var bio: String
)


//// User Dao
@Dao
interface UserBioDao{

    @Query("Select *from userbio")
    suspend fun getAllUserBio(): List<UserBio>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userBio: UserBio)

    @Query("Delete from userbio where id = :id")
    suspend fun deleteUserBio(id: Int)
}

@Database(entities = [UserBio::class], version = 1)
abstract class UserBioDatabase: RoomDatabase(){

    abstract fun userBioDao(): UserBioDao

    companion object {
        const val DBNAME = "userbiodatabase"
    }
}






//        @Volatile
//        private var Instance: UserBioDatabase? = null
//
//        fun getDatabase(context: Context): UserBioDatabase{
//            return Instance?: synchronized(this){
//                Room.databaseBuilder(
//                    context,
//                    UserBioDatabase::class.java,
//                    DBNAME
//                )
//                    .build()
//                    .also { Instance = it }
//            }
//
//        }


/// Meal
@Entity("meal")
data class Category(
    @PrimaryKey
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

// categories variable name should same as API json object
data class MealResponse(val categories: List<Category>)

@Dao
interface MealDao{
    @Query("SELECT *FROM meal")
    suspend fun getAllMeal(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Category)

    @Delete
    suspend fun deleteMeal(meal: Category)
}

@Database(
    entities = [Category::class],
    version = 1
)
abstract class MealDatabase: RoomDatabase(){

    abstract fun mealDao(): MealDao

    companion object{
        const val DBNAME = "mealdatabase"
    }
}