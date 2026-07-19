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
package com.mehdi.shortcut

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ShortcutsDslTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun dynamicShortcuts(): List<ShortcutInfoCompat> = ShortcutManagerCompat.getDynamicShortcuts(context)

    private fun pinnedIds(): List<String> =
        ShortcutManagerCompat.getShortcuts(context, ShortcutManagerCompat.FLAG_MATCH_PINNED).map { it.id }

    @Test
    fun `dynamic publishes shortcut with labels rank and intent`() {
        context.shortcuts {
            dynamic("compose_email") {
                shortLabel = "Compose"
                longLabel = "Compose a new email"
                icon = android.R.drawable.ic_menu_send
                rank = 1
                intent {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse("myapp://compose")
                    putExtra("source", "shortcut")
                }
            }
        }

        val published = dynamicShortcuts().single()
        assertEquals("compose_email", published.id)
        assertEquals("Compose", published.shortLabel.toString())
        assertEquals("Compose a new email", published.longLabel.toString())
        assertEquals(1, published.rank)
        val launched = published.intent
        assertNotNull(launched)
        assertEquals(Intent.ACTION_VIEW, launched!!.action)
        assertEquals("myapp://compose", launched.data.toString())
        assertEquals("shortcut", launched.getStringExtra("source"))
    }

    @Test
    fun `dynamic with same id updates instead of duplicating`() {
        context.shortcuts {
            dynamic("home") {
                shortLabel = "Home"
                intent { action = "open" }
            }
            dynamic("home") {
                shortLabel = "Home v2"
                intent { action = "open" }
            }
        }

        val published = dynamicShortcuts().single()
        assertEquals("Home v2", published.shortLabel.toString())
    }

    @Test
    fun `multiple intents form a back stack with the last one launched`() {
        context.shortcuts {
            dynamic("deep") {
                shortLabel = "Deep"
                intent { action = "root" }
                intent {
                    action = "leaf"
                    putExtra("depth", 2)
                }
            }
        }

        val intents = dynamicShortcuts().single().intents
        assertEquals(2, intents.size)
        assertEquals("root", intents.first().action)
        assertEquals("leaf", intents.last().action)
        assertEquals(2, intents.last().getIntExtra("depth", -1))
    }

    @Test
    fun `extras of all common types round-trip`() {
        context.shortcuts {
            dynamic("typed") {
                shortLabel = "Typed"
                intent {
                    action = "typed"
                    putExtra("string", "value")
                    putExtra("int", 7)
                    putExtra("long", 7L)
                    putExtra("boolean", true)
                    putExtra("double", 1.5)
                    putExtra("intArray", intArrayOf(1, 2, 3))
                    putExtra("stringArray", arrayOf<String?>("a", "b"))
                }
            }
        }

        val launched = dynamicShortcuts().single().intent!!
        assertEquals("value", launched.getStringExtra("string"))
        assertEquals(7, launched.getIntExtra("int", -1))
        assertEquals(7L, launched.getLongExtra("long", -1))
        assertTrue(launched.getBooleanExtra("boolean", false))
        assertEquals(1.5, launched.getDoubleExtra("double", -1.0), 0.0)
        assertEquals(listOf(1, 2, 3), launched.getIntArrayExtra("intArray")!!.toList())
        assertEquals(listOf("a", "b"), launched.getStringArrayExtra("stringArray")!!.toList())
    }

    @Test
    fun `intent target produces an explicit component`() {
        context.shortcuts {
            dynamic("explicit") {
                shortLabel = "Explicit"
                intent { target<Activity>() }
            }
        }

        val launched = dynamicShortcuts().single().intent!!
        assertEquals(Activity::class.java.name, launched.component!!.className)
        assertEquals(Intent.ACTION_VIEW, launched.action)
    }

    @Test
    fun `adaptive bitmap icon is accepted`() {
        val bitmap = Bitmap.createBitmap(108, 108, Bitmap.Config.ARGB_8888)
        context.shortcuts {
            dynamic("bitmapped") {
                shortLabel = "Bitmapped"
                adaptiveIcon(bitmap)
                intent { action = "open" }
            }
        }

        assertEquals("bitmapped", dynamicShortcuts().single().id)
    }

    @Test
    fun `update changes an existing shortcut but never publishes a new one`() {
        context.shortcuts {
            dynamic("home") {
                shortLabel = "Home"
                intent { action = "open" }
            }
            update("home") {
                shortLabel = "Home updated"
                intent { action = "open" }
            }
            update("ghost") {
                shortLabel = "Ghost"
                intent { action = "open" }
            }
        }

        val ids = dynamicShortcuts().map { it.id }
        assertEquals(listOf("home"), ids)
        assertEquals("Home updated", dynamicShortcuts().single().shortLabel.toString())
    }

    @Test
    fun `remove and removeAll delete dynamic shortcuts`() {
        context.shortcuts {
            dynamic("a") {
                shortLabel = "A"
                intent { action = "open" }
            }
            dynamic("b") {
                shortLabel = "B"
                intent { action = "open" }
            }
            remove("a")
        }
        assertEquals(listOf("b"), dynamicShortcuts().map { it.id })

        context.shortcuts { removeAll() }
        assertTrue(dynamicShortcuts().isEmpty())
    }

    @Test
    fun `pinned pins the shortcut and fires the result callback`() {
        var requested = false
        context.shortcuts {
            requested =
                pinned("call_mom") {
                    shortLabel = "Call mom"
                    intent {
                        action = Intent.ACTION_VIEW
                        putExtra("who", "mom")
                    }
                    resultCallback =
                        PendingIntent.getBroadcast(
                            context,
                            0,
                            Intent("com.mehdi.shortcut.test.PINNED").setPackage(context.packageName),
                            PendingIntent.FLAG_IMMUTABLE,
                        )
                }
        }

        assertTrue(requested)
        assertTrue("call_mom" in pinnedIds())
    }

    @Test
    fun `disable and enable round-trip a pinned shortcut`() {
        context.shortcuts {
            pinned("pin") {
                shortLabel = "Pin"
                intent { action = "open" }
            }
            disable("pin", message = "gone for now")
        }
        assertTrue("pin" in pinnedIds())

        context.shortcuts { enable("pin") }
        assertTrue("pin" in pinnedIds())
    }

    @Test
    fun `enable with unknown id is ignored`() {
        context.shortcuts { enable("nope") }
    }

    @Test
    fun `reportUsed and system state getters are safe`() {
        context.shortcuts {
            dynamic("used") {
                shortLabel = "Used"
                intent { action = "open" }
            }
            reportUsed("used")
            assertTrue(maxShortcutCountPerActivity > 0)
            assertFalse(isRateLimitingActive)
            assertTrue(isPinShortcutSupported)
        }
    }

    @Test
    fun `shortcut without an intent fails fast`() {
        val error =
            assertThrows(IllegalArgumentException::class.java) {
                context.shortcuts {
                    dynamic("broken") { shortLabel = "Broken" }
                }
            }
        assertTrue(error.message!!.contains("broken"))
    }

    @Test
    @Config(sdk = [23])
    fun `on the minimum supported api dynamic shortcuts are a safe no-op`() {
        context.shortcuts {
            dynamic("home") {
                shortLabel = "Home"
                intent { action = "open" }
            }
        }

        assertTrue(dynamicShortcuts().isEmpty())
    }

    @Test
    @Config(sdk = [23])
    fun `on the minimum supported api pinned request does not crash`() {
        context.shortcuts {
            pinned("pin") {
                shortLabel = "Pin"
                intent { action = "open" }
            }
        }
    }

    @Test
    fun `disabled message and categories are applied`() {
        context.shortcuts {
            dynamic("rich") {
                shortLabel = "Rich"
                disabledMessage = "Not now"
                categories = setOf("android.shortcut.conversation")
                isLongLived = true
                intent { action = "open" }
            }
        }

        val published = dynamicShortcuts().single()
        assertEquals("Not now", published.disabledMessage.toString())
        assertNull(published.intent!!.getStringExtra("absent"))
    }
}
