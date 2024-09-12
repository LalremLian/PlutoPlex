package com.lazydeveloper.trelloplex.presentation.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
//import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.data.Resource
import com.lazydeveloper.network.model.SearchResponse
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomImageAsync
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.ui.theme.Background_Black_70
import com.lazydeveloper.trelloplex.ui.theme.Loading_Orange
import com.lazydeveloper.trelloplex.util.CommonEnum
import com.lazydeveloper.trelloplex.util.getSharedPrefs
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navHostController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val searchNetworkState by viewModel.searchFlow.collectAsState()

    var isLoading by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier.background(Color.Red),
        query = searchQuery,
        onQueryChange = { searchQuery = it },
        onSearch = { query ->
            // Handle search ImeAction.Search here
            isLoading = true
            viewModel.fetchTopRatedMovie(searchQuery)
        },
        active = true,
        onActiveChange = { isActive ->
            // Handle active state change here
        },

        placeholder = { Text("Search by movie name") },
        leadingIcon = {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = "back",
                modifier = Modifier.clickable {
                    navHostController.popBackStack()
                }
            )
        },
        trailingIcon = {
            Icon(Icons.Default.Search, contentDescription = null,
                modifier = Modifier.clickable {
                    isLoading = true
                    viewModel.fetchTopRatedMovie(searchQuery)
                }
            )
        }
    ) {

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                LinearProgressIndicator(
                    color = Loading_Orange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.TopCenter)
                )
            }
        }
        searchNetworkState.let {
            when (it) {
                is Resource.Loading -> {}

                is Resource.Success -> {
                    isLoading = false
                    SearchList(
                        results = it.data.results,
                        navController = navHostController
                    )
                }

                is Resource.Error -> {
                    isLoading = false
                    Box(modifier = Modifier.fillMaxSize()) {
                        CustomText(
                            text = "No data found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }
                }

                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchList(
    results: List<SearchResponse.Result?>? = null,
    navController: NavController? = null
) {
    if(results.isNullOrEmpty())
    {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(0.dp),
        ) {
            items(9) { item ->
                    SearchItem(
                        it = null,
                        navController = navController
                    )

            }
        }
    }else{
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(0.dp),
        ) {
            items(results!!.size) { item ->
                if (results[item]?.mediaType.equals("movie")
                    || results[item]?.mediaType.equals("tv")
                ) {
                    SearchItem(
                        it = results[item]!!,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchItem(
    it: SearchResponse.Result? = null,
    navController: NavController? = null
) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val isEmpty = mPref.getString(CommonEnum.VERSION.toString(), "")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .background(Color.Transparent)
            .padding(8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable {
                if (it?.mediaType.equals("movie")) {
                    navController?.navigate(
                        Screen.MovieDetailsScreen.passArguments(
                            it?.id ?: 0
                        )
                    )
                } else {
                    navController?.navigate(
                        Screen.SeriesDetailsScreen.passArguments(
                            it?.id ?: 0
                        )
                    )
                }
            }
        ) {
            if(isEmpty != ""){
                CustomImage(
                    imageId = R.drawable.ic_logo,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxSize(),
                    contentDescription = "ImageRequest example",
                )
            }else{
                CustomImageAsync(
                    imageUrl = "${mPref.getString(CommonEnum.TMDB_IMAGE_PATH.toString(),"")}${it?.posterPath}",
                    size = 512,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "poster",
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
                    text = it?.mediaType!!.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}