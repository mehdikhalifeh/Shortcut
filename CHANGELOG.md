# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.0.0] - 2026-07-19

### Added

- `shortcuts { }` Kotlin DSL as the single entry point (`Context.shortcuts`).
- Full `Intent` support per shortcut: action, data URI, MIME type, flags, extras of every
  `Bundle` type, and explicit activity targeting via `target<Activity>()`.
- Multiple `intent { }` blocks per shortcut building a launch back stack.
- Icons from drawable resources, any `IconCompat`, or adaptive bitmaps.
- Complete `ShortcutManagerCompat` coverage: `dynamic` (publish-or-update),
  `update`, `remove`, `removeAll`, `disable(message = …)`, `enable`, `reportUsed`,
  `maxShortcutCountPerActivity`, `isRateLimitingActive`, `isPinShortcutSupported`.
- Pinned shortcut requests with an immutable-`PendingIntent` result callback.
- **New artifact `shortcut-compose`**: `DynamicShortcutEffect` (lifecycle-aware dynamic
  shortcuts) and `rememberPinShortcutRequester` (pin requests with composition-scoped
  result delivery).
- Binary-compatibility validation with committed API dumps for both artifacts.
- Dokka API docs published via GitHub Pages.

### Changed

- **Breaking:** distribution moved from JitPack (`com.github.MehdiKh93:Shortcut`) to
  Maven Central (`io.github.mehdikhalifeh:shortcut-core` / `shortcut-compose`).
- **Breaking:** minimum SDK raised from 15 to 23 (required by current AndroidX).
- Library rewritten in Kotlin with `explicitApi()`; built with AGP 9 built-in Kotlin.
- All shortcut operations now go through `ShortcutManagerCompat` — no `RequiresApi`
  restrictions; dynamic shortcuts are a safe no-op below API 25, pinned requests use the
  legacy launcher broadcast below API 26.

### Removed

- **Breaking:** `ShortcutUtils`, `Shortcut`, `Shortcut.ShortcutBuilder` and
  `IReceiveStringExtra`. Read shortcut extras directly from the launched activity's
  intent instead (see the README pattern).

### Fixed

- Crash on Android 12+ when requesting pinned shortcuts (`PendingIntent` now uses
  `FLAG_IMMUTABLE`).
- Removing a dynamic shortcut no longer resurrects it on the next add (internal id
  lists no longer grow unbounded).

## [1.0.2] - 2019-11-09

Legacy Java release distributed via JitPack.

[Unreleased]: https://github.com/mehdikhalifeh/Shortcut/compare/2.0.0...HEAD
[2.0.0]: https://github.com/mehdikhalifeh/Shortcut/compare/1.0.2...2.0.0
[1.0.2]: https://github.com/mehdikhalifeh/Shortcut/releases/tag/1.0.2
