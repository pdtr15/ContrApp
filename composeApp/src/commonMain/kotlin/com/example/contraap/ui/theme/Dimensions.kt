package com.example.contraap.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Dimensions {
    // Breakpoints
    const val COMPACT_WIDTH = 600
    const val MEDIUM_WIDTH = 840

    @Composable
    fun getScreenWidth(): Int {
        return LocalConfiguration.current.screenWidthDp
    }

    @Composable
    fun getScreenHeight(): Int {
        return LocalConfiguration.current.screenHeightDp
    }

    @Composable
    fun isCompact(): Boolean = getScreenWidth() < COMPACT_WIDTH

    @Composable
    fun isMedium(): Boolean = getScreenWidth() >= COMPACT_WIDTH && getScreenWidth() < MEDIUM_WIDTH

    @Composable
    fun isExpanded(): Boolean = getScreenWidth() >= MEDIUM_WIDTH

    // Padding responsivo
    @Composable
    fun paddingSmall(): Dp = when {
        isCompact() -> 8.dp
        isMedium() -> 12.dp
        else -> 16.dp
    }

    @Composable
    fun paddingMedium(): Dp = when {
        isCompact() -> 16.dp
        isMedium() -> 20.dp
        else -> 24.dp
    }

    @Composable
    fun paddingLarge(): Dp = when {
        isCompact() -> 24.dp
        isMedium() -> 28.dp
        else -> 32.dp
    }

    // Tamaños de fuente responsivos
    @Composable
    fun fontSizeSmall(): TextUnit = when {
        isCompact() -> 12.sp
        isMedium() -> 13.sp
        else -> 14.sp
    }

    @Composable
    fun fontSizeMedium(): TextUnit = when {
        isCompact() -> 14.sp
        isMedium() -> 15.sp
        else -> 16.sp
    }

    @Composable
    fun fontSizeLarge(): TextUnit = when {
        isCompact() -> 16.sp
        isMedium() -> 18.sp
        else -> 20.sp
    }

    @Composable
    fun fontSizeTitle(): TextUnit = when {
        isCompact() -> 20.sp
        isMedium() -> 24.sp
        else -> 28.sp
    }

    @Composable
    fun fontSizeHeadline(): TextUnit = when {
        isCompact() -> 24.sp
        isMedium() -> 28.sp
        else -> 32.sp
    }

    // Tamaños de iconos
    @Composable
    fun iconSizeSmall(): Dp = when {
        isCompact() -> 16.dp
        isMedium() -> 18.dp
        else -> 20.dp
    }

    @Composable
    fun iconSizeMedium(): Dp = when {
        isCompact() -> 24.dp
        isMedium() -> 28.dp
        else -> 32.dp
    }

    @Composable
    fun iconSizeLarge(): Dp = when {
        isCompact() -> 40.dp
        isMedium() -> 48.dp
        else -> 56.dp
    }

    // Tamaños de botones
    @Composable
    fun buttonHeight(): Dp = when {
        isCompact() -> 48.dp
        isMedium() -> 56.dp
        else -> 60.dp
    }

    // Tamaños de cards
    @Composable
    fun cardElevation(): Dp = when {
        isCompact() -> 2.dp
        isMedium() -> 3.dp
        else -> 4.dp
    }

    @Composable
    fun categoryCardWidth(): Dp = when {
        isCompact() -> 100.dp
        isMedium() -> 120.dp
        else -> 140.dp
    }

    @Composable
    fun categoryCardHeight(): Dp = when {
        isCompact() -> 85.dp
        isMedium() -> 100.dp
        else -> 115.dp
    }
}