package dk.isten.andro.mywebviewapplication

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.FORCE_DARK_ON
import androidx.webkit.WebSettingsCompat.ForceDark
import androidx.webkit.WebViewFeature
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.google.accompanist.web.rememberWebViewStateWithHTMLData
import dk.isten.andro.mywebviewapplication.ui.theme.MyWebViewApplicationTheme

class MainActivity : ComponentActivity() {
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
                    Greeting("Android")
                    MyWeb()
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
//            view?.evaluateJavascript(
//                FALLBACK_CSS,
//                null
//            )
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            view?.evaluateJavascript(
                FALLBACK_CSS,
                null
            )
        }
    }

    val darkTheme = isSystemInDarkTheme()
    WebView(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        state = state,
        onCreated = { view ->

            view.clearCache(true)
            view.settings.run {
                javaScriptEnabled = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                Log.d("Svend ", "$darkTheme")
                if(WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
                    WebSettingsCompat.setAlgorithmicDarkeningAllowed(this, true);
                }
            }
        },
        captureBackPresses = false,
        client = webClient,
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
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
    "const isDarkMode = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches; console.log(isDarkMode);var themeStyles = '@media (prefers-color-scheme: dark){ body { color: yellow; } }';var cssTheme = document.createElement('style');cssTheme.innerText = themeStyles;document.head.appendChild(cssTheme);"

