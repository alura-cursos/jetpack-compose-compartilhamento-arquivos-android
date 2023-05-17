package com.alura.concord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.alura.concord.extensions.showLog
import com.alura.concord.navigation.ConcordNavHost
import com.alura.concord.network.fileInGB
import com.alura.concord.network.formatFileSize
import com.alura.concord.network.gitHubgetFileSizeInKB
import com.alura.concord.ui.theme.ConcordTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(IO).launch {
            gitHubgetFileSizeInKB(fileInGB) {
                it?.let { tamanhoEmkb ->
                    showLog("Tamanho arquivo ${formatFileSize(tamanhoEmkb)}")
                } ?: run {
                    showLog("Erro ao obter tamanho do arquivo")
                }
            }
        }

        setContent {
            ConcordTheme {
                val navController = rememberNavController()
                ConcordNavHost(navController = navController)
            }
        }
    }

}


