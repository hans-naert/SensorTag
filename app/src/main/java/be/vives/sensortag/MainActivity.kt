package be.vives.sensortag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import be.vives.sensortag.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.scanButton.setOnClickListener { Toast.makeText(this,"Clicked on SCAN button", Toast.LENGTH_LONG).show() }
    }
}