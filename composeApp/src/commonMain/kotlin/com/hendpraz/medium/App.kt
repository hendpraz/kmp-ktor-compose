package com.hendpraz.medium

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

val client = HttpClient()

@Serializable
data class CatFactResponse(
    val fact: String,
    val length: Int
)

@Composable
@Preview
fun App() {
    MaterialTheme {
        val coroutineScope = rememberCoroutineScope()

        var catFact by remember { mutableStateOf("") }

        suspend fun fetchCatFact() {
            val responseBody = client.get("https://catfact.ninja/fact").bodyAsText()
            val catFactResponse = Json.decodeFromString<CatFactResponse>(responseBody)

            catFact = catFactResponse.fact
        }

        LaunchedEffect(Unit) {
            fetchCatFact()
        }

        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Random Cat Facts",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
            )

            if (catFact.isEmpty()) {
                Text("Loading...")
                return@Column
            }

            Box(
                modifier = Modifier.padding(16.dp)
                    .border(1.dp, MaterialTheme.colors.primary)
                    .sizeIn(maxWidth = 650.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = catFact,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(16.dp),
                )
            }

            Button(onClick = {
                coroutineScope.launch {
                    fetchCatFact()
                }
            }) {
                Text("Refresh!")
            }

        }
    }
}
