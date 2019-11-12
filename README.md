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
    .setShortcutId("dynamicshortcutId")
    .setShortcutLongLabel("dynamicshortcutLongLable")
    .setShortcutShortLabel("dynamicshortcutShortLabel")
    .setIntentAction("dynamicshortcutIntentAction")
    .setIntentStringExtraKey("dynamicshortcutKey")
    .setIntentStringExtraValue("dynamicshortcutValue")
    .build();
shortcutUtils.addDynamicShortCut(dynamicHomeShortcut, new IReceiveStringExtra() {
     @Override
     public void onReceiveStringExtra(String stringExtraKey, String stringExtraValue) {
        String intent = getIntent().getStringExtra(stringExtraKey);
            if (intent != null) {
                if (intent.equals("dynamicshortcutValue")) {
                    //write any code here
                }
            }
        }
    });
}
```

## Issues

Please send all issues and feedback to khalifeh.mehdi@gmail.com or Telegram ID: https://t.me/mehdikhalifeh
