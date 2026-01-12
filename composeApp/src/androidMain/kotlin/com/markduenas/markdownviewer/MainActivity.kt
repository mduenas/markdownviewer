package com.markduenas.markdownviewer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import java.io.BufferedReader

object IntentContentHolder {
    var initialContent: String? = null
    var initialFileName: String? = null
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Set app version from package info
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            AppVersionHolder.versionName = packageInfo.versionName ?: "1.0"
        } catch (e: Exception) {
            AppVersionHolder.versionName = "1.0"
        }

        // Handle incoming intent
        handleIntent(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return

        when (intent.action) {
            Intent.ACTION_VIEW -> {
                intent.data?.let { uri ->
                    loadContentFromUri(uri)
                }
            }
            Intent.ACTION_SEND -> {
                if (intent.type?.startsWith("text/") == true) {
                    intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)?.let { uri ->
                        loadContentFromUri(uri)
                    } ?: run {
                        // Plain text share
                        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { text ->
                            IntentContentHolder.initialContent = text
                            IntentContentHolder.initialFileName = "Shared Text"
                        }
                    }
                }
            }
        }
    }

    private fun loadContentFromUri(uri: Uri) {
        try {
            val content = contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.bufferedReader().use(BufferedReader::readText)
            }
            if (content != null) {
                IntentContentHolder.initialContent = content
                IntentContentHolder.initialFileName = uri.lastPathSegment ?: "Unknown"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}