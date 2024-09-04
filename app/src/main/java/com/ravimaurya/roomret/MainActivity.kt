package com.ravimaurya.roomret

import android.content.ClipDescription
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.intListOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.ravimaurya.roomret.approom.Category
import com.ravimaurya.roomret.approom.UserBio
import com.ravimaurya.roomret.ui.theme.RoomRetTheme
import com.ravimaurya.roomret.viewmodel.MealViewModel
import com.ravimaurya.roomret.viewmodel.RoomViewModel
import com.ravimaurya.roomret.viewmodel.UserBioViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            RoomRetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = "room") {

                        composable("home"){
                            HomeScreen(navController)
                        }

                        composable("room"){
                            RoomScreen(navController)
                        }
                        composable("meal"){
                            MealScreen(navController)
                        }
                        composable("mealroom"){
                            MealRoomScreen(navController)
                        }
                        composable(
                            route = "detailscreen?mealThumb={mealThumb}&mealCategory={mealCategory}&mealDescription={mealDescription}",
                            arguments = listOf(
                                navArgument("mealThumb"){
                                    type = NavType.StringType
                                    defaultValue = ""
                                } ,
                                navArgument("mealCategory"){
                                    type = NavType.StringType
                                    defaultValue = "Category"
                                },
                                navArgument("mealDescription"){
                                    type = NavType.StringType
                                    defaultValue = "description"
                                },

                            )
                        ){
                            DetailScreen(
                                navController = navController,
                                mealThumb = it.arguments?.getString("mealThumb"),
                                mealCategory = it.arguments?.getString("mealCategory"),
                                mealDescription = it.arguments?.getString("mealDescription")
                            )
                        }
                    }
                }
            }
        }
    }
}
// Home(Input Screen), Room, UserBioItem, Meal, MealItem, MealRoom, Detail


// HOme Screen
@Composable
fun HomeScreen(navController: NavController) {

    val viewModel = UserBioViewModel()

    val context = LocalContext.current
    var name by remember {
        mutableStateOf("")
    }
    var age by remember {
        mutableStateOf("")
    }
    var bio by remember {
        mutableStateOf("")
    }
    var isNameAgeBioEmpty by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Text(text = "Main Screen", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            label = { Text(text = "Name")},
            placeholder = { Text(text = "Enter name")},
            value = name,
            onValueChange ={
                name = it
            })
Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            label = { Text(text = "Age")},
            placeholder = { Text(text = "Enter age")},
            value = age,
            onValueChange ={
                age = it
            })
Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            label = { Text(text = "Bio")},
            placeholder = { Text(text = "Enter bio")},
            value = bio,
            onValueChange ={
                bio = it
            })

        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            //Click to save
            isNameAgeBioEmpty = (name.isEmpty() || age.isEmpty() || bio.isEmpty())
            if(isNameAgeBioEmpty){
                 Toast.makeText(context, "Name, Age, Bio can't be empty", Toast.LENGTH_LONG).show()
            }
            else{
                 Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show()
                 viewModel.onSaveClick(name, age, bio)
                name = ""
                age = ""
                bio = ""
                isNameAgeBioEmpty = true
            }
        }) {
            Text(text = "Save")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            //Click to go to see user bio
            navController.navigate("room"){
                popUpTo("meal"){
                    inclusive = true
                }
            }
        }) {
            Text(text = "Go to Room")
        }

    }

}

// Room Screen
@Composable
fun RoomScreen(navController: NavController) {

    val roomViewModel: RoomViewModel = viewModel()
    val userBio = roomViewModel.userBioState.value.userBio
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Room Screen", fontSize = 24.sp)
        Button(onClick = { 
            // Click to go to home
            navController.navigate("home"){
                popUpTo("meal"){
                    inclusive = true
                }
            }
        }) {
            Text(text = "Add More")
        }
        Button(onClick = {
            // Click to go to home
            navController.navigate("meal")
        }) {
            Text(text = "Go to Meals")
        }

        LazyColumn {
            items(userBio){ userBio ->
                
                UserBioItem(userBio = userBio, onDeleteClick = {
                    roomViewModel.deleteUser(userBio.id)
                })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }


    }

}

// userBio Item
@Composable
fun UserBioItem(userBio: UserBio, onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(7.dp))
            .background(Color.LightGray)
            .padding(10.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Name: "+userBio.name, color = Color.Red)
            Text(text = "Age: "+userBio.age)
            Text(text = "Bio: "+userBio.bio, color = Color.Blue)
        }
        IconButton(onClick = {
            onDeleteClick()
        }) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")
        }
    }
}

// Meal Screen
@Composable
fun MealScreen(navController: NavController) {

    val mealViewModel: MealViewModel = viewModel()
    val mealState by mealViewModel.mealState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Meal Screen", fontSize = 24.sp, textAlign = TextAlign.Center)

        Button(onClick = {
            // Back to Room
            navController.navigate("room"){
                popUpTo("meal"){
                    inclusive = true
                }
            }
        }) {
            Text(text = "User Room")
        }
        Button(onClick = {
            // Go to meal room Screen
            navController.navigate("mealroom"){
                popUpTo("meal"){
                    inclusive = true
                }
            }
        }) {
            Text(text = "Meal Room")
        }

        when{
            mealState.loading -> {
                // Loading
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            mealState.error != null ->{
                Text(text = "${mealState.error}")
            }
            else -> {
                LazyColumn {
                    items(mealState.meal) { meal ->
                        MealItem(meal = meal,
                           onDownloadClick =  {
                                mealViewModel.insertMealToRoom(
                                    meal.idCategory,
                                    meal.strCategory,
                                    meal.strCategoryThumb,
                                    meal.strCategoryDescription
                                )
                            },
                            onCategoryClick = {
                                navController.navigate("detailscreen?mealThumb=${meal.strCategoryThumb}&mealCategory=${meal.strCategory}&mealDescription=${meal.strCategoryDescription}"){
                                    popUpTo("meal"){
                                        inclusive = true
                                    }
                                }
                            })
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }


    }
}

// Meal Item
@Composable
fun MealItem(meal: Category,onDownloadClick: ()->Unit, onCategoryClick: ()-> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(7.dp))
            .background(Color.LightGray)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(300.dp)
                .clickable {
                    onCategoryClick()
                },
            painter = rememberAsyncImagePainter(model = meal.strCategoryThumb),
            contentDescription = "image"
        )


        Button(onClick = {
            onDownloadClick()
            Toast.makeText(context, "Downloaded", Toast.LENGTH_LONG).show()

        }) {
            Text(text = "Download")
        }

        Text(
           modifier = Modifier
               .fillMaxWidth()
               .clip(RoundedCornerShape(5.dp))
               .background(Color.White)
               .padding(5.dp),
            text = meal.strCategory,
            textAlign = TextAlign.Center
        )

    }
}

// Meal Room screen
@Composable
fun MealRoomScreen(navController: NavController) {

    val mealViewModel: MealViewModel = viewModel()
    val mealRoomState by mealViewModel.mealRoomState
    val context = LocalContext.current
    var onImageFocusEvent by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(mealViewModel) {
        mealViewModel.getMealsFromRoom()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Meal Room Screen", fontSize = 24.sp)
        Button(onClick = {
            // Navigate to Meal Screen
            navController.navigate("meal"){
                popUpTo("room"){
                    inclusive = true
                }
            }
        }) {
            Text(text = "Go Back")
        }
        if (mealRoomState.meal.isEmpty()){
            Text(text = "Empty room")
        }
        else{
        LazyColumn {
                items(mealRoomState.meal){meal ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Cyan)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Image(
                            modifier = Modifier
                                .size(300.dp)
                                .clickable {
                                    onImageFocusEvent = true
                                }
                                .onFocusChanged { onImageFocusEvent = false },
                            painter = rememberAsyncImagePainter(model = meal.strCategoryThumb),
                            contentDescription = "image")
                        if (onImageFocusEvent){
                            Button(onClick = {
                                mealViewModel.deleteMealFromRoom(meal)
                                Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show()
                            }) {
                                Text(text = "Delete")
                            }
                        }
                        Text( modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(5.dp))
                            .padding(5.dp),
                            text = meal.strCategory,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                    }
                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
        }


    }
}

// Detail Screen
@Composable
fun DetailScreen(navController: NavController, mealThumb:String?, mealCategory: String?, mealDescription: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Detail Screen", fontSize = 24.sp, textAlign = TextAlign.Center)
        Button(onClick = {
            navController.navigate("meal"){
                popUpTo("room"){
                    inclusive = true
                }
            }
        }) {
            Text(text = "Back")
        }
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(300.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.Cyan)
                .padding(10.dp),
            painter = rememberAsyncImagePainter(model = mealThumb),
            contentDescription = "image")
        Text(modifier = Modifier.padding( top = 15.dp, bottom = 15.dp), text = mealCategory!!, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        HorizontalDivider()
        Text(text = mealDescription!!, color = Color.DarkGray)
    }
}
