/*
 * Copyright 2019-2026 Mehdi Khalifeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mehdi.shortcutdemo.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mehdi.shortcut.compose.DynamicShortcutEffect
import com.mehdi.shortcut.compose.rememberPinShortcutRequester
import com.mehdi.shortcutdemo.R

/** Demonstrates the shortcut-compose module on top of shortcut-core. */
class ComposeShortcutsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ComposeShortcutsScreen()
            }
        }
    }
}

@Composable
private fun ComposeShortcutsScreen() {
    val context = LocalContext.current
    var effectEnabled by rememberSaveable { mutableStateOf(false) }
    var visitCount by rememberSaveable { mutableIntStateOf(1) }

    if (effectEnabled) {
        // Published while this screen is in composition, updated when
        // visitCount changes, removed when the switch turns off.
        DynamicShortcutEffect("compose_demo", visitCount) {
            shortLabel = "Visit #$visitCount"
            longLabel = "Compose-driven shortcut (visit #$visitCount)"
            icon = R.drawable.favorite
            intent {
                target<ComposeShortcutsActivity>()
                putExtra("source", "compose_shortcut")
            }
        }
    }

    val pinRequester =
        rememberPinShortcutRequester { pinnedId ->
            Toast.makeText(context, "Pinned: $pinnedId", Toast.LENGTH_SHORT).show()
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("shortcut-compose demo", style = MaterialTheme.typography.titleLarge)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Dynamic shortcut while this screen is open")
            Switch(checked = effectEnabled, onCheckedChange = { effectEnabled = it })
        }

        Button(onClick = { visitCount++ }, enabled = effectEnabled) {
            Text("Bump shortcut label (now #$visitCount)")
        }

        Button(
            onClick = {
                val requested =
                    pinRequester.request("compose_pinned") {
                        shortLabel = "Compose pin"
                        icon = R.drawable.search
                        intent {
                            target<ComposeShortcutsActivity>()
                            putExtra("source", "compose_pinned")
                        }
                    }
                if (!requested) {
                    Toast.makeText(context, "Pinning not supported", Toast.LENGTH_SHORT).show()
                }
            },
        ) {
            Text("Request pinned shortcut")
        }
    }
}
