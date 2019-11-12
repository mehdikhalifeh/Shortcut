# Shortcut
A simple library to add pinned and dynamic shortcuts

## Integrating the shortcut sdk into your android app
### Add jitpack maven repo to app module's `build.gradle`

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
### Add dependency

```gradle
dependencies {
  implementation 'com.github.mehdikhalifeh:Shortcut:1'
}
```

## Usage
### init `ShortcutUtils` class

```java
shortcutUtils = new ShortcutUtils(context);
```

### adding a `DynamicShortCut`

```java
Shortcut dynamicShortcut = new Shortcut.ShortcutBuilder()
    .setShortcutIcon(R.drawable.icon)
    .setShortcutId("dynamicshortcut")
    .setShortcutLongLabel("dynamicshortcut")
    .setShortcutShortLabel("dynamicshortcut")
    .setIntentAction("dynamicshortcut")
    .setIntentStringExtraKey("dynamicshorcutKey")
    .setIntentStringExtraValue("dynamicshorcutValue")
    .build();
shortcutUtils.addDynamicShortCut(dynamicShortcut, this);
```

## Issues

Please send all issues and feedback to khalifeh.mehdi@gmail.com or Telegram ID: https://t.me/mehdikhalifeh
