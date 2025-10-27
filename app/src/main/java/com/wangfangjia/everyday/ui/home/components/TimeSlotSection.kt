package com.wangfangjia.everyday.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wangfangjia.everyday.data.models.TimeSlotTask
import com.wangfangjia.everyday.ui.components.EmptyPlaceholder

/**
 * 时间段任务模块（时间轴布局，只读，但完成状态可操作）
 */
@Composable
fun TimeSlotSection(
    timeSlotTasks: List<TimeSlotTask>,
    onTaskCompletionChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                text = "今日任务",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            timeSlotTasks.forEach { task ->
                TimeSlotItem(
                    task = task,
                    onCompletionChanged = { isCompleted ->
                        onTaskCompletionChanged(task.timeSlotId, isCompleted)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun TimeSlotItem(
    task: TimeSlotTask,
    onCompletionChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // 时间段标签
        Text(
            text = "${task.startTime}～${task.endTime}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(110.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // 任务详情和勾选框
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (task.taskDetail.isEmpty()) {
                EmptyPlaceholder(text = "暂时未定义任务")
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.taskDetail,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                        
                        if (task.note.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "备注：${task.note}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // 完成状态勾选框
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = onCompletionChanged
                    )
                }
            }
        }
    }
}

