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
package com.mehdi.shortcut.compose

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class DynamicShortcutEffectTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun dynamicIds(): List<String> = ShortcutManagerCompat.getDynamicShortcuts(context).map { it.id }

    @Test
    fun `publishes on composition and removes on dispose`() {
        var show by mutableStateOf(true)
        composeRule.setContent {
            if (show) {
                DynamicShortcutEffect("fx") {
                    shortLabel = "FX"
                    intent { action = "open" }
                }
            }
        }
        composeRule.waitForIdle()
        assertEquals(listOf("fx"), dynamicIds())

        show = false
        composeRule.waitForIdle()
        assertTrue(dynamicIds().isEmpty())
    }

    @Test
    fun `re-publishes when a key changes`() {
        var count by mutableIntStateOf(1)
        composeRule.setContent {
            DynamicShortcutEffect("counter", count) {
                shortLabel = "Count $count"
                intent { action = "open" }
            }
        }
        composeRule.waitForIdle()
        assertEquals(
            "Count 1",
            ShortcutManagerCompat
                .getDynamicShortcuts(context)
                .single()
                .shortLabel
                .toString(),
        )

        count = 2
        composeRule.waitForIdle()
        assertEquals(
            "Count 2",
            ShortcutManagerCompat
                .getDynamicShortcuts(context)
                .single()
                .shortLabel
                .toString(),
        )
    }

    @Test
    fun `removeOnDispose false keeps the shortcut after leaving composition`() {
        var show by mutableStateOf(true)
        composeRule.setContent {
            if (show) {
                DynamicShortcutEffect("keeper", removeOnDispose = false) {
                    shortLabel = "Keeper"
                    intent { action = "open" }
                }
            }
        }
        composeRule.waitForIdle()
        show = false
        composeRule.waitForIdle()

        assertEquals(listOf("keeper"), dynamicIds())
    }
}
