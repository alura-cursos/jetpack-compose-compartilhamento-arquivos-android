

![Jetpack Compose: Compartilandho-Arquivos de Arquivos Android](https://github.com/alura-cursos/jetpack-compose-compartilhamento-arquivos-android/assets/35709152/c8d68da5-3ea8-4d3b-9636-16d8354582de)


Projeto continuação para o curso de arquivos no Android com Jetpack Compose e tópicos como download, criação, cópia, exclusão e compartilhamento de arquivos. Também exploraremos tecnologias como ShareSheet, FileProvider, SAF e Permissões de escrita em diferentes versões do Android.



## :hammer: Funcionalidades do projeto
Uma extensão do [App base](https://github.com/alura-cursos/jetpack-compose-armazenamento-arquivos-android/tree/main), com melhorias de código e adição das funcionalidades:
- Download de arquivos
- Compartilhamento e envio de arquivos por meio do Android Sharesheet
- Salvamento de arquivos no armazenamento geral do dispositivo



https://github.com/alura-cursos/jetpack-compose-compartilhamento-arquivos-android/assets/35709152/b3de8169-a633-4588-aa4c-6f3bfc1a325f



Para simular um aplicativo de troca de mensagens, o Concord, oferece as seguintes telas:

- `Aba inicial`: Exibie uma lista contatos fictícia, cada contato pode ser clicado para abrir o histórico de conversas.
- `Converas (Chat)`: Mostra o histórico de mensagens trocadas, como nosso app não está conectado a uma Web API, é possível apenas fazer o envio delas no momento.
- `Figurinhas (Sticker List)`: Usa o componente [ModalBottomSheet][modalbottomsheet-link] para exibir uma lista de imagens do armazenamento específico ou compartilhado.
- `Seletor de fotos (Photo Picker)`: Usa a UI nativa do Android para exibir imagens salvas no dispositivo.
- `Seletor de arquivos (File Picker)`: Usa a UI nativa do Android para exibir todos arquivos salvos no dispositivo.



## 🏠 Arquitetura
* Navigation
* Room Database
* Kotlin Coroutines e Flow
* ViewModel com StateFlow
* Hilt (injeção de dependência)

## ✔️ Outras técnicas e tecnologias utilizadas
* [Permissões de execução][permissao-execucao]
* [Android Sharesheet][sharesheet]
* [FileProvider][fileprovider]
* [Photo Picker][photopicker]
* [MediaStore][mediastore]
* [Storage Access Framework (SAF)][SAF]
* Kotlin
* Jetpack Compose
* Compose BOM
* [Coil][coil]
* [LocalDate][localdate] do Java 8+, compatível com versões abaixo do Android 8 graças ao [desugaring support][jdk8desugar]


## 📂 Acesso ao projeto
- Versão inicial: Veja o [código fonte][codigo-inicial] ou [baixe o projeto][download-inicial]
- Versão final: Veja o [código fonte][codigo-final] ou [baixe o projeto][download-final]
- [Arquivos de mídia para testes][arquivos]

## 🛠️ Abrir e rodar o projeto
Após baixar o projeto, você pode abri-lo com o Android Studio. Para isso, na tela de launcher clique em:

“Open” (ou alguma opção similar), procure o local onde o projeto está e o selecione (caso o projeto seja baixado via zip, é necessário extraí-lo antes de procurá-lo). Por fim, clique em “OK” o Android Studio deve executar algumas tasks do Gradle para configurar o projeto, aguarde até finalizar. Ao finalizar as tasks, você pode executar o App 🏆


## 📚 Mais informações do curso
Gostou do conteúdo e quer saber mais detalhes? Então [confira o curso na página da Alura](https://www.alura.com.br/curso-online-jetpack-compose-baixando-e-compartilhando-arquivos-android) 😉


[modalbottomsheet-link]: https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ModalBottomSheet(kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.material3.SheetState,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Color,kotlin.Function0,kotlin.Function1)
[photopicker]: https://developer.android.com/training/data-storage/shared/photopicker
[mediastore]: https://developer.android.com/training/data-storage/shared/media?hl=pt-br#query-collection
[SAF]: https://developer.android.com/guide/topics/providers/document-provider
[arquivos]: https://github.com/alura-cursos/jetpack-compose-armazenamento-arquivos-android/tree/arquivos
                                                                                                                                                                                 
[localdate]: https://developer.android.com/reference/java/time/LocalDate
[jdk8desugar]: https://developer.android.com/studio/write/java8-support#library-desugaring
[coil]: https://coil-kt.github.io/coil/

[permissao-execucao]: https://developer.android.com/training/permissions/requesting
[sharesheet]: https://developer.android.com/training/sharing/send
[fileprovider]: https://developer.android.com/reference/androidx/core/content/FileProvider

[codigo-inicial]: https://github.com/alura-cursos/jetpack-compose-compartilhamento-arquivos-android/commits/projeto-inicial
[download-inicial]: https://github.com/alura-cursos/jetpack-compose-compartilhamento-arquivos-android/archive/refs/heads/projeto-inicial.zip
[codigo-final]: https://github.com/alura-cursos/jetpack-compose-compartilhamento-arquivos-android/commits/aula-5
[download-final]: https://github.com/alura-cursos/jetpack-compose-compartilhamento-arquivos-android/archive/refs/heads/aula-5.zip




