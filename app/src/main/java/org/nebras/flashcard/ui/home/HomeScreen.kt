package org.nebras.flashcard.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.nebras.flashcard.data.Deck

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onDeckSelected: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Flashcard") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.decks) { deck ->
                DeckCard(deck = deck, onClick = { onDeckSelected(deck.title) })
            }
        }
    }
}

@Composable
private fun DeckCard(deck: Deck, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = deck.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${deck.cards.size} cards",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
