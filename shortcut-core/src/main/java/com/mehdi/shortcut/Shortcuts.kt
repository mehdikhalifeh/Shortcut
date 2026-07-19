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
package com.mehdi.shortcut

import android.content.Context

/**
 * Marks the shortcut DSL so receivers of an outer scope are not implicitly
 * available inside a nested block.
 */
@DslMarker
@Target(AnnotationTarget.CLASS)
public annotation class ShortcutsDsl

/**
 * Entry point of the shortcut DSL.
 *
 * ```kotlin
 * context.shortcuts {
 *     dynamic("compose_email") {
 *         shortLabel = "Compose"
 *         icon = R.drawable.ic_compose
 *         intent {
 *             action = Intent.ACTION_VIEW
 *             data = "myapp://compose".toUri()
 *             putExtra("source", "shortcut")
 *         }
 *     }
 * }
 * ```
 *
 * All operations are backed by
 * [androidx.core.content.pm.ShortcutManagerCompat]: dynamic shortcuts are a
 * safe no-op below API 25 and pinned shortcut requests fall back to the legacy
 * launcher broadcast below API 26.
 */
public fun Context.shortcuts(block: ShortcutsScope.() -> Unit) {
    ShortcutsScope(this).block()
}
