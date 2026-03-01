package com.internshala.flash.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.currentBackStackEntryAsState
import com.internshala.flash.LoginUi
import com.internshala.flash.data.InternetItem
import com.google.firebase.auth.FirebaseAuth
import com.internshala.flash.R
import androidx.compose.material3.AlertDialog


enum class FlashAppScreen(val title : String){
    Start("FlashCart"),
    Items("Choose Item"),
    Cart("Your Cart")
}

var canNavigateBack = false
val auth = FirebaseAuth.getInstance()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashApp(
        flashViewModel: FlashViewModel= viewModel(),
        navController: NavHostController = rememberNavController()
    ){

    val user by flashViewModel.user.collectAsState()
    val logoutClicked by flashViewModel.logoutClicked.collectAsState()
    auth.currentUser?.let { flashViewModel.setUser(it) }
    val isVisible by flashViewModel.isVisible.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FlashAppScreen.valueOf(
        backStackEntry?.destination?.route ?: FlashAppScreen.Start.name
    )
    canNavigateBack = navController.previousBackStackEntry != null
    val cartItems by flashViewModel.cartItems.collectAsState()


    if(isVisible){
        OfferScreen()
    }else if (user == null){
        LoginUi(flashViewModel = flashViewModel)
    } else{
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentScreen.title,
                                    fontSize = 26.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                if (currentScreen == FlashAppScreen.Cart) {
                                    Text(
                                        text = "(${cartItems.size})",
                                        fontSize = 26.sp,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.clickable{
                                    flashViewModel.setLogoutStatus(true)
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.logout),
                                    contentDescription = "Logout",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Logout",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(
                                        end = 14.dp,
                                    )
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        if (canNavigateBack){
                            IconButton(onClick = {
                                navController.navigateUp()
                            }){
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back Button")
                            }
                        }
                    }
                )
            },
            bottomBar ={ FlashAppBar(
                navHostController = navController,
                currentScreen = currentScreen,
                cartItems = cartItems
            )}
        ){ it ->
            NavHost(
                navController = navController , startDestination = FlashAppScreen.Start.name,
                modifier = Modifier.padding(it)
            ){
                composable(FlashAppScreen.Start.name){
                    StratScreen(
                        flashViewModel = flashViewModel,
                        onClickCategory = {
                            flashViewModel.updateSelectedCategory(it)
                            navController.navigate(FlashAppScreen.Items.name)
                        }
                    )
                }

                composable (FlashAppScreen.Items.name) {
                    InternetItemScreen(
                        flashViewModel = flashViewModel,
                        itemUiState = flashViewModel.itemUiState
                    )
                }

                composable (FlashAppScreen.Cart.name) {
                    CartScreen(
                        flashViewModel = flashViewModel,
                        onHomeButtonClicked = {
                            navController.navigate(FlashAppScreen.Start.name){
                                popUpTo(0)
                            }
                        }
                    )
                }
            }
        }
        if (logoutClicked){
            AlertCheck(onYesClicked = {
                flashViewModel.setLogoutStatus(false)
                auth.signOut()
                flashViewModel.clearData()
            },
                onNoClicked = {
                    flashViewModel.setLogoutStatus(false)
                }
            )
        }
    }
}


@Composable
fun FlashAppBar(
    navHostController: NavHostController,
    currentScreen: FlashAppScreen,
    cartItems: List<InternetItem>
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
            .padding(
                horizontal = 70.dp,
                vertical = 10.dp
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                navHostController.navigate(FlashAppScreen.Start.name){
                    popUpTo(0)
                }
            }
        ) {
            Icon(imageVector = Icons.Outlined.Home, contentDescription ="Home")
            Text(text = "Home", fontSize = 10.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                if (currentScreen != FlashAppScreen.Cart){
                    navHostController.navigate(FlashAppScreen.Cart.name)
                }
            }
        ) {
            Box {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Cart"
                )
                if (cartItems.isNotEmpty())
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp)
                        .size(15.dp)
                        .background(Color.Red, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        cartItems.size.toString(),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
            Text(text = "Cart", fontSize = 10.sp)
        }
    }
}

@Composable
fun AlertCheck(
    onYesClicked: () -> Unit,
    onNoClicked: () -> Unit

){
    AlertDialog(
        onDismissRequest = {
            onNoClicked()
        },
        title = {
            Text(text = "Logout?", fontWeight = FontWeight.Bold)
        },
        text = {
            Text(text = "Are you sure you want to logout?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onYesClicked()
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onNoClicked()
                }
            ) {
                Text("No")
            }
        },
        containerColor = Color.White
    )
}