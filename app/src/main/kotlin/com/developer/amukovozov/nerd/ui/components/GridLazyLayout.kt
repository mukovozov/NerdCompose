package com.developer.amukovozov.nerd.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.developer.amukovozov.nerd.ui.grid.IntervalList

sealed class GridType {
    class Fixed(val columns: Int) : GridType()
    class Adaptive(val minSize: Dp) : GridType()
}

@Composable
fun GridLazyLayout(
    gridType: GridType,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    content: LazyGridScope.() -> Unit
) {
    val scope = LazyGridScopeImpl()
    scope.apply(content)

    when (gridType) {
        is GridType.Adaptive -> {
            BoxWithConstraints(
                modifier = modifier
            ) {
                val columns = maxOf((maxWidth / gridType.minSize).toInt(), 1)
                LazyGrid(
                    columns = columns,
                    state = state,
                    contentPadding = contentPadding,
                    scope = scope
                )
            }
        }
        is GridType.Fixed -> {
            LazyGrid(
                columns = gridType.columns,
                state = state,
                contentPadding = contentPadding,
                scope = scope
            )
        }
    }
}

@Composable
private fun LazyGrid(
    columns: Int,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    scope: LazyGridScopeImpl
) {
    val rows = (scope.totalSize + columns - 1) / columns
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding
    ) {
        items(rows) { rowIndex ->
            Row {
                for (columnIndex in 0 until columns) {
                    val itemIndex = rowIndex * columns + columnIndex
                    if (itemIndex < scope.totalSize) {
                        Box(
                            modifier = Modifier.weight(1f, fill = true),
                            propagateMinConstraints = true
                        ) {
                            scope.contentFor(itemIndex, this@items).invoke()
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f, fill = true))
                    }
                }

            }
        }
    }
}

interface LazyGridScope {
    val totalSize: Int

    fun item(content: @Composable LazyItemScope.() -> Unit)

    fun items(count: Int, itemContent: @Composable LazyItemScope.(index: Int) -> Unit)
}

class LazyGridScopeImpl : LazyGridScope {
    private val intervals = IntervalList<LazyItemScope.(Int) -> (@Composable () -> Unit)>()

    override val totalSize get() = intervals.totalSize

    fun contentFor(index: Int, scope: LazyItemScope): @Composable () -> Unit {
        val interval = intervals.intervalForIndex(index)
        val localIntervalIndex = index - interval.startIndex

        return interval.content(scope, localIntervalIndex)
    }

    override fun item(content: @Composable LazyItemScope.() -> Unit) {
        intervals.add(1) { @Composable { content() } }
    }

    override fun items(count: Int, itemContent: @Composable LazyItemScope.(index: Int) -> Unit) {
        intervals.add(count) {
            @Composable { itemContent(it) }
        }
    }
}
