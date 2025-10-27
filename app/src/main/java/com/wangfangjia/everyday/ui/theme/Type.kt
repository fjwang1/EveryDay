package com.wangfangjia.everyday.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// 现代简约的排版系统（整体放大字号）
val Typography = Typography(
    // 大标题
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,       // 22 -> 24
        lineHeight = 30.sp,     // 28 -> 30
        letterSpacing = 0.sp
    ),
    // 中标题
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,       // 18 -> 20
        lineHeight = 26.sp,     // 24 -> 26
        letterSpacing = 0.sp
    ),
    // 小标题
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,       // 16 -> 18
        lineHeight = 24.sp,     // 22 -> 24
        letterSpacing = 0.sp
    ),
    // 正文大
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,       // 16 -> 18
        lineHeight = 26.sp,     // 24 -> 26
        letterSpacing = 0.5.sp
    ),
    // 正文中
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,       // 14 -> 16
        lineHeight = 22.sp,     // 20 -> 22
        letterSpacing = 0.25.sp
    ),
    // 正文小
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,       // 12 -> 14
        lineHeight = 18.sp,     // 16 -> 18
        letterSpacing = 0.4.sp
    ),
    // 标签
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,       // 14 -> 16
        lineHeight = 22.sp,     // 20 -> 22
        letterSpacing = 0.1.sp
    )
)