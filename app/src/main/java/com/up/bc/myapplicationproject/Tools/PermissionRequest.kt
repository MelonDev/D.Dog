package com.up.bc.myapplicationproject.Tools

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.ProgressParams
import com.up.bc.myapplicationproject.AddActivity
import com.up.bc.myapplicationproject.R
import java.io.ByteArrayOutputStream

class PermissionRequest() {

    private val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE

    companion object {
        const val REQUEST_PERMISSION_GALLERY = 56001
    }



    lateinit var waitDialog: DialogFragment


    lateinit var uploadDialog: DialogFragment
    lateinit var builder: CircleDialog.Builder
    var filepath: Uri? = null

    lateinit var activity :AddActivity

    fun load(activity :AddActivity) :PermissionRequest{
        this.activity = activity
        return this
    }

    fun request() :PermissionRequest{

        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            //getPermision(android.Manifest.permission.CAMERA, REQUEST_PERMISSION_CAMERA)
            setQuestionDialog(0, "คำอธิบาย", "คุณจำเป็นต้องทำการขอสิทธิ์การใช้งานคลังภาพของคุณ โดยให้คุณกด \"ขอสิทธิ์\" แล้วกด \"ยอมรับ\" ตามลำดับ", REQUEST_PERMISSION_GALLERY, "ขอสิทธิ์", "ยกเลิก")
        }else {
            goToGallery()
        }

        return this
    }

    fun getPermision(permission: String, requestCode: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
            } else {
                goToGallery()
            }
        }
    }

    fun bitmapToByteArray(bitmap: Bitmap, per: Int): ByteArray {
        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, per, baos)
        return baos.toByteArray()
    }

    fun imageCalculate(bitmap: Bitmap): ByteArray {
        val maxSize = 100

        var data = bitmapToByteArray(bitmap, 100)

        var count = maxSize
        var passed = false


        if ((data.size) / 1000 <= maxSize) {
            passed = true
        }

        while (((data.size) / 1000 > maxSize || !passed)) {
            if ((bitmapToByteArray(bitmap, count / 2).size) / 1000 < 100) {
                data = bitmapToByteArray(bitmap, count / 2)
                passed = true
            } else if ((bitmapToByteArray(bitmap, count / 4).size) / 1000 < 100) {
                data = bitmapToByteArray(bitmap, count / 4)
                passed = true
            } else {
                count /= 4
            }
        }

        return data
    }

    fun upload() {
        if (filepath != null) {

            val bmp = MediaStore.Images.Media.getBitmap(activity.contentResolver, filepath)


            val data = imageCalculate(bmp)

            Log.e("DATA", data.size.toString())

            showUploadDialog("กำลังอัปโหลด...")
            val ref = FirebaseStorage.getInstance().reference.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid.toString())
            ref.putBytes(data)
                    .addOnSuccessListener { it ->
                        ref.downloadUrl.addOnSuccessListener {
                            //cover = it.toString()
                            FirebaseDatabase.getInstance().reference.child("Peoples").child(FirebaseAuth.getInstance().currentUser!!.uid.toString()).child("Info").child("image").setValue(it.toString()).addOnSuccessListener {
                                uploadDialog.dismiss()
                                showCompleteDialog5("อัพโหลดเสร็จสิ้น")
                            }
                            //Picasso.get().load(it).into(this@LessonActivity.lesson_layout_cover_image)
                            //userInfo.photoURL = it.toString()
                        }
                    }.addOnFailureListener {
                        uploadDialog.dismiss()
                        showCompleteDialog5("เกิดข้อผิดพลาด")
                    }.addOnCanceledListener {
                        uploadDialog.dismiss()
                        Log.e("UPLOAD", "CANCEL")
                    }.addOnProgressListener {
                        val progress = 100.0 * it.bytesTransferred / it.totalByteCount
                        Log.e("PROGRESS", progress.toString())
                    }
        }
    }

    fun showCompleteDialog5(status: String) {
        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText(status)
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(activity, R.color.facebookColor)
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ") {
                    Log.e("", "")
                }
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                }.show()


    }



    fun showUploadDialog(string: String) {
        builder = CircleDialog.Builder()
        uploadDialog = builder
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setProgressText(string)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(activity.supportFragmentManager)
    }


    fun goToGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        activity.startActivityForResult(Intent.createChooser(intent, "เลือกรูปจาก"), REQUEST_PERMISSION_GALLERY)
    }

    fun setQuestionDialog(ID: Int, title: String, sub: String, requestCode: Int, positive: String, negative: String) {
        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText(sub)
                .configText { params ->
                    params!!.textSize = 50
                    params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    params.padding = intArrayOf(50, 10, 50, 70) //(Left,TOP,Right,Bottom)
                }
                .setTitle(title)
                .configTitle { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(activity, R.color.color_game_blue)
                }
                .setPositive(positive) {
                   getPermision(permission,REQUEST_PERMISSION_GALLERY)

                }
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(activity, R.color.color_game_blue)
                }
                .setNegative(negative, {

                })
                .configNegative { params ->
                    params.textSize = 50

                    params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                }
                .show()


    }

    fun setWaitDialog(string: String) {
        waitDialog = CircleDialog.Builder()
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setProgressText(string)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(activity.supportFragmentManager)
    }

    fun setWaitDialog(act :FragmentActivity,string: String) {
        waitDialog = CircleDialog.Builder()
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setProgressText(string)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(act.supportFragmentManager)
    }

    fun stopWaitDialog(){
        waitDialog.dismiss()
    }

    fun convertStringToInteger(str :String) :Int{
        return try {
            val i = str.toInt()
            i
        }catch (e :Exception){
            -1
        }
    }

    fun convertStringToDouble(str :String) :Double{
        return try {
            val i = str.toDouble()
            i
        }catch (e :Exception){
            -1.0
        }
    }

    fun getByte() : ByteArray{
        val bmp = MediaStore.Images.Media.getBitmap(activity.contentResolver, filepath)


        val data = imageCalculate(bmp)
        return data
    }

}