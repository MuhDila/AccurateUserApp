package com.muhdila.accurateuserapp.user.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhdila.accurateuserapp.R
import com.muhdila.accurateuserapp.core.presentation.asString
import com.muhdila.accurateuserapp.user.presentation.effect.UserEffect
import com.muhdila.accurateuserapp.user.presentation.effect.UserEvent
import com.muhdila.accurateuserapp.user.presentation.state.SortType
import com.muhdila.accurateuserapp.user.presentation.ui.components.*
import com.muhdila.accurateuserapp.user.presentation.viewmodel.UserViewModel
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    windowWidthSizeClass: WindowWidthSizeClass,
    viewModel: UserViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showFilterDialog by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

    val columnCount = when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact  -> 1
        WindowWidthSizeClass.Medium   -> 2
        else                           -> 3
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is UserEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message.asString(context))
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            UserTopBar(
                searchQuery = state.searchQuery,
                onSearchChanged = { viewModel.onEvent(UserEvent.SearchQueryChanged(it)) },
                sortType = state.sortType,
                onSortToggle = {
                    val next = when (state.sortType) {
                        SortType.NONE      -> SortType.NAME_ASC
                        SortType.NAME_ASC  -> SortType.NAME_DESC
                        SortType.NAME_DESC -> SortType.NONE
                    }
                    viewModel.onEvent(UserEvent.SortTypeChanged(next))
                },
                selectedCity = state.selectedCity,
                onFilterClick = { showFilterDialog = true },
                onRefresh = { viewModel.onEvent(UserEvent.Refresh) },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.onEvent(UserEvent.ShowAddDialog) },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text(stringResource(R.string.add_user), fontWeight = FontWeight.SemiBold) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            
            OfflineBanner(
                hasPendingSync = state.hasPendingSync,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (!state.isLoading && state.users.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.users_count_format, state.displayedUsers.size, state.users.size),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (state.selectedCity != null) {
                        FilterChip(
                            selected = true,
                            onClick = { viewModel.onEvent(UserEvent.CityFilterSelected(null)) },
                            label = { Text(state.selectedCity!!) },
                            trailingIcon = {
                                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.clear_filter), modifier = Modifier.size(14.dp))
                            }
                        )
                    }
                }
            }

            PullToRefreshBox(
                isRefreshing = state.isLoading && state.users.isNotEmpty(),
                onRefresh = { viewModel.onEvent(UserEvent.Refresh) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    state.isLoading && state.users.isEmpty() -> {
                        UserGridSkeleton(columnCount = columnCount)
                    }
                    else -> {
                        AdaptiveUserGrid(
                            users = state.displayedUsers,
                            columnCount = columnCount,
                            newlyCreatedUserId = state.newlyCreatedUserId
                        )
                    }
                }
            }
        }
    }

    if (showFilterDialog) {
        UserFilterDialog(
            cities = state.availableCities,
            selectedCity = state.selectedCity,
            onCitySelected = {
                viewModel.onEvent(UserEvent.CityFilterSelected(it))
                showFilterDialog = false
            },
            onDismiss = { showFilterDialog = false }
        )
    }

    if (state.showAddDialog) {
        AddUserDialog(
            formState = state.formState,
            isLoading = state.isAddingUser,
            onEvent = viewModel::onEvent,
            onDismiss = { viewModel.onEvent(UserEvent.DismissAddDialog) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserTopBar(
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    sortType: SortType,
    onSortToggle: () -> Unit,
    selectedCity: String?,
    onFilterClick: () -> Unit,
    onRefresh: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.accurate_users),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            actions = {
                
                IconButton(onClick = onSortToggle) {
                    Icon(
                        imageVector = when (sortType) {
                            SortType.NAME_ASC  -> Icons.Default.ArrowUpward
                            SortType.NAME_DESC -> Icons.Default.ArrowDownward
                            SortType.NONE      -> Icons.Default.SortByAlpha
                        },
                        contentDescription = stringResource(R.string.sort_users),
                        tint = if (sortType != SortType.NONE)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                IconButton(onClick = onFilterClick) {
                    BadgedBox(
                        badge = {
                            if (selectedCity != null) Badge()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(R.string.filter_city),
                            tint = if (selectedCity != null)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                IconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.refresh_users),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            scrollBehavior = scrollBehavior
        )
        
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = {
                Text(
                    stringResource(R.string.search_placeholder),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChanged("") }) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.clear_search))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}
