package com.wangfangjia.everyday.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wangfangjia.everyday.data.repository.DailyRepository
import com.wangfangjia.everyday.ui.components.CalendarDialog
import com.wangfangjia.everyday.ui.home.components.*

/**
 * 首页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    repository: DailyRepository,
    onNavigateToEdit: (String) -> Unit
) {
    val viewModel = remember { HomeViewModel(repository) }
    val currentDate by viewModel.currentDate.collectAsState()
    val dailyData by viewModel.dailyData.collectAsState()
    val datesWithData by viewModel.datesWithData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var showCalendarDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { showCalendarDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "选择日期",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = viewModel.formatDateForDisplay(currentDate),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToEdit(currentDate) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "编辑",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            dailyData?.let { data ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 每日提醒模块
                    ReminderSection(reminders = data.reminders)
                    
                    // 时间段任务模块
                    TimeSlotSection(
                        timeSlotTasks = data.timeSlotTasks,
                        onTaskCompletionChanged = { timeSlotId, isCompleted ->
                            viewModel.updateTaskCompletion(timeSlotId, isCompleted)
                        }
                    )
                    
                    // 快乐日历模块（只在有内容时显示）
                    HappyCalendarSection(content = data.happyCalendar)
                    
                    // 日记模块（只在有内容时显示）
                    DiarySection(content = data.diary)
                }
            }
        }
    }
    
    // 日历弹窗
    if (showCalendarDialog) {
        CalendarDialog(
            currentDate = currentDate,
            datesWithData = datesWithData,
            onDateSelected = { date ->
                viewModel.changeDate(date)
            },
            onDismiss = { showCalendarDialog = false }
        )
    }
}

