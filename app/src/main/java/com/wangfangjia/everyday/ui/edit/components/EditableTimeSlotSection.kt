package com.wangfangjia.everyday.ui.edit.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.wangfangjia.everyday.data.models.TimeSlotTask

/**
 * 可编辑时间段任务模块 - 现代化设计
 */
@Composable
fun EditableTimeSlotSection(
    timeSlotTasks: List<TimeSlotTask>,
    onTaskChanged: (TimeSlotTask) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "今日任务",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            timeSlotTasks.forEachIndexed { index, task ->
                EditableTimeSlotItem(
                    task = task,
                    onTaskChanged = onTaskChanged
                )
                if (index < timeSlotTasks.size - 1) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
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
            text = "${task.startTime} - ${task.endTime}",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
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
            maxLines = 4,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
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
            maxLines = 2,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        )
        
        // 完成状态勾选框
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { isChecked ->
                    onTaskChanged(task.copy(isCompleted = isChecked))
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "已完成",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

