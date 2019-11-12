package com.mehdi.shortcut.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.RequiresApi;


import com.mehdi.shortcut.interfaces.IReceiveStringExtra;
import com.mehdi.shortcut.model.Shortcut;

import java.util.ArrayList;
import java.util.List;

public class ShortcutUtils {
    private Activity context;
    private ShortcutManager shortcutManager;
    private List<ShortcutInfo> dynamicShortcutInfos;
    private List<String> disabledDynamicShortCutIds;
    private List<String> enabledDynamicShortCutIds;
    private List<String> removedDynamicShortCutIds;
    private List<String> removedPinnedShortCutIds;
    private List<String> enabledPinnedShortCutIds;
    private Intent pinnedShortcutCallbackIntent;

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public ShortcutUtils(Activity context) {
        this.context = context;
        shortcutManager = context.getSystemService(ShortcutManager.class);
        dynamicShortcutInfos = new ArrayList<>();
        disabledDynamicShortCutIds = new ArrayList<>();
        enabledDynamicShortCutIds = new ArrayList<>();
        removedDynamicShortCutIds = new ArrayList<>();
        removedPinnedShortCutIds = new ArrayList<>();
        enabledPinnedShortCutIds = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void addDynamicShortCut(Shortcut shortcut, IReceiveStringExtra iReceiveStringExtra) {
        Intent intent = new Intent(context.getApplicationContext(), context.getClass());
        intent.putExtra(shortcut.getIntentStringExtraKey(), shortcut.getIntentStringExtraValue());
        intent.setAction(shortcut.getIntentAction());
        ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, shortcut.getShortcutId())
                .setShortLabel(shortcut.getShortcutShortLabel())
                .setLongLabel(shortcut.getShortcutLongLabel())
                .setIcon(Icon.createWithResource(context, shortcut.getShortcutIcon()))
                .setIntent(intent)
                .build();
        iReceiveStringExtra.onReceiveStringExtra(shortcut.getIntentStringExtraKey(), shortcut.getIntentStringExtraValue());
        dynamicShortcutInfos.add(shortcutInfo);
        if (shortcutManager != null) {
            shortcutManager.setDynamicShortcuts(dynamicShortcutInfos);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initPinnedShortCut(Shortcut shortcut, IReceiveStringExtra iReceiveStringExtra) {
        if (shortcutManager.isRequestPinShortcutSupported()) {
            returnIntent(shortcut);
            iReceiveStringExtra.onReceiveStringExtra(shortcut.getIntentStringExtraKey(), shortcut.getIntentStringExtraValue());
            try {
                pinnedShortcutCallbackIntent =
                        shortcutManager.createShortcutResultIntent(returnShortcutInfo(shortcut));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void requestPinnedShortcut(Shortcut shortcut) {
        returnIntent(shortcut);
        returnShortcutInfo(shortcut);
        PendingIntent successCallback = PendingIntent.getBroadcast(context, 0,
                pinnedShortcutCallbackIntent, 0);
        if (shortcutManager != null) {
            shortcutManager.requestPinShortcut(returnShortcutInfo(shortcut),
                    successCallback.getIntentSender());
        }
    }

    private Intent returnIntent(Shortcut shortcut) {
        Intent intent = new Intent(context.getApplicationContext(), context.getClass());
        intent.putExtra(shortcut.getIntentStringExtraKey(), shortcut.getIntentStringExtraValue());
        intent.setAction(shortcut.getIntentAction());
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private ShortcutInfo returnShortcutInfo(Shortcut shortcut) {
        return new ShortcutInfo.Builder(context, shortcut.getShortcutId())
                .setShortLabel(shortcut.getShortcutShortLabel())
                .setLongLabel(shortcut.getShortcutLongLabel())
                .setIcon(Icon.createWithResource(context, shortcut.getShortcutIcon()))
                .setIntent(returnIntent(shortcut))
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void disablePinnedShortCut(Shortcut shortcut) {
        if (shortcutManager != null) {
            removedPinnedShortCutIds.add(shortcut.getShortcutId());
            shortcutManager.disableShortcuts(removedPinnedShortCutIds);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enablePinnedShortCut(Shortcut shortcut) {
        if (shortcutManager != null) {
            enabledPinnedShortCutIds.add(shortcut.getShortcutId());
            shortcutManager.enableShortcuts(enabledPinnedShortCutIds);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void removeDynamicShortCut(Shortcut shortcut) {
        if (shortcutManager != null) {
            removedDynamicShortCutIds.add(shortcut.getShortcutId());
            shortcutManager.removeDynamicShortcuts(removedDynamicShortCutIds);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void enableDynamicShortCut(Shortcut shortcut) {
        if (shortcutManager != null) {
            enabledDynamicShortCutIds.add(shortcut.getShortcutId());
            shortcutManager.enableShortcuts(enabledDynamicShortCutIds);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void disableDynamicShortCut(Shortcut shortcut) {
        if (shortcutManager != null) {
            disabledDynamicShortCutIds.add(shortcut.getShortcutId());
            shortcutManager.disableShortcuts(disabledDynamicShortCutIds);
        }
    }
}
