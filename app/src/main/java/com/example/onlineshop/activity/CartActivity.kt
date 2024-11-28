package com.example.onlineshop.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.rememberAsyncImagePainter
import com.example.onlineshop.R
import com.example.onlineshop.helper.ChangeNumberItemsListener
import com.example.onlineshop.helper.ManagementCart
import com.example.onlineshop.model.ItemsModel

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CartScreen(
                ManagementCart(this),
                onBackClick = { finish() }
            )
        }
    }
}

@Composable
private fun CartScreen(
    managementCart: ManagementCart = ManagementCart(LocalContext.current),
    onBackClick: () -> Unit
) {
    val cartItems = remember { mutableStateOf(managementCart.getListCart()) }
    val tax = remember { mutableStateOf(0.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        ConstraintLayout(modifier = Modifier.padding(top = 16.dp)) {
            val (backButton, cartText) = createRefs()
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(cartText) { centerTo(parent) },
                text = "My Cart",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onBackClick()
                    }
                    .constrainAs(backButton) {
                        top.linkTo(cartText.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(cartText.bottom)
                    }
            )
        }
        if (cartItems.value.isEmpty()) {
            Text(
                text = "Cart Is Empty",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 300.dp)
            )

        } else {
            CartList(cartItems = cartItems.value, managementCart = managementCart) {
                cartItems.value = managementCart.getListCart()
                calculateCart(managementCart, tax)
            }
            CartSummary(
                itemTotal = managementCart.getTotalFee(),
                tax = tax.value,
                delivery = 10.0
            )
        }

        /*ConstraintLayout {
            val summaryCart = createRef()
            Box(
                modifier = Modifier
                    .constrainAs(summaryCart) { bottom.linkTo(parent.bottom)}
            ) {
                CartSummary(
                    itemTotal = managementCart.getTotalFee(),
                    tax = tax.value,
                    delivery = 10.0
                )
            }
        }*/


    }
}

// I shall come again for you babe 💖.
/*
@Composable
fun CartScreen(
    managementCart: ManagementCart = ManagementCart(LocalContext.current),
    onBackClick: () -> Unit
) {
    val cartItems = remember { mutableStateOf(managementCart.getListCart()) }
    val tax = remember { mutableStateOf(0.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ConstraintLayout(modifier = Modifier.padding(top = 36.dp)) {
            val (backButton, cartList, summaryBox) = createRefs()

            // Back button
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onBackClick()
                    }
                    .constrainAs(backButton) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            // List and Header Content
            LazyColumn(
                modifier = Modifier
                    .constrainAs(cartList) {
                        top.linkTo(backButton.bottom, margin = 16.dp)
                        bottom.linkTo(summaryBox.top, margin = 16.dp)
                    }
                    .height(0.dp) // Add a size modifier
                    .fillMaxWidth()
            ) {
                // Header
                item {
                    Text(
                        text = "My Cart",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Cart items or Empty message
                if (cartItems.value.isEmpty()) {
                    item {
                        Text(
                            text = "Cart Is Empty",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                        )
                    }
                } else {
                    items(cartItems.value.size) { cartItem ->
                        // Replace with your CartListItem Composable
                        CartList(
                            cartItems = cartItems.value,
                            managementCart = managementCart
                        ) {
                            cartItems.value = managementCart.getListCart()
                            calculateCart(managementCart, tax)
                        }
                    }
                }
            }

            // Footer summary
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(summaryBox) {
                        bottom.linkTo(parent.bottom)
                    }
                    .height(100.dp) // Define the height of the summary box
            ) {
                CartSummary(
                    itemTotal = managementCart.getTotalFee(),
                    tax = tax.value,
                    delivery = 10.0
                )
            }
        }
    }
}
*/

// Sadly, this my babe isn't working the way I want now, but I will still come for you later.
@Composable
fun CartSummary(itemTotal: Double, tax: Double, delivery: Double) {
    val totalFee = itemTotal + tax + delivery
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        // row for item total:
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Item Total:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.grey)
            )
            Text(
                text = "$${itemTotal}",
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
        }

        // row for item total:
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Fee Delivery:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.grey)
            )
            Text(
                text = "$${delivery}",
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
        }

        // row for item total:
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Total Tax:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.grey)
            )
            Text(
                text = "$${tax}",
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
        }

        /*        // line divider:
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                        .height(0.2.dp)
                        .background(color = colorResource(R.color.grey))
                ) {}*/

        // line divider:
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(0.2.dp)
                .background(color = colorResource(R.color.grey))
        )

        // row for the total fee:
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Total:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.grey)
            )
            Text(
                text = "$${totalFee}",
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
        }

        // check out button:
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .background(
                        color = colorResource(R.color.purple),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Text(
                    text = "Check out",
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.white),
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            }
        }

        /*// check Out button:
        Button(
            onClick = {},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.purple)),
            modifier = Modifier
                .padding(top = 16.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Check Out",
                color = Color.White,
                fontSize = 18.sp
            )
        }*/
    }
}

fun calculateCart(managementCart: ManagementCart, tax: MutableState<Double>) {
    val percentageTax = 0.02
    tax.value = Math.round((managementCart.getTotalFee() * percentageTax) * 100) / 100.0
}

@Composable
fun CartList(
    cartItems: ArrayList<ItemsModel>,
    managementCart: ManagementCart,
    onItemChange: () -> Unit
) {
    LazyColumn(modifier = Modifier.padding(top = 16.dp)) { // He used top padding as 16.dp
        items(cartItems.size) { index ->
            CartItem(
                cartItems = cartItems,
                item = cartItems[index],
                managementCart = managementCart,
                onItemChange = onItemChange
            )
            Log.d("debug_cartScreen", "cartItems[index] = ${cartItems[index]}")
            Log.d("debug_cartScreen", "cartItems.size = ${cartItems.size}")
        }
    }
}

@Composable
fun CartItem(
    cartItems: ArrayList<ItemsModel>,
    item: ItemsModel,
    managementCart: ManagementCart,
    onItemChange: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
//            .fillMaxHeight()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
//        val(pic, titleText, feeEachTime, totalEachItem, quantity)  = createRefs()
//        val (pic, titleText, eachItemFee, totalEachItemFee, quantity, deleteRow) = createRefs()
        val (pic, titleText, eachItemFee, totalEachItemFee, quantity) = createRefs()
        /*val painter = rememberAsyncImagePainter(
           model = item.picUrl[0],
           onState = { state ->
               if (state is AsyncImagePainter.State.Error) {
                   Log.e("debug_cartScreen", "Failed to load image: ${state.result.throwable}")
               }
           }
       )*/
        // pic for each item
        Image(
            painter = rememberAsyncImagePainter(item.picUrl[0]),
//            painter = painter,,
//            placeholder = painterResource(R.drawable.cat1_1),
//            error = painterResource(R.drawable.cat1_1),
            contentDescription = "",
            modifier = Modifier
                .size(90.dp)
                .background(colorResource(R.color.light_grey), shape = RoundedCornerShape(10.dp))
                .padding(8.dp)
                .constrainAs(pic) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        Log.d("debug_cartScreen", "item.picUrl[0] = ${item.picUrl[0]}")
        // title for each item
        Text(
            text = item.title,
            modifier = Modifier
                .constrainAs(titleText) {
                    start.linkTo(pic.end)
                    top.linkTo(pic.top)
                }
                .padding(start = 8.dp, top = 8.dp)
        )
        Log.d("debug_cartScreen", "item.title = ${item.title}")
        // fee for each item
        Text(
            text = "$${item.price}",
            color = colorResource(R.color.purple),
            modifier = Modifier
                .constrainAs(eachItemFee) {
                    start.linkTo(titleText.start)
                    top.linkTo(titleText.bottom)
                }
                .padding(top = 8.dp, start = 9.dp)
        )
        // total fee for each item
        Text(
            text = "$${item.price * item.numberInCart}",
            color = colorResource(R.color.black),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(totalEachItemFee) {
                    start.linkTo(eachItemFee.start)
                    bottom.linkTo(pic.bottom)
                }
                .padding(start = 9.dp)
        )
        // quantity for each item
        /*Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(1.dp)
                .width(100.dp)
                .height(30.dp)
                .background(
                    color = colorResource(R.color.light_grey),
                    shape = RoundedCornerShape(10.dp))
                .constrainAs(deleteRow) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        ) {
            // minus button for each item
            Button(
                onClick = onItemChange,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.white)),
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
            ) {
                Text(
                    text = "-",
                    color = colorResource(R.color.black),
                    fontSize = 20.sp,
                )
            }
            // number for each item
            Text(
                text = "${item.numberInCart}",
                color = colorResource(R.color.black),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            // plus button for each item
            Button(
                onClick = onItemChange,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults
                    .buttonColors(colorResource(R.color.purple)),
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
            ) {
                Text(
                    text = "+",
                    color = colorResource(R.color.black),
                    fontSize = 20.sp
                )
            }
        }*/

        // quantity for each item
        ConstraintLayout(
            modifier = Modifier
                .width(100.dp)
                .constrainAs(quantity) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .background(
                    colorResource(R.color.light_grey),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            val (plusCartButton, minusCartButton, numberItemCart) = createRefs()
            // number for each item or increment text.
            Text(
                text = item.numberInCart.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .constrainAs(numberItemCart) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
            // plus button for each item
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(colorResource(R.color.purple), shape = RoundedCornerShape(10.dp))
                    .constrainAs(plusCartButton) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managementCart.plusItem(cartItems, cartItems.indexOf(item),
                            object : ChangeNumberItemsListener {
                                override fun onChanged() {
                                    onItemChange()
                                }
                            }
                        )
                    }
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            // minus button for each item
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(colorResource(R.color.white), shape = RoundedCornerShape(10.dp))
                    .constrainAs(minusCartButton) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managementCart.minusItem(cartItems, cartItems.indexOf(item),
                            object : ChangeNumberItemsListener {
                                override fun onChanged() {
                                    onItemChange()
                                }
                            }
                        )
                    }
            ) {
                Text(
                    text = "-",
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
