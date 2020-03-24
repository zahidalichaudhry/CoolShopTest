package com.zahid.coolshoptest.view


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.zahid.coolshoptest.viewmodel.MainViewModel

import com.zahid.coolshoptest.R
import com.zahid.coolshoptest.utils.Status
import kotlinx.android.synthetic.main.chose_source.view.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SignIn : Fragment() {
    private val mViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =
            inflater.inflate(R.layout.fragment_sign_in, container, false)


        ChoseOption()

        view.btn_sign_in.setOnClickListener {
            if (view.ed_email.text == null) {
                Toast.makeText(activity, "Please Enter Email", Toast.LENGTH_SHORT)
                    .show()

            } else if (view.ed_pass.text == null) {
                Toast.makeText(activity, "Please Enter Email", Toast.LENGTH_SHORT)
                    .show()

            } else {
                mViewModel.getSession(view.ed_email.text.toString(), view.ed_pass.text.toString())

            }


        }
        mViewModel.getSessionModel.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.status.SUCCESS -> {
                    (Objects.requireNonNull(activity) as MainActivity).showHidePleaseWaitDialog(
                        1
                    )

                    Toast.makeText(
                        activity,
                        it.data?.token + " " + it.data?.userid,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    getView()?.let { it1 ->
                        Navigation.findNavController(it1)
                            .navigate(R.id.actionttoProfile)
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
//                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

                }
                else -> {

                }
            }
        })

        return view
    }

    //////////////////////////Chose_Options/////////////////////////////////////
    private fun ChoseOption() {
        val builder: AlertDialog = AlertDialog.Builder(activity).create()
        val inflater: LayoutInflater = activity!!.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.chose_source, null)
        builder.setView(dialogView)
        builder.setTitle("Please Choose your Backend Default is Local")
        dialogView.Web.setOnClickListener {
            mViewModel.appManager.persistenceManager.isLocalSource(false)
            mViewModel.appManager.persistenceManager.removeSession()
            builder.dismiss()
        }
        dialogView.Local.setOnClickListener {
            mViewModel.appManager.persistenceManager.isLocalSource(true)
            mViewModel.appManager.persistenceManager.removeSession()
            builder.dismiss()
        }
        dialogView.Dismiss.setOnClickListener{
            builder.dismiss()

        }
        builder.show()
//        builder.setCancelable(false)
    }

}
