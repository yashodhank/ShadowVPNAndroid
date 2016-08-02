ShadowVPN for Android
=====================

[ShadowVPN] for Android. Still in progress.

[![Build Status](https://api.travis-ci.org/pexcn/ShadowVPNAndroid.svg)](https://travis-ci.org/pexcn/ShadowVPNAndroid)

How to build ShadowVPN
----------------------

This is a Gradle-based project that works best with [Android Studio].

1. Install the following software:
       - Android SDK:
         http://developer.android.com/sdk/index.html
       - Android NDK:
         http://developer.android.com/tools/sdk/ndk/index.html
       - Gradle:
         http://www.gradle.org/downloads
       - Android Studio:
         http://developer.android.com/sdk/installing/studio.html

2. Configure the `ANDROID_HOME` and `ANDROID_NDK_HOME` environment
   variables based on the location of the Android SDK and Android NDK.
   Additionally, consider adding `ANDROID_HOME/tools` and
   `ANDROID_HOME/platform-tools` to your `$PATH`.

3. Run the Android SDK Manager by pressing the SDK Manager toolbar button
   in Android Studio or by running the `android` command in a terminal
   window.

4. In the Android SDK Manager, ensure that the following are installed,
   and are updated to the latest available version:
       - Tools > Android SDK Platform-tools (rev 21 or above)
       - Tools > Android SDK Tools (rev 24.0.2 or above)
       - Tools > Android SDK Build-tools version 21 (rev 21.1.2 or above)
       - Android 5.0.1 > SDK Platform (API 21)
       - Extras > Android Support Repository
       - Extras > Android Support Library

5. Build native library.

        apt-get/brew install libtool automake
        git submodule update --init --recursive
        make

6. Import the project in Android Studio:

    1. Press File > Import Project
    2. Navigate to and choose the settings.gradle file in this project
    3. Press OK

7. Choose Build > Make Project in Android Studio or run the following
    command in the project root directory:

        gradle clean assembleDebug

8. To install on your test device:

        gradle installDebug


About this forked version
-------------------------

This version was forked from [Origin ShadowVPN for Android], the origin developer is [clowwindy]. Thanks him, my hero.  
I increase the user interface design working in progress.

（不会英文，但是为了保持和原来一样的风格强行用英文写了...感觉好丢人啊...）

[ShadowVPN]: https://github.com/pexcn/ShadowVPN
[Android Studio]:http://developer.android.com/sdk/installing/studio.html
[Origin ShadowVPN for Android]: https://github.com/clowwindy/ShadowVPNAndroid
[clowwindy]: https://github.com/clowwindy
