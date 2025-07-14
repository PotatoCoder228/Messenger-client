-keep class io.ktor.network.** { *; }
-keep class io.ktor.client.** { *; }
-keep class io.ktor.serialization.kotlinx.** { *; }
-keep class kotlinx.serialization.json.** { *; }
-keep class ru.ssshteam.potatocoder228.messenger.** { *; }
-keep class androidx.sqlite.** { *; }
-keep class android.database.** { *; }
-keep class org.hildan.krossbow.** { *; }
-ignorewarnings
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}