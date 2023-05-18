package com.alura.concord.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alura.concord.R
import com.alura.concord.data.DownloadStatus
import com.alura.concord.data.DownloadableFile
import com.alura.concord.data.MessageWithFile
import com.alura.concord.media.formatReadableFileSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MessageItemUser(message: MessageWithFile) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Row(Modifier.padding(start = 50.dp)) {
            val hasImage = message.mediaLink.isNotEmpty()
            val hasText = message.content.isNotEmpty()
            val intrinsicSizeLayout = if (hasImage) {
                IntrinsicSize.Min
            } else {
                IntrinsicSize.Max
            }

            Column(
                Modifier
                    .shadow(1.dp, shape = RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp))
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp),
                    )
                    .padding(4.dp)
                    .width(intrinsicSizeLayout)
            ) {
                if (hasImage) {
                    AsyncImage(
                        modifier = Modifier
                            .widthIn(
                                min = 200.dp,
                                max = 300.dp
                            )
                            .padding(2.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        imageUrl = message.mediaLink,
                        contentScale = ContentScale.FillWidth
                    )
                }
                if (hasText) {
                    Text(
                        text = message.content,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                val verticalDatePadding = if (hasText) 0.dp else 8.dp

                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 4.dp)
                        .padding(verticalDatePadding)
                        .offset(y = (-4).dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.date,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.width(2.dp))
                    Icon(
                        painterResource(id = R.drawable.ic_action_all_done),
                        stringResource(R.string.message_status),
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageItemOther(
    message: MessageWithFile,
    modifier: Modifier = Modifier,
    onContentDownload: () -> Unit = {},
    onShowFileOptions: (MessageWithFile) -> Unit = {},
) {
    var isSelected by remember { mutableStateOf(false) }

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.3f) else Color.Transparent,
        animationSpec = tween(durationMillis = 600)
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .background(containerColor)
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onShowFileOptions(message)
                        scope.launch {
                            isSelected = true
                            delay(800)
                            isSelected = false
                        }
                    },
                )
            },
        horizontalAlignment = Alignment.Start
    ) {
        Row(Modifier.padding(end = 50.dp)) {
            val hasImage = message.mediaLink.isNotEmpty()
            val hasText = message.content.isNotEmpty()
            val intrinsicSizeLayout = if (hasImage) {
                IntrinsicSize.Min
            } else {
                IntrinsicSize.Max
            }

            Column(
                Modifier
                    .shadow(1.dp, shape = RoundedCornerShape(0.dp, 10.dp, 10.dp, 10.dp))
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(0.dp, 10.dp, 10.dp, 10.dp),
                    )
                    .padding(4.dp)
                    .width(intrinsicSizeLayout)
            ) {
                message.downloadableFile?.let { contentFile ->
                    Box(
                        modifier = Modifier
                            .size(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DownloadButton(
                            status = contentFile.status,
                            fileSize = contentFile.size.formatReadableFileSize(),
                            onClickDownload = {
                                onContentDownload()
                            }
                        )
                    }
                }

                if (hasImage) {
                    AsyncImage(
                        modifier = Modifier
                            .widthIn(
                                min = 200.dp,
                                max = 300.dp
                            )
                            .padding(2.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        imageUrl = message.mediaLink,
                        contentScale = ContentScale.FillWidth
                    )
                }
                if (hasText) {
                    Text(
                        text = message.content,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                val verticalDatePadding = if (hasText) 0.dp else 8.dp

                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 4.dp)
                        .padding(verticalDatePadding)
                        .offset(y = (-4).dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.date,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.width(2.dp))
                }
            }
        }
    }
}

@Composable
fun DownloadButton(
    fileSize: String,
    modifier: Modifier = Modifier,
    onClickDownload: () -> Unit = {},
    status: DownloadStatus
) {
    Row(modifier = modifier
        .clickable { onClickDownload() }
        .padding(12.dp)) {
        Row(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
                .shadow(1.dp, shape = CircleShape)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (status == DownloadStatus.DOWNLOADING) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(4.dp),
                    color = Color.White
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_download),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = fileSize,
                    color = Color.White
                )
            }
        }
    }
}


@Preview
@Composable
fun MessageItemUserPreview() {
    MessageItemUser(MessageWithFile())
}

@Preview
@Composable
fun MessageItemOtherPreview() {
    MessageItemOther(
        MessageWithFile(
            idDownloadableFile = 1,
            downloadableFile = DownloadableFile(
                status = DownloadStatus.DOWNLOADING,
                name = "Arquivo teste.pdf",
                url = "url.teste",
                size = 123456,
            )
        ),
    )
}
