package com.app.matrimony.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import java.io.File

object Utils {

    fun launchWhatsapp(activity: Activity) {
        val uri = "http://api.whatsapp.com/send?phone=+919788978628" + "&text="
        activity.startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(uri)
        })
    }

    fun getImageFile(context: Context): File {
        val imageFileName = "JPEG_" + System.currentTimeMillis() + "_"
        val filePath: String =
            File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "$imageFileName.jpg"
            ).absolutePath
        return File(filePath)
    }

}