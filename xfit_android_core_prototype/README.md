# xfit Android Core Prototype

This is the first runnable Android prototype for **xfit** by **Gray Mentality**.

Subtitle:

> A compliance-based exercise program

## What this prototype does

This version is intentionally small.

It includes:

- Kotlin core workout engine
- Jetpack Compose single-screen UI
- Button to generate a sample xFit stage preview
- Smoke tests printed on-screen
- No Room database yet
- No login yet
- No PHP/MySQL backend yet

## Confirmed prototype rules

- 3 to 8 exercises
- 54 workouts per stage
- 9 mini-cycles of 6 workouts
- Rep phases:
  - Workouts 1-18: 15 reps
  - Workouts 19-36: 10 reps
  - Workouts 37-54: 5 reps
- Sets:
  - 1 set, then 2 sets, then 3 sets in each rep phase
- Intensity ramp:
  - 75%, 80%, 85%, 90%, 95%, 100%
- Weight rounding:
  - workouts 1-5 in each mini-cycle floor to nearest 2.5 lb
  - workout 6 in each mini-cycle equals target RM exactly
- xFit RM constants:
  - estimated 1RM = new15Rm × 1.40
  - estimated 10RM = estimated 1RM / 1.27
  - estimated 5RM = estimated 1RM / 1.11

## How to open

1. Unzip this folder.
2. Open Android Studio.
3. Choose **Open**.
4. Select the unzipped `xfit_android_core_prototype` folder.
5. Let Gradle sync.
6. Plug in your Android phone.
7. Enable USB debugging on the phone.
8. Press **Run** in Android Studio.

## Main files

```text
app/src/main/java/ca/graymentality/xfit/MainActivity.kt
app/src/main/java/ca/graymentality/xfit/core/WorkoutEngine.kt
```

## Notes

This project uses:

- Android Gradle Plugin 9.1.1
- Kotlin 2.3.20
- Jetpack Compose BOM 2026.04.01
- Activity Compose 1.13.0

If your Android Studio is older and Gradle sync fails, update Android Studio first.
