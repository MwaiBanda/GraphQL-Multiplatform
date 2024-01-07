plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("com.apollographql.apollo3") version "3.8.2"

}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api("com.apollographql.apollo3:apollo-runtime:3.8.2")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.mwaibanda.graphql_multiplatform"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}

apollo {
    service("service") {
        packageName.set("com.mwaibanda")
    }
}