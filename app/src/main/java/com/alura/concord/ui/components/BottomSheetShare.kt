package com.alura.concord.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alura.concord.R
import com.alura.concord.ui.theme.ConcordTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomShareSheet(
    onOpenWith: () -> Unit = {},
    onShare: () -> Unit = {},
    onSave: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val modalSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = modalSheetState,
        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        content = {
            BottomSheetShare(
                onOpenWith = {
                    onOpenWith()
                    onBack()
                },
                onShare = {
                    onShare()
                    onBack()
                },
                onSave = {
                    onSave()
                    onBack()
                }
            )
        },
        onDismissRequest = {
            onBack()
        },
    )
}

@Composable
fun BottomSheetShare(
    onOpenWith: () -> Unit = {},
    onShare: () -> Unit = {},
    onSave: () -> Unit = {},
) {
    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onOpenWith() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painterResource(id = R.drawable.ic_open_with),
                null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Abrir com",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )

        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onShare() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.Share,
                null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Compartilhar",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onSave() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painterResource(id = R.drawable.ic_save),
                null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Salvar como",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
fun BottomBottomSheetSharePreview() {
    ConcordTheme {
        BottomSheetShare()
    }
}