# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\adt-bundle-windows-x86-20140321\sdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-flattenpackagehierarchy
-verbose

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-ignorewarnings

-keep public class oauth.signpost.commonshttp.** { public protected *; }
-keep public class oauth.signpost.** { public protected *; }
-keep public class oauth.signpost.basic.** { public protected *; }
-keep public class oauth.signpost.exception.** { public protected *; }
-keep public class oauth.signpost.http.** { public protected *; }
-keep public class oauth.signpost.signature.** { public protected *; }

-keep public class com.google.gdata.util.common.base.** { public protected *; }
-keep public class android.** { public protected private *; } 

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keep public class * extends android.view.ViewGroup
{ public  <init>(android.content.Context, android.util.AttributeSet);
}
-keep public class * extends android.view.ViewGroup
{ public  <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep public class * extends android.widget.FrameLayout
{ public  <init>(android.content.Context, android.util.AttributeSet);
}
-keep public class * extends android.widget.FrameLayout
{ public  <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class * extends android.webkit.WebView
{ public  <init>(android.content.Context, android.util.AttributeSet);
}
-keep public class * extends android.webkit.WebView
{ public  <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class * extends android.widget.RelativeLayout
{ public  <init>(android.content.Context, android.util.AttributeSet);
}
-keep public class * extends android.widget.RelativeLayout
{ public  <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class * extends android.widget.ImageButton
{ public  <init>(android.content.Context, android.util.AttributeSet);
}

-keep public class * extends android.renderscript.RSSurfaceView
{ public  <init>(android.content.Context, android.util.AttributeSet);
}

-keep public class * extends android.widget.ImageView
{ public  <init>(android.content.Context);
}

-keep public class * extends android.widget.ImageView
{ public  <init>(android.content.Context, android.util.AttributeSet);
}

-keep public class * extends android.widget.ImageView
{ public  <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class * extends android.widget.TextView
{ public  <init>(android.content.Context);
}

-keep public class * extends android.widget.TextView
{ public  <init>(android.content.Context, android.util.AttributeSet);
}

-keep public class * extends android.widget.TextView
{ public  <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class * extends android.view.View
{ public  <init>(android.content.Context, android.util.AttributeSet);
}

-keep public class * extends android.view.View
{ public  <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class com.fox.exercisewell.CrossView
-keep public class com.fox.exercisewell.LineView
-keep public class com.fox.exercisewell.CutMaskView
-keep public class com.fox.exercisewell.FreedomDraw
-keep public class com.fox.exercisewell.RankActivity
-keep public class com.fox.exercisewell.SlimFriendState
-keep public class com.fox.exercisewell.StateActivity
-keep public class com.fox.exercisewell.FoxSportsState
-keep public class com.fox.exercisewell.NearbyActivity
-keep public class com.fox.exercisewell.FoxSportsSetting
-keep public class com.fox.exercisewell.map.SportingMapActivity
-keep public class com.fox.exercisewell.util.RoundedImage

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class java.lang.Object { !static <methods>; } 

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class com.fox.exercisewell.ListPreference { !static <methods>; }
-keepclassmembers class com.fox.exercisewell.CameraPreference { !static <methods>; }
-keepclassmembers class com.fox.exercisewell.PreferenceGroup { !static <methods>; }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep public class com.fox.exercisewell.R$*{
      public static final int *;
}

-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;} 
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
-keep class com.tencent.mm.sdk.** {*;}
-keep class cn.ingenic.** {*;}
-keep class cn.ingenic.indroidsync.** {*;}
-keep class com.android.vcard.** {*;}
-keep class android.annotation.** {*;}


-dontwarn android.content.res.XmlResourceParser
-dontwarn android.content.*
-dontwarn android.graphics.drawable.*
-dontwarn android.view.*
-dontwarn android.support.v4.**

-keep class com.baidu.**{*;}
-keep class vi.com.gdi.bgl.**{*;}

-keep class com.amap.api.**  {*;}      
-keep class com.autonavi.**  {*;}
-keep class com.a.a.**  {*;}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep,allowshrinking class org.android.agoo.service.* {
    public <fields>;
    public <methods>;
}

-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}
-keep class com.iflytek.voiceads.** {*;}

-keep public class **.R$*{
   public static final int *;
}




