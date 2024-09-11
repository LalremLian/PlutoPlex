package com.lazydeveloper.trelloplex.presentation.screen.filter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.network.model.MovieFilterResponse
import com.lazydeveloper.network.model.TvFilterResponse
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.AdsBanner
import com.lazydeveloper.trelloplex.presentation.composables.AnimatedPreloader
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomImageAsync
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.ui.theme.Background_Black
import com.lazydeveloper.trelloplex.ui.theme.Background_Black_10
import com.lazydeveloper.trelloplex.ui.theme.Background_Black_40
import com.lazydeveloper.trelloplex.ui.theme.Background_Black_70
import com.lazydeveloper.trelloplex.ui.theme.Loading_Orange
import com.lazydeveloper.trelloplex.ui.theme.Purple80
import com.lazydeveloper.trelloplex.util.AffiliateLink
import com.lazydeveloper.trelloplex.util.CommonEnum
import com.lazydeveloper.trelloplex.util.countries
import com.lazydeveloper.trelloplex.util.genres
import com.lazydeveloper.trelloplex.util.getSharedPrefs
import java.time.Year

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterScreen(navController: NavController, id: Int? = 0) {
    FilterScreenViewModel(navController, id)
}

fun getISO(selectedCountry: String): String? {
    for (country in countries) {
        if (country.name.equals(selectedCountry, ignoreCase = true)) {
            return country.iso_639_1
        }
    }
    return null
}

fun getSelectedGenreIds(selectedGenres: List<String>): List<String> {
    val selectedIds = mutableListOf<String>()
    for (genre in selectedGenres) {
        val foundGenre = genres.find { it.name.equals(genre, ignoreCase = true) }
        foundGenre?.let {
            selectedIds.add(it.id.toString())
        }
    }
    return selectedIds
}

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterScreenViewModel(
    navController: NavController,
    id: Int? = 0,
    viewModel: FilterTvScreenViewModel = hiltViewModel(),
    affiliateClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val singleSelectionStateType = remember { mutableIntStateOf(-1) }
    val multiSelectionStateGenre = remember { mutableStateOf(listOf<Int>()) }
    val singleSelectionStateCountry = remember { mutableIntStateOf(-1) }
    val singleSelectionStateYear = remember { mutableIntStateOf(-1) }

    fun resetSelections() {
        singleSelectionStateType.value = -1
        multiSelectionStateGenre.value = listOf()
        singleSelectionStateCountry.value = -1
        singleSelectionStateYear.value = -1
    }

    val currentYear = Year.now().value
    val previousFiveYearsIncludingCurrent = (currentYear downTo currentYear - 8).toList()

    var type by rememberSaveable { mutableStateOf("Movies") }
    var year by remember { mutableStateOf("2024") }
    var country by remember { mutableStateOf("US") }
    var genre by remember { mutableStateOf(listOf("10765")) }

    val filterTvItems = viewModel.popularMoviesFlow.value.collectAsLazyPagingItems()
    val filterMoviesItems = viewModel.movieFilterFlow.value.collectAsLazyPagingItems()

    var isApplyClicked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        type = if (id == 0) {
            "Movies"
        } else {
            "TV Series"
        }
    }

    if (!isApplyClicked) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Row {
                    CustomImage(
                        imageId = R.drawable.ic_back,
                        modifier = Modifier
                            .size(55.dp)
                            .padding(16.dp)
                            .clickable { navController.popBackStack() },
                        contentDescription = "back"
                    )
                    CustomText(
                        text = "Filter $type",
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .clickable { isApplyClicked = false },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.White,
                    )
                }

                CustomText(
                    text = "Genre",
                    modifier = Modifier.padding(top = 10.dp, start = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.White,
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    genres.forEachIndexed { index, item ->
                        CustomCheckbox(
                            text = item.name,
                            multiSelectionState = multiSelectionStateGenre,
                            index = index,
                            onSelectedGenre = {
                                genre = getSelectedGenreIds(it)
                                Log.d("FilterScreenViewModel", "Genre selected: $it")
                            }
                        )
                    }
                }
                CustomText(
                    text = "Countries",
                    modifier = Modifier.padding(top = 20.dp, start = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.White,
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    countries.forEachIndexed { index, item ->
                        CustomCheckbox(
                            text = item.name,
                            singleSelectionState = singleSelectionStateCountry,
                            index = index,
                            onCheckedChange = {
                                if (it) {
                                    country = getISO(item.name) ?: "US"
                                    Log.d("FilterScreenViewModel", "Country selected: ${country}")
                                }
                            }
                        )
                    }
                }

                CustomText(
                    text = "Release Year",
                    modifier = Modifier.padding(top = 20.dp, start = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.White,
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    previousFiveYearsIncludingCurrent.forEachIndexed { index, item ->
                        CustomCheckbox(
                            text = item.toString(),
                            singleSelectionState = singleSelectionStateYear,
                            index = index,
                            onCheckedChange = {
                                if (it) {
                                    year = item.toString()
                                }
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .width(145.dp)
                        .height(43.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Loading_Orange)
                        .clickable { resetSelections() }
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        CustomText(
                            text = "Reset",
                            color = Background_Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W500,
                            modifier = Modifier
                                .padding(top = 1.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .width(145.dp)
                        .height(43.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .clickable {
                            if (type == "Movies") {
                                viewModel.getFilterMovieFlow(year, genre, country)
                            } else {
                                viewModel.getFilterTvFlow(year, genre, country)
                            }
                            isApplyClicked = true
                        }
                        .border(1.dp, Loading_Orange, shape = RoundedCornerShape(5.dp))
                ) {
                    Row(modifier = Modifier.align(Alignment.Center)) {
                        CustomText(
                            text = "Apply",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W500,
                            modifier = Modifier.padding(top = 1.dp, end = 2.dp)
                        )
                    }
                }
            }
        }
    }

    if (isApplyClicked && type == "TV Series") {
        when (filterTvItems.loadState.refresh) {
            is LoadState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LinearProgressIndicator(
                        color = Loading_Orange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    )
                }
            }

            is LoadState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    AnimatedPreloader()
                }
            }

            is LoadState.NotLoading -> {}
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    CustomImage(
                        imageId = R.drawable.ic_back,
                        modifier = Modifier
                            .size(55.dp)
                            .padding(16.dp)
                            .clickable { navController.popBackStack() },
                        contentDescription = "back"
                    )
                    CustomText(
                        text = "Results",
                        modifier = Modifier.padding(top = 15.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.White,
                    )
                }
                CustomText(
                    text = "Filter",
                    modifier = Modifier
                        .padding(top = 15.dp, end = 16.dp)
                        .clickable { isApplyClicked = false },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.White,
                )
            }

            if (mPref.getString(CommonEnum.BANNER.toString(), "") == "true") {
                AdsBanner()
            } else {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clickable { affiliateClick.invoke() },
                ) {
                    val context = LocalContext.current
                    val imageLoader = ImageLoader.Builder(context)
                        .components {
                            if (SDK_INT >= 28) {
                                add(ImageDecoderDecoder.Factory())
                            } else {
                                add(GifDecoder.Factory())
                            }
                        }
                        .build()
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context).data(data = R.drawable.xbet_banner)
                                .apply(block = {
                                    size(Size(50000, 5000))
                                }).build(), imageLoader = imageLoader
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable {
                                val openURL = Intent(Intent.ACTION_VIEW)
                                openURL.data = Uri.parse(AffiliateLink)
                                context.startActivity(openURL)
                            },
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(128.dp),
                contentPadding = PaddingValues(0.dp),
            ) {
                if (filterTvItems.itemCount == 0) {
                    item {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CustomText(
                                text = "No result found",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    items(filterTvItems.itemCount) { index ->
                        val movie = filterTvItems[index]
                        FilterTvItem(movie!!, navController)
                    }
                }
            }
        }
    }

    if (isApplyClicked && type == "Movies") {
        when (filterMoviesItems.loadState.refresh) {
            is LoadState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LinearProgressIndicator(
                        color = Loading_Orange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    )
                }
            }

            is LoadState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    AnimatedPreloader()
                }
            }

            is LoadState.NotLoading -> {}
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    CustomImage(
                        imageId = R.drawable.ic_back,
                        modifier = Modifier
                            .size(55.dp)
                            .padding(16.dp)
                            .clickable { navController.popBackStack() },
                        contentDescription = "back"
                    )
                    CustomText(
                        text = "Results",
                        modifier = Modifier.padding(top = 15.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.White,
                    )
                }
                CustomText(
                    text = "Filter",
                    modifier = Modifier
                        .padding(top = 15.dp, end = 16.dp)
                        .clickable { isApplyClicked = false },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.White,
                )
            }

            if (mPref.getString(CommonEnum.BANNER.toString(), "") == "true") {
                AdsBanner()
            } else {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clickable { affiliateClick.invoke() },
                ) {
                    val context = LocalContext.current
                    val imageLoader = ImageLoader.Builder(context)
                        .components {
                            if (SDK_INT >= 28) {
                                add(ImageDecoderDecoder.Factory())
                            } else {
                                add(GifDecoder.Factory())
                            }
                        }
                        .build()
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context).data(data = R.drawable.xbet_banner)
                                .apply(block = {
                                    size(Size(50000, 5000))
                                }).build(), imageLoader = imageLoader
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable {
                                val openURL = Intent(Intent.ACTION_VIEW)
                                openURL.data = Uri.parse(AffiliateLink)
                                context.startActivity(openURL)
                            },
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(128.dp),
                contentPadding = PaddingValues(0.dp),
            ) {
                if (filterMoviesItems.itemCount == 0) {
                    item {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CustomText(
                                text = "No result found",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    items(filterMoviesItems.itemCount) { index ->
                        val movie = filterMoviesItems[index]
                        FilterMovieItem(movie!!, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomCheckbox(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
    onSelectedGenre: (List<String>) -> Unit = {},
    text: String = "Movies",
    singleSelectionState: MutableState<Int>? = null,
    multiSelectionState: MutableState<List<Int>>? = null,
    index: Int? = null,

    ): Any? {
    val genreList: MutableState<List<Int>> = remember { mutableStateOf(listOf()) }
    val isChecked = remember { mutableStateOf(false) }

    // Update isChecked based on single or multi selection state
    if (singleSelectionState != null && index != null) {
        isChecked.value = singleSelectionState.value == index
    } else if (multiSelectionState != null && index != null) {
        isChecked.value = multiSelectionState.value.contains(index)
    }

    Row {
        Checkbox(
            modifier = Modifier.size(36.dp),
            checked = isChecked.value,
            onCheckedChange = { checked ->
                if (singleSelectionState != null && index != null) {
                    if (checked) {
                        singleSelectionState.value = index
                    }
                } else if (multiSelectionState != null && index != null) {
                    if (checked) {
                        multiSelectionState.value += index
                        genreList.value = multiSelectionState.value
                    } else {
                        multiSelectionState.value -= index
                        genreList.value = multiSelectionState.value
                    }
                }
                onCheckedChange(checked)
                onSelectedGenre(genreList.value.map { genres[it].name })
            },
            enabled = true,
            colors = CheckboxDefaults.colors(
                checkedColor = Loading_Orange,
                uncheckedColor = Loading_Orange,
                checkmarkColor = Purple80,
                disabledCheckedColor = Background_Black_40,
                disabledUncheckedColor = Background_Black_40,
                disabledIndeterminateColor = Background_Black_40
            )
        )
        CustomText(
            text = text,
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            color = Background_Black_10,
        )

    }
    // Return single item or list based on selection state
    return singleSelectionState?.value ?: multiSelectionState?.value
}

@Composable
fun FilterMovieItem(
    it: MovieFilterResponse.Result,
    navController: NavController
) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val isEmpty = mPref.getString(CommonEnum.VERSION.toString(), "")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(Color.Transparent)
            .padding(8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable {
                Log.d("FilterTvItem", "Movie clicked: ${it.id}")
                navController.navigate(
                    Screen.MovieDetailsScreen.passArguments(
                        it.id ?: 0
                    )
                )
            }
        ) {
            if (isEmpty != "") {
                CustomImage(
                    imageId = R.drawable.ic_logo,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxSize(),
                    contentDescription = "ImageRequest example",
                )
            } else {
                CustomImageAsync(
                    imageUrl = "${
                        mPref.getString(
                            CommonEnum.TMDB_IMAGE_PATH.toString(),
                            ""
                        )
                    }${it?.posterPath}",
                    size = 512,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "ImageRequest example",
                )
            }
            Box(
                modifier = Modifier
                    .width(35.dp)
                    .height(25.dp)
                    .padding(start = 5.dp, top = 5.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .align(Alignment.TopStart)
                    .background(Background_Black_70)
            )
            {
                CustomText(
                    text = "HD",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

        }
        CustomText(
            text = it.title ?: "Error",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 2,
            overFlow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(130.dp)
                .padding(start = 5.dp, top = 5.dp, end = 5.dp)
                .align(Alignment.Start)
        )
    }
}

@Composable
fun FilterTvItem(
    it: TvFilterResponse.Result,
    navController: NavController
) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val isEmpty = mPref.getString(CommonEnum.VERSION.toString(), "")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(Color.Transparent)
            .padding(8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable {
                navController.navigate(
                    Screen.SeriesDetailsScreen.passArguments(
                        it.id ?: 0
                    )
                )
            }
        ) {
            if (isEmpty != "") {
                CustomImage(
                    imageId = R.drawable.ic_logo,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxSize(),
                    contentDescription = "ImageRequest example",
                )
            } else {
                CustomImageAsync(
                    imageUrl = "${
                        mPref.getString(
                            CommonEnum.TMDB_IMAGE_PATH.toString(),
                            ""
                        )
                    }${it?.posterPath}",
                    size = 512,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "ImageRequest example",
                )
            }
            Box(
                modifier = Modifier
                    .width(35.dp)
                    .height(25.dp)
                    .padding(start = 5.dp, top = 5.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .align(Alignment.TopStart)
                    .background(Background_Black_70)
            )
            {
                CustomText(
                    text = "HD",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

        }
        CustomText(
            text = it.name ?: "Error",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 2,
            overFlow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(130.dp)
                .padding(start = 5.dp, top = 5.dp, end = 5.dp)
                .align(Alignment.Start)
        )
    }
}