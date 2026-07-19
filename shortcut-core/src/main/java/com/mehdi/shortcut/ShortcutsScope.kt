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

import android.content.Context
import androidx.core.content.pm.ShortcutManagerCompat

/**
 * Receiver of [shortcuts]; every call maps directly onto
 * [ShortcutManagerCompat].
 */
@ShortcutsDsl
public class ShortcutsScope internal constructor(
    private val context: Context,
) {
    /**
     * Publishes (or updates, when [id] already exists) a dynamic shortcut via
     * [ShortcutManagerCompat.pushDynamicShortcut]. If the dynamic shortcut
     * limit is reached, the lowest-ranked shortcut is evicted automatically.
     *
     * No-op below API 25.
     */
    public fun dynamic(
        id: String,
        block: ShortcutSpec.() -> Unit,
    ) {
        val spec = ShortcutSpec(context, id).apply(block)
        ShortcutManagerCompat.pushDynamicShortcut(context, spec.build())
    }

    /**
     * Asks the launcher to pin a shortcut via
     * [ShortcutManagerCompat.requestPinShortcut]. On API 26+ the system shows
     * a confirmation dialog; below API 26 the legacy launcher broadcast is
     * used.
     *
     * Set [PinnedShortcutSpec.resultCallback] to be notified once the user
     * confirms the pin.
     *
     * @return `true` when the request was sent to the launcher, `false` when
     *   pinning is not supported on this device.
     */
    public fun pinned(
        id: String,
        block: PinnedShortcutSpec.() -> Unit,
    ): Boolean {
        val spec = PinnedShortcutSpec(context, id).apply(block)
        return ShortcutManagerCompat.requestPinShortcut(
            context,
            spec.build(),
            spec.resultCallback?.intentSender,
        )
    }

    /**
     * Updates already-published shortcuts (dynamic or pinned) with the same
     * [id] via [ShortcutManagerCompat.updateShortcuts]. Unlike [dynamic], this
     * never publishes a new shortcut.
     */
    public fun update(
        id: String,
        block: ShortcutSpec.() -> Unit,
    ) {
        val spec = ShortcutSpec(context, id).apply(block)
        ShortcutManagerCompat.updateShortcuts(context, listOf(spec.build()))
    }

    /** Removes the dynamic shortcuts with the given [ids]. */
    public fun remove(vararg ids: String) {
        ShortcutManagerCompat.removeDynamicShortcuts(context, ids.toList())
    }

    /** Removes all dynamic shortcuts published by the app. */
    public fun removeAll() {
        ShortcutManagerCompat.removeAllDynamicShortcuts(context)
    }

    /**
     * Disables the shortcuts with the given [ids]. A pinned shortcut stays
     * visible but greyed out; launching it shows [message] when provided.
     */
    public fun disable(
        vararg ids: String,
        message: CharSequence? = null,
    ) {
        ShortcutManagerCompat.disableShortcuts(context, ids.toList(), message)
    }

    /**
     * Re-enables previously [disable]d shortcuts. Ids that are not currently
     * published are ignored.
     */
    public fun enable(vararg ids: String) {
        val known =
            ShortcutManagerCompat
                .getShortcuts(
                    context,
                    ShortcutManagerCompat.FLAG_MATCH_DYNAMIC or
                        ShortcutManagerCompat.FLAG_MATCH_PINNED or
                        ShortcutManagerCompat.FLAG_MATCH_MANIFEST,
                ).filter { it.id in ids }
        if (known.isNotEmpty()) {
            ShortcutManagerCompat.enableShortcuts(context, known)
        }
    }

    /**
     * Reports that the shortcut with [id] was used, feeding the launcher's
     * prediction ranking. Call this when the user performs the action from
     * inside the app as well.
     */
    public fun reportUsed(id: String) {
        ShortcutManagerCompat.reportShortcutUsed(context, id)
    }

    /** Maximum number of dynamic + manifest shortcuts per launcher activity. */
    public val maxShortcutCountPerActivity: Int
        get() = ShortcutManagerCompat.getMaxShortcutCountPerActivity(context)

    /** Whether shortcut mutations are currently rate limited by the system. */
    public val isRateLimitingActive: Boolean
        get() = ShortcutManagerCompat.isRateLimitingActive(context)

    /** Whether the default launcher supports [pinned] shortcut requests. */
    public val isPinShortcutSupported: Boolean
        get() = ShortcutManagerCompat.isRequestPinShortcutSupported(context)
}
