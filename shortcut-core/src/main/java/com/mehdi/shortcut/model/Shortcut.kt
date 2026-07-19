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

/**
 * Immutable description of an app shortcut: identity, labels, icon and the
 * single string extra carried by the launch intent.
 *
 * Create instances with [ShortcutBuilder].
 */
public class Shortcut(
    builder: ShortcutBuilder,
) {
    public val intentStringExtraKey: String? = builder.intentStringExtraKey
    public val intentStringExtraValue: String? = builder.intentStringExtraValue
    public val intentAction: String? = builder.intentAction
    public val shortcutId: String? = builder.shortcutId
    public val shortcutShortLabel: String? = builder.shortcutShortLabel
    public val shortcutLongLabel: String? = builder.shortcutLongLabel
    public val shortcutIcon: Int = builder.shortcutIcon

    /** Fluent builder kept source-compatible with the original Java API. */
    public class ShortcutBuilder {
        internal var intentStringExtraKey: String? = null
            private set
        internal var intentStringExtraValue: String? = null
            private set
        internal var intentAction: String? = null
            private set
        internal var shortcutId: String? = null
            private set
        internal var shortcutShortLabel: String? = null
            private set
        internal var shortcutLongLabel: String? = null
            private set
        internal var shortcutIcon: Int = 0
            private set

        public fun setIntentStringExtraKey(intentStringExtraKey: String?): ShortcutBuilder =
            apply { this.intentStringExtraKey = intentStringExtraKey }

        public fun setIntentStringExtraValue(intentStringExtraValue: String?): ShortcutBuilder =
            apply { this.intentStringExtraValue = intentStringExtraValue }

        public fun setIntentAction(intentAction: String?): ShortcutBuilder = apply { this.intentAction = intentAction }

        public fun setShortcutId(shortcutId: String?): ShortcutBuilder = apply { this.shortcutId = shortcutId }

        public fun setShortcutShortLabel(shortcutShortLabel: String?): ShortcutBuilder =
            apply { this.shortcutShortLabel = shortcutShortLabel }

        public fun setShortcutLongLabel(shortcutLongLabel: String?): ShortcutBuilder =
            apply { this.shortcutLongLabel = shortcutLongLabel }

        public fun setShortcutIcon(shortcutIcon: Int): ShortcutBuilder = apply { this.shortcutIcon = shortcutIcon }

        public fun build(): Shortcut = Shortcut(this)
    }
}
