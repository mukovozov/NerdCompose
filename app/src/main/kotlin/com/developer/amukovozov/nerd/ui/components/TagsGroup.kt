package com.developer.amukovozov.nerd.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
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
import com.developer.amukovozov.nerd.model.feed.CountableTag
import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.ui.theme.primaryTextColor
import timber.log.Timber

private val tagHeight = 32.dp
private var tagPadding = 8.dp
private var tagTopPadding = 0.dp

@Composable
fun CountableTagsGroup(tags: List<CountableTag>, modifier: Modifier = Modifier) {
    tagTopPadding = 16.dp
    tagPadding = 12.dp
    TagsGroupLayout(modifier = modifier) {
        tags.map {
            ChipWithCounter(it.tag.title, it.tag.emojiCode, it.tag.backgroundColor, it.tag.textColor, it.count)
        }
    }
}

@Composable
fun TagsGroup(modifier: Modifier = Modifier, tags: List<Tag>) {
    tagTopPadding = 0.dp
    tagPadding = 8.dp
    TagsGroupLayout(modifier = modifier) {
        tags.map { tag ->
            Chip(
                text = tag.title,
                emojiCode = tag.emojiCode,
                backgroundColor = tag.backgroundColor,
                textColor = tag.textColor
            )
        }
    }
}

@Composable
private fun TagsGroupLayout(modifier: Modifier, content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = { content.invoke() }
    ) { measurables, constraints ->
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

        layout(constraints.maxWidth, tagHeight.roundToPx() * rowCount + tagTopPadding.roundToPx()) {
            var chipX = 0
            var chipY = tagTopPadding.roundToPx()
            var drawedRow = 0
            placeableToRow.entries.forEach { entry ->
                val (placealbe, row) = entry
                when {
                    drawedRow == 0 -> {
                        placealbe.placeRelative(x = chipX, y = chipY)
                    }
                    row == drawedRow -> {
                        chipX += placealbe.width + tagPadding.roundToPx()
                        placealbe.placeRelative(x = chipX, y = chipY)
                    }
                    else -> {
                        chipY += tagHeight.roundToPx() + tagPadding.roundToPx() + tagTopPadding.roundToPx()
                        chipX = 0
                        placealbe.placeRelative(x = chipX, y = chipY)
                    }
                }
                Timber.d("ROW = $drawedRow, chipX= $chipX chipY = $chipY, width = ${placealbe.width} height = ${placealbe.height}")
                drawedRow = row
            }
        }
    }
}

@Composable
fun ChipWithCounter(
    text: String,
    emojiCode: String?,
    backgroundColor: String?,
    textColor: String?,
    count: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Chip(
            text = text,
            emojiCode = emojiCode,
            backgroundColor = backgroundColor,
            textColor = textColor
        )
        if (count > 0) {
            Card(
                modifier = Modifier
                    .offset(x = 12.dp, y = (-16).dp)
                    .align(Alignment.TopEnd),
                border = BorderStroke(color = Color.Black, width = Dp.Hairline),
                shape = RoundedCornerShape(12.dp),
                backgroundColor = textColor.hexToColor(defaultColor = Color.Black)
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.caption,
                    color = primaryTextColor,
                    modifier = Modifier
                        .padding(
                            vertical = 8.dp,
                            horizontal = if (count < 10) 8.dp else 4.dp
                        )
                )
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

            val tColor = textColor.hexToColor()
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
            Tag(2, "vtoroy", null, "3373BFEE", "FF73BFEE"),
            Tag(3, "tretiy", null, "33EECAA4", "FFEECAA4")
        )
    )
}

@Composable
@Preview
fun CountableTagsGroupPreview() {
    CountableTagsGroup(
        tags = listOf(
            CountableTag(Tag(1, "perviy", null, "33C22D3D", "FFC22D3D"), count = 56),
            CountableTag(Tag(2, "vtoroy", null, "3373BFEE", "FF73BFEE"), count = 12),
            CountableTag(Tag(3, "tretiy", null, "33EECAA4", "FFEECAA4"), count = 3)
        )
    )
}


private fun String?.hexToColor(defaultColor: Color = Color.White): Color {
    val colorRgb = this?.toLong(radix = 16) ?: return defaultColor
    return Color(colorRgb)
}