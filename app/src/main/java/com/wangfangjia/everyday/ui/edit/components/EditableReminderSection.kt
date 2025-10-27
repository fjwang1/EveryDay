package com.wangfangjia.everyday.ui.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * 可编辑提醒列表
 */
@Composable
fun EditableReminderSection(
    reminders: List<String>,
    onRemindersChanged: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var editingText by remember { mutableStateOf("") }
    
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "每日提醒",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                IconButton(onClick = { 
                    editingIndex = null
                    editingText = ""
                    showAddDialog = true 
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "新增提醒",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (reminders.isEmpty()) {
                Text(
                    text = "暂无提醒，点击右上角添加",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                reminders.forEachIndexed { index, reminder ->
                    SwipeToDeleteItem(
                        reminder = reminder,
                        onDelete = {
                            val newReminders = reminders.toMutableList()
                            newReminders.removeAt(index)
                            onRemindersChanged(newReminders)
                        },
                        onEdit = {
                            editingIndex = index
                            editingText = reminder
                            showAddDialog = true
                        }
                    )
                    
                    if (index < reminders.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
    
    // 添加/编辑对话框
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(if (editingIndex != null) "编辑提醒" else "添加提醒") },
            text = {
                OutlinedTextField(
                    value = editingText,
                    onValueChange = { editingText = it },
                    label = { Text("提醒内容") },
                    placeholder = {
                        Text(
                            text = "输入提醒内容...",
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
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editingText.isNotBlank()) {
                            val newReminders = reminders.toMutableList()
                            if (editingIndex != null) {
                                newReminders[editingIndex!!] = editingText
                            } else {
                                newReminders.add(editingText)
                            }
                            onRemindersChanged(newReminders)
                        }
                        showAddDialog = false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
private fun SwipeToDeleteItem(
    reminder: String,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        // 背景删除按钮
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = Color.White
                )
            }
        }
        
        // 提醒内容卡片
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            // 如果滑动超过一定距离，则显示删除按钮
                            offsetX = if (offsetX < -50) -100f else 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffset = offsetX + dragAmount
                            offsetX = newOffset.coerceIn(-100f, 0f)
                        }
                    )
                },
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "• $reminder",
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

