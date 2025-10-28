package com.wangfangjia.everyday.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wangfangjia.everyday.data.repository.DailyRepository
import com.wangfangjia.everyday.ui.components.CalendarDialog
import com.wangfangjia.everyday.ui.home.components.*
import androidx.compose.ui.res.stringResource
import com.wangfangjia.everyday.R

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    repository: DailyRepository,
    initialDate: String? = null,
    onNavigateToEdit: (String) -> Unit
) {
    val viewModel = remember { HomeViewModel(repository, initialDate) }
    val currentDate by viewModel.currentDate.collectAsState()
    val dailyData by viewModel.dailyData.collectAsState()
    val datesWithData by viewModel.datesWithData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var showCalendarDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = {
            // 让顶部栏避开系统状态栏
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { showCalendarDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "选择日期",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = viewModel.formatDateForDisplay(currentDate),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { onNavigateToEdit(currentDate) }) {
                        Text(
                            text = stringResource(R.string.edit),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
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
                        .padding(horizontal = 12.dp, vertical = 16.dp),
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
                    
                    // 快乐日历模块
                    HappyCalendarSection(content = data.happyCalendar)
                    
                    // 日记模块
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