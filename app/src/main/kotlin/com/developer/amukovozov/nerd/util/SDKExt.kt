package com.developer.amukovozov.nerd.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun openInChromeTab(context: Context, url: String) {
    val customTab = CustomTabsIntent.Builder().build()
    customTab.launchUrl(context, Uri.parse(url))
}