package com.alura.concord.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alura.concord.R
import com.alura.concord.data.Message

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
                    Modifier.fillMaxWidth()
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
fun MessageItemOther(message: Message) {
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
                    Modifier.fillMaxWidth()
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

@Preview
@Composable
fun MessageItemUserPreview() {
    MessageItemUser(Message())
}