package com.markduenas.markdownviewer.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownCodeFence
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import org.intellij.markdown.ast.ASTNode

@Composable
fun MarkdownContent(
    content: String,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = Alignment.TopCenter
    ) {
        // Constrain width for readability (max ~800dp)
        Box(
            modifier = Modifier
                .widthIn(max = 800.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Markdown(
                content = content,
                colors = markdownColor(),
                typography = markdownTypography(),
                imageTransformer = Coil3ImageTransformerImpl,
                components = markdownComponents(
                    codeFence = { codeFenceData ->
                        val language = codeFenceData.node.findChildOfType(
                            org.intellij.markdown.MarkdownTokenTypes.FENCE_LANG
                        )?.let { langNode ->
                            codeFenceData.content.substring(langNode.startOffset, langNode.endOffset)
                        }?.trim()?.lowercase()

                        if (language == "mermaid") {
                            // Extract the code content (without the fence markers)
                            val codeContent = extractCodeContent(codeFenceData.node, codeFenceData.content)
                            MermaidDiagram(code = codeContent)
                        } else {
                            // Use default code fence rendering
                            MarkdownCodeFence(
                                content = codeFenceData.content,
                                node = codeFenceData.node
                            )
                        }
                    }
                )
            )
        }
    }
}

/**
 * Extract the actual code content from a code fence node.
 */
private fun extractCodeContent(node: ASTNode, content: String): String {
    // Find the CODE_FENCE_CONTENT children
    val codeLines = mutableListOf<String>()

    node.children.forEach { child ->
        if (child.type.toString() == "CODE_FENCE_CONTENT") {
            codeLines.add(content.substring(child.startOffset, child.endOffset))
        }
    }

    return if (codeLines.isNotEmpty()) {
        codeLines.joinToString("\n")
    } else {
        // Fallback: try to extract content between fence markers
        val lines = content.substring(node.startOffset, node.endOffset).lines()
        if (lines.size > 2) {
            lines.drop(1).dropLast(1).joinToString("\n")
        } else {
            ""
        }
    }
}

/**
 * Extension to find a child node of a specific type.
 */
private fun ASTNode.findChildOfType(type: org.intellij.markdown.IElementType): ASTNode? {
    return children.firstOrNull { it.type == type }
}
