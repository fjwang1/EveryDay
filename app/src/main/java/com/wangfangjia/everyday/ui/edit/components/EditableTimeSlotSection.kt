package com.wangfangjia.everyday.ui.edit.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wangfangjia.everyday.data.models.TimeSlotTask

/**
 * 可编辑时间段任务模块
 */
@Composable
fun EditableTimeSlotSection(
    timeSlotTasks: List<TimeSlotTask>,
    onTaskChanged: (TimeSlotTask) -> Unit,
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
                EditableTimeSlotItem(
                    task = task,
                    onTaskChanged = onTaskChanged
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun EditableTimeSlotItem(
    task: TimeSlotTask,
    onTaskChanged: (TimeSlotTask) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 时间段标签
        Text(
            text = "${task.startTime}～${task.endTime}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // 任务详情输入框
        OutlinedTextField(
            value = task.taskDetail,
            onValueChange = { newDetail ->
                onTaskChanged(task.copy(taskDetail = newDetail))
            },
            label = { Text("任务详情") },
            placeholder = {
                Text(
                    text = "输入今日任务内容...",
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Gray.copy(alpha = 0.6f),
                                Color.Gray.copy(alpha = 0.3f)
                            )
                        )
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 备注输入框
        OutlinedTextField(
            value = task.note,
            onValueChange = { newNote ->
                onTaskChanged(task.copy(note = newNote))
            },
            label = { Text("备注（可选）") },
            placeholder = {
                Text(
                    text = "添加备注信息...",
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Gray.copy(alpha = 0.6f),
                                Color.Gray.copy(alpha = 0.3f)
                            )
                        )
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 1,
            maxLines = 2
        )
        
        // 完成状态勾选框
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { isChecked ->
                    onTaskChanged(task.copy(isCompleted = isChecked))
                }
            )
            Text(
                text = "已完成",
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

