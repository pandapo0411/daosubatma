package com.vibecode.daosubatma.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.vibecode.daosubatma.core.model.GhostDefinition
import com.vibecode.daosubatma.core.model.localizedName

@Composable
fun GhostArtwork(ghost: GhostDefinition, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val resourceId = remember(ghost.assetName) {
        context.resources.getIdentifier(ghost.assetName, "drawable", context.packageName)
    }
    if (resourceId != 0) {
        Image(
            painter = painterResource(resourceId),
            contentDescription = ghost.localizedName(context),
            modifier = modifier,
        )
    }
}
