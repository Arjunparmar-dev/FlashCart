package com.internshala.flash.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.internshala.flash.R
import com.internshala.flash.data.InternetItem

@Composable
fun ItemScreen(
    flashViewModel: FlashViewModel,
    items : List<InternetItem>
){
    val flashUiState by flashViewModel.uiState.collectAsState()
    val selectedCategory = stringResource(flashUiState.selectedCategory)
    val database = items.filter {
        it.itemCategory.equals(selectedCategory, ignoreCase = true)
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        item(
            span = { GridItemSpan(2)  }
        ){
            Column {
                Image(
                    painter = painterResource(R.drawable.itembanner),
                    contentDescription = "Offer",
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(77, 178, 82, 255)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .height(25.dp)
                ) {
                    Text(
                        text =stringResource(flashUiState.selectedCategory),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        }

        items(database){
            ItemCard(
                stringResourceId = it.itemName,
                imageResourceId = it.imageUrl,
                itemQuantity = it.itemCategory,
                itemPrice = it.itemPrice,
                itemCategory = it.itemCategory,
                flashViewModel = flashViewModel
            )
        }
    }
}

@Composable
fun InternetItemScreen(
    flashViewModel: FlashViewModel,
    itemUiState:FlashViewModel.ItemUiState
){
    when(itemUiState){
        is FlashViewModel.ItemUiState.Loading -> {
            LoadingScreen()
        }
        is FlashViewModel.ItemUiState.Success -> {
            ItemScreen(flashViewModel = flashViewModel , items = itemUiState.item)
        }
        else -> {
            ErrorScreen(flashViewModel = flashViewModel)
        }
    }
}

@Composable
fun ItemCard(
    stringResourceId: String,
    imageResourceId: String,
    itemQuantity: String,
    itemPrice: Int,
    itemCategory: String,
    flashViewModel: FlashViewModel
) {
    val context = LocalContext.current
    Column(modifier = Modifier.width(150.dp)) {
        Card(
            colors = CardDefaults.cardColors(
                Color(248,221,248,255)
            )
        ) {
            Box {
                AsyncImage(
                    model = imageResourceId,
                    contentDescription = stringResourceId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(255, 100, 90, 255)
                        )
                    ) {
                        Text(
                            text = "25% Off",
                            color = Color.White,
                            fontSize = 8.sp,
                            modifier = Modifier.padding(
                                horizontal = 5.dp,
                                vertical = 2.dp
                            )
                        )
                    }
                }
            }
        }
        Text(
            text = stringResourceId,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            maxLines = 1,
            textAlign = TextAlign.Left
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
               Text(
                   text = "Rs. $itemPrice",
                   fontSize = 9.sp,
                   maxLines = 1,
                   textAlign = TextAlign.Center,
                   color = Color.Gray,
                   textDecoration = TextDecoration.LineThrough
               )
                Text(
                    text = "Rs. ${itemPrice*75/100}",
                    fontSize = 11.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = Color(246, 79, 40, 255)
                )
            }
            Text(
                text = itemQuantity,
                fontSize = 14.sp,
                maxLines = 1,
                textAlign = TextAlign.Right,
                color = Color.Gray
            )
        }
        Card(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .clickable {
                flashViewModel.addToDatabase(
                    InternetItem(
                        itemName = stringResourceId,
                        itemQuantity = itemQuantity,
                        itemPrice = itemPrice,
                        imageUrl = imageResourceId,
                        itemCategory =itemCategory
                    )
                )
                Toast
                    .makeText(context, "Added to Cart", Toast.LENGTH_SHORT)
                    .show()
            },
            colors = CardDefaults.cardColors(
                containerColor = Color(68, 217, 74, 255)
            )
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .height(24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add to Cart",
                    fontSize = 11.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(){
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.loading),
            contentDescription = "Loading"
        )
    }
}


@Composable
fun ErrorScreen(flashViewModel: FlashViewModel){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.error),
            contentDescription = "Error",
        )
        Text("Oops! Internet unavailable. Please check your connection or retry after turning your wifi or mobile data on.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textAlign = TextAlign.Center
        )
        Button(
            onClick = {
                flashViewModel.getFlashItems()
            }
        ) {
            Text("Retry")
        }
    }
}
