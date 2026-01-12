package com.markduenas.markdownviewer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.multiplatform.webview.web.WebView
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
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

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
                    actions = {
                        // Zoom controls
                        FilledTonalIconButton(
                            onClick = { scale = (scale - 0.25f).coerceAtLeast(0.5f) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Zoom out"
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        FilledTonalIconButton(
                            onClick = {
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = "Reset zoom"
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        FilledTonalIconButton(
                            onClick = { scale = (scale + 0.25f).coerceAtMost(3f) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Zoom in"
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
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
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.5f, 3f)
                            offsetX += pan.x
                            offsetY += pan.y
                        }
                    }
            ) {
                htmlContent?.let { html ->
                    val webViewState = rememberWebViewStateWithHTMLData(
                        data = html,
                        baseUrl = "https://localhost"
                    )

                    WebView(
                        state = webViewState,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offsetX,
                                translationY = offsetY
                            )
                    )
                }

                // Zoom level indicator
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
                        text = "${(scale * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
