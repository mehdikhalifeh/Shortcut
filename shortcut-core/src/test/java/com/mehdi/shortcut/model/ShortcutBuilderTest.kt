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
package com.mehdi.shortcut.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ShortcutBuilderTest {
    @Test
    fun `builder copies every field into the model`() {
        val shortcut =
            Shortcut
                .ShortcutBuilder()
                .setShortcutId("id")
                .setShortcutShortLabel("short")
                .setShortcutLongLabel("long")
                .setShortcutIcon(42)
                .setIntentAction("action")
                .setIntentStringExtraKey("key")
                .setIntentStringExtraValue("value")
                .build()

        assertEquals("id", shortcut.shortcutId)
        assertEquals("short", shortcut.shortcutShortLabel)
        assertEquals("long", shortcut.shortcutLongLabel)
        assertEquals(42, shortcut.shortcutIcon)
        assertEquals("action", shortcut.intentAction)
        assertEquals("key", shortcut.intentStringExtraKey)
        assertEquals("value", shortcut.intentStringExtraValue)
    }

    @Test
    fun `unset fields default to null and zero icon`() {
        val shortcut = Shortcut.ShortcutBuilder().build()

        assertNull(shortcut.shortcutId)
        assertNull(shortcut.shortcutShortLabel)
        assertNull(shortcut.shortcutLongLabel)
        assertNull(shortcut.intentAction)
        assertNull(shortcut.intentStringExtraKey)
        assertNull(shortcut.intentStringExtraValue)
        assertEquals(0, shortcut.shortcutIcon)
    }
}
