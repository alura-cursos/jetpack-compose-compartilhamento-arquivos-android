package com.alura.concord.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.alura.concord.R
import com.alura.concord.ui.theme.ConcordTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetFile(
    onSelectPhoto: () -> Unit = {},
    onSelectFile: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val modalSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = modalSheetState,
        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        content = {
            BottomSheetFiles(
                onSelectPhoto = {
                    onSelectPhoto()
                },
                onSelectFile = {
                    onSelectFile()
                }
            )

        },
        onDismissRequest = {
            onBack()
        },
    )
}

@Composable
fun BottomSheetFiles(
    onSelectPhoto: () -> Unit = {},
    onSelectFile: () -> Unit = {},
) {
    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tipo de arquivo",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = onSelectPhoto,
                    Modifier.background(
                        color = Color("#7F66FE".toColorInt()),
                        shape = CircleShape
                    )
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_action_gallery),
                        null,
                        tint = Color.White
                    )
                }
                Text(
                    text = "Foto", color = MaterialTheme.colorScheme.onBackground
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { },
                    Modifier.background(
                        color = Color("#FFFC2E74".toColorInt()),
                        shape = CircleShape
                    )
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_action_cam),
                        null,
                        tint = Color.White
                    )
                }
                Text(text = "Câmera", color = MaterialTheme.colorScheme.onBackground)
            }

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = onSelectFile,
                    Modifier.background(color = Color("#F86633".toColorInt()), shape = CircleShape)
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_action_folder),
                        null,
                        tint = Color.White,
                    )
                }
                Text(text = "Ver tudo", color = MaterialTheme.colorScheme.onBackground)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
fun BottomSheetFilesPreview() {
    ConcordTheme {
        BottomSheetFiles()
    }
}