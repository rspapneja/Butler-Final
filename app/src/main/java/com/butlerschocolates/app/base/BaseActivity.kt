package com.butlerschocolates.app.base

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.butlerschocolates.app.BuildConfig
import com.butlerschocolates.app.R
import com.butlerschocolates.app.callback.ImagePickerCallBack
import com.butlerschocolates.app.database.DatabaseHelper
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.util.Console
import com.butlerschocolates.app.util.FileCompressor
import com.butlerschocolates.app.util.Utilities
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import com.rkm.sse.util.ImageUtility
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

open class BaseActivity : AppCompatActivity() {

    var UserImageFile = ""

    var data: Intent? = null
    var mCompressor: FileCompressor? = null
    var imageUtility: ImageUtility? = null
    var destination: File? = null
    var outputFileUri: Uri? = null

    var imagePickerCallBack: ImagePickerCallBack? = null

    open val TAG = "Tag BaseActivity"

    var databaseHelper: DatabaseHelper? = null

    var newToken:String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        //initialize database
        databaseHelper = DatabaseHelper(this)

        getToken()
    }

    /*
   * @method
   * -request CameraAndGallery permissions
   * */
    fun requestCameraAndGalleryPermissions(context: FragmentActivity,imagePickerCallBack: ImagePickerCallBack) {

        this.imagePickerCallBack = imagePickerCallBack
        Dexter.withActivity(context)
            .withPermissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                 // check if all permissions are granted

                    if (report!!.areAllPermissionsGranted()) {
                        showCameraGalleryDialog(context)
                    } else {
                        if (report!!.deniedPermissionResponses.size == 0) {
                            showCameraGalleryDialog(context)
                        }
                    }

                  // check for permanent denial of any permission
                    if (report!!.deniedPermissionResponses.size != 0) {

                        /* utilities?.showSettingsAlert(
                            activity!!,
                            "Camera and Storage Permission!!",
                            "Camera and Storage is not enabled. Do you want to go to app pemission setting?",
                            true
                        )*/
                    } else {
                        if (report.isAnyPermissionPermanentlyDenied()) {

                            /*utilities?.showSettingsAlert(
                                activity!!,
                                "Camera and Storage Permission!!",
                                "Camera and Storage is not enabled. Do you want to go to app pemission setting?",
                                true
                            )*/
                        }
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            })
            .withErrorListener(object : PermissionRequestErrorListener {
                override fun onError(error: DexterError?) {
                    Console.Log(TAG, error.toString())
                }
            }).onSameThread().check()
    }

    fun showCameraGalleryDialog(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_camera_gallery, null)
        dialogBuilder.setView(dialogView)
        val cameraText = dialogView.findViewById(R.id.tv_take_photo) as TextView
        val galleryText = dialogView.findViewById(R.id.tv_gallery) as TextView
        val crossImg = dialogView.findViewById(R.id.iv_message_cross) as ImageView
        val alertDialog = dialogBuilder.create()
        cameraText.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            val root: File = createImageFile();
            destination = root
            outputFileUri = FileProvider.getUriForFile(
                context, BuildConfig.APPLICATION_ID + ".provider", destination!!
            );
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, GlobalConstants.Request_Code_CameraPicker);
            alertDialog.dismiss()
        }
        galleryText.setOnClickListener {
            var pickIntent = Intent(Intent.ACTION_PICK);
            pickIntent.setType("image/*");
            startActivityForResult(pickIntent, GlobalConstants.Request_Code_GalleryPicker);
            alertDialog.dismiss()
        }
        crossImg.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }


    fun onCaptureImageResult(imageType: String) {
        try {
            if (imageType == "camera") {
                UserImageFile = mCompressor!!.compressToFile(destination!!).toString();
            } else {
                UserImageFile =
                    mCompressor!!.compressToFile(File(getRealPathFromUri(data!!.getData()!!)))
                        .toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        imageUtility!!.fileSize(UserImageFile)
        imagePickerCallBack!!.showUserImagePath(UserImageFile)
    }

    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    fun getRealPathFromUri(contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = getContentResolver().query(contentUri, proj, null, null, null)
            assert(cursor != null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    /**
     * Create file with current timestamp name
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

    fun showTwoDecimalPos(doubleValue: Double?): String {
        return String.format("%.2f", doubleValue)
    }

    fun getToken():String {

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(
            this@BaseActivity,
            OnCompleteListener {
                newToken = it.getResult()!!.getToken()

                Utilities(this@BaseActivity).writePref("DeviceToken",newToken!!);
            })
        return newToken!!
    }
}