package com.ssas.tpcms.android.utils.files

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {
    @Throws(IOException::class)
    @JvmStatic
    fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "TPCMS_" + timeStamp + "_"
        val directory = "tpcms"
        val directoryPath = context.filesDir.path + File.separator + directory
        //val directoryPath =context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path + File.separator + directory
        //Environment.getExternalStorageDirectory().path + File.separator + directory
        val storageDir = File(directoryPath)
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        return File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
    }

    private val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_INTERNAL
            ) else
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI


    fun createImageFileQ(context: Context): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "TPCMS_" + timeStamp + "_"

        //val directory = Environment.DIRECTORY_PICTURES
        val directory = Environment.DIRECTORY_PICTURES

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
            put(MediaStore.Images.Media.RELATIVE_PATH, directory)
            put(MediaStore.Images.Media.MIME_TYPE, "image/*")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            context.contentResolver.openOutputStream(it).use { out ->

            }
        }

        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        context.contentResolver.update(uri!!, values, null, null)
        resolver.delete(uri, null, null)
        return uri
    }


    @JvmStatic
    fun getRealPathFromUri(context: Context, data: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(data, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    /* @JvmStatic
     fun getRealPathFromUri(context: Context, uri: Uri): String {
         var cursor: Cursor? = null
         try {
             val proj = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
             cursor = context.contentResolver.query(uri, proj, null, null, null)
             var gotOne=cursor!!.moveToFirst()
             if(gotOne){

             }
             val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

             return cursor.getString(columnIndex)
         } finally {
             cursor?.close()
         }
     }*/

}
