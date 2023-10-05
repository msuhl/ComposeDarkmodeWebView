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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Greeting()
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

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            view?.evaluateJavascript(
                FALLBACK_CSS,
                null
            )
        }
    }

    WebView(
        modifier = Modifier
            .fillMaxSize(),
        state = state,
        onCreated = { view ->

            view.clearCache(true)
            view.settings.run {
                javaScriptEnabled = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                if (WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
                    WebSettingsCompat.setAlgorithmicDarkeningAllowed(this, true);
                }
            }
        },
        captureBackPresses = false,
        client = webClient,
    )
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
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
        Greeting()
    }
}

private const val webContent = "<html><head></head><body><p>Hej Svend</p></body></html>"

const val FALLBACK_CSS =
    "const isDarkMode = window.matchMedia && ('(prefers-color-scheme: dark)').matches; console.log(isDarkMode);var themeStyles = '@media (prefers-color-scheme: dark){ body { color: #FFFF00;    background: #0000FF; } }';var cssTheme = document.createElement('style');cssTheme.innerText = themeStyles;document.head.appendChild(cssTheme);"

