package com.pdmcourse2026.basictemplate.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pdmcourse2026.basictemplate.data.remote.PlaceDto
import com.pdmcourse2026.basictemplate.viewmodel.HomeState
import com.pdmcourse2026.basictemplate.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel,
               onNavigateToResults: () -> Unit ,
               onNavigateToOptions: () -> Unit = {} ) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("RankeUCA - Vota por tu lugar") })
        },
        bottomBar = {
            if (state is HomeState.Success && (state as HomeState.Success).votedPlaceId != null) {
                Button(
                    onClick = onNavigateToResults,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Ir a resultados ->")
                }
            }
        },
        floatingActionButton = {
              FloatingActionButton(
                onClick = { onNavigateToOptions() }
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Administrar opciones")
            } }

    ) { padding ->
        when (val currentState = state) {
            is HomeState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is HomeState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text("Error: ${currentState.message}", color = MaterialTheme.colorScheme.error)
                }
            }
            is HomeState.Success -> {
                LazyColumn(
                    contentPadding = padding,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(currentState.places) { place ->
                        PlaceItem(
                            place = place,
                            isSelected = currentState.votedPlaceId == place.id,
                            onClick = { viewModel.vote(place.id) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceItem(place: PlaceDto, isSelected: Boolean, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = { Text(place.name) },
        leadingContent = {
            AsyncImage(
                model = place.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        },
        trailingContent = {
            RadioButton(selected = isSelected, onClick = null)
        }
    )
}
