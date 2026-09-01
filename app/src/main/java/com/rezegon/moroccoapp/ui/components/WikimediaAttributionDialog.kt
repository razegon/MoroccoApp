package com.rezegon.moroccoapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.rezegon.moroccoapp.domain.model.WikimediaImage

@Composable
fun WikimediaAttributionDialog(
    image: WikimediaImage,
    onDismiss: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Informacje o zdjęciu")
        },

        text = {
            Column {

                image.author?.let {
                    Text("Autor: $it")
                }

                image.license?.let { license ->

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    image.licenseUrl?.let { url ->

                        Text(
                            text = buildAnnotatedString {
                                append("Licencja: ")

                                withLink(
                                    LinkAnnotation.Url(
                                        url = url,
                                        linkInteractionListener = {
                                            uriHandler.openUri(url)
                                        }
                                    )
                                ) {
                                    append(license)
                                }
                            }
                        )

                    } ?: Text(
                        text = "Licencja: $license"
                    )
                }

                image.credit?.let {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Credit: $it"
                    )
                }

                image.filePageUrl?.let { url ->

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = buildAnnotatedString {

                            withLink(
                                LinkAnnotation.Url(
                                    url = url,
                                    linkInteractionListener = {
                                        uriHandler.openUri(url)
                                    }
                                )
                            ) {
                                append("Źródło: Wikimedia Commons")
                            }
                        }
                    )
                }
            }
        },

        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Zamknij")
            }
        }
    )
}