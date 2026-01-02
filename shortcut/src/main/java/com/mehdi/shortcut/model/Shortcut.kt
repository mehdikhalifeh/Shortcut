package com.mehdi.shortcut.model

class Shortcut private constructor(shortcutBuilder: ShortcutBuilder) {
    val intentStringExtraKey: String = shortcutBuilder.intentStringExtraKey
    val intentStringExtraValue: String = shortcutBuilder.intentStringExtraValue
    val intentAction: String = shortcutBuilder.intentAction
    val shortcutId: String = shortcutBuilder.shortcutId
    val shortcutShortLabel: String = shortcutBuilder.shortcutShortLabel
    val shortcutLongLabel: String = shortcutBuilder.shortcutLongLabel
    val shortcutIcon: Int = shortcutBuilder.shortcutIcon

    class ShortcutBuilder {
        lateinit var intentStringExtraKey: String
        lateinit var intentStringExtraValue: String
        lateinit var intentAction: String
        lateinit var shortcutId: String
        lateinit var shortcutShortLabel: String
        lateinit var shortcutLongLabel: String
        var shortcutIcon: Int = 0

        fun setIntentStringExtraKey(intentStringExtraKey: String) = apply {
            this.intentStringExtraKey = intentStringExtraKey
        }

        fun setIntentStringExtraValue(intentStringExtraValue: String) = apply {
            this.intentStringExtraValue = intentStringExtraValue
        }

        fun setIntentAction(intentAction: String) = apply {
            this.intentAction = intentAction
        }

        fun setShortcutId(shortcutId: String) = apply {
            this.shortcutId = shortcutId
        }

        fun setShortcutShortLabel(shortcutShortLabel: String) = apply {
            this.shortcutShortLabel = shortcutShortLabel
        }

        fun setShortcutLongLabel(shortcutLongLabel: String) = apply {
            this.shortcutLongLabel = shortcutLongLabel
        }

        fun setShortcutIcon(shortcutIcon: Int) = apply {
            this.shortcutIcon = shortcutIcon
        }

        fun build(): Shortcut = Shortcut(this)
    }
}
