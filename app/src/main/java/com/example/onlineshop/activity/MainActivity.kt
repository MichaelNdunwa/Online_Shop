package com.example.onlineshop.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.onlineshop.R
import com.example.onlineshop.activity.CartActivity
import com.example.onlineshop.model.CategoryModel
import com.example.onlineshop.model.ItemsModel
import com.example.onlineshop.model.SliderModel
import com.example.onlineshop.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityScreen {
                startActivity(Intent(this, CartActivity::class.java))
            }
        }
    }
}


@Composable

fun MainActivityScreen(onCartClick: () -> Unit) {
    val viewModel = MainViewModel()
    val banners = remember { mutableStateListOf<SliderModel>() }
    val categories = remember { mutableStateListOf<CategoryModel>() }
    val recommended = remember { mutableStateListOf<ItemsModel>() }
//    val showBannerLoading by remember { mutableStateOf(true) }
    val showBannerLoading = remember { mutableStateOf(true) }
//    val showCategoryLoading by remember { mutableStateOf(true) }
    val showCategoryLoading = remember { mutableStateOf(true) }
    val showRecommendedLoading = remember { mutableStateOf(true) }

    // Call MainViewModel to load data:
    // call loadBanner() function from MainView class to pass data into banners list:
    LaunchedEffect(Unit) {
        viewModel.loadBanners()
        viewModel.banners.observeForever {
            banners.clear()
            banners.addAll(it)
            showBannerLoading.value = false
        }
    }

    // call loadCategories() function from MainView class to pass data into categories list:
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
        viewModel.categories.observeForever {
            categories.clear()
            categories.addAll(it)
            showCategoryLoading.value = false
        }
    }

    // call loadRecommended() function from MainView class to pass data into recommended list:
    LaunchedEffect(Unit) {
        viewModel.loadRecommended()
        viewModel.recommended.observeForever {
            recommended.clear()
            recommended.addAll(it)
            showRecommendedLoading.value = false
        }
    }

    ConstraintLayout(modifier = Modifier.background(Color.White)) {
        val (scrollList, bottomMenu) = createRefs()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(scrollList) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
        ) {
            // Header:
            item {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Welcome Back", color = Color.Black)
                        Text(
                            "Michael",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row {
                        Image(
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Image(
                            painter = painterResource(R.drawable.bell_icon),
                            contentDescription = ""
                        )
                    }
                }
            }

            // Banners:
            item {
//               if (showBannerLoading) {
                if (showBannerLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Banners(banners)
                }
            }

            // Category text:
            item {
                SectionTitle(title = "Categories", actionText = "See all")
            }

            // Category List container:
            item {
                if (showCategoryLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(50.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    CategoryList(categories = categories)
                }
            }

            // Recommended Section Text:
            item {
                SectionTitle(title = "Recommended", actionText = "See all")
            }

            // Recommended List Container:
            item {
                if (showRecommendedLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
//                    RecommendedList(recommended = recommended)
                    ListItems(recommended)
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }

        }

        // Bottom Navigation:
        BottomMenu(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomMenu) {
                    bottom.linkTo(parent.bottom)
                },
            onItemClick = onCartClick
        )

    }
}

/*@Composable
fun RecommendedList(recommended: SnapshotStateList<ItemsModel>) {
    var selectedIndex = remember { mutableStateOf(-1) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        items(recommended.size) { index ->
            RecommendedItem(
                item = recommended[index],
//                isSelected = selectedIndex.value == index,
                onItemClick = {
                    selectedIndex.value = index
                }
            )
        }
    }
}*/

/*@Composable
fun RecommendedItem(item: ItemsModel, onItemClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onItemClick)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        // Item image:
        AsyncImage(
            model = (item.picUrl),
            contentDescription = item.title
        )
    }
}*/


@Composable
fun CategoryList(categories: SnapshotStateList<CategoryModel>) {
//    var selectedIndex by remember { mutableStateOf(-1) }
    var selectedIndex = remember { mutableStateOf(-1) }
//    var selectedIndex = remember { mutableStateOf(0) } // I set the mutableState to 0 index, because I want the the first item in the category list to be checked once the app MainActivity opens.
    val context = LocalContext.current
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        items(categories.size) { index ->
            CategoryItem(
                item = categories[index],
//                isSelected = selectedIndex == index,
                isSelected = selectedIndex.value == index,
                onItemClick = {
//                    selectedIndex = index
                    selectedIndex.value = index
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(context, ListItemsActivity::class.java).apply {
                            putExtra("id", categories[index].id.toString())
                            putExtra("title", categories[index].title)
                        }
                        context.startActivity(intent, null)
                    }, 1000)
                })
        }
    }
}

@Composable
fun CategoryItem(item: CategoryModel, isSelected: Boolean, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onItemClick)
            .background(
                color = if (isSelected) colorResource(R.color.purple) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = (item.picUrl),
            contentDescription = item.title,
            modifier = Modifier
                .size(45.dp)
                .background(
                    color = if (isSelected) Color.Transparent else colorResource(R.color.light_grey),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentScale = ContentScale.Inside,
            colorFilter = if (isSelected) {
                ColorFilter.tint(Color.White)
            } else {
                ColorFilter.tint(Color.Black)
            }
        )
        if (isSelected) {
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun Banners(banners: List<SliderModel>) {
    AutoSlidingCarousel(banners = banners)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
//    pagerState: PagerState = rememberPagerState(),
    pagerState: PagerState = remember { PagerState() },
    banners: List<SliderModel>
) {
//    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    val isDragged = pagerState.interactionSource.collectIsDraggedAsState()

    Column(modifier = modifier.fillMaxSize()) {
        HorizontalPager(count = banners.size, state = pagerState) { page ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(banners[page].url)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    .height(150.dp)
            )
        }
        DotIndicator(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally),
            totalDots = banners.size,
//            selectedIndex = if (isDragged) pagerState.currentPage else pagerState.currentPage,
            selectedIndex = if (isDragged.value) pagerState.currentPage else pagerState.currentPage,
            dotSize = 8.dp
        )
    }
}

@Composable
fun DotIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = colorResource(R.color.purple),
    unSelectedColor: Color = colorResource(R.color.light_grey),
    dotSize: Dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )
            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}

// Categories:
@Composable
fun SectionTitle(title: String, actionText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Categories text:
        Text(
            text = title,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        // See all text:
        Text(
            text = actionText,
            color = colorResource(R.color.purple),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
fun BottomMenu(modifier: Modifier, onItemClick: () -> Unit) {
    Row(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
            .background(
                colorResource(R.color.purple),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomMenuItem(icon = painterResource(R.drawable.btn_1), text = "Explorer")
        BottomMenuItem(icon = painterResource(R.drawable.btn_2), text = "Cart", onItemClick = onItemClick )
        BottomMenuItem(icon = painterResource(R.drawable.btn_3), text = "Favorite")
        BottomMenuItem(icon = painterResource(R.drawable.btn_4), text = "My Order")
        BottomMenuItem(icon = painterResource(R.drawable.btn_5), text = "Profile")
    }
}

@Composable
fun BottomMenuItem(icon: Painter, text: String, onItemClick: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .height(60.dp)
            .clickable { onItemClick?.invoke() }
//            .clickable { onItemClick }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = text, tint = Color.White)
        Text(text, color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(top = 8.dp))
    }

}