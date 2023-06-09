package com.alura.concord.ui.chat

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alura.concord.R
import com.alura.concord.database.entities.Author
import com.alura.concord.data.MessageWithFile
import com.alura.concord.data.messageEntityListSamples
import com.alura.concord.database.entities.toMessageFile
import com.alura.concord.ui.components.*

@Composable
fun MessageScreen(
    state: MessageListUiState,
    modifier: Modifier = Modifier,
    onSendMessage: () -> Unit = {},
    onShowSelectorFile: () -> Unit = {},
    onShowSelectorStickers: () -> Unit = {},
    onDeselectMedia: () -> Unit = {},
    onBack: () -> Unit = {},
    onContentDownload: (MessageWithFile) -> Unit = {},
    onShowFileOptions: (MessageWithFile) -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppBarChatScreen(
                state = state,
                onBackClick = onBack
            )
        }) { paddingValues ->
        Column(
            modifier
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                .padding(paddingValues)
        ) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .weight(8f), reverseLayout = true
            ) {
                items(state.messages.reversed(), contentType = { it.author }) { message ->

                    when (message.author) {
                        Author.OTHER -> {
                            MessageItemOther(
                                message = message,
                                onContentDownload = {
                                    onContentDownload(message)
                                },
                                onShowFileOptions = {
                                    onShowFileOptions(message)
                                },
                            )
                        }

                        Author.USER -> {
                            MessageItemUser(message)
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            if (state.mediaInSelection.isNotEmpty()) {
                SelectedMediaContainer(state, onDeselectMedia)
            }

            EntryTextBar(
                state = state,
                onShowSelectorFile = onShowSelectorFile,
                onClickSendMessage = onSendMessage,
                onAcessSticker = onShowSelectorStickers
            )
            Spacer(Modifier.height(2.dp))
        }
    }
}

@Composable
private fun SelectedMediaContainer(
    state: MessageListUiState,
    onDeselectMedia: () -> Unit,
) {
    Divider(
        Modifier
            .height(0.4.dp)
            .alpha(0.5f), color = MaterialTheme.colorScheme.outline
    )
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimaryContainer),
    ) {
        AsyncImage(
            modifier = Modifier
                .size(150.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(5)),
            imageUrl = state.mediaInSelection
        )
        IconButton(
            onClick = {
                onDeselectMedia()
            },
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopEnd)
                .alpha(0.5f)
                .clickable {}
                .shadow(1.dp, RoundedCornerShape(100))
                .background(
                    Color.Black,
                    CircleShape
                )
                .size(22.dp),
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = stringResource(R.string.icon_message_or_mic),
                modifier = Modifier.padding(4.dp),
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarChatScreen(
    state: MessageListUiState, onBackClick: () -> Unit = {}
) {
    TopAppBar(
        navigationIcon = {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable {
                        onBackClick()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = null
                )

                AsyncImageProfile(
                    imageUrl = state.profilePicOwner,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(32.dp)
                        .clip(CircleShape),
                )
            }
        },
        title = {
            Text(text = state.ownerName, fontWeight = FontWeight.Medium)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        )
    )
}

@Composable
private fun EntryTextBar(
    state: MessageListUiState,
    onShowSelectorFile: () -> Unit = {},
    onClickSendMessage: () -> Unit = {},
    onAcessSticker: () -> Unit = {},
) {
    val barHeight = 56.dp
    val hasContentToSend = state.hasContentToSend

    Row(
        modifier = Modifier
            .padding(4.dp)
            .height(barHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(100))
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(100)
                )
                .weight(5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onAcessSticker) {
                Icon(
                    painterResource(id = R.drawable.ic_action_sticker),
                    "file",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .weight(1f)
                )
            }

            BasicTextField(
                value = state.messageValue,
                onValueChange = state.onMessageValueChange,
                modifier = Modifier.weight(5F),
                textStyle = TextStyle.Default.copy(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                decorationBox = { innerValue ->
                    Box {
                        if (state.messageValue.isEmpty()) {
                            Text(
                                stringResource(R.string.message),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        innerValue()
                    }
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.surfaceVariant),
            )

            IconButton(
                onClick = onShowSelectorFile,
                modifier = Modifier
                    .weight(1f)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_action_files),
                    "file",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.rotate(-45f)
                )
            }

            Spacer(Modifier.width(6.dp))

            IconButton(
                onClick = onShowSelectorFile,
                modifier = Modifier
                    .weight(1f)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_action_cam),
                    "file",
                    tint = MaterialTheme.colorScheme.outline,
                )
            }
            Spacer(Modifier.width(6.dp))
        }

        Spacer(Modifier.width(6.dp))

        IconButton(
            onClick = {
                if (hasContentToSend) {
                    onClickSendMessage()
                }
            },
            modifier = Modifier
                .clickable {}
                .shadow(1.dp, RoundedCornerShape(100))
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(100)
                )
        ) {
            val micIcon = painterResource(id = R.drawable.ic_action_mic)
            val sendIcon = painterResource(id = R.drawable.ic_action_send)

            val transition = updateTransition(hasContentToSend, label = "Crossfade transition")
            val iconSize by transition.animateDp(label = "Icon size") { if (it) 24.dp else 24.dp }
            val iconAlpha by transition.animateFloat(label = "Icon alpha") { if (it) 1f else 1f }

            Crossfade(targetState = hasContentToSend) { hasContent ->
                Icon(
                    if (hasContent) sendIcon else micIcon,
                    stringResource(R.string.icon_message_or_mic),
                    tint = Color.White,
                    modifier = Modifier
                        .size(iconSize)
                        .alpha(iconAlpha)
                )
            }
        }
    }

}

@Preview
@Composable
fun ChatScreenPreview() {
    MessageScreen(
        MessageListUiState(
            ownerName = "Alberto",
            messages = messageEntityListSamples.map { it.toMessageFile() },
        )
    )
}