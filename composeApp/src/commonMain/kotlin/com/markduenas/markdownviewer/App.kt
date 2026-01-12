package com.markduenas.markdownviewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.markduenas.markdownviewer.data.RecentItemsRepository
import com.markduenas.markdownviewer.data.UrlFetcher
import com.markduenas.markdownviewer.data.rememberKeyValueStorage
import com.markduenas.markdownviewer.model.ContentSource
import com.markduenas.markdownviewer.model.RecentItem
import com.markduenas.markdownviewer.model.RecentItemType
import com.markduenas.markdownviewer.model.ViewerState
import com.markduenas.markdownviewer.model.currentTimeMillis
import com.markduenas.markdownviewer.ui.components.EmptyState
import com.markduenas.markdownviewer.ui.components.ErrorDisplay
import com.markduenas.markdownviewer.ui.components.LoadingIndicator
import com.markduenas.markdownviewer.ui.components.MarkdownContent
import com.markduenas.markdownviewer.ui.dialogs.AboutDialog
import com.markduenas.markdownviewer.ui.dialogs.RecentFilesDialog
import com.markduenas.markdownviewer.ui.dialogs.UrlInputDialog
import com.markduenas.markdownviewer.ui.theme.MarkdownViewerTheme
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.readString
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MarkdownViewerTheme {
        var viewerState by remember { mutableStateOf(ViewerState()) }
        var showMenu by remember { mutableStateOf(false) }
        var showUrlDialog by remember { mutableStateOf(false) }
        var showRecentDialog by remember { mutableStateOf(false) }
        var showAboutDialog by remember { mutableStateOf(false) }
        var recentItems by remember { mutableStateOf<List<RecentItem>>(emptyList()) }

        val coroutineScope = rememberCoroutineScope()
        val urlFetcher = remember { UrlFetcher() }

        // Check for initial content from intent/document open
        LaunchedEffect(Unit) {
            getInitialContent()?.let { initial ->
                viewerState = ViewerState(
                    source = ContentSource.LocalFile(
                        path = initial.fileName,
                        name = initial.fileName
                    ),
                    content = initial.content
                )
                clearInitialContent()
            }
        }

        // Recent items storage
        val storage = rememberKeyValueStorage()
        val recentRepository = remember(storage) { RecentItemsRepository(storage) }

        // Helper function to add item to recent and update state
        fun addToRecent(source: ContentSource) {
            val item = RecentItem.fromSource(source)
            recentRepository.addRecentItem(item)
        }

        // Helper function to load URL
        fun loadUrl(url: String) {
            viewerState = ViewerState(
                source = ContentSource.RemoteUrl(url),
                isLoading = true
            )
            coroutineScope.launch {
                urlFetcher.fetch(url)
                    .onSuccess { content ->
                        val source = ContentSource.RemoteUrl(url)
                        viewerState = ViewerState(
                            source = source,
                            content = content,
                            isLoading = false
                        )
                        addToRecent(source)
                    }
                    .onFailure { error ->
                        viewerState = ViewerState(
                            source = ContentSource.RemoteUrl(url),
                            isLoading = false,
                            error = error.message ?: "Failed to load URL"
                        )
                    }
            }
        }

        // File picker launcher
        val filePickerLauncher = rememberFilePickerLauncher(
            type = FileKitType.File(extensions = listOf("md", "markdown")),
            mode = FileKitMode.Single
        ) { file: PlatformFile? ->
            file?.let {
                coroutineScope.launch {
                    val content = it.readString()
                    val source = ContentSource.LocalFile(
                        path = it.path ?: "",
                        name = it.name
                    )
                    viewerState = ViewerState(
                        source = source,
                        content = content
                    )
                    addToRecent(source)
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(viewerState.title) },
                    colors = TopAppBarDefaults.topAppBarColors(),
                    actions = {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu"
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Open File") },
                                onClick = {
                                    showMenu = false
                                    filePickerLauncher.launch()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Open URL") },
                                onClick = {
                                    showMenu = false
                                    showUrlDialog = true
                                }
                            )
                            if (viewerState.hasContent) {
                                DropdownMenuItem(
                                    text = { Text("Close") },
                                    onClick = {
                                        showMenu = false
                                        viewerState = ViewerState()
                                    }
                                )
                            }
                            DropdownMenuItem(
                                text = { Text("Recent") },
                                onClick = {
                                    showMenu = false
                                    recentItems = recentRepository.getRecentItems()
                                    showRecentDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("About") },
                                onClick = {
                                    showMenu = false
                                    showAboutDialog = true
                                }
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    viewerState.isLoading -> {
                        LoadingIndicator()
                    }
                    viewerState.error != null -> {
                        ErrorDisplay(
                            error = viewerState.error!!,
                            onRetry = {
                                val source = viewerState.source
                                if (source is ContentSource.RemoteUrl) {
                                    loadUrl(source.url)
                                }
                            },
                            onDismiss = {
                                viewerState = ViewerState()
                            }
                        )
                    }
                    viewerState.hasContent -> {
                        MarkdownContent(
                            content = viewerState.content,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        EmptyState(
                            onOpenFile = { filePickerLauncher.launch() },
                            onOpenUrl = { showUrlDialog = true }
                        )
                    }
                }
            }
        }

        // URL input dialog
        if (showUrlDialog) {
            UrlInputDialog(
                onDismiss = { showUrlDialog = false },
                onUrlSubmit = { url ->
                    showUrlDialog = false
                    loadUrl(url)
                }
            )
        }

        // Recent files dialog
        if (showRecentDialog) {
            RecentFilesDialog(
                recentItems = recentItems,
                onItemClick = { item ->
                    showRecentDialog = false
                    when (item.type) {
                        RecentItemType.URL -> {
                            loadUrl(item.path)
                        }
                        RecentItemType.FILE -> {
                            // For files, we can't re-read them without the file picker
                            // due to platform security restrictions.
                            // We'll show a message or try to open via the file picker
                            // For now, just update the recent timestamp
                            recentRepository.addRecentItem(
                                item.copy(lastOpened = currentTimeMillis())
                            )
                            // Show message that file needs to be re-opened
                            viewerState = ViewerState(
                                error = "Please use 'Open File' to re-open local files due to security restrictions."
                            )
                        }
                    }
                },
                onItemRemove = { item ->
                    recentRepository.removeRecentItem(item.path)
                    recentItems = recentRepository.getRecentItems()
                },
                onClearAll = {
                    recentRepository.clearRecentItems()
                    recentItems = emptyList()
                },
                onDismiss = { showRecentDialog = false }
            )
        }

        // About dialog
        if (showAboutDialog) {
            AboutDialog(
                onDismiss = { showAboutDialog = false }
            )
        }
    }
}
