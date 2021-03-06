package be.vives.sensortag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.vives.sensortag.databinding.DeviceItemBinding

class DeviceAdapter(val deviceList: List<Device>, val clickListener: (Device)->Unit) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>(){

    class DeviceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(binding: DeviceItemBinding, device: Device, clickListener: (Device)->Unit) {
            binding.deviceNameTextView.text = device.device.name?:"Unnamed"
            binding.addressTextView.text= device.device.address
            itemView.setOnClickListener { clickListener(device)}
        }
    }

    lateinit var binding: DeviceItemBinding

    // Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        binding= DeviceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeviceViewHolder(binding.root)
    }

    // Returns size of data list
    override fun getItemCount(): Int {
        return deviceList.size
    }

    // Displays data at a certain position
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(binding, deviceList[position], clickListener)
    }

}