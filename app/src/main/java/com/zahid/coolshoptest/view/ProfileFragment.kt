package com.zahid.coolshoptest.view


import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.zahid.coolshoptest.viewmodel.MainViewModel
import com.zahid.coolshoptest.R
import com.zahid.coolshoptest.api.ApiUtils.IMAG_BASE_URL
import com.zahid.coolshoptest.managers.MediaManager
import com.zahid.coolshoptest.utils.Status
import kotlinx.android.synthetic.main.chose_image_type.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.img_profile
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private val mViewModel: MainViewModel by activityViewModels()
    var file_uri: Uri? = null
    val CAMERA_REQUEST = 1888
    val RESULT_LOAD_IMAGE = 1

    private val CAMERA = Manifest.permission.CAMERA
    private val EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    private val PHOTO_PERMISSION_CODE = 1234
    private var Permisions = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        mViewModel.getUser()
        mViewModel.loadFromSession()

        mViewModel.getUserData.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.status.SUCCESS -> {
                    (Objects.requireNonNull(activity) as MainActivity).showHidePleaseWaitDialog(
                        1
                    )
                    view.tv_email.text = it.data?.email
                    view.tv_pass.text = it.data?.password

                    if (it.data?.avatarUrl != null && it.data?.avatarUrl != "") {

                        if(mViewModel.appManager.persistenceManager.isLocalSource){

                            img_profile.setImageBitmap(mViewModel.appManager.mediaManager.decodBitmap(it.data?.avatarUrl))

                        }else{
                            Glide.with(activity!!)
                                .load(IMAG_BASE_URL+ it.data?.avatarUrl)
                                .placeholder(R.drawable.user)
                                .into(view.img_profile)
                        }

                    }
                }
                Status.status.ERROR -> {
                    (Objects.requireNonNull(activity) as MainActivity).showHidePleaseWaitDialog(
                        1
                    )
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

                }
                Status.status.LOADING -> {
                    (Objects.requireNonNull(activity) as MainActivity).showHidePleaseWaitDialog(
                        0
                    )

                }
                else -> {

                }
            }
        })
        view.img_profile.setOnClickListener{
            askPermissions()
        }
        

        return view
    }


    //////////////////////////Chose_Options/////////////////////////////////////
    private fun ChoseOption() {
        val builder: AlertDialog = AlertDialog.Builder(activity).create()
        val inflater: LayoutInflater = activity!!.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.chose_image_type, null)
        builder.setView(dialogView)
        builder.setTitle("Please Image Option")
        dialogView.gallery.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE
            )
            builder.dismiss()
        }
        dialogView.camera.setOnClickListener {
            val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(photoCaptureIntent, CAMERA_REQUEST
            )
            builder.dismiss()
        }
        builder.show()
    }
    ////////////////////////////////////////////////////Activity Result///////////////////////
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE) {
            if (resultCode == RESULT_OK) {
                var newBitmap:Bitmap
                if (data != null) {
                    val contentURI = data.data
                    try {
                         newBitmap = MediaManager.handleSamplingAndRotationBitmap(this.activity, contentURI)
                        file_uri = mViewModel.appManager.mediaManager.getImageUri(activity, newBitmap)
                        view?.img_profile?.setImageURI(file_uri)
                        mViewModel.postAvatar(newBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(activity, "No Image Selected!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                val extras = data!!.extras
                val imageBitmap = extras!!["data"] as Bitmap?
                file_uri = mViewModel.appManager.mediaManager.getImageUri(activity, imageBitmap)
                var newBitmap: Bitmap? = null
                try {
                    newBitmap = MediaManager.handleSamplingAndRotationBitmap(activity, file_uri)
                    view?.img_profile?.setImageURI(file_uri)
                    mViewModel.postAvatar(newBitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(activity, "No Image Captured!", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }




    ///////////Permissions////////////////////////
    private fun askPermissions() {
        val permission = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (activity?.applicationContext?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(
                    activity?.applicationContext!!,
                    CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Permisions = true
                ChoseOption()
            } else {
               requestPermissions(
                    permission,
                    PHOTO_PERMISSION_CODE
                )
            }
        } else { requestPermissions(
                permission,
                PHOTO_PERMISSION_CODE
            )
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Permisions = false
        when (requestCode) {
            PHOTO_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty()) {
                    var i = 0
                    while (i < grantResults.size) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Permisions = false
                        } else {
                            Permisions = true
                            ChoseOption()
                        }
                        i++
                    }
                }
            }
        }
    }

}
