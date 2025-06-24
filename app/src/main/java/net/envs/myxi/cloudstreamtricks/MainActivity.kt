package net.envs.myxi.cloudstreamtricks

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_layout)

        val serverInfo = getSharedPreferences("ServerInfo", MODE_PRIVATE)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val serverHostname = findViewById<EditText>(R.id.serverHostname)
        val serverPort = findViewById<EditText>(R.id.serverPort)

        serverHostname.setText(serverInfo.getString("hostname", null))
        serverPort.setText(serverInfo.getString("port", null))

        saveButton.setOnClickListener {
            val hostname = serverHostname.text.toString()
            val port = serverPort.text.toString()

            serverInfo.edit {
                putString("hostname", hostname)
                putString("port", port)
            }

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }
    }
}
