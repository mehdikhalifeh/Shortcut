package com.mehdi.shortcutdemo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mehdi.shortcut.interfaces.IReceiveStringExtra;
import com.mehdi.shortcut.model.Shortcut;
import com.mehdi.shortcut.model.ShortcutUtils;
import com.mehdi.shortcutdemo.R;

public class MainActivity extends AppCompatActivity implements IReceiveStringExtra, View.OnClickListener {
    private ShortcutUtils shortcutUtils;
    private Button btn_add_pinned_search, btn_add_pinned_favorite, btn_disable_pinned_search, btn_remove_dynamic_home, btn_enable_dynamic_home, btn_disable_dynamic_home, btn_enable_pinned_search;
    Shortcut dynamicHomeShortcut, dynamicSearchShortcut, dynamicFavoriteShortcut, pinnedSearchShortcut, pinnedFavoriteShortcut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setWidgetListeners();
        initShortcuts();
    }

    private void initViews() {
        btn_add_pinned_search = findViewById(R.id.btn_add_pinned_search);
        btn_add_pinned_favorite = findViewById(R.id.btn_add_pinned_favorite);
        btn_disable_pinned_search = findViewById(R.id.btn_disable_pinned_search);
        btn_enable_pinned_search = findViewById(R.id.btn_enable_pinned_search);
        btn_enable_dynamic_home = findViewById(R.id.btn_enable_dynamic_home);
        btn_remove_dynamic_home = findViewById(R.id.btn_remove_dynamic_home);
        btn_disable_dynamic_home = findViewById(R.id.btn_disable_dynamic_home);
    }

    private void setWidgetListeners() {
        btn_add_pinned_search.setOnClickListener(this);
        btn_add_pinned_favorite.setOnClickListener(this);
        btn_disable_pinned_search.setOnClickListener(this);
        btn_enable_pinned_search.setOnClickListener(this);
        btn_enable_dynamic_home.setOnClickListener(this);
        btn_remove_dynamic_home.setOnClickListener(this);
        btn_disable_dynamic_home.setOnClickListener(this);
    }

    private void initShortcuts() {
        shortcutUtils = new ShortcutUtils(this);
        addDynamicHomeShortCut();
        addDynamicFavoriteShortCut();
        addDynamicSearchShortCut();
        initPinnedSearchShortCut();
        initPinnedFavoriteShortCut();
    }

    private void addDynamicHomeShortCut() {
        dynamicHomeShortcut = new Shortcut.ShortcutBuilder()
                .setShortcutIcon(R.drawable.home)
                .setShortcutId("dynamicHome")
                .setShortcutLongLabel("Home")
                .setShortcutShortLabel("Home")
                .setIntentAction("dynamicHome")
                .setIntentStringExtraKey("dynamicHomeKey")
                .setIntentStringExtraValue("dynamicHomeValue")
                .build();
        shortcutUtils.addDynamicShortCut(dynamicHomeShortcut, this);
    }

    private void addDynamicSearchShortCut() {
        dynamicSearchShortcut = new Shortcut.ShortcutBuilder()
                .setShortcutIcon(R.drawable.search)
                .setShortcutId("dynamicSearch")
                .setShortcutLongLabel("Search")
                .setShortcutShortLabel("Search")
                .setIntentAction("dynamicSearch")
                .setIntentStringExtraKey("dynamicSearchKey")
                .setIntentStringExtraValue("dynamicSearchValue")
                .build();
        shortcutUtils.addDynamicShortCut(dynamicSearchShortcut, this);
    }

    private void addDynamicFavoriteShortCut() {
        dynamicFavoriteShortcut = new Shortcut.ShortcutBuilder()
                .setShortcutIcon(R.drawable.favorite)
                .setShortcutId("dynamicFavorite")
                .setShortcutLongLabel("Favorite")
                .setShortcutShortLabel("Favorite")
                .setIntentAction("dynamicFavorite")
                .setIntentStringExtraKey("dynamicFavoriteKey")
                .setIntentStringExtraValue("dynamicFavoriteValue")
                .build();
        shortcutUtils.addDynamicShortCut(dynamicFavoriteShortcut, this);
    }

    private void initPinnedSearchShortCut() {
        pinnedSearchShortcut = new Shortcut.ShortcutBuilder()
                .setShortcutIcon(R.drawable.search)
                .setShortcutId("pinnedSearch")
                .setShortcutLongLabel("pinnedSearch")
                .setShortcutShortLabel("pinnedSearch")
                .setIntentAction("pinnedSearch")
                .setIntentStringExtraKey("pinnedSearchKey")
                .setIntentStringExtraValue("pinnedSearchValue")
                .build();
        shortcutUtils.initPinnedShortCut(pinnedSearchShortcut, this);
    }

    private void initPinnedFavoriteShortCut() {
        pinnedFavoriteShortcut = new Shortcut.ShortcutBuilder()
                .setShortcutIcon(R.drawable.favorite)
                .setShortcutId("pinnedFavorite")
                .setShortcutLongLabel("pinnedFavorite")
                .setShortcutShortLabel("pinnedFavorite")
                .setIntentAction("pinnedFavorite")
                .setIntentStringExtraKey("pinnedFavoriteKey")
                .setIntentStringExtraValue("pinnedFavoriteValue")
                .build();
        shortcutUtils.initPinnedShortCut(pinnedFavoriteShortcut, this);
    }

    @Override
    public void onReceiveStringExtra(String stringExtraKey, String stringExtraValue) {
        String intent = getIntent().getStringExtra(stringExtraKey);
        if (intent != null) {
            if (intent.equals("dynamicFavoriteValue")) {
                Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();
            }
            if (intent.equals("dynamicSearchValue")) {
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
            }
            if (intent.equals("dynamicHomeValue")) {
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
            }
            if (intent.equals("pinnedSearchValue")) {
                finish();
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
            if (intent.equals("pinnedFavoriteValue")) {
                finish();
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
            }
        }
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_pinned_search:
                shortcutUtils.requestPinnedShortcut(pinnedSearchShortcut);
                break;
            case R.id.btn_add_pinned_favorite:
                shortcutUtils.requestPinnedShortcut(pinnedFavoriteShortcut);
                break;
            case R.id.btn_disable_pinned_search:
                shortcutUtils.disablePinnedShortCut(pinnedSearchShortcut);
                showAlertDialog(getResources().getString(R.string.disabled_pinned_search_message));
                break;
            case R.id.btn_enable_pinned_search:
                shortcutUtils.enablePinnedShortCut(pinnedSearchShortcut);
                showAlertDialog(getResources().getString(R.string.enabled_pinned_search_message));
                break;
            case R.id.btn_remove_dynamic_home:
                shortcutUtils.removeDynamicShortCut(dynamicHomeShortcut);
                showAlertDialog(getResources().getString(R.string.removed_dynamic_home_message));
                break;
            case R.id.btn_disable_dynamic_home:
                shortcutUtils.disableDynamicShortCut(dynamicHomeShortcut);
                showAlertDialog(getResources().getString(R.string.disable_dynamic_home_message));
                break;
            case R.id.btn_enable_dynamic_home:
                shortcutUtils.enableDynamicShortCut(dynamicHomeShortcut);
                showAlertDialog(getResources().getString(R.string.enable_dynamic_home_message));
                break;
        }
    }
}
