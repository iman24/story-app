package com.imanancin.storyapp1.ui.add

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.imanancin.storyapp1.R
import com.imanancin.storyapp1.ViewModelFactory
import com.imanancin.storyapp1.data.remote.Result
import com.imanancin.storyapp1.databinding.ActivityAddStoryBinding
import com.imanancin.storyapp1.ui.stories.StoriesActivity
import com.imanancin.storyapp1.utils.Helper
import java.io.OutputStream
import java.io.InputStream
import java.io.File
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream



class AddStoryActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var activity: AddStoryActivity
    private var file: File? = null
    private val viewModel: AddStoryViewModel by viewModels {
        ViewModelFactory.getInstance(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this

        requestPermission()
        setUpUI()

    }

    private fun setUpUI() {
        binding.apply {
            buttonCamera.setOnClickListener(activity)
            buttonGallery.setOnClickListener(activity)
            buttonAdd.setOnClickListener(activity)
            buttonCamera.isEnabled = true
        }
    }

    private fun requestPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            file = convertBitmapToFile(imageBitmap)
            binding.ivPreview.setImageBitmap(imageBitmap)
        }
    }

    private val openGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode == RESULT_OK) {
            val img: Uri = res.data?.data as Uri
            file = convertUriToFile(img)
            binding.ivPreview.setImageURI(img)
        }
    }

    private fun pickImage() {
        val intent = Intent().apply {
            action = ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        openGallery.launch(chooser)
    }

    private fun convertBitmapToFile(bitmap: Bitmap): File {
        val bos = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0, bos)
        val bitmapdata: ByteArray = bos.toByteArray()
        val tempFile = Helper.createCustomTempFile(this)
        FileOutputStream(tempFile).apply {
            write(bitmapdata)
            flush()
            close()
        }
        return tempFile
    }

    private fun convertUriToFile(uri: Uri): File {
            val tempFile = Helper.createCustomTempFile(this)
            val `is` = contentResolver.openInputStream(uri) as InputStream
            val os: OutputStream = FileOutputStream(tempFile)
            val buf = ByteArray(1024)
            var len: Int
            while (`is`.read(buf).also { len = it } > 0) os.write(buf, 0, len)
            os.close()
            `is`.close()
            return tempFile
    }


    override fun onClick(p0: View) {
        when(p0.id) {
            R.id.button_camera -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                launcherIntentCamera.launch(intent)
            }
            R.id.button_gallery -> {
                pickImage()
            }
            R.id.button_add -> {
                if (file == null) {
                    Toast.makeText(
                        activity,
                        getString(R.string.image_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.uploadImage(file!!, binding.edAddDescription.text.toString())
                        .observe(activity) { result ->
                            if (result != null) {
                                when (result) {
                                    is Result.Loading -> {
                                        showLoading(true)
                                    }
                                    is Result.Success -> {
                                        Intent(activity, StoriesActivity::class.java).apply {
                                            startActivity(this)
                                            Toast.makeText(
                                                activity,
                                                result.data.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finishAffinity()
                                        }
                                    }
                                    is Result.Error -> {
                                        Toast.makeText(
                                            activity,
                                            result.exception,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        showLoading(false)
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    private fun showLoading(loadingState: Boolean) {
        if(loadingState) {
            with(binding){
                buttonAdd.isEnabled = false
                buttonAdd.text = getString(R.string.loading)
            }
        }else {
            with(binding){
                buttonAdd.isEnabled = true
                buttonAdd.text = getString(R.string.upload)
            }
        }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.granted_camera_permission),
                    Toast.LENGTH_SHORT
                ).show()
                binding.buttonCamera.isEnabled = false
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}