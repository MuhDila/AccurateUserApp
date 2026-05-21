# Preserve Line numbers for release stacktraces
-keepattributes SourceFile,LineNumberTable

# Preserve @Keep annotations
-keep @androidx.annotation.Keep class * { *; }

# Moshi Keep Rules
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-dontwarn okio.**
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }

# App DTOs, Entities & Models (Serialization/Database safety)
-keep class com.muhdila.accurateuserapp.user.data.remote.dto.** { *; }
-keep class com.muhdila.accurateuserapp.user.data.local.entity.** { *; }
-keep class com.muhdila.accurateuserapp.user.domain.model.** { *; }