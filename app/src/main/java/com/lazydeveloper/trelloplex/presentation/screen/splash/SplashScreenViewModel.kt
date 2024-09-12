package com.lazydeveloper.trelloplex.presentation.screen.splash

import androidx.lifecycle.ViewModel
import com.lazydeveloper.data.preference.SessionPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val sessionPreference: SessionPreference
): ViewModel() {
    val mPref = sessionPreference
}