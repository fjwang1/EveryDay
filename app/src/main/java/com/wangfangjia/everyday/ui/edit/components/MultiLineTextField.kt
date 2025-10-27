package com.wangfangjia.everyday.ui.edit.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * 多行文本编辑框（灰色渐变提示）
 */
@Composable
fun MultiLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    minLines: Int = 3
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = {
            Text(
                text = label,
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
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = (minLines * 24).dp),
        minLines = minLines,
        maxLines = 10
    )
}

