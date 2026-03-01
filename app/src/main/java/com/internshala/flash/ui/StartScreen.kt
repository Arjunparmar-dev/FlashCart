package com.internshala.flash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.internshala.flash.R
import com.internshala.flash.data.DataSource

@Composable
fun StratScreen(
    flashViewModel : FlashViewModel,
    onClickCategory:(Int) -> Unit
) {
    LocalContext.current


    LazyVerticalGrid(
        GridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ){
        item(
            span = { GridItemSpan(2)  }
        ){
            Column {
                Image(
                    painter = painterResource(R.drawable.categorybanner),
                    contentDescription = "Offer",
                    modifier = Modifier.fillMaxSize()
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(77, 178, 82, 255)
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .height(25.dp)
                ) {
                    Text(
                        text = "Shop by Category",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        }

        items(DataSource.loadCategories()){
            CategoriesCard(
                stringResourceId = it.stringResourceId,
                imageResourceId =it.imageResourceId,
                flashViewModel = flashViewModel,
                onClickCategory = onClickCategory
            )
        }
    }
}

@Composable
fun CategoriesCard(
    stringResourceId: Int,
    imageResourceId: Int,
    flashViewModel: FlashViewModel,
    onClickCategory: (Int) -> Unit
){
    val categoryName = stringResource(id = stringResourceId)
    Card(
        modifier = Modifier.clickable{
            flashViewModel.updateClickText(categoryName)
            //Toast.makeText(context, "Card is Clicked", Toast.LENGTH_SHORT).show()
            onClickCategory(stringResourceId)
        },
        colors = CardDefaults.cardColors(
            Color(248,221,248,255)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = categoryName,
                fontSize = 17.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Image(painter = painterResource(imageResourceId),
                alignment = Alignment.Center,
                contentDescription = "Fresh Fruits",
                modifier = Modifier.size(150.dp)
            )
        }
    }
}