# Keep generic signatures (necessary for Gson and TypeToken)
-keepattributes Signature

# Keep the TypeToken class and its generics
-keep class com.google.gson.reflect.TypeToken { *; }

# Preserve classes that implement or extend TypeToken
-keep class * extends com.google.gson.reflect.TypeToken { *; }

# Gson specific rules for handling reflection
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter { *; }

# Preserve model classes (replace 'model' with your actual package structure)
-keep class **.model.** { *; }

# Keep any necessary annotations
-keepattributes *Annotation*