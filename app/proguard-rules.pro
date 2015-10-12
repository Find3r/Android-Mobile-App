# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\bi\Documents\Carlos\Carlos\adt-bundle-windows-x86-20140321\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

##---------------Begin: proguard configuration for Gson ----------
# Gson uses generic type information stored in a class file when working with
#fields. Proguard removes such information by default, so configure it to keep
#all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

-keep class com.nansoft.find3r.models.** { *; }
-keepclassmembers class com.nansoft.find3r.models.** { *; }

-keep class com.google.common.collect.** { *; }
-keepclassmembers class com.google.common.collect.** { *; }

-keep class javax.annotation.** { *; }
-keepclassmembers class javax.annotation.** { *; }


-keep class com.google.guava.** { *; }
-keepclassmembers class com.google.guava.** { *; }

-keep class sun.misc.Unsafe.** { *; }
-keepclassmembers class sun.misc.Unsafe.** { *; }

-keep class com.fasterxml.jackson.core.** { *; }
-keepclassmembers class com.fasterxml.jackson.core.** { *; }

-dontwarn com.fasterxml.jackson.core.**

-dontwarn com.google.guava.**

-dontwarn javax.annotation.**
-dontwarn sun.misc.Unsafe

##---------------End: proguard configuration for Gson ----------