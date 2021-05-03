package com.developer.amukovozov.nerd.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.developer.amukovozov.nerd.model.Tag

private val tagHeight = 32.dp
private val tagPadding = 8.dp

@Composable
fun TagsGroup(modifier: Modifier = Modifier, tags: List<Tag>) {

    Layout(
        modifier = modifier,
        content = {
            tags.map { Chip(text = it.title) }
        }) { measurables, constraints ->
        var rowCount = 1
        var measurableRawWidth = 0
        val placeableToRow = linkedMapOf<Placeable, Int>()

        val layoutWidth = constraints.minWidth.rangeTo(constraints.maxWidth).maxOrNull() ?: constraints.minWidth

        measurables.mapIndexed { index, measurable ->
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
            var drawedRaw = 1
            placeableToRow.entries.forEach { entry ->
                val (placealbe, row) = entry
                if (row == drawedRaw) {
                    placealbe.placeRelative(
                        x = chipX,
                        y = chipY
                    )
                    chipX += placealbe.width + tagPadding.roundToPx()
                } else {
                    chipY = tagHeight.roundToPx() + tagPadding.roundToPx()
                    chipX = 0
                    placealbe.placeRelative(
                        x = chipX,
                        y = chipY
                    )
                }
                drawedRaw = row


            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Composable
@Preview
fun TagsGroupPreview() {
    TagsGroup(
        tags = listOf(
            Tag(title = "perviy", id = 1),
            Tag(title = "vtoroy", id = 2),
            Tag(title = "tretiy", id = 3)
        )
    )
}