# Android News Reader Example App

This is an example news reader app that can be used as a base for creating a standalone, native Android app for your news site or blog.

## Features

* Article viewer utilizing an RSS feed as its backend, with article contents served via a `<content:encoded>` tag.
* Lightly-customized WebView intended for displaying a social feed.
* YouTube channel viewer utilizing the [YouTube Data API](https://developers.google.com/youtube/v3/docs/) accessed via Retrofit.

## Configuration

The app can be used out-of-the-box with some minimal configuration.

<details>
<summary>Show configuration items</summary>

### Base domain for articles
This is the domain that your RSS feed is hosted on.

* Location: [Fragments.kt line 36](https://github.com/farmerbb/News-Reader-Example/blob/master/app/src/main/java/com/example/newsreader/Fragments.kt#L36)

### RSS feed path
Relative to the base domain specified above.  Use a leading slash for the path.

* Location: [Fragments.kt line 37](https://github.com/farmerbb/News-Reader-Example/blob/master/app/src/main/java/com/example/newsreader/Fragments.kt#L37)

### Social feed URL
URL for the social feed loaded inside of a WebView.  This example uses an HTML file located in the assets folder with a Twitter embed.

* Location: [SocialFragment.kt line 42](https://github.com/farmerbb/News-Reader-Example/blob/master/app/src/main/java/com/example/newsreader/SocialFragment.kt#L42)

### YouTube Data API key
This is required for the YouTube tab of the app to work.

* Location: [YouTubeViewModel.kt line 44](https://github.com/farmerbb/News-Reader-Example/blob/master/app/src/main/java/com/example/newsreader/YouTubeViewModel.kt#L44)

### YouTube channel ID
Channel ID to fetch YouTube video data from.

* Location: [YouTubeViewModel.kt line 41](https://github.com/farmerbb/News-Reader-Example/blob/master/app/src/main/java/com/example/newsreader/YouTubeViewModel.kt#L41)

### Intent handling
Basic intent handling is supported for your site's domain by adding it to the manifest as well as a spot inside MainActivity.

* Location: [AndroidManifest.xml lines 42&ndash;47](https://github.com/farmerbb/News-Reader-Example/blob/master/app/src/main/AndroidManifest.xml#L42-L47), [MainActivity line 154](https://github.com/farmerbb/News-Reader-Example/blob/master/app/src/main/java/com/example/newsreader/MainActivity.kt#L154)

</details>

## How to Build
Prerequisites:
* Windows / MacOS / Linux
* JDK 8
* Android SDK
* Internet connection (to download dependencies)

First, ensure that you have added a YouTube Data API key to the app, as described in the Configuration section above.  Once all the prerequisites are met, make sure that the `ANDROID_HOME` environment variable is set to your Android SDK directory, then run `./gradlew assembleDebug` at the base directory of the project to start the build. After the build completes, navigate to `app/build/outputs/apk/debug` where you will end up with an APK file ready to install on your Android device.

## Origin

This example is based off of the Android app for [atU2.com](https://www.atu2.com), a now-defunct U2 fan site.  The app was first published in March 2018 and was last updated in March 2019.  Therefore, the app may not utilize the latest best practices for Android development as of this repo's publication in December 2020.

The atU2 app codebase has had minimal changes for the initial commit of this open source release:
* Dependencies have been updated to their latest versions as of December 2020, and changes have been made to remove usage of deprecated code.
  * NOTE: Kotlin Android Extensions is still being used in this project. This component is [scheduled to be removed](https://android-developers.googleblog.com/2020/11/the-future-of-kotlin-android-extensions.html) by Google in September 2021.

* The app is stripped of its atU2 branding and now utilizes the [XDA-Developers](https://www.xda-developers.com) portal as an example site.
* A proprietary web-based social component has been removed, as well as the app's YouTube API key.
