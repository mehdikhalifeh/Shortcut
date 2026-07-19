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
class PinShortcutRequesterTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun pinnedIds(): List<String> =
        ShortcutManagerCompat.getShortcuts(context, ShortcutManagerCompat.FLAG_MATCH_PINNED).map { it.id }

    @Test
    fun `request pins the shortcut and fires onResult with its id`() {
        var resultId: String? = null
        var requester: PinShortcutRequester? = null
        composeRule.setContent {
            requester = rememberPinShortcutRequester { resultId = it }
        }
        composeRule.waitForIdle()

        val requested =
            requester!!.request("pin_me") {
                shortLabel = "Pin me"
                intent { action = "open" }
            }
        composeRule.waitForIdle()

        assertTrue(requested)
        assertTrue("pin_me" in pinnedIds())
        assertEquals("pin_me", resultId)
    }

    @Test
    fun `isSupported reflects the launcher capability`() {
        var requester: PinShortcutRequester? = null
        composeRule.setContent {
            requester = rememberPinShortcutRequester()
        }
        composeRule.waitForIdle()

        assertTrue(requester!!.isSupported)
    }
}
