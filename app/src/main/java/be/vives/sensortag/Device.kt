package be.vives.sensortag

import android.bluetooth.BluetoothDevice

data class Device(val device: BluetoothDevice, val name: String, val address: String)