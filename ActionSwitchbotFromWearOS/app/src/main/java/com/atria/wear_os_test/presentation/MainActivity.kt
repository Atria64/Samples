package com.atria.wear_os_test.presentation

import android.Manifest
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.atria.wear_os_test.presentation.theme.Wear_os_testTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.abs


class MainActivity : ComponentActivity(),SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var viewModel: MainViewModel
    private lateinit var switchBotRepository: SwitchBotRepository

    //TODO : ちゃんとステータス取ってやったほうが当然良い
    private var isOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        switchBotRepository = SwitchBotRepository()
        requestPermissions(
            arrayOf(
                Manifest.permission.BODY_SENSORS,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 0
        )
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            WearApp(viewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        val accel = sensorManager.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER
        )
        sensorManager.registerListener(this,accel,SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            detectMotion(
                event.values[0],
                event.values[1],
                event.values[2],
            )
            viewModel.updateAcceleration(
                event.values[0],
                event.values[1],
                event.values[2],
            )
        }
    }
    // めちゃ雑モーション検知、多重起動すると思う、雑。
    private fun detectMotion(x:Float, y:Float, z:Float){
        val isHandRaise = x > 8.0
        val ydiff = y - (viewModel.acceleration.value?.sensorY?:0f)
        val zdiff = z - (viewModel.acceleration.value?.sensorZ?:0f)
        if (isHandRaise && abs(zdiff) > 5){
            viewModel.updateLatestMove("Switch!")
            Log.d("debug","switch")
            lifecycleScope.launch(Dispatchers.IO) {
                if (isOn){
                    turnOff()
                }else turnOn()
                isOn = !isOn
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // nothing
    }

    private suspend fun turnOn():Boolean{
        return suspendCoroutine { continuation ->
            lifecycleScope.launch(Dispatchers.IO) {
                kotlin.runCatching {
                    switchBotRepository.turnOn()
                }.onSuccess {
                    continuation.resume(true)
                }.onFailure { e ->
                    Log.d("debug","Failure $e")
                    continuation.resume(false)
                }
            }
        }
    }
    private suspend fun turnOff():Boolean{
        return suspendCoroutine { continuation ->
            lifecycleScope.launch(Dispatchers.IO) {
                kotlin.runCatching {
                    switchBotRepository.turnOff()
                }.onSuccess {
                    continuation.resume(true)
                }.onFailure { e ->
                    Log.d("debug","Failure $e")
                    continuation.resume(false)
                }
            }
        }
    }
}

@Composable
fun WearApp(viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Wear_os_testTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            val acceleration : Acceleration? by viewModel.acceleration.observeAsState()
            val latestMove : String? by viewModel.latestMove.observeAsState()
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                text = acceleration.toString(),
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                text = latestMove.toString(),
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(
        MainViewModel()
    )
}
