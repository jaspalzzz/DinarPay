package com.ssas.tpcms.android.utils.files

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseActivitySimple
import com.ssas.tpcms.android.utils.PermissionUtils
import com.ssas.tpcms.android.utils.Utils
import java.io.File
import java.io.IOException


class PickMediaHelper @JvmOverloads constructor(
    var context: Context,
    private var callback: Callback? = null
) {

    private var photoFile: File? = null
    val fileExtensions = arrayOf("jpg", "png", "jpeg")
    var actionId = 0
    lateinit var resultListener: PermissionResult


    val fromGallery: Intent
        get() = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    val fromCamera: Intent?
        get() {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(context.packageManager) != null) {
                photoFile = createFile()
                if (photoFile != null) {
                    var uri: Uri? = null
                    if(Build.VERSION.SDK_INT > 24) {
                        uri = FileProvider.getUriForFile(
                            context,
                            context.getString(R.string.app_id),
                            photoFile!!
                        )
                    } else {
                        uri = Uri.fromFile(photoFile)
                    }
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                }
                return intent
            }
            return null
        }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun setActiondId(id: Int) {
        actionId = id
    }

    fun showDialogFromFragment(fragment: Fragment) {
        AlertDialog.Builder(fragment.activity!!, R.style.CustomAlertDialog)
            .setTitle(R.string.pick_image)
            .setItems(R.array.source_items) { dialog, which ->
                when (which) {
                    0 -> {
                        if (PermissionUtils.hasCameraPermission(
                                fragment.context,
                                PermissionUtils.CAMERA_PERMISSION
                            )
                        ) {
                            fragment.startActivityForResult(fromCamera, REQUEST_PHOTO_CAMERA)
                        } else {
                            PermissionUtils.requestPermissions(
                                fragment.activity!!,
                                PermissionUtils.CAMERA_PERMISSION,
                                PermissionUtils.PERMISSION_REQUEST_CAMERA_CODE
                            )
                        }
                    }
                    1 ->
                        if (PermissionUtils.hasCameraPermission(
                                fragment.context,
                                PermissionUtils.GALLARY_PERMISSION
                            )
                        ) {
                            fragment.startActivityForResult(fromGallery, REQUEST_PHOTO_GALLERY)
                        } else {
                            PermissionUtils.requestPermissions(
                                fragment.activity!!,
                                PermissionUtils.GALLARY_PERMISSION,
                                PermissionUtils.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_CODE
                            )
                        }
                }
                resultListener = object : PermissionResult {
                    override fun onGranted(permission: Array<String>) {

                        if (!permission.isNullOrEmpty()) {
                            permission.forEach { s ->
                                Log.e("jaspal", "Permission result   " + permission)
                            }
                        }

                        if (permission == PermissionUtils.CAMERA_PERMISSION) {
                            if (PermissionUtils.hasCameraPermission(
                                    fragment.context,
                                    PermissionUtils.CAMERA_PERMISSION
                                )
                            ) {
                                fragment.startActivityForResult(fromCamera, REQUEST_PHOTO_CAMERA)
                            } else {
                                PermissionUtils.requestPermissions(
                                    fragment.activity!!,
                                    PermissionUtils.CAMERA_PERMISSION,
                                    PermissionUtils.PERMISSION_REQUEST_CAMERA_CODE
                                )
                            }
                        } else {
                            if (PermissionUtils.hasCameraPermission(
                                    fragment.context,
                                    PermissionUtils.GALLARY_PERMISSION
                                )
                            ) {
                                fragment.startActivityForResult(fromGallery, REQUEST_PHOTO_GALLERY)
                            } else {
                                PermissionUtils.requestPermissions(
                                    fragment.activity!!,
                                    PermissionUtils.GALLARY_PERMISSION,
                                    PermissionUtils.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_CODE
                                )
                            }
                        }
                    }

                    override fun denied() {

                    }
                }

            }.show()
    }

    fun showDialogFromActivity(activity: Activity) {
        AlertDialog.Builder(activity, R.style.CustomAlertDialog)
            .setTitle(R.string.pick_image)
            .setItems(R.array.source_items) { dialog, which ->
                when (which) {
                    0 -> {
                        if (PermissionUtils.hasCameraPermission(
                                activity,
                                PermissionUtils.CAMERA_PERMISSION
                            )
                        ) {
                            activity.startActivityForResult(fromCamera, REQUEST_PHOTO_CAMERA)
                        } else {
                            PermissionUtils.requestPermissions(
                                activity!!,
                                PermissionUtils.CAMERA_PERMISSION,
                                PermissionUtils.PERMISSION_REQUEST_CAMERA_CODE
                            )
                        }
                    }
                    1 -> if (PermissionUtils.hasCameraPermission(
                            activity,
                            PermissionUtils.GALLARY_PERMISSION
                        )
                    ) {
                        activity.startActivityForResult(fromGallery, REQUEST_PHOTO_GALLERY)
                    } else {
                        PermissionUtils.requestPermissions(
                            activity!!,
                            PermissionUtils.GALLARY_PERMISSION,
                            PermissionUtils.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_CODE
                        )
                    }
                }
                resultListener = object : PermissionResult {
                    override fun onGranted(permission: Array<String>) {

                        if (!permission.isNullOrEmpty()) {
                            permission.forEach { s ->
                                Log.e("jaspal", "Permission result   " + permission)
                            }
                        }

                        if (permission == PermissionUtils.CAMERA_PERMISSION) {
                            if (PermissionUtils.hasCameraPermission(
                                    activity,
                                    PermissionUtils.CAMERA_PERMISSION
                                )
                            ) {
                                activity.startActivityForResult(fromCamera, REQUEST_PHOTO_CAMERA)
                            } else {
                                PermissionUtils.requestPermissions(
                                    activity!!,
                                    PermissionUtils.CAMERA_PERMISSION,
                                    PermissionUtils.PERMISSION_REQUEST_CAMERA_CODE
                                )
                            }
                        } else {
                            if (PermissionUtils.hasCameraPermission(
                                    activity,
                                    PermissionUtils.GALLARY_PERMISSION
                                )
                            ) {
                                activity.startActivityForResult(fromGallery, REQUEST_PHOTO_GALLERY)
                            } else {
                                PermissionUtils.requestPermissions(
                                    activity!!,
                                    PermissionUtils.GALLARY_PERMISSION,
                                    PermissionUtils.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_CODE
                                )
                            }
                        }
                    }

                    override fun denied() {

                    }
                }
            }.show()
    }

    fun takePhoto(activity: DialogFragment) {
        activity.startActivityForResult(fromCamera, REQUEST_PHOTO_CAMERA)
    }

    fun choosePhoto(activity: DialogFragment) {
        activity.startActivityForResult(fromGallery, REQUEST_PHOTO_GALLERY)
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        when (requestCode) {
            REQUEST_PHOTO_CAMERA -> if (resultCode == Activity.RESULT_OK) {
                if (photoFile == null) {
                    try {
                        photoFile = File(data?.data!!.path!!)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                if (callback != null && photoFile != null)
                    callback!!.onImagePicked(photoFile!!.absolutePath, actionId!!)
                return true
            }
            REQUEST_PHOTO_GALLERY -> if (resultCode == Activity.RESULT_OK) {
                val url = data?.data!!.toString()
                val realPath = FileUtils.getRealPathFromUri(context, data.data!!)
                if (url != null && realPath != null) {
                    if (url.startsWith("content://com.google.android.apps.photos.content") || realPath.startsWith(
                            "https://scontent"
                        )
                    ) {
                        if (callback != null) callback!!.onImagePicked(realPath, actionId!!)
                        //                        loadFileFromGooglePhotos(url);
                    } else {
                        val uri = Uri.parse(realPath)
                        if (uri.scheme != null && (uri.scheme == "http" || uri.scheme == "https")) {
                            if (callback != null)
                                callback!!.onError(context.getString(R.string.cant_pick_image))
                        } else {
                            if (callback != null) callback!!.onImagePicked(realPath, actionId!!)
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    fun isCorrectFormat(file: File): Boolean? {
        for (extension in fileExtensions) {
            if (file.name.toLowerCase().endsWith(extension)) {
                return true
            }
        }
        return false
    }

    fun isCorrectfileSize(file: File): Boolean? {
        var fileSize = (file.length() / 1024) / 1024
        Log.e("jaspal", "File size >> " + fileSize)
        if (fileSize <= 1) {
            return true
        }
        return false
    }

    fun checkFileDimens(path: String, context: Context) {
        var options = BitmapFactory.Options();
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        val width = options.outWidth
        val height = options.outHeight
        val widthMM = pxToMm(width.toFloat(), context).toInt()
        val heightMM = pxToMm(height.toFloat(), context).toInt()
        Log.e("jaspal", "Width of image " + width + "in mm " + widthMM)
        Log.e("jaspal", "height of image " + height + "in mm " + heightMM)
    }

    fun pxToMm(px: Float, context: Context): Float {
        val dm = context.resources.displayMetrics
        return px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1f, dm)
    }

    private fun getPathFromIntent(data: Intent): String? {
        val uri = data.data
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri!!, filePathColumn, null, null, null)
        var path: String? = null
        if (cursor!!.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            path = cursor.getString(columnIndex)
        }
        cursor.close()
        return path
    }


    private fun createFile(): File? {
        var file: File? = null
        try {
            file = FileUtils.createImageFile(context)
            Log.e("jaspal", ">>>>>  " + file?.path)

        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return file
    }

    fun decodeBitmap(path: String): Bitmap {
        return BitmapFactory.decodeFile(path)
    }


    interface Callback {
        fun onImagePicked(path: String, request: Int)
        fun onError(message: String)
    }

    companion object {
        val ACTION_Id: String = "ACTION_ID"
        val REQUEST_PHOTO_CAMERA = 5100
        val REQUEST_PHOTO_GALLERY = 5101
        private val TAG = PickMediaHelper::class.java.simpleName
    }

    interface PermissionResult {
        fun onGranted(permission: Array<String>)
        fun denied()
    }
}