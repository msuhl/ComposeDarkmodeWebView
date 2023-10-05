package dk.isten.andro.mywebviewapplication

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.recreate
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING
import androidx.webkit.WebSettingsCompat.FORCE_DARK_ON
import androidx.webkit.WebSettingsCompat.ForceDark
import androidx.webkit.WebViewFeature
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.google.accompanist.web.rememberWebViewStateWithHTMLData
import dk.isten.andro.mywebviewapplication.ui.theme.MyWebViewApplicationTheme

class MainActivity : AppCompatActivity() {
    private var darkBool = true
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(true);

        setContent {
            MyWebViewApplicationTheme {
//                val activity = LocalContext.current.findActivity()
//                val dark = isSystemInDarkTheme()
//                val currentNightMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
//                when (currentNightMode) {
//                    Configuration.UI_MODE_NIGHT_NO -> {} // Night mode is not active, we're using the light theme.
//                    Configuration.UI_MODE_NIGHT_YES -> {} // Night mode is active, we're using dark theme.
//                }
//
//                Log.d("Svend compose dark ", isSystemInDarkTheme().toString())
//                Log.d("Svend system dark ", "${AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES}")
//                if (!isSystemInDarkTheme() && isSystemInDarkTheme() != (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)) {
//                    Log.d("Svend isSystemInDarkTheme ", "$dark")
//                    Log.d("Svend getSystemFaultTheme ", "${AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES}")
//
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                    uiManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
//
////                    recreate(activity)
//
//                } else {
//                    Log.d("Svend isSystemInDarkTheme ", "$dark")
//                    Log.d("Svend getSystemFaultTheme ", "${AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES}")
//
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                    uiManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
//
////                    recreate(activity)
//
//                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Greeting("Android")
                        MyWeb()
                    }
                }
            }
        }
    }
}

@Composable
private fun MyWeb() {
    val state = rememberWebViewStateWithHTMLData(webContent)

    val webClient = object : AccompanistWebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            return false

        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            view?.evaluateJavascript(
                FALLBACK_CSS,
                null
            )
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

        }
    }

    val darkTheme = isSystemInDarkTheme()
    val darkThemeLegacy =
        AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES


    WebView(
        modifier = Modifier
            .fillMaxSize(),
        state = state,
        onCreated = { view ->

            view.clearCache(true)
            view.settings.run {
                javaScriptEnabled = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

//                if (WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
//                    WebSettingsCompat.setAlgorithmicDarkeningAllowed(this, true);
//                }

                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK_STRATEGY)) {
                    WebSettingsCompat.setForceDarkStrategy(
                        this,
                        DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING
                    )                }
            }
        },
        captureBackPresses = false,
        client = webClient,
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var night = remember { mutableStateOf(false) }
    val activity = LocalContext.current.findActivity()
    Log.d("Svend night ", "${night.value}")
    Button(onClick = {
    }) {
        Text(
            text = "Change theme!",
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyWebViewApplicationTheme {
        Greeting("Android")
    }
}

private const val webContent = "<html><head></head><body><p>Hej Svend</p></body></html>"

const val FALLBACK_CSS =
    "const isDarkMode = window.matchMedia && ('(prefers-color-scheme: dark)').matches; console.log(isDarkMode);var themeStyles = '@media (prefers-color-scheme: dark){ body { color: yellow;    background: #383838; } }';var cssTheme = document.createElement('style');cssTheme.innerText = themeStyles;document.head.appendChild(cssTheme);"

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Activity not found.")
}
