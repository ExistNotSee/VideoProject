plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    val androidBuildTools: Map<*, *> = rootProject.ext["androidBuildTools"] as Map<*, *>
    compileSdk = androidBuildTools["compileSdk"] as Int

    defaultConfig {
        minSdk = androidBuildTools["minSdk"] as Int
        targetSdk = androidBuildTools["targetSdk"] as Int

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                cppFlags("-frtti -fexceptions -Wno-deprecated-declarations")
                cFlags("-DSTDC_HEADERS")
            }
        }
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
//            abiFilters.add("x86_64")
        }
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
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.18.1"
        }
    }
    ndkVersion = "21.4.7075529"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":libSDKBase"))
    implementation(project(":v_Base"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$rootProject.kotlin.kotlin_stdlib")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}