package com.lazydeveloper.plutoplex.player

import androidx.media3.common.Format
import androidx.media3.common.Tracks

class TrackInfo(val trackGroup: Tracks.Group, val trackIndex: Int) {
    
    val format: Format
        get() = trackGroup.getTrackFormat(trackIndex)
}