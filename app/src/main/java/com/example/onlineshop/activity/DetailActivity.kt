package com.example.onlineshop.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.rememberAsyncImagePainter
import com.example.onlineshop.R
import com.example.onlineshop.helper.ManagementCart
import com.example.onlineshop.model.ItemsModel


class DetailActivity : AppCompatActivity() {
    private lateinit var item: ItemsModel
    private lateinit var managementCart: ManagementCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = intent.getParcelableExtra("object")!!
        managementCart = ManagementCart(this@DetailActivity)
        setContent {
            DetailScreen(
                item = item,
                onBackClick = { finish() },
                onAddToCartClick = {
                    item.numberInCart = 1
                    managementCart.insertItem(item)
                },
                onCartClick = { startActivity(Intent(this, CartActivity::class.java)) }
            )
        }
    }
}


@Composable
fun DetailScreen(
    item: ItemsModel,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onCartClick: () -> Unit
) {

    var selectedImageUrl = remember { mutableStateOf(item.picUrl.first()) }
    var selectedModelIndex = remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(top = 36.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            val (back, favorite) = createRefs()
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = "",
                modifier = Modifier
                    .clickable { onBackClick() }
                    .constrainAs(back) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )
            Image(
                painter = painterResource(R.drawable.fav_icon),
                contentDescription = "",
                modifier = Modifier
                    .constrainAs(favorite) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }
        Image(
            painter = rememberAsyncImagePainter(model = selectedImageUrl.value),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
                .background(colorResource(R.color.light_grey), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        )
        LazyRow(modifier = Modifier.padding(vertical = 16.dp)) {
            items(item.picUrl.size) { imageUrl ->
                ImageThumbnail(
                    imageUrl = item.picUrl[imageUrl],
//                    isSelected = selectedImageUrl.value == imageUrl.toString(),
//                    onClick = { selectedImageUrl.value = imageUrl.toString() }
                    isSelected = selectedImageUrl.value == item.picUrl[imageUrl],
                    onClick = { selectedImageUrl.value = item.picUrl[imageUrl] }
                )
//                Log.d("isSelected", "selectedImageUrl = ${selectedImageUrl.value} and ${imageUrl.toString()}")
//                Log.d("isSelected", "${item.picUrl[imageUrl]}")
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = item.title,
                fontSize = 23.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 16.dp)
            )
            Text(
                text = "$${item.price}",
                fontSize = 22.sp
            )
        }
        RatingBar(rating = item.rating)
        ModelSelected(
            models = item.model,
            selectedModeIndex = selectedModelIndex.value,
            onModelSelected = { selectedModelIndex.value = it }
        )
        Text(
            text = item.description,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onAddToCartClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.purple)),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Buy Now",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
            IconButton(
                onClick = onCartClick,
                modifier = Modifier.background(
                    colorResource(R.color.light_grey),
                    shape = RoundedCornerShape(10.dp)
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.btn_2),
                    contentDescription = "Cart",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun RatingBar(rating: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = "Select Model",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(R.drawable.star),
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = rating.toString(), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ModelSelected(
    models: List<String>,
    selectedModeIndex: Int,
    onModelSelected: (Int) -> Unit
) {
    LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
        itemsIndexed(models) { index, model ->
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(48.dp)
                    .then(
                        if (index == selectedModeIndex) {
                            Modifier.border(
                                1.dp,
                                colorResource(R.color.purple),
                                RoundedCornerShape(10.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .background(
                        if (index == selectedModeIndex) colorResource(R.color.light_purple) else
                            colorResource(R.color.light_grey),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onModelSelected(index) }
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = model,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = if (index == selectedModeIndex) colorResource(R.color.purple) else colorResource(
                        R.color.black
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@Composable
fun ImageThumbnail(
    imageUrl: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backColor = if (isSelected) colorResource(R.color.light_purple)
    else colorResource(R.color.light_grey)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(55.dp)
            .then(
                if (isSelected) {
                    Modifier.border(1.dp, colorResource(R.color.purple), RoundedCornerShape(10.dp))
                } else {
                    Modifier
                }
            )
            .background(backColor, shape = RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        )
    }
}