package com.app.matrimony.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    fun checkAndRequestPermission(
        activity: Activity,
        permission: String,
        resultLauncher: ActivityResultLauncher<String>,
        callback: (Boolean) -> Unit
    ) {
        when {
            ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                callback.invoke(true)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
            }

            else -> {
                resultLauncher.launch(permission)
            }
        }
    }

    fun requestPhotoFromCamera(
        activity: Activity,
        destinationUri: Uri?,
        perResultLauncher: ActivityResultLauncher<String>,
        actResultLauncher: ActivityResultLauncher<Intent>
    ) {
        checkAndRequestPermission(
            activity,
            Manifest.permission.CAMERA,
            perResultLauncher
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, destinationUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            actResultLauncher.launch(intent)
        }
    }

    fun requestPhotoFromGallery(
        activity: Activity,
        perResultLauncher: ActivityResultLauncher<String>,
        actResultLauncher: ActivityResultLauncher<Intent>
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }.also {
            checkAndRequestPermission(activity, it, perResultLauncher) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                actResultLauncher.launch(
                    Intent.createChooser(
                        intent,
                        "Choose Photos"
                    )
                )
            }
        }
    }

}