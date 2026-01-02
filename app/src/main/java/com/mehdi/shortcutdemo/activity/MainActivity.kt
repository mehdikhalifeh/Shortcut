package com.mehdi.shortcutdemo.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mehdi.shortcut.interfaces.IReceiveStringExtra
import com.mehdi.shortcut.model.Shortcut
import com.mehdi.shortcut.util.ShortcutUtils
import com.mehdi.shortcutdemo.R

class MainActivity : ComponentActivity(), IReceiveStringExtra {
    private lateinit var shortcutUtils: ShortcutUtils
    private lateinit var dynamicHomeShortcut: Shortcut
    private lateinit var dynamicSearchShortcut: Shortcut
    private lateinit var dynamicFavoriteShortcut: Shortcut
    private lateinit var pinnedSearchShortcut: Shortcut
    private lateinit var pinnedFavoriteShortcut: Shortcut

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initShortcuts()
        setContent {
            MaterialTheme {
                MainScreen(
                    onAddPinnedSearch = { shortcutUtils.requestPinnedShortcut(pinnedSearchShortcut) },
                    onAddPinnedFavorite = { shortcutUtils.requestPinnedShortcut(pinnedFavoriteShortcut) },
                    onDisablePinnedSearch = {
                        shortcutUtils.disablePinnedShortCut(pinnedSearchShortcut)
                        showAlertDialog(getString(R.string.disabled_pinned_search_message))
                    },
                    onEnablePinnedSearch = {
                        shortcutUtils.enablePinnedShortCut(pinnedSearchShortcut)
                        showAlertDialog(getString(R.string.enabled_pinned_search_message))
                    },
                    onRemoveDynamicHome = {
                        shortcutUtils.removeDynamicShortCut(dynamicHomeShortcut)
                        showAlertDialog(getString(R.string.removed_dynamic_home_message))
                    },
                    onDisableDynamicHome = {
                        shortcutUtils.disableDynamicShortCut(dynamicHomeShortcut)
                        showAlertDialog(getString(R.string.disable_dynamic_home_message))
                    },
                    onEnableDynamicHome = {
                        shortcutUtils.enableDynamicShortCut(dynamicHomeShortcut)
                        showAlertDialog(getString(R.string.enable_dynamic_home_message))
                    }
                )
            }
        }
    }

    private fun initShortcuts() {
        shortcutUtils = ShortcutUtils(this)
        addDynamicHomeShortCut()
        addDynamicFavoriteShortCut()
        addDynamicSearchShortCut()
        initPinnedSearchShortCut()
        initPinnedFavoriteShortCut()
    }

    private fun addDynamicHomeShortCut() {
        dynamicHomeShortcut = Shortcut.ShortcutBuilder()
            .setShortcutIcon(R.drawable.home)
            .setShortcutId("dynamicHome")
            .setShortcutLongLabel("Home")
            .setShortcutShortLabel("Home")
            .setIntentAction("dynamicHome")
            .setIntentStringExtraKey("dynamicHomeKey")
            .setIntentStringExtraValue("dynamicHomeValue")
            .build()
        shortcutUtils.addDynamicShortCut(dynamicHomeShortcut, this)
    }

    private fun addDynamicSearchShortCut() {
        dynamicSearchShortcut = Shortcut.ShortcutBuilder()
            .setShortcutIcon(R.drawable.search)
            .setShortcutId("dynamicSearch")
            .setShortcutLongLabel("Search")
            .setShortcutShortLabel("Search")
            .setIntentAction("dynamicSearch")
            .setIntentStringExtraKey("dynamicSearchKey")
            .setIntentStringExtraValue("dynamicSearchValue")
            .build()
        shortcutUtils.addDynamicShortCut(dynamicSearchShortcut, this)
    }

    private fun addDynamicFavoriteShortCut() {
        dynamicFavoriteShortcut = Shortcut.ShortcutBuilder()
            .setShortcutIcon(R.drawable.favorite)
            .setShortcutId("dynamicFavorite")
            .setShortcutLongLabel("Favorite")
            .setShortcutShortLabel("Favorite")
            .setIntentAction("dynamicFavorite")
            .setIntentStringExtraKey("dynamicFavoriteKey")
            .setIntentStringExtraValue("dynamicFavoriteValue")
            .build()
        shortcutUtils.addDynamicShortCut(dynamicFavoriteShortcut, this)
    }

    private fun initPinnedSearchShortCut() {
        pinnedSearchShortcut = Shortcut.ShortcutBuilder()
            .setShortcutIcon(R.drawable.search)
            .setShortcutId("pinnedSearch")
            .setShortcutLongLabel("pinnedSearch")
            .setShortcutShortLabel("pinnedSearch")
            .setIntentAction("pinnedSearch")
            .setIntentStringExtraKey("pinnedSearchKey")
            .setIntentStringExtraValue("pinnedSearchValue")
            .build()
        shortcutUtils.initPinnedShortCut(pinnedSearchShortcut, this)
    }

    private fun initPinnedFavoriteShortCut() {
        pinnedFavoriteShortcut = Shortcut.ShortcutBuilder()
            .setShortcutIcon(R.drawable.favorite)
            .setShortcutId("pinnedFavorite")
            .setShortcutLongLabel("pinnedFavorite")
            .setShortcutShortLabel("pinnedFavorite")
            .setIntentAction("pinnedFavorite")
            .setIntentStringExtraKey("pinnedFavoriteKey")
            .setIntentStringExtraValue("pinnedFavoriteValue")
            .build()
        shortcutUtils.initPinnedShortCut(pinnedFavoriteShortcut, this)
    }

    override fun onReceiveStringExtra(stringExtraKey: String, stringExtraValue: String) {
        intent.getStringExtra(stringExtraKey)?.let { payload ->
            when (payload) {
                "dynamicFavoriteValue" -> Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show()
                "dynamicSearchValue" -> Toast.makeText(this, "search", Toast.LENGTH_SHORT).show()
                "dynamicHomeValue" -> Toast.makeText(this, "home", Toast.LENGTH_SHORT).show()
                "pinnedSearchValue" -> {
                    finish()
                    startActivity(Intent(this, SearchActivity::class.java))
                }
                "pinnedFavoriteValue" -> {
                    finish()
                    startActivity(Intent(this, FavoriteActivity::class.java))
                }
            }
        }
    }

    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton("Ok") { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }
}

@Composable
private fun MainScreen(
    onAddPinnedSearch: () -> Unit,
    onAddPinnedFavorite: () -> Unit,
    onDisablePinnedSearch: () -> Unit,
    onEnablePinnedSearch: () -> Unit,
    onRemoveDynamicHome: () -> Unit,
    onDisableDynamicHome: () -> Unit,
    onEnableDynamicHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActionButton(text = "add pinned search shortcut", onClick = onAddPinnedSearch)
        ActionButton(text = "add pinned favorite shortcut", onClick = onAddPinnedFavorite)
        ActionButton(text = "disable pinned search shortcut", onClick = onDisablePinnedSearch)
        ActionButton(text = "enable pinned search shortcut", onClick = onEnablePinnedSearch)
        ActionButton(text = "remove dynamic home shortcut", onClick = onRemoveDynamicHome)
        ActionButton(text = "disable dynamic home shortcut", onClick = onDisableDynamicHome)
        ActionButton(text = "enable dynamic home shortcut", onClick = onEnableDynamicHome)
    }
}

@Composable
private fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
