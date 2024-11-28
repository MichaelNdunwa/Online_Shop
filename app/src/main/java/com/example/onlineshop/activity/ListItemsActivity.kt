package com.example.onlineshop.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.onlineshop.R
import com.example.onlineshop.viewmodel.MainViewModel

class ListItemsActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private var id: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = intent.getStringExtra("id") ?: ""
        title = intent.getStringExtra("title") ?: ""

        setContent {

            ListItemScreen(
                title = title,
                onBackClick = { finish() },
                viewModel = viewModel,
                id = id
            )
        }
    }
}

@Composable
fun ListItemScreen(
    title: String,
    onBackClick: () -> Unit,
    viewModel: MainViewModel,
    id: String
) {
//    val items by viewModel.recommended.observeAsState(emptyList())
    val items = viewModel.recommended.observeAsState(emptyList())
    var isLoading  = remember { mutableStateOf(true) }

    LaunchedEffect(id) {
        viewModel.loadFiltered(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ConstraintLayout(modifier = Modifier.padding(top = 36.dp, start = 16.dp, end = 16.dp)) {
            val (backButton, cartText) = createRefs()
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(cartText) { centerTo(parent) },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                text = title
            )

            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onBackClick()
                    }
                    .constrainAs(backButton) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )
        }
        if (isLoading.value) {
//            Spacer(modifier = Modifier.height(320.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(50.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
//            CircularProgressIndicator()
        } else {
            ListItemsFullSize(items.value)
        }
    }

    LaunchedEffect(items.value) {
        isLoading.value = items.value.isEmpty()
    }
}