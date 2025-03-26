package org.geeksforgeeks.demo

import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var imageUris: ArrayList<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageUris = ArrayList()
        recyclerView = findViewById(R.id.recyclerView)

        requestPermissions()
        prepareRecyclerView()
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        if (checkPermission()) {
            Toast.makeText(this, "Permissions granted..", Toast.LENGTH_SHORT).show()
            imagePath
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun prepareRecyclerView() {
        adapter = Adapter(this, imageUris)
        val manager = GridLayoutManager(this, 4)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
    }

    private val imagePath: Unit
        get() {
            val isSDPresent = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
            if (isSDPresent) {
                val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
                val orderBy = MediaStore.Images.Media._ID
                val cursor = contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    orderBy
                )
                val count = cursor!!.count
                for (i in 0 until count) {
                    cursor.moveToPosition(i)
                    val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    imageUris.add(cursor.getString(dataColumnIndex))
                }
                cursor.close()
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE ->
                if (grantResults.isNotEmpty()) {
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
                        imagePath
                    } else {
                        Toast.makeText(this, "Permissions denied, Permissions are required to use the app..", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 200
    }
}