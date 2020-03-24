package com.zahid.coolshoptest.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.zahid.coolshoptest.viewmodel.MainViewModel
import com.zahid.coolshoptest.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_title.*

class MainActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
   private val mainViewModel: MainViewModel by viewModels()
    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this@MainActivity

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        val navController = Navigation.findNavController(this,
            R.id.fragment
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            tv_title.text = destination.label
        }
        if (mainViewModel.appManager.persistenceManager.isSessionAvaible){
            navController.navigate(R.id.actionttoProfile)
        }
        progressBar = progressBar1
    }

    fun showHidePleaseWaitDialog(i: Int) {
        if (i == 0) {
            progressBar.visibility = View.VISIBLE
        } else if (i == 1) {
            progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.cancelJobs()
    }
}
