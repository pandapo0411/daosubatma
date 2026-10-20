package com.vibecode.daosubatma.feature.hunt

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlin.math.PI
import kotlin.math.abs

data class DeviceOrientation(val yaw: Float, val pitch: Float, val isAvailable: Boolean)

@Composable
fun rememberDeviceOrientation(): DeviceOrientation {
    val context = LocalContext.current
    val orientation = remember { mutableStateOf(DeviceOrientation(0f, 0f, false)) }
    DisposableEffect(context) {
        val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        if (sensor == null) return@DisposableEffect onDispose { }
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val matrix = FloatArray(9)
                val values = FloatArray(3)
                SensorManager.getRotationMatrixFromVector(matrix, event.values)
                SensorManager.getOrientation(matrix, values)
                orientation.value = DeviceOrientation(values[0], values[1], true)
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
        onDispose { manager.unregisterListener(listener) }
    }
    return orientation.value
}

fun angularDistance(a: Float, b: Float): Float {
    var delta = abs(a - b) % (2f * PI.toFloat())
    if (delta > PI.toFloat()) delta = 2f * PI.toFloat() - delta
    return delta
}

/**
 * The shortest signed rotation from [from] to [to].  Keeping the sign lets the
 * hunt screen project a world direction to the correct side of the camera,
 * rather than keeping the ghost in the centre of the UI.
 */
fun signedAngularDifference(from: Float, to: Float): Float {
    val fullTurn = 2f * PI.toFloat()
    var delta = (to - from) % fullTurn
    if (delta > PI.toFloat()) delta -= fullTurn
    if (delta < -PI.toFloat()) delta += fullTurn
    return delta
}
