package be.vives.sensortag

import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Device(val bluetoothDevice: BluetoothDevice/*, val name: String, val address: String*/) :Parcelable