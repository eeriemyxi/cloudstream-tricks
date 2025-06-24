package net.envs.myxi.cloudstreamtricks

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.system.exitProcess

val MEDIA_TYPE_M3U8 = "application/vnd.apple.mpegurl; charset=utf-8".toMediaType()

class M3u8HandlerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient()

            val serverInfo = getSharedPreferences("ServerInfo", MODE_PRIVATE)
            val hostname = serverInfo.getString("hostname", null)
            val port = serverInfo.getString("port", null)

            val uri = intent.data.toString().toUri()
            val inputStream = contentResolver.openInputStream(uri)
            val content = inputStream?.bufferedReader().use { it?.readText() }

            if (content == null) {
                return@launch
            }

            var url = "http://${hostname}:${port}/play"
            var req = Request.Builder().url(url)
                .post(content.toRequestBody(MEDIA_TYPE_M3U8)).build()

            try {
                val response = client.newCall(req).execute()
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val respBody = response.body.string()

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@M3u8HandlerActivity,
                        "Message from server: $respBody",
                        Toast.LENGTH_SHORT
                    ).show()
                    exitProcess(0)
                }

                response.body.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
