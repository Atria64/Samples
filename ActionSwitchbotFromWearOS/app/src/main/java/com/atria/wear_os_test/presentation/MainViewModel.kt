package com.atria.wear_os_test.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
    private val _acceleration: MutableLiveData<Acceleration> by lazy{
        MutableLiveData<Acceleration>().also {
            it.value = Acceleration(0f,0f,0f)
        }
    }
    val acceleration: LiveData<Acceleration> = _acceleration
    fun updateAcceleration(x:Float,y:Float,z:Float){
        _acceleration.value = Acceleration(x,y,z)
    }
    private val _latestMove: MutableLiveData<String> by lazy{
        MutableLiveData<String>().also {
            it.value = "None"
        }
    }
    val latestMove: LiveData<String> = _latestMove
    fun updateLatestMove(str:String){
        _latestMove.value = str
    }

}

data class Acceleration(
    var sensorX : Float,
    var sensorY : Float,
    var sensorZ : Float,
)
