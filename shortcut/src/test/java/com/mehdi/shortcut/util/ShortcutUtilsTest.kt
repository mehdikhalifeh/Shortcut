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
package com.mehdi.shortcut.util

import android.app.Activity
import androidx.core.content.pm.ShortcutManagerCompat
import com.mehdi.shortcut.model.Shortcut
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ShortcutUtilsTest {
    private lateinit var activity: Activity
    private lateinit var shortcutUtils: ShortcutUtils

    @Before
    fun setUp() {
        activity = Robolectric.buildActivity(Activity::class.java).setup().get()
        shortcutUtils = ShortcutUtils(activity)
    }

    private fun shortcut(id: String): Shortcut =
        Shortcut
            .ShortcutBuilder()
            .setShortcutId(id)
            .setShortcutShortLabel("$id-short")
            .setShortcutLongLabel("$id-long")
            .setShortcutIcon(android.R.drawable.ic_menu_add)
            .setIntentAction("action-$id")
            .setIntentStringExtraKey("$id-key")
            .setIntentStringExtraValue("$id-value")
            .build()

    private fun dynamicIds(): List<String> = ShortcutManagerCompat.getDynamicShortcuts(activity).map { it.id }

    private fun pinnedIds(): List<String> =
        ShortcutManagerCompat.getShortcuts(activity, ShortcutManagerCompat.FLAG_MATCH_PINNED).map { it.id }

    @Test
    fun `addDynamicShortCut publishes the shortcut and echoes the extra`() {
        var receivedKey: String? = null
        var receivedValue: String? = null

        shortcutUtils.addDynamicShortCut(shortcut("home")) { key, value ->
            receivedKey = key
            receivedValue = value
        }

        assertEquals(listOf("home"), dynamicIds())
        assertEquals("home-key", receivedKey)
        assertEquals("home-value", receivedValue)
    }

    @Test
    fun `re-adding a shortcut with the same id does not duplicate it`() {
        shortcutUtils.addDynamicShortCut(shortcut("home")) { _, _ -> }
        shortcutUtils.addDynamicShortCut(shortcut("home")) { _, _ -> }

        assertEquals(listOf("home"), dynamicIds())
    }

    @Test
    fun `removed shortcut stays removed after another add`() {
        shortcutUtils.addDynamicShortCut(shortcut("home")) { _, _ -> }
        shortcutUtils.addDynamicShortCut(shortcut("search")) { _, _ -> }

        shortcutUtils.removeDynamicShortCut(shortcut("home"))
        assertEquals(listOf("search"), dynamicIds())

        shortcutUtils.addDynamicShortCut(shortcut("favorite")) { _, _ -> }
        assertEquals(setOf("search", "favorite"), dynamicIds().toSet())
        assertTrue("home must not be resurrected", "home" !in dynamicIds())
    }

    @Test
    fun `requestPinnedShortcut pins the shortcut`() {
        var callbackInvoked = false
        shortcutUtils.initPinnedShortCut(shortcut("pinned")) { _, _ -> callbackInvoked = true }
        shortcutUtils.requestPinnedShortcut(shortcut("pinned"))

        assertTrue(callbackInvoked)
        assertTrue("pinned" in pinnedIds())
    }

    @Test
    fun `disable and enable pinned shortcut round-trip does not lose the shortcut`() {
        shortcutUtils.initPinnedShortCut(shortcut("pinned")) { _, _ -> }
        shortcutUtils.requestPinnedShortcut(shortcut("pinned"))

        shortcutUtils.disablePinnedShortCut(shortcut("pinned"))
        assertTrue("pinned" in pinnedIds())

        shortcutUtils.enablePinnedShortCut(shortcut("pinned"))
        assertTrue("pinned" in pinnedIds())
    }

    @Test
    fun `disable and enable dynamic shortcut do not crash`() {
        shortcutUtils.addDynamicShortCut(shortcut("home")) { _, _ -> }

        shortcutUtils.disableDynamicShortCut(shortcut("home"))
        shortcutUtils.enableDynamicShortCut(shortcut("home"))
    }

    @Test
    @Config(sdk = [23])
    fun `on the minimum supported api dynamic shortcuts are a safe no-op`() {
        shortcutUtils.addDynamicShortCut(shortcut("home")) { _, _ -> }

        assertTrue(dynamicIds().isEmpty())
    }

    @Test
    @Config(sdk = [23])
    fun `on the minimum supported api pinned shortcut request does not crash`() {
        shortcutUtils.initPinnedShortCut(shortcut("pinned")) { _, _ -> }
        shortcutUtils.requestPinnedShortcut(shortcut("pinned"))
    }
}
