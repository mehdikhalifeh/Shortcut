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

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.mehdi.shortcut.PinnedShortcutSpec
import com.mehdi.shortcut.shortcuts

/**
 * Requests pinned shortcuts from composition; obtain one with
 * [rememberPinShortcutRequester].
 */
public class PinShortcutRequester internal constructor(
    private val context: Context,
) {
    internal val callbackAction: String = "com.mehdi.shortcut.compose.PIN_RESULT.${context.packageName}"

    /** Whether the current launcher supports pin requests at all. */
    public val isSupported: Boolean
        get() {
            var supported = false
            context.shortcuts { supported = isPinShortcutSupported }
            return supported
        }

    /**
     * Asks the launcher to pin a shortcut described by [block], exactly like
     * [com.mehdi.shortcut.ShortcutsScope.pinned]. Unless [block] sets its own
     * [PinnedShortcutSpec.resultCallback], the requester installs one so the
     * `onResult` callback passed to [rememberPinShortcutRequester] fires when
     * the user confirms.
     *
     * @return `true` when the request was sent to the launcher.
     */
    public fun request(
        id: String,
        block: PinnedShortcutSpec.() -> Unit,
    ): Boolean {
        var requested = false
        context.shortcuts {
            requested =
                pinned(id) {
                    block()
                    if (resultCallback == null) {
                        resultCallback =
                            PendingIntent.getBroadcast(
                                context,
                                id.hashCode(),
                                Intent(callbackAction)
                                    .setPackage(context.packageName)
                                    .putExtra(EXTRA_SHORTCUT_ID, id),
                                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
                            )
                    }
                }
        }
        return requested
    }

    internal companion object {
        internal const val EXTRA_SHORTCUT_ID = "com.mehdi.shortcut.compose.extra.SHORTCUT_ID"
    }
}

/**
 * Remembers a [PinShortcutRequester] whose [onResult] fires with the shortcut
 * id once the user confirms pinning. The result receiver is registered only
 * while this composable is in composition.
 */
@Composable
public fun rememberPinShortcutRequester(onResult: (shortcutId: String) -> Unit = {}): PinShortcutRequester {
    val context = LocalContext.current.applicationContext
    val requester = remember(context) { PinShortcutRequester(context) }
    val latestOnResult = rememberUpdatedState(onResult)

    DisposableEffect(requester) {
        val receiver =
            object : BroadcastReceiver() {
                override fun onReceive(
                    receiverContext: Context,
                    intent: Intent,
                ) {
                    val id = intent.getStringExtra(PinShortcutRequester.EXTRA_SHORTCUT_ID) ?: return
                    latestOnResult.value(id)
                }
            }
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter(requester.callbackAction),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        onDispose { context.unregisterReceiver(receiver) }
    }

    return requester
}
