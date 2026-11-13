// Plugins declarados aqui e aplicados no módulo :app
plugins {
    alias(libs.plugins.android.application) version "9.4.0" apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.objectbox) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
