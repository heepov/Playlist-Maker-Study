package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.search_btn).setOnClickListener(
            object : View.OnClickListener{override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity,"You clicked on Search Button",Toast.LENGTH_SHORT).show()
            }}
        )

        findViewById<Button>(R.id.library_btn).setOnClickListener {
            Toast.makeText(this, "You clicked on ${(it as Button).text} Button", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.settings_btn).setOnClickListener {
            setContentView(R.layout.activity_settings)
//            Toast.makeText(this, "You clicked on ${(it as Button).text} Button", Toast.LENGTH_SHORT).show()
        }

    }
}
