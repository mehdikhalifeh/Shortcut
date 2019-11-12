# Shortcut
A simple library to add dynamic and pinned shortcuts

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
ShortcutUtils shortcutUtils = new ShortcutUtils(context);
```

### adding a `DynamicShortcut`

```java
Shortcut dynamicShortcut = new Shortcut.ShortcutBuilder()
    .setShortcutIcon(R.drawable.icon)
    .setShortcutId("dynamicShortcutId")
    .setShortcutLongLabel("dynamicShortcutLongLable")
    .setShortcutShortLabel("dynamicShortcutShortLabel")
    .setIntentAction("dynamicShortcutIntentAction")
    .setIntentStringExtraKey("dynamicShortcutKey")
    .setIntentStringExtraValue("dynamicShortcutValue")
    .build();
shortcutUtils.addDynamicShortCut(dynamicHomeShortcut, new IReceiveStringExtra() {
     @Override
     public void onReceiveStringExtra(String stringExtraKey, String stringExtraValue) {
        String intent = getIntent().getStringExtra(stringExtraKey);
            if (intent != null) {
                if (intent.equals("dynamicShortcutValue")) {
                    //write any code here
                }
            }
        }
    });
}
```


### disabling a `DynamicShortcut`
```java
shortcutUtils.disableDynamicShortCut(dynamicShortcut);
```

### removing a `DynamicShortcut`
```java
shortcutUtils.removeDynamicShortCut(dynamicShortcut);
```

### enabling a `DynamicShortcut`
```java
shortcutUtils.enableDynamicShortCut(dynamicShortcut);
```




<img src="git_dynamic_shortcut.gif"/>






## Issues

Please send all issues and feedback to khalifeh.mehdi@gmail.com or Telegram ID: https://t.me/mehdikhalifeh
