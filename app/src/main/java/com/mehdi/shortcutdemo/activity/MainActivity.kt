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

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mehdi.shortcut.shortcuts
import com.mehdi.shortcutdemo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleShortcutLaunch()
        setWidgetListeners()
        publishDynamicShortcuts()
    }

    /**
     * The 2.0 pattern for reading shortcut extras: plain intent extras in the
     * target activity — no callback interface involved.
     */
    private fun handleShortcutLaunch() {
        when (intent.getStringExtra(EXTRA_DESTINATION)) {
            DEST_HOME -> {
                shortcuts { reportUsed(ID_DYNAMIC_HOME) }
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show()
            }
            DEST_SEARCH -> {
                finish()
                startActivity(Intent(this, SearchActivity::class.java))
            }
            DEST_FAVORITE -> {
                finish()
                startActivity(Intent(this, FavoriteActivity::class.java))
            }
        }
    }

    private fun publishDynamicShortcuts() {
        shortcuts {
            dynamic(ID_DYNAMIC_HOME) {
                shortLabel = "Home"
                longLabel = "Open the home screen"
                icon = R.drawable.home
                rank = 0
                intent {
                    action = ACTION_OPEN
                    target<MainActivity>()
                    putExtra(EXTRA_DESTINATION, DEST_HOME)
                }
            }
            dynamic(ID_DYNAMIC_SEARCH) {
                shortLabel = "Search"
                longLabel = "Open search"
                icon = R.drawable.search
                rank = 1
                intent {
                    action = ACTION_OPEN
                    target<MainActivity>()
                    putExtra(EXTRA_DESTINATION, DEST_SEARCH)
                }
            }
            dynamic(ID_DYNAMIC_FAVORITE) {
                shortLabel = "Favorite"
                longLabel = "Open favorites"
                icon = R.drawable.favorite
                rank = 2
                intent {
                    action = ACTION_OPEN
                    target<MainActivity>()
                    putExtra(EXTRA_DESTINATION, DEST_FAVORITE)
                }
            }
        }
    }

    private fun requestPin(
        id: String,
        label: String,
        iconRes: Int,
        destination: String,
    ) {
        shortcuts {
            val requested =
                pinned(id) {
                    shortLabel = label
                    longLabel = label
                    icon = iconRes
                    intent {
                        action = ACTION_OPEN
                        target<MainActivity>()
                        putExtra(EXTRA_DESTINATION, destination)
                    }
                    resultCallback =
                        PendingIntent.getBroadcast(
                            this@MainActivity,
                            0,
                            Intent(ACTION_PIN_CONFIRMED).setPackage(packageName),
                            PendingIntent.FLAG_IMMUTABLE,
                        )
                }
            if (!requested) {
                Toast.makeText(this@MainActivity, "Pinning not supported by this launcher", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setWidgetListeners() {
        findViewById<Button>(R.id.btn_add_pinned_search).setOnClickListener {
            requestPin(ID_PINNED_SEARCH, "pinnedSearch", R.drawable.search, DEST_SEARCH)
        }
        findViewById<Button>(R.id.btn_add_pinned_favorite).setOnClickListener {
            requestPin(ID_PINNED_FAVORITE, "pinnedFavorite", R.drawable.favorite, DEST_FAVORITE)
        }
        findViewById<Button>(R.id.btn_disable_pinned_search).setOnClickListener {
            shortcuts { disable(ID_PINNED_SEARCH, message = "Search is temporarily disabled") }
            showAlertDialog(getString(R.string.disabled_pinned_search_message))
        }
        findViewById<Button>(R.id.btn_enable_pinned_search).setOnClickListener {
            shortcuts { enable(ID_PINNED_SEARCH) }
            showAlertDialog(getString(R.string.enabled_pinned_search_message))
        }
        findViewById<Button>(R.id.btn_remove_dynamic_home).setOnClickListener {
            shortcuts { remove(ID_DYNAMIC_HOME) }
            showAlertDialog(getString(R.string.removed_dynamic_home_message))
        }
        findViewById<Button>(R.id.btn_disable_dynamic_home).setOnClickListener {
            shortcuts { disable(ID_DYNAMIC_HOME) }
            showAlertDialog(getString(R.string.disable_dynamic_home_message))
        }
        findViewById<Button>(R.id.btn_enable_dynamic_home).setOnClickListener {
            shortcuts { enable(ID_DYNAMIC_HOME) }
            showAlertDialog(getString(R.string.enable_dynamic_home_message))
        }
        findViewById<Button>(R.id.btn_open_compose_demo).setOnClickListener {
            startActivity(Intent(this, ComposeShortcutsActivity::class.java))
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

    private companion object {
        const val ACTION_OPEN = "com.mehdi.shortcutdemo.OPEN"
        const val ACTION_PIN_CONFIRMED = "com.mehdi.shortcutdemo.PIN_CONFIRMED"
        const val EXTRA_DESTINATION = "destination"
        const val DEST_HOME = "home"
        const val DEST_SEARCH = "search"
        const val DEST_FAVORITE = "favorite"
        const val ID_DYNAMIC_HOME = "dynamicHome"
        const val ID_DYNAMIC_SEARCH = "dynamicSearch"
        const val ID_DYNAMIC_FAVORITE = "dynamicFavorite"
        const val ID_PINNED_SEARCH = "pinnedSearch"
        const val ID_PINNED_FAVORITE = "pinnedFavorite"
    }
}
