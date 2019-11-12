package com.mehdi.shortcut.model;

public class Shortcut {
    private String intentStringExtraKey, intentStringExtraValue, intentAction, shortcutId, shortcutShortLabel, shortcutLongLabel;
    int shortcutIcon;

    public Shortcut(ShortcutBuilder shortcutBuilder) {
        this.intentStringExtraKey = shortcutBuilder.intentStringExtraKey;
        this.intentStringExtraValue = shortcutBuilder.intentStringExtraValue;
        this.intentAction = shortcutBuilder.intentAction;
        this.shortcutId = shortcutBuilder.shortcutId;
        this.shortcutShortLabel = shortcutBuilder.shortcutShortLabel;
        this.shortcutLongLabel = shortcutBuilder.shortcutLongLabel;
        this.shortcutIcon = shortcutBuilder.shortcutIcon;
    }

    public String getIntentStringExtraKey() {
        return intentStringExtraKey;
    }

    public String getIntentStringExtraValue() {
        return intentStringExtraValue;
    }

    public String getIntentAction() {
        return intentAction;
    }

    public String getShortcutId() {
        return shortcutId;
    }

    public String getShortcutShortLabel() {
        return shortcutShortLabel;
    }

    public String getShortcutLongLabel() {
        return shortcutLongLabel;
    }

    public int getShortcutIcon() {
        return shortcutIcon;
    }

    public static class ShortcutBuilder {
        private String intentStringExtraKey;
        private String intentStringExtraValue;
        private String intentAction;
        private String shortcutId;
        private String shortcutShortLabel;
        private String shortcutLongLabel;
        private int shortcutIcon;

        public ShortcutBuilder setIntentStringExtraKey(String intentStringExtraKey) {
            this.intentStringExtraKey = intentStringExtraKey;
            return this;
        }

        public ShortcutBuilder setIntentStringExtraValue(String intentStringExtraValue) {
            this.intentStringExtraValue = intentStringExtraValue;
            return this;
        }

        public ShortcutBuilder setIntentAction(String intentAction) {
            this.intentAction = intentAction;
            return this;
        }

        public ShortcutBuilder setShortcutId(String shortcutId) {
            this.shortcutId = shortcutId;
            return this;
        }

        public ShortcutBuilder setShortcutShortLabel(String shortcutShortLabel) {
            this.shortcutShortLabel = shortcutShortLabel;
            return this;
        }

        public ShortcutBuilder setShortcutLongLabel(String shortcutLongLabel) {
            this.shortcutLongLabel = shortcutLongLabel;
            return this;
        }

        public ShortcutBuilder setShortcutIcon(int shortcutIcon) {
            this.shortcutIcon = shortcutIcon;
            return this;
        }

        public Shortcut build() {
            return new Shortcut(this);
        }
    }
}
