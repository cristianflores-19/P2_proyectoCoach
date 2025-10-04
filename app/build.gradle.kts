plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.coachprueba"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.coachprueba"
        minSdk = 24
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation("com.google.android.material:material:1.11.0")
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // RecyclerView para listas
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ViewModel y LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")

    // Retrofit para comunicación con API Ollama
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Room Database (alternativa moderna a SQLite)
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // SharedPreferences para sesión
    implementation("androidx.preference:preference:1.2.1")

    // OkHttp para conexión con Ollama
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // JSON para manejo de respuestas
    implementation("org.json:json:20230227")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}