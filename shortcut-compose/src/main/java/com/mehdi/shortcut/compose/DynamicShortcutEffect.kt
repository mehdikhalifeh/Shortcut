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
package com.mehdi.shortcut.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.mehdi.shortcut.ShortcutSpec
import com.mehdi.shortcut.shortcuts

/**
 * Publishes a dynamic shortcut while this composable is in composition.
 *
 * The shortcut is published (or updated in place) when the effect enters
 * composition and whenever [id] or any of [keys] changes — pass as [keys]
 * whatever state the [builder] reads, mirroring the
 * [androidx.compose.runtime.LaunchedEffect] idiom. When the effect leaves
 * composition the shortcut is removed if [removeOnDispose] is `true` (the
 * default); pass `false` for shortcuts that should outlive the screen.
 *
 * ```kotlin
 * DynamicShortcutEffect("compose_email", draftCount) {
 *     shortLabel = "Compose ($draftCount drafts)"
 *     icon = R.drawable.ic_compose
 *     intent { target<ComposeActivity>() }
 * }
 * ```
 */
@Composable
public fun DynamicShortcutEffect(
    id: String,
    vararg keys: Any?,
    removeOnDispose: Boolean = true,
    builder: ShortcutSpec.() -> Unit,
) {
    val context = LocalContext.current.applicationContext
    DisposableEffect(id, *keys) {
        context.shortcuts { dynamic(id, builder) }
        onDispose {
            if (removeOnDispose) {
                context.shortcuts { remove(id) }
            }
        }
    }
}
