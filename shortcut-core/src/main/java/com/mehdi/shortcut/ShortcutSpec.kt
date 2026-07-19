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

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.graphics.drawable.IconCompat

/**
 * Describes one shortcut inside a [shortcuts] block.
 *
 * At least one [intent] is required. When several intents are added, they form
 * the activity back stack: the **last** intent is the one launched, and the
 * preceding ones are what the back button walks through.
 */
@ShortcutsDsl
public open class ShortcutSpec internal constructor(
    private val context: Context,
    /** The stable identity of the shortcut. */
    public val id: String,
) {
    private val intents = mutableListOf<Intent>()
    private var iconCompat: IconCompat? = null

    /** Short label shown under the shortcut icon. Required. */
    public var shortLabel: CharSequence? = null

    /** Longer label shown when the launcher has room for it. */
    public var longLabel: CharSequence? = null

    /** Message shown when the user taps this shortcut while it is disabled. */
    public var disabledMessage: CharSequence? = null

    /** Relative ordering among the app's shortcuts; lower ranks appear first. */
    public var rank: Int = 0

    /**
     * Marks the shortcut as long-lived, allowing the system to cache it (for
     * example for sharing targets and conversation notifications).
     */
    public var isLongLived: Boolean = false

    /** Launcher categories, e.g. `ShortcutInfo.SHORTCUT_CATEGORY_CONVERSATION`. */
    public var categories: Set<String>? = null

    /** Icon from a drawable resource. Ignored when [icon] or [adaptiveIcon] is also called. */
    @DrawableRes
    public var icon: Int = 0

    /** Icon from an arbitrary [IconCompat]; wins over the [icon] resource. */
    public fun icon(icon: IconCompat) {
        iconCompat = icon
    }

    /** Adaptive icon from a full-bleed [bitmap]; wins over the [icon] resource. */
    public fun adaptiveIcon(bitmap: Bitmap) {
        iconCompat = IconCompat.createWithAdaptiveBitmap(bitmap)
    }

    /**
     * Adds a launch intent built with the [IntentSpec] DSL. Repeat to build a
     * back stack; the last intent added is the one launched.
     */
    public fun intent(block: IntentSpec.() -> Unit) {
        intents += IntentSpec(context).apply(block).build()
    }

    /** Adds a pre-built launch [intent]. The intent must have an action set. */
    public fun intent(intent: Intent) {
        intents += intent
    }

    internal fun build(): ShortcutInfoCompat {
        require(intents.isNotEmpty()) { "Shortcut '$id' needs at least one intent { } block" }
        val builder = ShortcutInfoCompat.Builder(context, id)
        shortLabel?.let(builder::setShortLabel)
        longLabel?.let(builder::setLongLabel)
        disabledMessage?.let(builder::setDisabledMessage)
        builder.setRank(rank)
        if (isLongLived) builder.setLongLived(true)
        categories?.let(builder::setCategories)
        resolveIcon()?.let(builder::setIcon)
        builder.setIntents(intents.toTypedArray())
        return builder.build()
    }

    private fun resolveIcon(): IconCompat? =
        iconCompat
            ?: icon.takeIf { it != 0 }?.let { IconCompat.createWithResource(context, it) }
}

/**
 * A [ShortcutSpec] for [ShortcutsScope.pinned] requests, adding the pin-result
 * callback.
 */
@ShortcutsDsl
public class PinnedShortcutSpec internal constructor(
    context: Context,
    id: String,
) : ShortcutSpec(context, id) {
    /**
     * Notified once the user confirms pinning. Create it with
     * [PendingIntent.getBroadcast] and [PendingIntent.FLAG_IMMUTABLE]; the
     * system fires its [PendingIntent.getIntentSender] on confirmation.
     */
    public var resultCallback: PendingIntent? = null
}
