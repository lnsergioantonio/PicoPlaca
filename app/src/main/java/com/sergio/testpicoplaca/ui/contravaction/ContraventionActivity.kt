package com.sergio.testpicoplaca.ui.contravaction

import android.app.Activity
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.sergio.testpicoplaca.R
import com.sergio.testpicoplaca.data.db.Contravention
import com.sergio.testpicoplaca.ui.addNew.AddNewActivity
import kotlinx.android.synthetic.main.activity_main.*

class ContraventionActivity : AppCompatActivity() {
    companion object{
        val ADD_NEW_REQUEST = 1
    }
    private val viewModel: HistoryViewModel by lazy {
        ViewModelProviders.of(this).get(
            HistoryViewModel(
                application
            )::class.java)
    }

    private val adapter = ContraventionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(true)
        collapsingToolbarLayout.isTitleEnabled = false

        fab.setOnClickListener {
            val intent = Intent(this, AddNewActivity::class.java)
            startActivityForResult(intent, ADD_NEW_REQUEST)
        }

        recycler.adapter = adapter
        viewModel.allData.observe(this, Observer {
            adapter.contraventionList=it
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== ADD_NEW_REQUEST && resultCode== Activity.RESULT_OK){
            data?.let {
                val newContravention = Contravention()
                newContravention.date = it.getStringExtra(AddNewActivity.EXTRA_CURRENT_TIME)
                newContravention.dateRegister = it.getStringExtra(AddNewActivity.EXTRA_DATE)
                newContravention.licensePlate = it.getStringExtra(AddNewActivity.EXTRA_LICENCE_PLATE)
                newContravention.contravention = it.getBooleanExtra(AddNewActivity.EXTRA_CONTRAVENTION,false)

                viewModel.insert(newContravention)
            }
        }else{
            Snackbar.make(main_content,
                getString(R.string.dont_save),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}
