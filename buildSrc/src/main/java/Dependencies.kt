object Modules {
    const val APP = ":app"

    const val NAVIGATION = ":navigation"

    const val CORE_FRAMEWORK = ":core_framework"
    const val CORE_TEST = ":common_test"

    const val LOCAL = ":data:local"
    const val REMOTE = ":data:remote"
    const val MODEL = ":data:model"
    const val REPOSITORY = ":data:repository"

    const val FEATURE_HOME = ":features:home"
    const val FEATURE_DETAIL = ":features:detail"
}

object ApplicationId {
    const val ID = "com.mkdev.testapplication"
}

object Releases {
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"
}

object Versions {
    const val KOTLIN = "1.3.72"
    const val GRADLE = "4.0.1"

    const val COMPILE_SDK = 30
    const val MIN_SDK = 21
    const val TARGET_SDK = 30

    const val APP_COMPAT = "1.1.0"
    const val CORE_KTX = "1.3.1"
    const val CONSTRAINT_LAYOUT = "1.1.3"
    const val RECYCLER_VIEW = "1.2.0-alpha05"

    const val RETROFIT = "2.8.1"
    const val RETROFIT_COROUTINES = "0.9.2"
    const val RETROFIT_GSON = "2.9.0"
    const val GSON = "2.8.6"
    const val OKHTTP = "4.8.0"

    const val COROUTINES = "1.3.5"
    const val KOIN = "2.0.1"

    const val LIFECYCLE = "2.2.0"
    const val NAVIGATION = "2.3.0"
    const val SAFE_ARGS = "2.3.0"
    const val ROOM = "2.2.5"

    const val JUNIT = "4.13"
    const val ANDROID_JUNIT = "1.1.0"
    const val ANDROID_TEST_RUNNER = "1.2.0"
    const val ESPRESSO_CORE = "3.2.0"
    const val MOCK_WEB_SERVER = "2.7.5"
    const val ARCH_CORE_TEST = "2.1.0"
    const val MOCKK = "1.10.0"
    const val MOCKITO_KOTLIN = "2.1.0"
    const val MOCKITO_CORE = "2.8.9"
    const val FRAGMENT_TEST = "1.3.0-alpha07"
    const val COROUTINES_TEST = "1.3.7"

    const val TIMBER = "4.7.1"
    const val GLIDE = "4.11.0"
    const val PREFERENCE = "1.1.1"
}

object Libraries {
    // KOIN
    const val KOIN = "org.koin:koin-android:${Versions.KOIN}"
    const val KOIN_VIEW_MODEL = "org.koin:koin-androidx-viewmodel:${Versions.KOIN}"
    const val KOIN_SCOPE = "org.koin:koin-androidx-scope:${Versions.KOIN}"

    //CACHE
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"

    //Remote
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_GSON_CONVERTER = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val HTTP_LOGGING_INTERCEPTOR =
        "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val RETROFIT_COROUTINE_ADAPTER =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.RETROFIT_COROUTINES}"

    // GLIDE
    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"

    const val PREFERENCE = "androidx.preference:preference-ktx:${Versions.PREFERENCE}"
}

object KotlinLibraries {
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}"
    const val KOTLIN_COROUTINE_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
}

object AndroidLibraries {
    // KOTLIN
    const val KOTLIN_COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"

    // ANDROID
    const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val CONSTRAINT =
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val LIFECYCLE_VIEW_MODEL =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_EXTENSIONS = "androidx.lifecycle:lifecycle-extensions:${Versions.LIFECYCLE}"
    const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${Versions.RECYCLER_VIEW}"
    const val NAVIGATION_FRAGMENT =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_RUNTIME =
        "androidx.navigation:navigation-runtime:${Versions.NAVIGATION}"
}

object TestLibraries {
    // ANDROID TEST
    const val ANDROID_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROID_TEST_RUNNER}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
    const val ESPRESSO_CORE_LIB =
        "androidx.test.espresso:espresso-contrib:${Versions.ESPRESSO_CORE}"
    const val ARCH_CORE_TEST = "androidx.arch.core:core-testing:${Versions.ARCH_CORE_TEST}"
    const val ANDROID_JUNIT = "androidx.test.ext:junit:${Versions.ANDROID_JUNIT}"
    const val FRAGMENT_TEST = "androidx.fragment:fragment-testing:${Versions.FRAGMENT_TEST}"

    // KOIN
    const val KOIN = "org.koin:koin-test:${Versions.KOIN}"

    // MOCK WEB SERVER
    const val MOCK_WEB_SERVER = "com.squareup.okhttp:mockwebserver:${Versions.MOCK_WEB_SERVER}"

    // MOCK
    const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
    const val MOCKITO_KOTLIN =
        "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.MOCKITO_KOTLIN}"
    const val MOCKITO_CORE = "org.mockito:mockito-core:${Versions.MOCKITO_CORE}"

    // COROUTINE
    const val COROUTINES_TEST =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES_TEST}"
}