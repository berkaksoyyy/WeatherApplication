import java.util.Properties


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.squareup.anvil").version("2.4.7-1-8")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.berkaksoy.weatherapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.berkaksoy.weatherapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        multiDexEnabled = true

        val projectProperties = readProperties(file("../local.properties"))
        buildConfigField("String", "APP_ID", projectProperties.getProperty("APP_ID"))
        buildConfigField("String", "API_KEY", projectProperties.getProperty("API_KEY"))
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
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-android-compiler:2.46")
    implementation("com.google.android.libraries.places:places:3.2.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("io.coil-kt:coil-gif:2.1.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    kapt("com.google.dagger:dagger-compiler:2.46.1")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.jonathanfinerty.once:once:1.3.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.google.dagger:dagger:2.46.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("androidx.test:core:1.5.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.9.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}