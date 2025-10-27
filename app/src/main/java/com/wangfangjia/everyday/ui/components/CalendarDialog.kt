package com.wangfangjia.everyday.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * 日历弹窗
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDialog(
    currentDate: String,
    datesWithData: List<String>,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val displayFormatter = DateTimeFormatter.ofPattern("yyyy年M月")
    
    // 当前选中日期
    val selectedDate = remember {
        try {
            LocalDate.parse(currentDate, dateFormatter)
        } catch (e: Exception) {
            LocalDate.now()
        }
    }
    
    val yearMonth = YearMonth.from(selectedDate)
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // 月份标题
                Text(
                    text = yearMonth.format(displayFormatter),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    textAlign = TextAlign.Center
                )
                
                // 星期标题
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("日", "一", "二", "三", "四", "五", "六").forEach { day ->
                        Text(
                            text = day,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // 日期网格
                CalendarGrid(
                    yearMonth = yearMonth,
                    selectedDate = selectedDate,
                    datesWithData = datesWithData,
                    dateFormatter = dateFormatter,
                    onDateSelected = { date ->
                        onDateSelected(date)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CalendarGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    datesWithData: List<String>,
    dateFormatter: DateTimeFormatter,
    onDateSelected: (String) -> Unit
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0 = Sunday
    
    // 创建日历网格数据（包含空白日期）
    val calendarDays = mutableListOf<LocalDate?>()
    
    // 添加月初空白
    repeat(firstDayOfWeek) {
        calendarDays.add(null)
    }
    
    // 添加当月日期
    for (day in 1..daysInMonth) {
        calendarDays.add(yearMonth.atDay(day))
    }
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(calendarDays) { date ->
            if (date != null) {
                val dateStr = date.format(dateFormatter)
                val hasData = dateStr in datesWithData
                val isSelected = date == selectedDate
                
                CalendarDayItem(
                    day = date.dayOfMonth,
                    hasData = hasData,
                    isSelected = isSelected,
                    onClick = {
                        onDateSelected(dateStr)
                    }
                )
            } else {
                // 空白占位
                Box(modifier = Modifier.size(40.dp))
            }
        }
    }
}

@Composable
private fun CalendarDayItem(
    day: Int,
    hasData: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                color = when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    hasData -> MaterialTheme.colorScheme.primaryContainer
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                hasData -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            },
            textAlign = TextAlign.Center,
            fontWeight = if (isSelected || hasData) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}