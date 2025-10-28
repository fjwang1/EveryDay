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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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
                    key(reminder + index) { // 添加唯一key确保每个item状态独立
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
                    }

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
            title = {
                Text(
                    text = if (editingIndex != null) "编辑提醒" else "添加提醒",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                OutlinedTextField(
                    value = editingText,
                    onValueChange = { editingText = it },
                    placeholder = {
                        Text(
                            text = "输入提醒内容...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            confirmButton = {
                Button(
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
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAddDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("取消")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
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
    val deleteButtonWidth = 60.dp // 减少宽度从80dp到60dp
    val deleteButtonWidthPx = with(LocalDensity.current) { deleteButtonWidth.toPx() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        // 背景删除按钮 - 固定在右侧，左侧无圆角
        Box(
            modifier = Modifier
                .width(deleteButtonWidth)
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .background(
                    Color.Red,
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 8.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 8.dp
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(40.dp) // 稍微减小按钮尺寸
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp) // 稍微减小图标尺寸
                )
            }
        }

        // 提醒内容卡片 - 可滑动
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(reminder) { // 使用reminder作为key，确保每个item独立
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            // 滑动超过一半删除按钮宽度就完全展开
                            offsetX = if (offsetX < -deleteButtonWidthPx / 2) {
                                -deleteButtonWidthPx
                            } else {
                                0f
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffset = offsetX + dragAmount
                            // 限制滑动范围：最多向左滑动删除按钮的宽度
                            offsetX = newOffset.coerceIn(-deleteButtonWidthPx, 0f)
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}