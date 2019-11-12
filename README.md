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

##  maven

```gradle
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

```


## Add dependency
### gradle

```gradle
dependencies {
  implementation 'com.github.mehdikhalifeh:Shortcut:1.0.1'
}
```

### maven
```gradle
<dependency>
	    <groupId>com.github.mehdikhalifeh</groupId>
	    <artifactId>Shortcut</artifactId>
	    <version>1.0.1</version>
</dependency>
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
