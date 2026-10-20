package com.vibecode.daosubatma.feature.hunt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vibecode.daosubatma.R
import com.vibecode.daosubatma.core.model.TalismanType
import com.vibecode.daosubatma.ui.Gold
import com.vibecode.daosubatma.ui.Ink
import com.vibecode.daosubatma.ui.Mist
import com.vibecode.daosubatma.ui.Paper

@Composable
fun TalismanPickerDialog(
    inventory: Map<TalismanType, Int>,
    onDismiss: () -> Unit,
    onSelected: (TalismanType) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            Modifier.fillMaxWidth().background(Ink, RoundedCornerShape(22.dp)).border(2.dp, Gold, RoundedCornerShape(22.dp)).padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(stringResource(R.string.choose_talisman), color = Gold, fontSize = 21.sp, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.choose_talisman_hint), color = Mist, fontSize = 12.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(14.dp))
            TalismanType.entries.chunked(2).forEach { row ->
                Row(Modifier.fillMaxWidth()) {
                    row.forEach { talisman ->
                        TalismanPickerCard(talisman, inventory[talisman] ?: 0, Modifier.weight(1f)) {
                            onSelected(talisman)
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
            Text(stringResource(R.string.cancel), color = Paper, modifier = Modifier.clickable(onClick = onDismiss).padding(8.dp))
        }
    }
}

@Composable
private fun TalismanPickerCard(talisman: TalismanType, amount: Int, modifier: Modifier, onClick: () -> Unit) {
    val available = talisman.isUnlimited || amount > 0
    Column(
        modifier.padding(horizontal = 5.dp).background(Ink.copy(alpha = .72f), RoundedCornerShape(12.dp)).border(1.dp, if (available) Mist else Mist.copy(alpha = .25f), RoundedCornerShape(12.dp)).alpha(if (available) 1f else .38f).clickable(enabled = available, onClick = onClick).padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(talisman.artworkRes),
            contentDescription = stringResource(talisman.nameRes),
            modifier = Modifier.height(78.dp).size(width = 42.dp, height = 78.dp),
        )
        Text(stringResource(talisman.nameRes), color = Paper, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(stringResource(talisman.detailRes), color = Mist, fontSize = 9.sp, textAlign = TextAlign.Center, lineHeight = 11.sp)
        Text(if (talisman.isUnlimited) stringResource(R.string.unlimited) else stringResource(R.string.talisman_amount, amount), color = Gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
