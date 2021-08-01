package com.developer.amukovozov.nerd.ui.components.searchbar.autocomplete

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developer.amukovozov.nerd.ui.theme.black

private typealias ItemSelected<T> = (T) -> Unit

@Stable
interface AutoCompleteScope<T : AutoCompleteEntity> : AutoCompleteDesignScope {
    var isSearching: Boolean
    fun filter(query: String)
    fun onItemSelected(block: ItemSelected<T> = {})
}

@Stable
interface AutoCompleteDesignScope {
    var boxWidthPercentage: Float
    var shouldWrapContentHeight: Boolean
    var boxMaxHeight: Dp
    var boxBorderStroke: BorderStroke
    var boxShape: Shape
}

data class AutoCompleteState<T : AutoCompleteEntity>(
    var filteredItems: List<T> = emptyList(),
    var query: String = ""
) : AutoCompleteScope<T> {
    private var onItemSelectedBlock: ItemSelected<T>? = null

    var filteredItemsState by mutableStateOf(filteredItems)
    fun selectItem(item: T) {
        onItemSelectedBlock?.invoke(item)
    }

    override var isSearching by mutableStateOf(false)
    override var boxWidthPercentage by mutableStateOf(.9f)
    override var shouldWrapContentHeight by mutableStateOf(false)
    override var boxMaxHeight: Dp by mutableStateOf(TextFieldDefaults.MinHeight * 3)
    override var boxBorderStroke by mutableStateOf(BorderStroke(2.dp, black))
    override var boxShape: Shape by mutableStateOf(RoundedCornerShape(8.dp))

    override fun filter(query: String) {
//        filteredItems = startItems.filter { entity ->
//            entity.filter(query)
//        }
    }

    override fun onItemSelected(block: ItemSelected<T>) {
        onItemSelectedBlock = block
    }
}