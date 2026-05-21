package com.muhdila.accurateuserapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class DynamicString(val value: String): UiText
    data class StringResourceId(@androidx.annotation.StringRes val id: Int, val args: List<Any> = emptyList()): UiText
}

@Composable
fun UiText.asString(): String = when (this) {
    is UiText.DynamicString -> value
    is UiText.StringResourceId -> stringResource(id, *args.toTypedArray())
}

fun UiText.asString(context: android.content.Context): String = when (this) {
    is UiText.DynamicString -> value
    is UiText.StringResourceId -> context.getString(id, *args.toTypedArray())
}
