package com.santimattius.basic.skeleton.core.storage

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.santimattius.basic.skeleton.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileRepository(
    private val appContext: Context,
) {

    fun createImageFile(): Uri {
        val file = appContext.createImageFile()
        return FileProvider.getUriForFile(
            appContext, BuildConfig.APPLICATION_ID + ".provider", file
        ) ?: Uri.EMPTY
    }

    private fun Context.createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyy-MM-dd-HHmm-ss", Locale.ROOT).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        return File.createTempFile(
            imageFileName, ".jpg", externalCacheDir
        )
    }
}