# Contributing

Thanks for your interest in improving Shortcut!

## Building

- JDK 17+ and an Android SDK with platform 37.
- `./gradlew build` runs assembly, ktlint, Android Lint, unit tests and the
  binary-compatibility check for both library modules.

## Guidelines

- **Kotlin only**, with `explicitApi()` — every public symbol needs an explicit
  visibility modifier and KDoc.
- **Formatting** is enforced by ktlint (`ktlint_official` style): run
  `./gradlew ktlintFormat` before committing.
- **Tests**: changes to library behavior need JUnit + Robolectric coverage
  (`./gradlew test`).
- **Public API changes** must update the committed dumps: `./gradlew apiDump`
  and include the changed `api/*.api` files in your PR. CI fails otherwise.
- **Commits** follow [Conventional Commits](https://www.conventionalcommits.org)
  (`feat:`, `fix:`, `docs:`, `build:`, `test:`…); breaking changes use `!` and a
  `BREAKING CHANGE:` footer.
- Keep PRs small and focused; one logical change per PR.

## Releasing (maintainers)

1. Update `CHANGELOG.md` and the versions in both modules' `mavenPublishing.coordinates`.
2. Merge to `master` with green CI.
3. Push a tag matching the version (e.g. `2.0.0`) — `release.yml` publishes both
   artifacts to Maven Central.
