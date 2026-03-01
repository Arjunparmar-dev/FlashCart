package com.internshala.flash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.internshala.flash.R
import com.internshala.flash.data.InternetItem
import com.internshala.flash.data.InternetItemWithQuantity

@Composable
fun CartScreen(
    flashViewModel: FlashViewModel,
    onHomeButtonClicked: () -> Unit
){
    val cartItems by flashViewModel.cartItems.collectAsState()
    val cartItemWithQuantity = cartItems.groupBy { it }
        .map {
            (item , _) -> InternetItemWithQuantity(
                item,
                cartItems.size
            )
        }
    if (cartItems.isNotEmpty()){
        LazyColumn(
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Image(
                    painter = painterResource(R.drawable.cartbanner) ,
                    contentDescription = "Offer",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            item {
                Text(
                    text = "Review Items",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            items(cartItemWithQuantity) {
                CartCard(
                    it.item ,
                    flashViewModel,
                    it.quantity
                )
            }
            item {
                Text(
                    text = "Billing Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            val totalPrice = cartItems.sumOf {
                it.itemPrice * 75/100
            }
            val handlingCharge: Int = totalPrice*1/100
            val deliveryCharge = 30
            val grandTotal = totalPrice + handlingCharge + deliveryCharge

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(236, 236, 236, 255)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        BillRow(itemName = "Item Total" , itemPrice = totalPrice, fontWeight = FontWeight.Normal)
                        BillRow("Handling Charge" , itemPrice = handlingCharge, fontWeight = FontWeight.Light)
                        BillRow("Delivery Charge" , itemPrice = deliveryCharge, fontWeight = FontWeight.Light)
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 5.dp),
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                        BillRow("To Pay" , itemPrice = grandTotal, FontWeight.Bold)
                    }
                }
            }
        }
    }else{
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.cart_empty),
                contentDescription = "Empty Cart",
                modifier = Modifier.size(70.dp)
            )
            Text(
                text = "Your Cart is Empty",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp)
            )

            FilledTonalButton(onClick = {
                onHomeButtonClicked()
            }) {
                Text(
                    text = "Browse Products"
                )
            }
        }
    }
}

@Composable
fun CartCard(
    cartItem: InternetItem,
    flashViewModel: FlashViewModel,
    cartItemQuantity: Int
){
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = cartItem.imageUrl,
            contentDescription = "Item Image",
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .padding(5.dp)
                .weight(4f),
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.error_image)
        )

        Column(
            modifier = Modifier.fillMaxHeight()
                .padding(5.dp)
                .weight(4f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = cartItem.itemName,
                fontSize = 20.sp,
                maxLines = 1
            )
            Text(
                text = cartItem.itemQuantity,
                fontSize = 14.sp,
                maxLines = 1
            )
        }

        Column(
            modifier = Modifier.fillMaxHeight()
                .padding(5.dp)
                .weight(3f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Rs. ${cartItem.itemPrice}",
                fontSize = 14.sp,
                maxLines = 1,
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough
            )
            Text(
                text = "Rs. ${cartItem.itemPrice * 75/100}",
                fontSize = 18.sp,
                maxLines = 1,
                color = Color(254, 116, 105, 255)
            )
        }

        Column(
            modifier = Modifier.fillMaxHeight()
                .weight(3f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Quantity $cartItemQuantity",
                fontSize =11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier.clickable{
                    flashViewModel.removeFromCart(cartItem)
                }
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(254, 116, 105, 255)
                )
            ) {
                Text(
                    text = "Remove",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun BillRow(
    itemName: String,
    itemPrice: Int,
    fontWeight: FontWeight
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = itemName,fontWeight = fontWeight)
        Text(text = "Rs. $itemPrice",fontWeight = fontWeight)
    }

}