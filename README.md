# Shortcut
A simple library to add dynamic and pinned shortcuts

## Requirements

- **minSdk 23** (Android 6.0, required by current AndroidX). Dynamic shortcuts appear on API 25+, pinned shortcuts on API 26+ with a
  legacy-launcher fallback below that — the library handles the version checks internally via
  `ShortcutManagerCompat`, so no `Build.VERSION` checks are needed in your code.
- **AndroidX.** The library depends on `androidx.core`; apps still on the legacy support libraries must
  [migrate to AndroidX](https://developer.android.com/jetpack/androidx/migrate) first.
- The public API is unchanged from 1.x — existing Java and Kotlin integrations compile as-is.

## Building from source

The project builds with the Gradle wrapper (Gradle 9.6, AGP 9.3, built-in Kotlin) and needs JDK 17+
and an Android SDK with platform 36:

```
./gradlew build
```

## Add the dependency

The library is published on **Maven Central** as `io.github.mehdikhalifeh:shortcut-core`
(no extra repository configuration needed):

```kotlin
dependencies {
    implementation("io.github.mehdikhalifeh:shortcut-core:1.1.0")
}
```

> **Migrating from 1.x?** Only the coordinates changed —
> `com.github.MehdiKh93:Shortcut` → `io.github.mehdikhalifeh:shortcut-core`. The API is
> source-compatible, so a coordinate swap is the whole migration.

<details>
<summary><b>Legacy 1.x (JitPack)</b></summary>

Versions up to `1.0.2` were distributed through JitPack and remain available for old
projects:

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    implementation 'com.github.MehdiKh93:Shortcut:1.0.2'
}
```

</details>
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


### disabling a `DynamicShortcut` temporary
```java
shortcutUtils.disableDynamicShortCut(dynamicShortcut);
```

### removing a `DynamicShortcut` temporary
```java
shortcutUtils.removeDynamicShortCut(dynamicShortcut);
```

### enabling a `DynamicShortcut` temporary
```java
shortcutUtils.enableDynamicShortCut(dynamicShortcut);
```




<img src="git_dynamic_shortcut.gif"/>


### initing a `PinnedShortcut`

```java
Shortcut pinnedShortcut = new Shortcut.ShortcutBuilder()
    .setShortcutIcon(R.drawable.icon)
    .setShortcutId("pinnedShortcutId")
    .setShortcutLongLabel("pinnedShortcutLongLabel")
    .setShortcutShortLabel("pinnedShortcutShortLabel")
    .setIntentAction("pinnedShortcutIntentAction")
    .setIntentStringExtraKey("pinnedShortcutKey")
    .setIntentStringExtraValue("pinnedShortcutValue")
    .build();
shortcutUtils.initPinnedShortCut(pinnedShortcut, new IReceiveStringExtra() {
    @Override
    public void onReceiveStringExtra(String stringExtraKey, String stringExtraValue) {
        String intent = getIntent().getStringExtra(stringExtraKey);
            if (intent != null) {
                if (intent.equals("pinnedShortcutValue")) {
                        //write any code here
                }
            }
        }
    });
}
```

### requesting a `PinnedShortcut`
```java
shortcutUtils.requestPinnedShortcut(pinnedShortcut);
```

### disabling a `PinnedShortcut`
```java
shortcutUtils.disablePinnedShortCut(pinnedShortcut);
```

### enabling a `PinnedShortcut`
```java
shortcutUtils.enablePinnedShortCut(pinnedShortcut);
```



<img src="git_pinned_shortcut.gif"/>



## Issues

Please send all issues and feedback to khalifeh.mehdi@gmail.com or Telegram ID: https://t.me/mehdikhalifeh

## License
```
   Copyright 2019 Mehdi Khalifeh

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
