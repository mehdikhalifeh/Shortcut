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
import android.app.PendingIntent
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.mehdi.shortcut.interfaces.IReceiveStringExtra
import com.mehdi.shortcut.model.Shortcut

/**
 * Registers and manages dynamic and pinned app shortcuts.
 *
 * Backed entirely by [ShortcutManagerCompat]: dynamic shortcuts are a no-op
 * below API 25 and pinned shortcuts fall back to the legacy launcher broadcast
 * below API 26, so no [android.os.Build] version checks are required by callers.
 *
 * Shortcut launch intents target the [Activity] passed to the constructor.
 */
public class ShortcutUtils(
    private val activity: Activity,
) {
    private val dynamicShortcuts: MutableList<ShortcutInfoCompat> = mutableListOf()
    private var pinnedShortcutCallbackIntent: Intent? = null

    /** Publishes [shortcut] as a dynamic shortcut and echoes its extra to [iReceiveStringExtra]. */
    public fun addDynamicShortCut(
        shortcut: Shortcut,
        iReceiveStringExtra: IReceiveStringExtra,
    ) {
        val shortcutInfo = shortcut.toShortcutInfo()
        iReceiveStringExtra.onReceiveStringExtra(shortcut.intentStringExtraKey, shortcut.intentStringExtraValue)
        dynamicShortcuts.removeAll { it.id == shortcutInfo.id }
        dynamicShortcuts += shortcutInfo
        ShortcutManagerCompat.setDynamicShortcuts(activity, dynamicShortcuts)
    }

    /** Prepares the result-callback intent for a later [requestPinnedShortcut] call. */
    public fun initPinnedShortCut(
        shortcut: Shortcut,
        iReceiveStringExtra: IReceiveStringExtra,
    ) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(activity)) {
            iReceiveStringExtra.onReceiveStringExtra(shortcut.intentStringExtraKey, shortcut.intentStringExtraValue)
            pinnedShortcutCallbackIntent =
                ShortcutManagerCompat.createShortcutResultIntent(activity, shortcut.toShortcutInfo())
        }
    }

    /** Asks the launcher to pin [shortcut]; shows the system pinning dialog on API 26+. */
    public fun requestPinnedShortcut(shortcut: Shortcut) {
        val shortcutInfo = shortcut.toShortcutInfo()
        val callbackIntent =
            pinnedShortcutCallbackIntent
                ?: ShortcutManagerCompat.createShortcutResultIntent(activity, shortcutInfo)
        val successCallback =
            PendingIntent.getBroadcast(
                activity,
                0,
                callbackIntent,
                PendingIntent.FLAG_IMMUTABLE,
            )
        ShortcutManagerCompat.requestPinShortcut(activity, shortcutInfo, successCallback.intentSender)
    }

    /** Disables the pinned shortcut with the same id as [shortcut]. */
    public fun disablePinnedShortCut(shortcut: Shortcut) {
        ShortcutManagerCompat.disableShortcuts(activity, listOf(shortcut.requiredId()), null)
    }

    /** Re-enables the pinned shortcut with the same id as [shortcut]. */
    public fun enablePinnedShortCut(shortcut: Shortcut) {
        ShortcutManagerCompat.enableShortcuts(activity, listOf(shortcut.toShortcutInfo()))
    }

    /** Removes the dynamic shortcut with the same id as [shortcut]. */
    public fun removeDynamicShortCut(shortcut: Shortcut) {
        val id = shortcut.requiredId()
        dynamicShortcuts.removeAll { it.id == id }
        ShortcutManagerCompat.removeDynamicShortcuts(activity, listOf(id))
    }

    /** Re-enables the dynamic shortcut with the same id as [shortcut]. */
    public fun enableDynamicShortCut(shortcut: Shortcut) {
        ShortcutManagerCompat.enableShortcuts(activity, listOf(shortcut.toShortcutInfo()))
    }

    /** Disables the dynamic shortcut with the same id as [shortcut]. */
    public fun disableDynamicShortCut(shortcut: Shortcut) {
        ShortcutManagerCompat.disableShortcuts(activity, listOf(shortcut.requiredId()), null)
    }

    private fun Shortcut.requiredId(): String = requireNotNull(shortcutId) { "shortcutId is required" }

    private fun Shortcut.toShortcutInfo(): ShortcutInfoCompat {
        val intent =
            Intent(activity.applicationContext, activity.javaClass).apply {
                putExtra(intentStringExtraKey, intentStringExtraValue)
                action = intentAction
            }
        return ShortcutInfoCompat
            .Builder(activity, requiredId())
            .apply {
                shortcutShortLabel?.let(::setShortLabel)
                shortcutLongLabel?.let(::setLongLabel)
                setIcon(IconCompat.createWithResource(activity, shortcutIcon))
                setIntent(intent)
            }.build()
    }
}
