package com.vibecode.daosubatma.feature.hunt

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.vibecode.daosubatma.R
import com.vibecode.daosubatma.core.audio.GameAudio
import com.vibecode.daosubatma.core.audio.SoundEffect
import com.vibecode.daosubatma.core.model.TalismanType
import com.vibecode.daosubatma.core.model.GhostCatalog
import com.vibecode.daosubatma.core.model.GhostDefinition
import com.vibecode.daosubatma.core.model.localizedName
import com.vibecode.daosubatma.core.model.TaoistProgress
import com.vibecode.daosubatma.core.model.TaoistProgression
import com.vibecode.daosubatma.ui.Gold
import com.vibecode.daosubatma.ui.GhostArtwork
import com.vibecode.daosubatma.ui.GoldButton
import com.vibecode.daosubatma.ui.Ink
import com.vibecode.daosubatma.ui.Mist
import com.vibecode.daosubatma.ui.Paper
import kotlin.math.abs
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

private const val GHOST_APPEARANCE_LIMIT_SECONDS = 2 * 60
// Approximate half-fields of view for a rear camera in portrait. The ghost is
// deliberately hidden outside these limits, just as a fixed object in the room
// would leave the camera frame when the player turns away.
private const val CAMERA_HORIZONTAL_HALF_FOV = .52f
private const val CAMERA_VERTICAL_HALF_FOV = .43f
// Keep the reveal window tighter than the physical camera view. This makes the
// ghost feel tied to its chosen spot: it appears when the player points at it
// and vanishes promptly when the camera is pointed elsewhere.
private const val GHOST_VISIBILITY_YAW = .30f
private const val GHOST_VISIBILITY_PITCH = .25f

@Composable
fun GameScreen(
    inventory: Map<TalismanType, Int>,
    taoistProgress: TaoistProgress,
    audio: GameAudio,
    onBack: () -> Unit,
    onUseTalisman: (TalismanType, GhostDefinition) -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { hasCameraPermission = it }
    val orientation = rememberDeviceOrientation()
    var targetYaw by remember { mutableStateOf<Float?>(null) }
    var targetPitch by remember { mutableStateOf<Float?>(null) }
    var ghostFound by remember { mutableStateOf(false) }
    var showTalismanPicker by remember { mutableStateOf(false) }
    var encounterVersion by remember { mutableStateOf(0) }
    var ghostSecondsLeft by remember(encounterVersion) {
        mutableStateOf(GHOST_APPEARANCE_LIMIT_SECONDS)
    }
    val ghost = remember(encounterVersion) { GhostCatalog.randomEncounter() }
    val ghostOffset = remember(encounterVersion) {
        GhostOffset(
            x = Random.nextInt(-72, 73).dp,
            y = Random.nextInt(-52, 41).dp,
        )
    }
    fun skipGhost() {
        ghostFound = false
        showTalismanPicker = false
        targetYaw = orientation.yaw + .62f
        targetPitch = orientation.pitch - .10f
        encounterVersion += 1
        audio.play(SoundEffect.ScanPulse)
    }
    LaunchedEffect(orientation.yaw) {
        if (targetYaw == null && orientation.isAvailable) {
            targetYaw = orientation.yaw + .62f
            targetPitch = orientation.pitch - .10f
        }
    }
    val yawToGhost = targetYaw?.let { signedAngularDifference(orientation.yaw, it) } ?: Float.MAX_VALUE
    val pitchToGhost = targetPitch?.let { it - orientation.pitch } ?: Float.MAX_VALUE
    // The ghost has one fixed direction in the room. It is rendered only while
    // that direction lies inside the camera's field of view.
    val ghostInCameraView = targetYaw != null &&
        abs(yawToGhost) < GHOST_VISIBILITY_YAW &&
        abs(pitchToGhost) < GHOST_VISIBILITY_PITCH
    val aimedAtGhost = targetYaw != null && angularDistance(orientation.yaw, targetYaw!!) < .18f && abs(pitchToGhost) < .15f
    if (aimedAtGhost && !ghostFound) {
        ghostFound = true
        ghostSecondsLeft = GHOST_APPEARANCE_LIMIT_SECONDS
        audio.play(SoundEffect.GhostAppear)
    }

    LaunchedEffect(ghostFound, encounterVersion) {
        if (!ghostFound) return@LaunchedEffect
        while (ghostFound && ghostSecondsLeft > 0) {
            delay(1_000)
            ghostSecondsLeft -= 1
        }
        if (ghostFound && ghostSecondsLeft == 0) skipGhost()
    }

    Box(Modifier.fillMaxSize().background(Ink)) {
        if (hasCameraPermission) {
            CameraPreview(Modifier.fillMaxSize())
        } else {
            CameraPermissionPanel { permissionLauncher.launch(Manifest.permission.CAMERA) }
        }
        Box(Modifier.fillMaxSize().background(Ink.copy(alpha = .16f)))
        if (!ghostFound) ScanningRadar(Modifier.fillMaxSize())
        if (ghostFound && ghostInCameraView) {
            WorldAnchoredGhost(ghost, yawToGhost, pitchToGhost, ghostOffset, Modifier.fillMaxSize())
        }
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 12.dp, start = 18.dp, end = 18.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(R.string.back),
                    color = Paper,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onBack).padding(vertical = 10.dp),
                )
                Spacer(Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    if (ghostFound) {
                        Text(
                            stringResource(
                                R.string.ghost_window,
                                ghostSecondsLeft / 60,
                                ghostSecondsLeft % 60,
                            ),
                            color = Gold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Text(stringResource(R.string.yang_energy, 10), color = Paper, fontWeight = FontWeight.Bold)
                    Text(
                        stringResource(
                            R.string.taoist_experience,
                            stringResource(TaoistProgression.rankFor(taoistProgress.level).titleRes),
                            taoistProgress.level,
                            taoistProgress.experience,
                            TaoistProgression.experienceForNextLevel(taoistProgress.level),
                        ),
                        color = Paper.copy(alpha = .86f),
                        fontSize = 9.sp,
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            if (!orientation.isAvailable) SensorFallback { ghostFound = true }
            if (ghostFound) {
                GhostEncounter(
                    ghost = ghost,
                    isInCameraView = ghostInCameraView,
                    onChooseTalisman = {
                        audio.play(SoundEffect.ButtonTalisman)
                        showTalismanPicker = true
                    },
                    onSkip = ::skipGhost,
                )
            } else {
                RadarHint(aimedAtGhost)
            }
            Spacer(Modifier.height(28.dp))
        }
        if (showTalismanPicker) {
            TalismanPickerDialog(
                inventory = inventory,
                onDismiss = { showTalismanPicker = false },
                onSelected = { talisman ->
                    showTalismanPicker = false
                    audio.play(SoundEffect.ChooseTalisman)
                    onUseTalisman(talisman, ghost)
                },
            )
        }
    }
}

@Composable
private fun CameraPermissionPanel(onRequest: () -> Unit) = Column(Modifier.fillMaxSize().background(Ink).padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
    Spacer(Modifier.weight(1f))
    Text(stringResource(R.string.camera_permission_title), color = Paper, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(12.dp))
    Text(stringResource(R.string.camera_permission_body), color = Mist)
    Spacer(Modifier.height(28.dp))
    GoldButton(stringResource(R.string.allow_camera), onRequest)
    Spacer(Modifier.weight(1f))
}

@Composable
private fun RadarHint(isAimedAtGhost: Boolean) = Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    Text(stringResource(if (isAimedAtGhost) R.string.ghost_near else R.string.scan_slowly), color = Gold, fontSize = 17.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(10.dp))
    Text("◌  ◌  ◉  ◌  ◌", color = Paper.copy(alpha = if (isAimedAtGhost) 1f else .65f), fontSize = 25.sp)
    Spacer(Modifier.height(18.dp))
    Text(stringResource(R.string.follow_bell), color = Paper, fontSize = 13.sp)
}

@Composable
private fun ScanningRadar(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "scanRadar")
    val sweepAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2_600, easing = LinearEasing)),
        label = "radarSweep",
    )
    Canvas(modifier.padding(horizontal = 38.dp, vertical = 190.dp)) {
        val radius = size.minDimension / 2f
        val center = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height / 2f)
        val radarColor = Color(0xFF8EE8E0).copy(alpha = .18f)
        val glowColor = Color(0xFF8EE8E0).copy(alpha = .06f)
        val topLeft = androidx.compose.ui.geometry.Offset(center.x - radius, center.y - radius)
        val arcSize = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)

        drawCircle(color = glowColor, radius = radius)
        repeat(3) { index ->
            drawCircle(
                color = radarColor,
                radius = radius * (index + 1) / 3f,
                center = center,
                style = Stroke(width = 1.2.dp.toPx()),
            )
        }
        drawLine(radarColor, androidx.compose.ui.geometry.Offset(center.x - radius, center.y), androidx.compose.ui.geometry.Offset(center.x + radius, center.y), 1.dp.toPx())
        drawLine(radarColor, androidx.compose.ui.geometry.Offset(center.x, center.y - radius), androidx.compose.ui.geometry.Offset(center.x, center.y + radius), 1.dp.toPx())
        drawArc(
            color = Color(0xFF8EE8E0).copy(alpha = .14f),
            startAngle = sweepAngle - 38f,
            sweepAngle = 38f,
            useCenter = true,
            topLeft = topLeft,
            size = arcSize,
        )
        val radians = Math.toRadians(sweepAngle.toDouble())
        val needleEnd = androidx.compose.ui.geometry.Offset(
            center.x + kotlin.math.cos(radians).toFloat() * radius,
            center.y + kotlin.math.sin(radians).toFloat() * radius,
        )
        drawLine(Color(0xFFB9FFF5).copy(alpha = .52f), center, needleEnd, 2.dp.toPx())
        drawCircle(Color(0xFFB9FFF5).copy(alpha = .68f), 4.dp.toPx(), center)
    }
}

@Composable
private fun SensorFallback(onFind: () -> Unit) = Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    Text(stringResource(R.string.sensor_unavailable), color = Paper, fontSize = 13.sp)
    Spacer(Modifier.height(8.dp))
    Text(stringResource(R.string.manual_scan), color = Gold, modifier = Modifier.clickable(onClick = onFind))
}

@Composable
private fun GhostEncounter(
    ghost: GhostDefinition,
    isInCameraView: Boolean,
    onChooseTalisman: () -> Unit,
    onSkip: () -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    Text(ghost.localizedName(context), color = Paper, fontSize = 19.sp, fontWeight = FontWeight.Black)
    Text(
        text = if (isInCameraView) {
            stringResource(R.string.ghost_capture_rate, stringResource(ghost.rarity.labelRes), (ghost.rarity.captureRate * 100).toInt())
        } else {
            stringResource(R.string.ghost_out_of_frame)
        },
        color = Paper,
        fontSize = 12.sp,
        letterSpacing = 1.sp,
    )
    Spacer(Modifier.height(15.dp))
    GoldButton(
        label = stringResource(R.string.choose_talisman),
        onClick = onChooseTalisman,
    )
    Spacer(Modifier.height(10.dp))
    Text(
        text = stringResource(R.string.skip_ghost),
        color = Paper.copy(alpha = .86f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clickable(onClick = onSkip).padding(vertical = 8.dp, horizontal = 20.dp),
    )
    }
}

@Composable
private fun WorldAnchoredGhost(
    ghost: GhostDefinition,
    yawOffset: Float,
    pitchOffset: Float,
    ghostOffset: GhostOffset,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier) {
        // At the field-of-view edge the centre of the ghost reaches the edge of
        // the preview. It then disappears entirely on the next camera movement.
        val x = maxWidth * (yawOffset / CAMERA_HORIZONTAL_HALF_FOV) / 2f
        val y = maxHeight * (-pitchOffset / CAMERA_VERTICAL_HALF_FOV) / 2f
        GhostArtwork(
            ghost = ghost,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x + ghostOffset.x, y + ghostOffset.y)
                .height(284.dp),
        )
    }
}


private data class GhostOffset(val x: androidx.compose.ui.unit.Dp, val y: androidx.compose.ui.unit.Dp)
