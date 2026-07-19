/*
 * Copyright 2019-2026 Mehdi Khalifeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mehdi.shortcutdemo.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mehdi.shortcut.interfaces.IReceiveStringExtra
import com.mehdi.shortcut.model.Shortcut
import com.mehdi.shortcut.util.ShortcutUtils
import com.mehdi.shortcutdemo.R

class MainActivity :
    AppCompatActivity(),
    IReceiveStringExtra,
    View.OnClickListener {
    private lateinit var shortcutUtils: ShortcutUtils
    private lateinit var dynamicHomeShortcut: Shortcut
    private lateinit var dynamicSearchShortcut: Shortcut
    private lateinit var dynamicFavoriteShortcut: Shortcut
    private lateinit var pinnedSearchShortcut: Shortcut
    private lateinit var pinnedFavoriteShortcut: Shortcut

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setWidgetListeners()
        initShortcuts()
    }

    private fun setWidgetListeners() {
        listOf(
            R.id.btn_add_pinned_search,
            R.id.btn_add_pinned_favorite,
            R.id.btn_disable_pinned_search,
            R.id.btn_enable_pinned_search,
            R.id.btn_enable_dynamic_home,
            R.id.btn_remove_dynamic_home,
            R.id.btn_disable_dynamic_home,
        ).forEach { findViewById<Button>(it).setOnClickListener(this) }
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
        dynamicHomeShortcut =
            Shortcut
                .ShortcutBuilder()
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
        dynamicSearchShortcut =
            Shortcut
                .ShortcutBuilder()
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
        dynamicFavoriteShortcut =
            Shortcut
                .ShortcutBuilder()
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
        pinnedSearchShortcut =
            Shortcut
                .ShortcutBuilder()
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
        pinnedFavoriteShortcut =
            Shortcut
                .ShortcutBuilder()
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

    override fun onReceiveStringExtra(
        stringExtraKey: String?,
        stringExtraValue: String?,
    ) {
        when (intent.getStringExtra(stringExtraKey)) {
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

    private fun showAlertDialog(message: String) {
        AlertDialog
            .Builder(this)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton("Ok") { dialog, _ -> dialog.cancel() }
            .show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_add_pinned_search -> shortcutUtils.requestPinnedShortcut(pinnedSearchShortcut)
            R.id.btn_add_pinned_favorite -> shortcutUtils.requestPinnedShortcut(pinnedFavoriteShortcut)
            R.id.btn_disable_pinned_search -> {
                shortcutUtils.disablePinnedShortCut(pinnedSearchShortcut)
                showAlertDialog(getString(R.string.disabled_pinned_search_message))
            }
            R.id.btn_enable_pinned_search -> {
                shortcutUtils.enablePinnedShortCut(pinnedSearchShortcut)
                showAlertDialog(getString(R.string.enabled_pinned_search_message))
            }
            R.id.btn_remove_dynamic_home -> {
                shortcutUtils.removeDynamicShortCut(dynamicHomeShortcut)
                showAlertDialog(getString(R.string.removed_dynamic_home_message))
            }
            R.id.btn_disable_dynamic_home -> {
                shortcutUtils.disableDynamicShortCut(dynamicHomeShortcut)
                showAlertDialog(getString(R.string.disable_dynamic_home_message))
            }
            R.id.btn_enable_dynamic_home -> {
                shortcutUtils.enableDynamicShortCut(dynamicHomeShortcut)
                showAlertDialog(getString(R.string.enable_dynamic_home_message))
            }
        }
    }
}
