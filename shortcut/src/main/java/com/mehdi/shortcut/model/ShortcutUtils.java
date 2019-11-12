package com.mehdi.shortcut.model;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.RequiresApi;


import com.mehdi.shortcut.interfaces.IReceiveStringExtra;

import java.util.ArrayList;
import java.util.List;

public class ShortcutUtils {
    private Activity context;
    private ShortcutManager shortcutManager;
    private List<ShortcutInfo> dynamicShortcutInfos;
    private List<String> disabledDynamicShortCutIds;
    private List<String> enabledDynamicShortCutIds;
    private ArrayList<String> removedDynamicShortCutIds;
    private List<String> removedPinnedShortCutIds;
    private List<String> enabledPinnedShortCutIds;
    private Intent pinnedShortcutCallbackIntent;

    public ShortcutUtils(Activity context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager = context.getSystemService(ShortcutManager.class);
        }
        dynamicShortcutInfos = new ArrayList<>();
        disabledDynamicShortCutIds = new ArrayList<>();
        enabledDynamicShortCutIds = new ArrayList<>();
        removedDynamicShortCutIds = new ArrayList<>();
        removedPinnedShortCutIds = new ArrayList<>();
        enabledPinnedShortCutIds = new ArrayList<>();
    }

    public void addDynamicShortCut(Shortcut shortcut, IReceiveStringExtra iReceiveStringExtra) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
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
    }

    public void initPinnedShortCut(Shortcut shortcut, IReceiveStringExtra iReceiveStringExtra) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
    }


    public void requestPinnedShortcut(Shortcut shortcut) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            returnIntent(shortcut);
            returnShortcutInfo(shortcut);
            PendingIntent successCallback = PendingIntent.getBroadcast(context, 0,
                    pinnedShortcutCallbackIntent, 0);
            if (shortcutManager != null) {
                shortcutManager.requestPinShortcut(returnShortcutInfo(shortcut),
                        successCallback.getIntentSender());
            }
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

    public void disablePinnedShortCut(Shortcut shortcut) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (shortcutManager != null) {
                removedPinnedShortCutIds.add(shortcut.getShortcutId());
                shortcutManager.disableShortcuts(removedPinnedShortCutIds);
            }
        }
    }

    public void enablePinnedShortCut(Shortcut shortcut) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (shortcutManager != null) {
                enabledPinnedShortCutIds.add(shortcut.getShortcutId());
                shortcutManager.enableShortcuts(enabledPinnedShortCutIds);
            }
        }
    }

    public void removeDynamicShortCut(Shortcut shortcut) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            if (shortcutManager != null) {
                removedDynamicShortCutIds.add(shortcut.getShortcutId());
                shortcutManager.removeDynamicShortcuts(removedDynamicShortCutIds);
            }
        }
    }

    public void enableDynamicShortCut(Shortcut shortcut) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            if (shortcutManager != null) {
                enabledDynamicShortCutIds.add(shortcut.getShortcutId());
                shortcutManager.enableShortcuts(enabledDynamicShortCutIds);
            }
        }
    }

    public void disableDynamicShortCut(Shortcut shortcut) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            if (shortcutManager != null) {
                disabledDynamicShortCutIds.add(shortcut.getShortcutId());
                shortcutManager.disableShortcuts(disabledDynamicShortCutIds);
            }
        }
    }
}
