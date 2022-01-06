# openlogin-android-sdk

Torus OpenLogin SDK for Android applications.

`openlogin-android-sdk` is a client-side library you can use with your Android app to authenticate users using [OpenLogin](https://openlogin.com).

## Requirements

Android API version 24 or newer is required.

## Installation

### Add OpenLogin to Gradle

In your project-level `build.gradle` file, add JitPack repository:

```groovy
allprojects {
    repositories {
        // ...
        maven { url "https://jitpack.io" }
    }
}
```

Then, in your app-level `build.gradle` dependencies section, add the following:

```groovy
dependencies {
    // ...
    implementation 'org.torusresearch:openlogin-android-sdk:-SNAPSHOT'
}
```

**Note**: This SDK is currently in beta, using `-SNAPSHOT` to make sure you receive latest updates 
and be aware that there may be breaking changes.

### Permissions

Open your app's `AndroidManifest.xml` file and add the following permission:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Integrating

### Configure an OpenLogin project

Go to [Developer Dashboard](https://developer.tor.us), create or select an OpenLogin project:

- Add `{YOUR_APP_PACKAGE_NAME}://auth` to **Whitelist URLs**.

- Copy the Project ID for usage later.

### Configure Deep Link 

Open your app's `AndroidManifest.xml` file and add the following deep link intent filter to your sign-in activity:

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />

    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />

    <!-- Accept URIs: {YOUR_APP_PACKAGE_NAME}://* -->
    <data android:scheme="{YOUR_APP_PACKAGE_NAME}" />
</intent-filter>
```

### Initialize OpenLogin

In your sign-in activity', create an `OpenLogin` instance with your OpenLogin project's configurations and 
configure it like this:

```kotlin
class MainActivity : AppCompatActivity() {
    // ...
    lateinit var openlogin: OpenLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openlogin = OpenLogin(OpenLoginOptions(context = this,
            clientId = getString(R.string.openlogin_client_id),
            network = OpenLogin.Network.MAINNET,
            redirectUrl = Uri.parse("{YOUR_APP_PACKAGE_NAME}://auth")))

        // Handle user signing in when app is not alive
        openlogin.setResultUrl(intent?.data)
        
        // ...
    }
    
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        // Handle user signing in when app is active
        openlogin.setResultUrl(intent?.data)

        // ...
    }

    private fun onClickLogin() {
        val selectedLoginProvider = OpenLogin.Provider.GOOGLE   // Can be Google, Facebook, Twitch etc
        openlogin.login(LoginParams(selectedLoginProvider))
    }
    
    //...
}
```

Make sure your sign-in activity `launchMode` is set to `singleTop` in your `AndroidManifest.xml`:

```xml
<activity
    android:launchMode="singleTop"
    android:name=".YourActivity">
    // ...
</activity>
```

## API Reference

```kotlin
class OpenLogin(
    var openLoginOptions : OpenLoginOptions
) {
    // Trigger login flow that shows a modal for user to select one of supported providers to login,
    // e.g. Google, Facebook, Twitter, Passwordless, etc 
    fun login() {} 
    
    // Trigger login flow using login params. Specific Login Provider can be set through Login Params
    fun login(
        loginParams: LoginParams,
    ) {}
} 

data class OpenLoginOptions(
    context: Context, // Android context to launch Web-based authentication, usually is the current activity
    clientId: String, // Your OpenLogin project ID
    network: Network, // Network to run OpenLogin, either MAINNET or TESTNET
    redirectUrl: Uri? = null, // URL that OpenLogin will redirect API responses
)

data class LoginParams(
    val loginProvider: OpenLogin.Provider,
    val reLogin: Boolean? = null,
    val skipTKey: Boolean? = null,
    val extraLoginOptions: ExtraLoginOptions? = null,
    val redirectUrl: Uri? = null,
    val appState: String? = null
)

```