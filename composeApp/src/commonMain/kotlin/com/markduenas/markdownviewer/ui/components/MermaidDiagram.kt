package com.markduenas.markdownviewer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import markdownviewer.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MermaidDiagram(
    code: String,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    var htmlContent by remember { mutableStateOf<String?>(null) }

    // Load and populate template
    LaunchedEffect(code, isDarkTheme) {
        try {
            val template = Res.readBytes("files/mermaid_template.html").decodeToString()
            val themeClass = if (isDarkTheme) "dark" else "light"
            val mermaidTheme = if (isDarkTheme) "dark" else "default"

            htmlContent = template
                .replace("{{THEME}}", themeClass)
                .replace("{{MERMAID_THEME}}", mermaidTheme)
                .replace("{{MERMAID_CODE}}", code.trim())
        } catch (e: Exception) {
            // If template loading fails, show error
            htmlContent = """
                <!DOCTYPE html>
                <html><body style="color: red; font-family: monospace; padding: 16px;">
                Error loading Mermaid template: ${e.message}
                </body></html>
            """.trimIndent()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        htmlContent?.let { html ->
            val webViewState = rememberWebViewStateWithHTMLData(
                data = html,
                baseUrl = "https://localhost"
            )

            WebView(
                state = webViewState,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp, max = 500.dp)
            )
        }
    }
}
