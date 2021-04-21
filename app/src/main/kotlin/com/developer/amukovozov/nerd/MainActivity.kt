package com.developer.amukovozov.nerd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.developer.amukovozov.nerd.ui.screens.Home
import ru.developer.amukovozov.nerd.ui.theme.NerdTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Home() }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NerdTheme {
        Home()
    }
}