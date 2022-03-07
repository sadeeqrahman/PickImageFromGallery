package com.encoders.pickimagefromgallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_ASK_PERMISSIONS = 123
    private val PICK_IMAGE = 100
    private lateinit var gallery_image:ImageView

    private lateinit var pick_from_gallery: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gallery_image = findViewById(R.id.gallery_image)
        pick_from_gallery = findViewById(R.id.pick_from_gallery)

        pick_from_gallery.setOnClickListener {
            if (isStoragePermissionGranted()) {
                openGallery()
            }
        }
    }

    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                // Log.v(TAG, "Permission is granted")
                true
            } else {
                // Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_ASK_PERMISSIONS)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //  Log.v(TAG, "Permission is granted")
            true
        }
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            val imageUri = data!!.data
            gallery_image.setImageURI(imageUri)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted, do your stuff
                openGallery()
            } else {
                // Permission Denied
                Toast.makeText(this@MainActivity, "Storage Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}