-keep class io.ktor.network.** { *; }
-keep class io.ktor.client.** { *; }
-keep class io.ktor.serialization.** { *; }
-keep class kotlinx.serialization.** { *; }
-keep class ru.ssshteam.potatocoder228.messenger.** { *; }
-keep class androidx.sqlite.** { *; }
-ignorewarnings
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}