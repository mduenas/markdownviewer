package com.markduenas.markdownviewer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import markdownviewer.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun FullScreenMermaidViewer(
    code: String,
    onDismiss: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    var htmlContent by remember { mutableStateOf<String?>(null) }

    // Load and populate fullscreen template (enables native zoom/scroll)
    LaunchedEffect(code, isDarkTheme) {
        try {
            val template = Res.readBytes("files/mermaid_fullscreen_template.html").decodeToString()
            val themeClass = if (isDarkTheme) "dark" else "light"
            val mermaidTheme = if (isDarkTheme) "dark" else "default"

            htmlContent = template
                .replace("{{THEME}}", themeClass)
                .replace("{{MERMAID_THEME}}", mermaidTheme)
                .replace("{{MERMAID_CODE}}", code.trim())
        } catch (e: Exception) {
            htmlContent = """
                <!DOCTYPE html>
                <html><body style="color: red; font-family: monospace; padding: 16px;">
                Error loading Mermaid template: ${e.message}
                </body></html>
            """.trimIndent()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mermaid Diagram") },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors()
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                htmlContent?.let { html ->
                    val webViewState = rememberWebViewStateWithHTMLData(
                        data = html,
                        baseUrl = "https://localhost"
                    )

                    WebView(
                        state = webViewState,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Hint text
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                            MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Pinch to zoom",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
