plugins {
    id("com.android.application")
}

android {
    namespace = "com.next.up.code.myskripsi"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.next.up.code.myskripsi"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // scalable unit size
    implementation("com.intuit.sdp:sdp-android:1.0.6")

    // scalable unit text size
    implementation("com.intuit.ssp:ssp-android:1.0.6")




    implementation ("com.amitshekhar.android:android-networking:1.0.2")

    implementation ("com.github.gabriel-TheCode:AestheticDialogs:1.3.6")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("gun0912.ted:tedpermission:2.2.3") // Untuk mengelola izin
    implementation ("com.github.Ru95Gasol:File-Picker:1.0")


    implementation ("com.github.zzz40500:android-shapeLoadingView:1.0.3.2")
    /* Helper */
    implementation ("com.github.TistoW:MyHelper:1.1.26")

    implementation("com.github.dhaval2404:imagepicker:2.1")

}