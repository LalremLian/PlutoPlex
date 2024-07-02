plugins {
    with(libs.plugins) {
        id(ksp.get().pluginId)
        id(kotlin.kapt.get().pluginId)
        id(com.android.application.get().pluginId)
        id(org.jetbrains.kotlin.android.get().pluginId)
        id(kotlin.parcelize.get().pluginId)
        id(com.gms.google.services.get().pluginId)
        id(androidx.navigation.safeargs.get().pluginId)
        id(com.google.dagger.hilt.android.get().pluginId)
        id(com.google.firebase.crashlytics.get().pluginId)
    }
}

android {
    namespace = "com.lazydeveloper.plutoplex"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lazydeveloper.plutoplex"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":data"))
    with(libs) {
        // View
        implementation(splashscreen)
        implementation(material)
        implementation(constraint.layout)
        implementation(bundles.pager)
        implementation(datetime.picker)
        coreLibraryDesugaring(desugar)
        implementation(runtime.permission)
        implementation(number.picker)
        implementation(skydove.animation)
        implementation(system.ui.controller)

        // Kotlin
        implementation(core.ktx)
        implementation(kotlin.coroutines)

        // Hilt
        implementation(bundles.hilt)
        ksp(hilt.compiler)
        ksp(hilt.compiler.ksp)
        ksp(hilt.android.compiler)
        implementation ("androidx.paging:paging-compose:1.0.0-alpha13")

        // Jetpack
        implementation(platform(compose.bom))
        implementation(bundles.compose)
        implementation(bundles.lifecycle)
        implementation(work.manager.ktx)
        implementation(bundles.lifecycle)
        implementation(bundles.navigation)
        implementation("com.google.firebase:firebase-auth:21.0.1")
        implementation("androidx.credentials:credentials:1.3.0-alpha01")
        implementation("androidx.credentials:credentials-play-services-auth:1.1.0")
        implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")

        // Image
        implementation(bundles.coil)
        implementation(barcode.scanner)
        implementation(circleindicator)

        // Player
        implementation(bundles.media3.player)

        // Network
        implementation(gson)

        implementation ("com.google.accompanist:accompanist-webview:0.33.2-alpha")

        // Ads & Mediation
        implementation("com.google.android.gms:play-services-ads:22.0.0")
        implementation("com.google.ads.mediation:facebook:6.17.0.0")
        implementation("com.unity3d.ads:unity-ads:4.10.0")
        implementation("com.google.ads.mediation:unity:4.10.0.0")
        implementation(libs.ironsource)

        // Lottie
        implementation("com.airbnb.android:lottie-compose:4.0.0")

        implementation(platform("com.google.firebase:firebase-bom:30.3.2"))
        implementation("com.google.firebase:firebase-firestore-ktx")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.2")

        // Firebase
        implementation(bundles.firebase)

        // Play Services
        implementation(bundles.play.services)
    }
}