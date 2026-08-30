# AGENTS.md

Engineering guide for contributors and coding agents working on `velocityiodev/AppLovin-MAX-SDK-Android`.

## What This Repository Is

This is a **fork** of [AppLovin/AppLovin-MAX-SDK-Android](https://github.com/AppLovin/AppLovin-MAX-SDK-Android), maintained by the Velocity Ads team for end-to-end testing of the Velocity Ads MAX adapter on Android.

AppLovin's repo contains third-party mediation adapter source code for every ad network integrated with AppLovin MAX. The two demo apps it ships are the primary vehicle for testing custom network adapters (like ours) against the real MAX mediation stack.

Our working branch is **`velocity-ads-integration`**. All Velocity-specific changes live here; `main` always mirrors AppLovin's upstream `main` unmodified.

---

## Repository Layout

| Path | Purpose |
|------|---------|
| `AppLovin MAX Demo App - Kotlin/` | Kotlin demo app — primary integration test target |
| `AppLovin MAX Demo App - Java/` | Java demo app — secondary integration test target |
| `<NetworkName>/` | AppLovin-maintained third-party adapter source (do not modify) |

### What We Added on `velocity-ads-integration`

- **Local Maven dependencies** — both demo `build.gradle` files add `mavenLocal()` and depend on `io.velocity:ads-sdk` (Android SDK) and `io.velocity:ads-max-adapter` (MAX adapter) from local Maven.
- **AGP `8.7.3`, Gradle `8.9`, Kotlin `2.1.21`, Adjust SDK `5.7.0`** — updated in both demos to resolve compatibility issues.
- **Ad unit IDs** wired into every format (interstitial, rewarded, banner, MREC, native, adaptive banner) for both demos.
- **`AdaptiveBannerAdActivity`** — demonstrates anchored adaptive banners.
- **`PrivacySettingsActivity`** — GDPR / CCPA three-state `RadioGroup` (Not Set / No / Yes) with a Reset button that clears persisted consent values from `SharedPreferences`.
- **Removed deprecated native-ad template option** from both demos.
- **Native ad placer fix** — `MaxNativeAdViewBinder` and explicit `adSize` wired for `MaxRecyclerAdapter`.
- **Layout Editor banner/MREC XML** `adUnitId` placeholder replaced with real values; `android:id` restored on `MaxAdView`.
- **`native_custom_ad_view.xml` layout fixes** — advertiser text height changed to `wrap_content`, title constraint fixed to prevent text overflow.
- **`GlobalApplication`** migrated to Adjust SDK v5 API (`initSdk`, removed lifecycle callbacks, replaced `AD_REVENUE_APPLOVIN_MAX` constant with `"applovin_max_sdk"` string).

---

## Keeping Up With AppLovin Upstream

AppLovin regularly pushes adapter updates, SDK bumps, and new demo features to their public repo. Pull them into `velocity-ads-integration` periodically:

```bash
# Fetch latest from AppLovin (remote is named upstream)
git fetch upstream

# Merge into our branch (or rebase — your preference)
git checkout velocity-ads-integration
git merge upstream/main

# Resolve any conflicts (typically only in build.gradle files, ad unit ID
# constants, and layout XML files), then push.
git push origin velocity-ads-integration
```

**What conflicts to expect:**
- `build.gradle` (root and app-level) — AppLovin may bump AGP or Kotlin versions; preserve our `mavenLocal()` additions and version choices.
- Ad unit ID constants in Activity files — AppLovin may reset them to placeholders; restore our values.
- `activity_layout_editor_banner_ad.xml` / `activity_layout_editor_mrec_ad.xml` — AppLovin may reset `adUnitId`; restore our values and ensure `android:id` is preserved on `MaxAdView`.
- `GlobalApplication.kt` / `GlobalApplication.java` — AppLovin may update Adjust usage; re-apply our v5 migration if needed.
- New adapter folders at the repo root — accept them unconditionally; they are AppLovin-maintained.

---

## Demo App Parity Rule

There are two Android demo apps in this repo:

| App | Language |
|-----|----------|
| `AppLovin MAX Demo App - Kotlin/` | Kotlin |
| `AppLovin MAX Demo App - Java/` | Java |

**Every change made to one demo must be applied to the other.** This includes:

- New ad format activities (e.g. adaptive banner, new fullscreen format).
- New utility screens (e.g. privacy settings, debug tools).
- Ad unit ID updates.
- SDK dependency version bumps.
- Bug fixes in ad loading, impression tracking, or event handling.
- Layout XML changes (shared layout files should be kept in sync by copying).
- String resource additions.
- `AndroidManifest.xml` activity registrations.

When making a change, verify both demos build without errors before committing:

```bash
# Kotlin
cd "AppLovin MAX Demo App - Kotlin"
./gradlew assembleDebug

# Java
cd "../AppLovin MAX Demo App - Java"
./gradlew assembleDebug
```

---

## Local Dependency Setup

Both demo apps depend on locally published artifacts. Publish them to Maven local before building:

```bash
# Android SDK
cd velocityads-android-sdk-internal
./gradlew :velocityadssdk:publishToMavenLocal

# Android MAX adapter
cd velocityads-android-max-adapter
./gradlew :velocity-max-adapter:publishToMavenLocal
```

Both demo `build.gradle` root files include `mavenLocal()` in their repository lists, so Gradle will resolve these artifacts automatically after publishing.

---

## Key Wired Ad Unit IDs

| Format | Ad Unit ID |
|--------|-----------|
| Interstitial | `03c95df417312d2f` |
| Rewarded | `ea72031813a73e9f` |
| Banner | `cc8444b19cff9bda` |
| Adaptive Banner | `06f89bd35c0d5a8c` |
| MREC | `d487d94bad48d798` |
| Native | `fbca8f772f36695f` |

AppLovin MAX SDK Key: stored in `GlobalApplication.kt` / `GlobalApplication.java` as `YOUR_SDK_KEY`.
