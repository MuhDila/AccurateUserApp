package com.muhdila.accurateuserapp.user.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhdila.accurateuserapp.R

private val WarningColor = Color(0xFFF59E0B)

@Composable
fun OfflineBanner(
    hasPendingSync: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = hasPendingSync,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            color = WarningColor.copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudOff,
                    contentDescription = null,
                    tint = WarningColor,
                    modifier = Modifier.size(18.dp)
                )
                Column {
                    Text(
                        text = stringResource(R.string.pending_sync_title),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = WarningColor
                    )
                    Text(
                        text = stringResource(R.string.pending_sync_subtitle),
                        style = MaterialTheme.typography.labelSmall,
                        color = WarningColor.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
