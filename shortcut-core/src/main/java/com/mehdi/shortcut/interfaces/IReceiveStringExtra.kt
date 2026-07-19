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
package com.mehdi.shortcut.interfaces

/**
 * Callback echoing the string extra attached to a shortcut's launch intent.
 *
 * Invoked synchronously while the shortcut is being registered, mirroring the
 * behavior of the original 1.x Java API.
 */
public fun interface IReceiveStringExtra {
    public fun onReceiveStringExtra(
        stringExtraKey: String?,
        stringExtraValue: String?,
    )
}
