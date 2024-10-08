-keepattributes Signature
-keep class com.google.gson.** { *; }
-keep class * {
    **; // Preserve all generic signatures
}