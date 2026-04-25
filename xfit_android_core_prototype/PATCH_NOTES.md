# Patch Notes

## 0.1.2

Compatibility rebuild for Android Studio versions that support AGP 8.13.0 but not AGP 9.x.

Changed:

```kotlin
id("com.android.application") version "8.13.0" apply false
id("org.jetbrains.kotlin.android") version "2.3.20" apply false
id("org.jetbrains.kotlin.plugin.compose") version "2.3.20" apply false
```

Restored this in `app/build.gradle.kts`:

```kotlin
id("org.jetbrains.kotlin.android")
```

Changed Gradle wrapper:

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.13-bin.zip
```

AGP 8.13.0 requires Gradle 8.13 minimum.
