package com.wangfangjia.everyday.ui.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wangfangjia.everyday.data.repository.DailyRepository
import com.wangfangjia.everyday.ui.components.CalendarDialog
import com.wangfangjia.everyday.ui.edit.components.*

/**
 * 编辑页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    date: String,
    repository: DailyRepository,
    onNavigateBack: () -> Unit
) {
    val viewModel = remember { EditViewModel(repository, date) }
    val currentDate by viewModel.currentDate.collectAsState()
    val draftData by viewModel.draftData.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val hasUnsavedChanges by viewModel.hasUnsavedChanges.collectAsState()
    
    var showCalendarDialog by remember { mutableStateOf(false) }
    var showUnsavedChangesDialog by remember { mutableStateOf(false) }
    
    // 获取所有有数据的日期（用于日历显示）
    var datesWithData by remember { mutableStateOf<List<String>>(emptyList()) }
    LaunchedEffect(Unit) {
        datesWithData = repository.getAllDates()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = viewModel.formatDateForDisplay(currentDate),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showCalendarDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "选择日期"
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (hasUnsavedChanges) {
                            showUnsavedChangesDialog = true
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.saveData {
                                onNavigateBack()
                            }
                        },
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "保存"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        draftData?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 每日提醒模块（可编辑）
                EditableReminderSection(
                    reminders = data.reminders,
                    onRemindersChanged = { newReminders ->
                        viewModel.updateReminders(newReminders)
                    }
                )
                
                // 时间段任务模块（可编辑）
                EditableTimeSlotSection(
                    timeSlotTasks = data.timeSlotTasks,
                    onTaskChanged = { updatedTask ->
                        viewModel.updateTimeSlotTask(updatedTask)
                    }
                )
                
                // 快乐日历模块（可编辑）
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "快乐日历",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        MultiLineTextField(
                            value = data.happyCalendar,
                            onValueChange = { newContent ->
                                viewModel.updateHappyCalendar(newContent)
                            },
                            label = "记录今日快乐的事情",
                            minLines = 3
                        )
                    }
                }
                
                // 日记模块（可编辑）
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "今日日记",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        MultiLineTextField(
                            value = data.diary,
                            onValueChange = { newContent ->
                                viewModel.updateDiary(newContent)
                            },
                            label = "记录今日的感想和总结",
                            minLines = 5
                        )
                    }
                }
            }
        }
    }
    
    // 日历弹窗
    if (showCalendarDialog) {
        CalendarDialog(
            currentDate = currentDate,
            datesWithData = datesWithData,
            onDateSelected = { selectedDate ->
                if (hasUnsavedChanges) {
                    // TODO: 提示保存
                }
                viewModel.changeDate(selectedDate)
            },
            onDismiss = { showCalendarDialog = false }
        )
    }
    
    // 未保存更改提示对话框
    if (showUnsavedChangesDialog) {
        AlertDialog(
            onDismissRequest = { showUnsavedChangesDialog = false },
            title = { Text("未保存的更改") },
            text = { Text("您有未保存的更改，是否保存后离开？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.saveData {
                            onNavigateBack()
                        }
                    }
                ) {
                    Text("保存并离开")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showUnsavedChangesDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("不保存")
                }
            }
        )
    }
}

