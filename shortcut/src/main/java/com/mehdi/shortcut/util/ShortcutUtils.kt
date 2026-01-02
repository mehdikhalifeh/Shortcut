package com.mehdi.shortcut.util

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import com.mehdi.shortcut.interfaces.IReceiveStringExtra
import com.mehdi.shortcut.model.Shortcut

class ShortcutUtils(private val context: Activity) {
    private val shortcutManager: ShortcutManager? =
        context.getSystemService(ShortcutManager::class.java)
    private val dynamicShortcutInfos = mutableListOf<ShortcutInfo>()
    private val disabledDynamicShortCutIds = mutableListOf<String>()
    private val enabledDynamicShortCutIds = mutableListOf<String>()
    private val removedDynamicShortCutIds = mutableListOf<String>()
    private val removedPinnedShortCutIds = mutableListOf<String>()
    private val enabledPinnedShortCutIds = mutableListOf<String>()
    private var pinnedShortcutCallbackIntent: Intent? = null

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun addDynamicShortCut(shortcut: Shortcut, iReceiveStringExtra: IReceiveStringExtra) {
        val intent = Intent(context.applicationContext, context::class.java).apply {
            putExtra(shortcut.intentStringExtraKey, shortcut.intentStringExtraValue)
            action = shortcut.intentAction
        }
        val shortcutInfo = ShortcutInfo.Builder(context, shortcut.shortcutId)
            .setShortLabel(shortcut.shortcutShortLabel)
            .setLongLabel(shortcut.shortcutLongLabel)
            .setIcon(Icon.createWithResource(context, shortcut.shortcutIcon))
            .setIntent(intent)
            .build()
        iReceiveStringExtra.onReceiveStringExtra(
            shortcut.intentStringExtraKey,
            shortcut.intentStringExtraValue
        )
        dynamicShortcutInfos.add(shortcutInfo)
        shortcutManager?.dynamicShortcuts = dynamicShortcutInfos
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initPinnedShortCut(shortcut: Shortcut, iReceiveStringExtra: IReceiveStringExtra) {
        if (shortcutManager?.isRequestPinShortcutSupported != true) {
            return
        }
        iReceiveStringExtra.onReceiveStringExtra(
            shortcut.intentStringExtraKey,
            shortcut.intentStringExtraValue
        )
        pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(
            returnShortcutInfo(shortcut)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun requestPinnedShortcut(shortcut: Shortcut) {
        val intentSender = pinnedShortcutCallbackIntent?.let {
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            PendingIntent.getBroadcast(context, 0, it, flags).intentSender
        }
        shortcutManager?.requestPinShortcut(returnShortcutInfo(shortcut), intentSender)
    }

    private fun returnIntent(shortcut: Shortcut): Intent =
        Intent(context.applicationContext, context::class.java).apply {
            putExtra(shortcut.intentStringExtraKey, shortcut.intentStringExtraValue)
            action = shortcut.intentAction
        }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun returnShortcutInfo(shortcut: Shortcut): ShortcutInfo =
        ShortcutInfo.Builder(context, shortcut.shortcutId)
            .setShortLabel(shortcut.shortcutShortLabel)
            .setLongLabel(shortcut.shortcutLongLabel)
            .setIcon(Icon.createWithResource(context, shortcut.shortcutIcon))
            .setIntent(returnIntent(shortcut))
            .build()

    @RequiresApi(Build.VERSION_CODES.O)
    fun disablePinnedShortCut(shortcut: Shortcut) {
        removedPinnedShortCutIds.add(shortcut.shortcutId)
        shortcutManager?.disableShortcuts(removedPinnedShortCutIds)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun enablePinnedShortCut(shortcut: Shortcut) {
        enabledPinnedShortCutIds.add(shortcut.shortcutId)
        shortcutManager?.enableShortcuts(enabledPinnedShortCutIds)
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun removeDynamicShortCut(shortcut: Shortcut) {
        removedDynamicShortCutIds.add(shortcut.shortcutId)
        shortcutManager?.removeDynamicShortcuts(removedDynamicShortCutIds)
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun enableDynamicShortCut(shortcut: Shortcut) {
        enabledDynamicShortCutIds.add(shortcut.shortcutId)
        shortcutManager?.enableShortcuts(enabledDynamicShortCutIds)
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun disableDynamicShortCut(shortcut: Shortcut) {
        disabledDynamicShortCutIds.add(shortcut.shortcutId)
        shortcutManager?.disableShortcuts(disabledDynamicShortCutIds)
    }
}
