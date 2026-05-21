package com.muhdila.accurateuserapp.user.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhdila.accurateuserapp.R
import com.muhdila.accurateuserapp.user.domain.model.User

@Composable
fun AdaptiveUserGrid(
    users: List<User>,
    columnCount: Int,
    newlyCreatedUserId: String? = null,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    LaunchedEffect(users) {
        if (users.isNotEmpty()) {
            gridState.scrollToItem(0)
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(columnCount),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (users.isEmpty()) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_users_found),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(users, key = { it.id }) { user ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + scaleIn(initialScale = 0.92f),
                ) {
                    UserCard(
                        user = user,
                        isHighlighted = user.id == newlyCreatedUserId
                    )
                }
            }
        }
    }
}
