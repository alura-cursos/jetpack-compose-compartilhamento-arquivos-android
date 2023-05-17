package com.alura.concord.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alura.concord.R
import com.alura.concord.data.DownloadStatus
import com.alura.concord.data.DownloadableContent
import com.alura.concord.data.Message
import com.alura.concord.network.formatFileSize

@Composable
fun MessageItemUser(message: Message) {
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
    message: Message,
    onContentDownload: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
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
                message.downloadableContent?.let { contentFile ->
                    Box(
                        modifier = Modifier
                            .size(200.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        DownloadButton(
                            status = contentFile.status,
                            fileSize = formatFileSize(contentFile.size),
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
                    Icons.Default.ArrowDropDown,
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
    MessageItemUser(Message())
}

@Preview
@Composable
fun MessageItemOtherPreview() {
    MessageItemOther(
        Message(
            idDownloadableContent = 1,
            downloadableContent = DownloadableContent(
                1,
                "file",
                123456
            )
        )
    )
}
