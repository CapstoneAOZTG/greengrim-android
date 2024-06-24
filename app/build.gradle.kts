import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.aoztg.greengrim"
    compileSdk = 34

    Properties().apply {
        load(FileInputStream("$rootDir/local.properties"))
    }

    buildFeatures{
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.aoztg.greengrim"
        minSdk = 27
        targetSdk = 34
        versionCode = 35
        versionName = "1.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "NAVER_CLIENT_ID", getProperty("naverClientId"))
        buildConfigField("String", "NAVER_CLIENT_SECRET", getProperty("naverClientSecret"))
        buildConfigField("String", "NAVER_CLIENT_NAME", getProperty("naverClientName"))
        buildConfigField("String", "KAKAO_API_KEY", getProperty("kakaoAppKey"))
        buildConfigField("String", "BASE_DEV_URL", getProperty("baseDevUrl"))
        buildConfigField("String", "BASE_PROD_URL", getProperty("baseProdUrl"))
        buildConfigField("String", "SOCKET_URL", getProperty("socketUrl"))
        buildConfigField("String", "MASTER_JWT", getProperty("masterJwt"))
        manifestPlaceholders["KAKAO_API_KEY"] = getProperty("kakaoAppKey")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // hilt
    val hiltVersion = "2.48"
    implementation("com.google.dagger:hilt-android:${hiltVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${hiltVersion}")

    // retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // okHttp
    val okHttpVersion = "5.0.0-alpha.2"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:$okHttpVersion")

    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    // room
    val roomVersion = "2.4.3"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.1")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Live Data
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    // naver login
    implementation("com.navercorp.nid:oauth-jdk8:5.6.0")

    // kakao login
    implementation("com.kakao.sdk:v2-all:2.17.0")

    // google login
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.android.gms:play-services-auth:19.2.0")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Circle Indicator
    implementation("me.relex:circleindicator:2.1.6")

    // Custom Calendar
    implementation("com.kizitonwose.calendar:view:2.3.0")

    // stomp
    implementation("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")

    // rx
    implementation("io.reactivex.rxjava2:rxjava:2.2.5")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0")

    // FCM
    implementation("com.google.firebase:firebase-messaging-ktx")
//    apply plugin: 'com.google.gms.google-services'

    // Datastore Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-core:1.0.0")

    // Lottie
    implementation("com.airbnb.android:lottie:5.0.3")

}