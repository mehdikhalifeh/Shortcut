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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * Builds one launch [Intent] of a shortcut.
 *
 * [action] defaults to [Intent.ACTION_VIEW] because the system requires every
 * shortcut intent to carry an action. Prefer an explicit [target] activity
 * over implicit resolution so the shortcut cannot be intercepted by other
 * apps.
 */
@ShortcutsDsl
public class IntentSpec internal constructor(
    private val context: Context,
) {
    private val intent = Intent(Intent.ACTION_VIEW)

    /** Intent action. Defaults to [Intent.ACTION_VIEW]; must not end up null. */
    public var action: String
        get() = intent.action ?: Intent.ACTION_VIEW
        set(value) {
            intent.action = value
        }

    /** Data URI, e.g. `"myapp://compose".toUri()`. */
    public var data: Uri?
        get() = intent.data
        set(value) {
            intent.data = value
        }

    /** MIME type. Setting it does not clear [data] (unlike [Intent.setType]). */
    public var type: String?
        get() = intent.type
        set(value) {
            intent.setDataAndType(intent.data, value)
        }

    /** Intent flags, e.g. `Intent.FLAG_ACTIVITY_CLEAR_TASK`. */
    public var flags: Int
        get() = intent.flags
        set(value) {
            intent.flags = value
        }

    /** Adds [flag] to [flags]. */
    public fun addFlags(flag: Int) {
        intent.addFlags(flag)
    }

    /** Targets [activityClass] explicitly (recommended over implicit resolution). */
    public fun target(activityClass: Class<out Activity>) {
        intent.setClass(context, activityClass)
    }

    /** Targets activity [T] explicitly (recommended over implicit resolution). */
    public inline fun <reified T : Activity> target() {
        target(T::class.java)
    }

    public fun putExtra(
        name: String,
        value: String?,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: CharSequence?,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Boolean,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Byte,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Char,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Short,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Int,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Long,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Float,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Double,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Parcelable?,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Serializable?,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Bundle?,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: IntArray?,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: LongArray?,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: BooleanArray?,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: FloatArray?,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: DoubleArray?,
    ) {
        intent.putExtra(name, value)
    }

    public fun putExtra(
        name: String,
        value: Array<String?>?,
    ) {
        intent.putExtra(name, value)
    }

    /** Copies every extra from [bundle] into the intent. */
    public fun putExtras(bundle: Bundle) {
        intent.putExtras(bundle)
    }

    internal fun build(): Intent = Intent(intent)
}
