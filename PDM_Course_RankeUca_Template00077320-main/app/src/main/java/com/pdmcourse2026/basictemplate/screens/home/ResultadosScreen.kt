package com.pdmcourse2026.basictemplate.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pdmcourse2026.basictemplate.viewmodel.ResultsState
import com.pdmcourse2026.basictemplate.viewmodel.ResultsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadosScreen(viewModel: ResultsViewModel, onNavigateToHome: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val isRefreshing = state is ResultsState.Loading

    // Forzamos la actualización de datos cada vez que entramos a la pantalla
    LaunchedEffect(Unit) {
        viewModel.fetchResults()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Resultados de Votación") })
        },
        bottomBar = {
            Button(
                onClick = onNavigateToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Volver a votar")
            }
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.fetchResults() },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val currentState = state) {
                is ResultsState.Loading -> {
                    // Mostramos un indicador central solo si no hay datos
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ResultsState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error: ${currentState.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                is ResultsState.Success -> {
                    val places = currentState.places
                    if (places.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay resultados todavía")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(places) { place ->
                                ListItem(
                                    headlineContent = { Text(place.name) },
                                    supportingContent = { Text("Votos: ${place.votes ?: 0}") },
                                    leadingContent = {
                                        AsyncImage(
                                            model = place.imageUrl,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}
