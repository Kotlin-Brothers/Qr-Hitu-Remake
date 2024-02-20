package com.example.qr_hitu.functions

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaCodec.MetricsConstants.MIME_TYPE
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.example.qr_hitu.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.IOException

class UpdateVer(private val context: Context) {
    private val client = OkHttpClient()
    private val url = "https://api.github.com/repos/Kotlin-Brothers/Qr-Hitu-Remake/releases/latest"
    private val request = Request.Builder().url(url).build()


    fun checkUpdate(version: String): String {
        client.newCall(request).execute().use {
            if(!it.isSuccessful) {
                return ""
            }

            val json = JSONObject(it.body!!.string())
            val latestVersion = json.getString("name")

            if(version != latestVersion) {
                Log.d("update", "$version $latestVersion")
                return latestVersion
            } else {
                return ""
            }
        }
    }

    fun enqueueDownload(versionName: String) {
        val destination = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + versionName + ".apk"
        val uri = Uri.parse("file://$destination")
        val file = File(destination)
        if (file.exists()) file.delete()
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUrl = "https://github.com/Kotlin-Brothers/Qr-Hitu-Remake/releases/latest/download"
        val downloadUri = Uri.parse("$downloadUrl/$versionName.apk")
        val request = DownloadManager.Request(downloadUri)
        request.setMimeType(MIME_TYPE)
        request.setDestinationUri(uri)

        showInstallOption(destination)
        downloadManager.enqueue(request)
    }

    @SuppressLint("InlinedApi")
    private fun showInstallOption(
        destination: String
    ) {
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent
            ) {
                val contentUri = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    File(destination)
                )
                val install = Intent(Intent.ACTION_VIEW)
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                install.data = contentUri
                context.startActivity(install)
                context.unregisterReceiver(this)
            }
        }
        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED)
    }
}