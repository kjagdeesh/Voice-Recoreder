# Voice Recorder Android Application-

This Android Application provides simple way of recording audio files and playing recorded audio files.

![App Recording screen](https://github.com/charyjagdeesh/Voice-Recoreder/blob/master/Screenshots/Record.png?raw=true)

for more screenshots [click here](https://github.com/charyjagdeesh/Voice-Recoreder/tree/master/Screenshots)

## 📖  Getting started

- Please add below user permission to your app `AndroidManifest`:
```
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />  
	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />  
	<uses-permission android:name="android.permission.RECORD_AUDIO" />
```
- Please add below dependencies in your `build.gradle (app level)`
```
dependencies {
	//Material Design Components  
	implementation 'com.google.android.material:material:1.2.0-alpha03'  
  
	//Android Jetpak Navigation  
	def nav_version = "2.2.0"  
	implementation "androidx.navigation:navigation-fragment:$nav_version"  
	implementation "androidx.navigation:navigation-ui:$nav_version"  
	...
}
```


#### To Setup App Center Crash Report Analytics
- Please add below dependencies in your `build.gradle (app level)`
```
dependencies {
	//App center  
	def appCenterSdkVersion = '3.3.0'  
	implementation "com.microsoft.appcenter:appcenter-analytics:${appCenterSdkVersion}"  
	implementation "com.microsoft.appcenter:appcenter-crashes:${appCenterSdkVersion}"
	...
}
```
- In MainActivity.java add below code
```
AppCenter.start(getApplication(), "Enter your App Center app secret code here", Analytics.class, Crashes.class);
```

To get your [App Center](https://appcenter.ms/) secret app code follow this [documentation](https://docs.microsoft.com/en-us/appcenter/quickstarts/)


## 💖  Support my projects

I tried to help everyone by making it open-source and you can even modify the source code too. Please support me in creating stuff like this by giving star and sharing the projects you like.	

Thanks,
Jagdeesh

