package com.rkm.sse.util

/**
 * Image capturing related utility methods
 */

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody


class ImageUtility(private val context: Context) {

    //get file name
    val filename: String
        get() {

            val root = file1
            root.mkdirs()
            val filename = uniqueImageFilename
            val file = File(root, filename)
            return file.absolutePath
        }

    public val file1: File
        get() = File(
            Environment.getExternalStorageDirectory().toString() + File.separator + "Butler App"
                    + File.separator
        )

    /**
     * Display a dialog_address to select between Camera and Gallery to pick your image
     *
     * @param cameraRequestCode  request code for camera use
     * @param galleryRequestCode request code gallery use
     */

    /**
     * Compresses the image
     *
     * @param filePath : Location of image file
     * @return compressed image file path
     */
    fun compressImage(filePath: String): String {
        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp: Bitmap? = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth
        val maxHeight = 1024.0f
        val maxWidth = 812.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

        options.inSampleSize =
            ImageUtility.calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp!!, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(
                Paint.FILTER_BITMAP_FLAG
            )
        )

        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height,
                matrix, true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val filename = filename
        try {
            out = FileOutputStream(filename)
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            if (bmp != null) {
                bmp.recycle()
                bmp = null
            }
            scaledBitmap?.recycle()
        }
        return filename

    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    //get file path from its URI
    fun getRealPathFromURI(context: Activity, contentUri: Uri): String {
        // can post image
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.managedQuery(contentUri, proj, null, null, null)// Which
        // columns
        // to
        // return
        // WHERE clause; which rows to return (all rows)
        // WHERE clause selection arguments (none)
        // Order-by clause (ascending by name)
        val column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    // load image into image view using Picasso


    /**
     * Compress image and convert to multipart
     *
     * @param filePath path of the file to be converted
     * @return multipart image for the path supplied
     */


    /**
     * get request body image
     */
    fun getRequestBodyImage(file: File): RequestBody {
    //    return RequestBody.create(MediaType.parse("image/jpg"), file)
        return RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
    }

    /**
     * convert image to base 64 string
     *
     * @param filePath path of image
     * @return base 64 string
     */
    fun getBase64Image(filePath: String): String {
        var filePath = filePath
        filePath = getCompressedImage(filePath)
        val bm = BitmapFactory.decodeFile(filePath)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos) //bm is the bitmap object
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    /**
     * get compressed image
     */
    private fun getCompressedImage(filePath: String): String {
        val newFilePath: String
        val file_size = Integer.parseInt((File(filePath).length() shr 10).toString())
        if (file_size >= 120) {
            newFilePath = compressImage(filePath)
        } else {
            newFilePath = filePath
        }
        return filePath
    }

    fun hasPermissionInManifest(context: Context, permissionName: String): Boolean {
        val packageName = context.packageName
        try {
            val packageInfo = context.packageManager
                .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            val declaredPermisisons = packageInfo.requestedPermissions
            if (declaredPermisisons != null && declaredPermisisons.size > 0) {
                for (p in declaredPermisisons) {
                    if (p == permissionName) {
                        return true
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {

        }

        return false
    }

    fun checksize(uri: Uri, context: Context): Boolean {
        val file = File(uri.path!!)
        var size: Long = 0

        try {
            /*// Get the number of bytes in the file
        long sizeInBytes = file.length();*/
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor!!.moveToFirst()
            size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE))
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //transform in MB
        val sizeInMb = size / (1024 * 1024)
        return sizeInMb < 12
    }

    companion object {

        fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round(height.toFloat() / reqHeight.toFloat())
                } else {
                    inSampleSize = Math.round(width.toFloat() / reqWidth.toFloat())
                }
            }
            return inSampleSize
        }

        //return a unique file name
        val uniqueImageFilename: String
            get() = "img_" + System.currentTimeMillis() + ".jpg"
    }

    fun getMyFile():File
    {
       return File(
            Environment.getExternalStorageDirectory().toString() + File.separator + "Butler App"
                    + File.separator)
    }

    fun getUniqueImageFilename():String
    {
        return "img_" + System.currentTimeMillis() + ".jpg";
    }

    fun fileSize(filePath:String):String
    {
        var file =  File(filePath);
        var length = file.length();
        length = length/1024;

       // System.out.println("File Path : " + file.getPath() + ", File size : " + length +" KB)
        return length.toString()
    }
}