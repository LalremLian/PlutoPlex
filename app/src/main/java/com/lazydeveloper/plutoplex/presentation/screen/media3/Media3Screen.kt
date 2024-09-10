package com.lazydeveloper.plutoplex.presentation.screen.media3

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.PlayerView
import androidx.media3.ui.TimeBar
import androidx.navigation.NavController
import com.lazydeveloper.plutoplex.R
import com.lazydeveloper.plutoplex.player.PlayerViewModel
import com.lazydeveloper.plutoplex.player.TrackInfo
import com.lazydeveloper.plutoplex.player.VideoItem
import com.lazydeveloper.plutoplex.presentation.composables.CustomAlertDialog
import com.lazydeveloper.plutoplex.presentation.composables.CustomImage
import com.lazydeveloper.plutoplex.presentation.composables.CustomText
import com.lazydeveloper.plutoplex.ui.theme.Background_Black_40
import com.lazydeveloper.plutoplex.ui.theme.Loading_Orange
import com.lazydeveloper.plutoplex.presentation.composables.findActivity
import com.lazydeveloper.plutoplex.presentation.composables.showInterstitial
import com.lazydeveloper.plutoplex.presentation.composables.showInterstitial2
import com.lazydeveloper.plutoplex.util.toHhMmSs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*@Composable
fun Media3Screen(url: String, navController: NavController) {
    val context = LocalContext.current
    val player = remember { ExoPlayer.Builder(context).build() }
    val isExitDialogVisible = rememberSaveable { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(false) }
    var isTimeOver by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(2000000) //40 minutes
        isTimeOver = true
    }

    BackHandler { isExitDialogVisible.value = true }

    CustomAlertDialog(
        showDialog = isExitDialogVisible,
        message = "Are you sure you want to exit?",
        confirmText = "Yes",
        dismissText = "No",
        onConfirmClick = {
            if (isTimeOver){
                showInterstitial(
                    context,
                    onAdDismissed = {
                        navController.popBackStack()
                    },
                    onError = {
                        showInterstitial2(
                            context,
                            onAdDismissed = {
                                navController.popBackStack()
                            },
                            onError = {
                                navController.popBackStack()
                            }
                        )
                    }
                )
            }else{
                navController.popBackStack()
            }
        },
        onDismissClick = {
            isExitDialogVisible.value = false
        }
    )

    val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            isBuffering = when (state) {
                Player.STATE_BUFFERING -> true
                Player.STATE_READY -> false
                else -> isBuffering
            }
        }
    }

    DisposableEffect(player) {
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        isBuffering = true
        player.prepare()
        player.playWhenReady = true

        player.addListener(playerListener)

        onDispose {
            player.removeListener(playerListener)
            player.release()
        }
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                this.player = player
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(context) {
        val activity = context.findActivity()
        val window = activity?.window ?: return@DisposableEffect onDispose {}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.apply {
                hide(WindowInsetsCompat.Type.statusBars())
                hide(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            onDispose {
                insetsController.apply {
                    show(WindowInsetsCompat.Type.statusBars())
                    show(WindowInsetsCompat.Type.navigationBars())
                    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                }
            }
        } else {
            // For Android versions below R, use deprecated methods
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

            onDispose {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    // Show buffering indicator if player is buffering
    if (isBuffering) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Loading_Orange,
                strokeWidth = 2.dp,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}*/

@OptIn(UnstableApi::class) @Composable
fun Media3Screen(
    url: String,
    title: String,
    navController: NavController,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val localConfig = LocalConfiguration.current

    val isExitDialogVisible = rememberSaveable { mutableStateOf(false) }
    var isTimeOver by remember { mutableStateOf(false) }

    val isPlaying by playerViewModel.isPlayingStateFlow.collectAsStateWithLifecycle()
    val isPlaybackStarted by playerViewModel.isPlaybackStartedStateFlow.collectAsStateWithLifecycle()
    val isPlayerLoading by playerViewModel.isPlayerLoadingStateFlow.collectAsStateWithLifecycle()
    val currentVideoItem by playerViewModel.currentVideoItemStateFlow.collectAsStateWithLifecycle()
    val resizeMode by playerViewModel.resizeModeStateFlow.collectAsStateWithLifecycle()
    val playerOrientation by playerViewModel.playerOrientationStateFlow.collectAsStateWithLifecycle()

    val playerHeight = if (playerOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        (localConfig.screenWidthDp * 9 / 16).dp
    } else {
        localConfig.screenHeightDp.dp
    }

    LaunchedEffect(key1 = true) {
        delay(2000000) //40 minutes
        isTimeOver = true
    }

    LaunchedEffect(Unit) {
        playerViewModel.playNewVideo(
            VideoItem(
                name = "Second Item",
                mediaItem = MediaItem.fromUri(url),
                contentUri = url.toUri()
            )
        )
    }


    var showControls by rememberSaveable {
        mutableStateOf(false)
    }
    var showQualityControls by rememberSaveable {
        mutableStateOf(false)
    }

    if (showQualityControls) {
        Popup(
            offset = IntOffset(y = 100, x = 0),
            alignment = Alignment.TopEnd,
            properties = PopupProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            onDismissRequest = {
                showQualityControls = false
            }
        ) {
            VideoQualityMenu(
                playerViewModel = playerViewModel,
                onSelectedMenuItem = { showQualityControls = false }
            )
        }
    }

    LaunchedEffect(key1 = showControls) {
        if (showControls) {
            delay(4_000)
            showControls = false
            showQualityControls = false
        }
    }
    BackHandler { isExitDialogVisible.value = true }

    CustomAlertDialog(
        showDialog = isExitDialogVisible,
        message = "Are you sure you want to exit?",
        confirmText = "Yes",
        dismissText = "No",
        onConfirmClick = {
            if (isTimeOver){
                showInterstitial(
                    context,
                    onAdDismissed = {
                        playerViewModel.releasePlayer()
                        navController.popBackStack()
                    },
                    onError = {
                        showInterstitial2(
                            context,
                            onAdDismissed = {
                                playerViewModel.releasePlayer()
                                navController.popBackStack()
                            },
                            onError = {
                                playerViewModel.releasePlayer()
                                navController.popBackStack()
                            }
                        )
                    }
                )
            }else{
                playerViewModel.releasePlayer()
                navController.popBackStack()
            }
        },
        onDismissClick = {
            isExitDialogVisible.value = false
        }
    )

    val playerViewModifier = if (playerOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        Modifier
            .fillMaxWidth()
            .height(playerHeight)
    } else {
        Modifier.fillMaxSize()
    }.background(Color.Black)

//    BackPressHandler {
//        if (playerOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            playerViewModel.rotateScreen()
//        } else {
//            navController.popBackStack()
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = playerViewModifier
        ) {
            AmberItPlayerView(
                modifier = playerViewModifier,
                playerViewModel = playerViewModel,
                isPlaying = isPlaying,
                isPlaybackStarted = isPlaybackStarted,
                resizeMode = resizeMode,
                isControlsVisible = showControls,
                onClick = { showControls = it }
            )

            if (isPlayerLoading) {
                CircularProgressIndicator(
                    color = Loading_Orange, // Set the desired color for the ProgressBar
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .matchParentSize() // Ensure the Column takes the full height of the Box
                    .background(
                        if (showControls) {
                            Color.Black.copy(0.5f)
                        } else {
                            Color.Transparent
                        }
                    ),
            ) {
                AnimatedVisibility(
                    visible = showControls,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)// Add this modifier
                ) {
                    // Content of the first row (e.g., UpperControls)
                    UpperControls(
                        title = title,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onBackIconClick = {
                            isExitDialogVisible.value = true
                        },
                        onCastClick = {

                        },
                        onVideoProfileClick = {
                            showQualityControls = true
                        }
                    )
                }

                // Third row
                AnimatedVisibility(
                    showControls,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F) // Add this modifier
                ) {
                    // Content of the third row (e.g., MiddleControls)
                    MiddleControls(
                        isPlaying = isPlaying,
                        onClick = {
                            showControls = !showControls
                        },
                        onPlayPauseClick = {
                            playerViewModel.playPause()
                        },
                        onSeekForwardClick = {
                            playerViewModel.forward(10000)
                        },
                        onSeekBackwardClick = {
                            playerViewModel.rewind(10000)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                AnimatedVisibility(
                    showControls,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F) // Add this modifier
                ) {
                    BottomControls(
                        player = playerViewModel.player,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                        totalTime = playerViewModel.player.contentDuration,
                        onRotateScreenClick = {

                        },
                        resizeMode = {
                            Log.e("DARK", "resizeMode: ")
                            playerViewModel.resizeVideoFrame()
                        },
                        onResizeModeChange = {  }
                    )
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun AmberItPlayerView(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel,
    isPlaying: Boolean = false,
    isPlaybackStarted: Boolean = false,
    resizeMode: Int = AspectRatioFrameLayout.RESIZE_MODE_FIT,
    isControlsVisible: Boolean = false,
    onClick: (Boolean) -> Unit = {}
) {
    val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
        }

        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
        }

        override fun onPlaybackStateChanged(state: Int) {
            // Callback when the playback state changes
            when (state) {
                Player.STATE_IDLE, Player.STATE_BUFFERING -> {
                    // Handle buffering state
                    // Example: Media is buffering and not yet ready for playback
                    playerViewModel.setLoadingState(true)
                    onClick(false)
                }

                Player.STATE_READY, Player.STATE_ENDED -> {
                    // Handle Ready and End State
                    // Example: Media is ready for playback or ended
                    playerViewModel.setLoadingState(false)

                    if (state == Player.STATE_READY) {
                        playerViewModel.isPlaybackStarted(true)
                    }
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.d("PlaybackError", "onPlayerError: ${error.message}")
        }

        override fun onTracksChanged(tracks: Tracks) {
            // Update UI using current tracks.
        }
    }

    var lifecycle by rememberSaveable {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner, key2 = context) {
        val activity = context.findActivity()
        val window = activity?.window ?: return@DisposableEffect onDispose {}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val observer = LifecycleEventObserver { _, event ->
                lifecycle = event
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            WindowCompat.setDecorFitsSystemWindows(activity.window, false)
            WindowInsetsControllerCompat(activity.window, activity.window.decorView).let { controller ->
                controller.hide(
                    WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.displayCutout()
                )
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                activity.window.attributes = activity.window.attributes.apply {
                    layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            }

            onDispose {
                playerViewModel.player.removeListener(listener)
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        } else {
            // For Android versions below R, use deprecated methods
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

            onDispose {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = playerViewModel.player.also {
                    it.addListener(listener)
                }
                this.useController = false
                this.resizeMode = resizeMode
                this.keepScreenOn = isPlaying
            }
        },
        update = {
            it.apply {
                this.useController = false
                this.resizeMode = resizeMode
                this.keepScreenOn = isPlaying
            }
            when (lifecycle) {
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    it.player?.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                }

                else -> Unit
            }
        },
        modifier = modifier.clickable(
            onClick = {
                if (isPlaybackStarted) {
                    onClick(!isControlsVisible)
                }
            },
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
        )
    )
}

@Composable
@Preview
fun UpperControls(
    title: String = "",
    modifier: Modifier = Modifier,
    onBackIconClick: () -> Unit = {},
    onCastClick: () -> Unit = {},
    onVideoProfileClick: () -> Unit = {},
) {
    val iconModifier = Modifier
        .padding(8.dp)
        .size(24.dp)
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                onBackIconClick.invoke()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = iconModifier,
                tint = Color.White
            )
        }
        CustomText(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .padding(
                    start = 4.dp,
                    end = 8.dp,
                    top = 12.dp,
                )
                .width(400.dp)
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
        IconButton(
            onClick = {
                onVideoProfileClick()
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_setting),
                modifier = iconModifier,
                contentDescription = "Settings",
            )
        }
    }
}

@Composable
@Preview
private fun MiddleControls(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    onClick: () -> Unit = {},
    onPlayPauseClick: () -> Unit = {},
    onSeekForwardClick: () -> Unit = {},
    onSeekBackwardClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,

        ) {
        MiddleControlsItem(
            icon = R.drawable.seek_backward,
            contentDescription = "Seek Backward",
            onIconClick = onSeekBackwardClick,
            onSingleClick = onClick,
            onDoubleClick = onSeekBackwardClick,
            size = 26.dp
        )

        MiddleControlsItem(
            icon = if (isPlaying) androidx.media3.ui.R.drawable.exo_icon_pause else R.drawable.ic_play_player,
            contentDescription = "Play/Pause",
            onIconClick = onPlayPauseClick,
            onSingleClick = onClick,
            onDoubleClick = onPlayPauseClick,
            modifier = Modifier.padding(horizontal = 44.dp),
            size = 46.dp
        )

        MiddleControlsItem(
            icon = R.drawable.seek_forward,
            contentDescription = "Seek Forward",
            onIconClick = onSeekForwardClick,
            onSingleClick = onClick,
            onDoubleClick = onSeekForwardClick,
            size = 26.dp
        )
    }
}

@kotlin.OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MiddleControlsItem(
    @DrawableRes icon: Int,
    contentDescription: String,
    onIconClick: () -> Unit,
    onSingleClick: () -> Unit,
    onDoubleClick: () -> Unit,
    size: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .combinedClickable(
                onClick = onSingleClick,
                onDoubleClick = onDoubleClick
            ),
        contentAlignment = Alignment.Center
    ) {
        CustomImage(
            imageId = icon,
            modifier = Modifier.
            clickable { onIconClick() }
                .size(size)
        )
//        IconButton(onClick = onIconClick) {
//            Image(
//                painter = painterResource(id = icon),
//                contentDescription = ""
//            )
//        }
    }
}

@Preview
@UnstableApi
@Composable
private fun BottomControls(
    modifier: Modifier = Modifier,
    player: Player? = null,
    totalTime: Long = 0L,
    onRotateScreenClick: () -> Unit = {},
    resizeMode: () -> Unit = {},
    onResizeModeChange: () -> Unit = {}
) {
    var currentTime by remember {
        mutableLongStateOf(player?.currentPosition ?: 0L)
    }

    var isSeekInProgress by remember {
        mutableStateOf(false)
    }

    val timerCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        timerCoroutineScope.launch {
            while (true) {
                delay(500)
                if (!isSeekInProgress) {
                    currentTime = player?.currentPosition ?: 0L
                    Log.d("PlayerScreen", "timer running $currentTime")
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomSeekBar(
                player = player,
                isSeekInProgress = { isInProgress ->
                    isSeekInProgress = isInProgress
                },
                onSeekBarMove = { position ->
                    currentTime = position
                },
                totalDuration = totalTime,
                currentTime = currentTime,
                modifier = Modifier.weight(1F)
            )

            IconButton(
                onClick = resizeMode,
                modifier = Modifier
                    .size(32.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_full_screen),
                    contentDescription = null,
                    tint = Color.White
                )
            }

//            IconButton(
//                onClick = onResizeModeChange,
//                modifier = Modifier
//                    .size(32.dp)
//            ) {
//                Icon(
//                    painterResource(id = R.drawable.ic_pip),
//                    stringResource(id = R.string.picture_in_picture),
//                    tint = Color.White,
//                )
//            }
        }
        CustomText(
            text = "${currentTime.toHhMmSs()} / ${totalTime.toHhMmSs()}",
            modifier = Modifier.padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@UnstableApi
@Composable
fun CustomSeekBar(
    modifier: Modifier = Modifier,
    player: Player? = null,
    isSeekInProgress: (Boolean) -> Unit,
    onSeekBarMove: (Long) -> Unit,
    currentTime: Long,
    totalDuration: Long
) {
    AndroidView(
        factory = { context ->
            val listener = object : TimeBar.OnScrubListener {

                var previousScrubPosition = 0L

                override fun onScrubStart(timeBar: TimeBar, position: Long) {
                    isSeekInProgress(true)
                    previousScrubPosition = position
                }

                override fun onScrubMove(timeBar: TimeBar, position: Long) {
                    onSeekBarMove(position)
                }

                override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                    if (canceled) {
                        player?.seekTo(previousScrubPosition)
                    } else {
                        player?.seekTo(position)
                    }
                    isSeekInProgress(false)
                }
            }
            DefaultTimeBar(context).apply {
                setScrubberColor(Loading_Orange.toArgb())
                setPlayedColor(Loading_Orange.toArgb())
                setUnplayedColor(Background_Black_40.toArgb())
                addListener(listener)
                setDuration(totalDuration)
                setPosition(player?.currentPosition ?: 0)
            }
        },
        update = {
            it.apply {
                setPosition(currentTime)
            }
        },

        modifier = modifier
    )
}

@OptIn(UnstableApi::class)
@Composable
fun VideoQualityMenu(
    playerViewModel: PlayerViewModel,
    onSelectedMenuItem: () -> Unit = {}
) {
    val trackType = C.TRACK_TYPE_VIDEO
    val trackGroups = ArrayList<Tracks.Group>()
    for (trackGroup in playerViewModel.player.currentTracks.groups) {
        if (trackGroup.type == trackType) {
            trackGroups.add(trackGroup)
        }
    }

    Box(
        modifier = Modifier.padding(top = 10.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(4))
                .background(Color.Black)
                .padding(vertical = 4.dp)
                .wrapContentHeight()
                .wrapContentWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn {
                // Iterate through track groups and tracks to log profiles
                for (trackGroupIndex in trackGroups.indices) {
                    val trackGroup1 = trackGroups[trackGroupIndex]
                    val isOverridesEmpty = playerViewModel.player.trackSelectionParameters.overrides.isEmpty()

                    item {
                        VideoQualityItem(text = "Auto", isSelected = isOverridesEmpty) {
                            playerViewModel.player.trackSelectionParameters =
                                playerViewModel.player.trackSelectionParameters
                                    .buildUpon()
                                    .clearOverrides()
                                    .build()
                            onSelectedMenuItem.invoke()
                        }
                    }
                    items(trackGroup1.length) { trackIndex ->
//                        val isDisabled = playerViewModel.player.trackSelectionParameters.disabledTrackTypes.contains(trackType)
                        val trackInfo = TrackInfo(trackGroup1, trackIndex)
                        val trackGroup = trackInfo.trackGroup.mediaTrackGroup
                        val isSelected = if (!isOverridesEmpty) {
                            trackGroup1.isTrackSelected(trackIndex)
                        } else {
                            false
                        }
                        val format = trackInfo.format
                        val profile = "${format.height}p"
                        VideoQualityItem(text = profile, isSelected = isSelected) {
                            playerViewModel.player.trackSelectionParameters =
                                playerViewModel.player.trackSelectionParameters
                                    .buildUpon()
                                    .setOverrideForType(
                                        TrackSelectionOverride(
                                            trackGroup, /* trackIndex= */
                                            trackIndex
                                        )
                                    )
                                    .build()
                            onSelectedMenuItem.invoke()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoQualityItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick.invoke() }
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = text,
            color = if (isSelected) {
                Color.Red
            } else {
                Color.White
            },
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.size(4.dp))
        if (isSelected) {
            Icon(
                painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                tint = Color.Red
            )
        }
    }
}

private fun handleSystemBarsVisibility(playerOrientation: Int, activity: Activity) {
    if (playerOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        WindowInsetsControllerCompat(activity.window, activity.window.decorView).let { controller ->
            controller.hide(
                WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.displayCutout()
            )
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.window.attributes = activity.window.attributes.apply {
                layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }
    } else {
        WindowCompat.setDecorFitsSystemWindows(activity.window, true)
        WindowInsetsControllerCompat(activity.window, activity.window.decorView).let { controller ->
            controller.show(
                WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.displayCutout()
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.window.attributes = activity.window.attributes.apply {
                layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
            }
        }
    }
}