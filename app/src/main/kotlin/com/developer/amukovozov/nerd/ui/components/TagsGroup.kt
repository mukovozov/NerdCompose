package com.developer.amukovozov.nerd.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.emoji.text.EmojiCompat
import com.developer.amukovozov.nerd.model.Tag

private val tagHeight = 32.dp
private val tagPadding = 8.dp

@Composable
fun TagsGroup(modifier: Modifier = Modifier, tags: List<Tag>) {

    Layout(
        modifier = modifier,
        content = {
            tags.map {
                Chip(
                    text = it.title,
                    emojiCode = it.emojiCode,
                    backgroundColor = it.backgroundColor,
                    textColor = it.textColor
                )
            }
        }) { measurables, constraints ->
        var rowCount = 1
        var measurableRawWidth = 0
        val placeableToRow = linkedMapOf<Placeable, Int>()

        val layoutWidth = constraints.minWidth.rangeTo(constraints.maxWidth).maxOrNull() ?: constraints.minWidth

        measurables.map { measurable ->
            val placeable = measurable.measure(constraints)
            val proposalRawWidth = measurableRawWidth + placeable.width
            measurableRawWidth = if (proposalRawWidth < layoutWidth) {
                proposalRawWidth
            } else {
                rowCount++
                placeable.width
            }

            placeableToRow[placeable] = rowCount

            placeable
        }

        layout(constraints.maxWidth, tagHeight.roundToPx() * rowCount) {
            var chipX = 0
            var chipY = 0
            var drawedRaw = 0
            placeableToRow.entries.forEach { entry ->
                val (placealbe, row) = entry
                when {
                    drawedRaw == 0 -> {
                        placealbe.placeRelative(x = chipX, y = chipY)
                    }
                    row == drawedRaw -> {
                        chipX += placealbe.width + tagPadding.roundToPx()
                        placealbe.placeRelative(x = chipX, y = chipY)
                    }
                    else -> {
                        chipY += tagHeight.roundToPx() + tagPadding.roundToPx()
                        chipX = 0
                        placealbe.placeRelative(x = chipX, y = chipY)
                    }
                }
                drawedRaw = row
            }
        }
    }
}

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    text: String,
    emojiCode: String?,
    backgroundColor: String?,
    textColor: String?
) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = backgroundColor.hexToColor(defaultColor = Color.Black)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            emojiCode?.let {
                val processed = EmojiCompat.get().process(emojiCode)
                Text(text = processed.toString())
                Spacer(Modifier.width(4.dp))
            }

            val tColor = textColor.hexToColor(defaultColor = Color.White)
            Text(text = text, color = tColor)
        }
    }
}

@Composable
@Preview
fun TagsGroupPreview() {
    TagsGroup(
        tags = listOf(
            Tag(1, "perviy", null, "33C22D3D", "FFC22D3D"),
            Tag(2, "vtoroy", null, null, null),
            Tag(3, "tretiy", null, null, null)
        )
    )
}

private fun String?.hexToColor(defaultColor: Color): Color {
    val colorRgb = this?.toLong(radix = 16) ?: return defaultColor
    return Color(colorRgb)
}