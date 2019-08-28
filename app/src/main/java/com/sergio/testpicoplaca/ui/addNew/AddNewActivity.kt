package com.sergio.testpicoplaca.ui.addNew

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.sergio.testpicoplaca.R
import com.sergio.testpicoplaca.ui.contravaction.HistoryViewModel
import com.sergio.testpicoplaca.utils.TimeHelper
import kotlinx.android.synthetic.main.activity_add_new.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AddNewActivity : AppCompatActivity() {
    companion object{
        val EXTRA_CONTRAVENTION ="CONTRAVENTION"
        val EXTRA_LICENCE_PLATE="LICENCE_PLATE"
        val EXTRA_DATE="DATE"
        val EXTRA_CURRENT_TIME="CURRENT_TIME"
    }
    private var mDate = ""
    private var mTime  = ""
    private var dayOfWeek: Int?=null

    private var isDeny = false
    private val viewModel: HistoryViewModel by lazy {
        ViewModelProviders.of(this).get(
            HistoryViewModel(
                application
            )::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new)
        val calendar= Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = getString(R.string.add_new)
        btnDate.setOnClickListener { showDatePicker(year,month,day) }
        btnHour.setOnClickListener { showTimePicker() }
        btnSearch.setOnClickListener { searchAndCalcCase() }


        viewModel.allDataFilter.observe(this, Observer {
            labelCount.text = it.size.toString()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu,menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.save_data){
            save()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun showDatePicker(year: Int, month: Int, day: Int) {
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            mDate       = TimeHelper.getStyleDate(dayOfMonth,monthOfYear,year)
            dayOfWeek   = TimeHelper.getDayOfWeak(year, monthOfYear, dayOfMonth)
            btnDate.text = mDate
        },year,month,day)
        .show()
    }

    @SuppressLint("SetTextI18n")
    private fun showTimePicker() {
        val c  = Calendar.getInstance()
        val hour    = c.get(Calendar.HOUR_OF_DAY)
        val minute  = c.get(Calendar.MINUTE)

        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ view, h, m ->
            mTime = h.toString() + ":" + m
            btnHour.text = mTime
        },hour,minute,false)
        .show()
    }

    private fun searchAndCalcCase(){
        validateForm()
        inputLicensePlate.text.let {
            viewModel.getByField(it.toString())
        }
    }

    private fun validateForm(): Boolean {
        val licencePlate = inputLicensePlate.text.toString()
        val licenceStr = licencePlate.substring(licencePlate.length-1)
        if(licencePlate.trim().isEmpty() || mDate.trim().isEmpty() || mTime.trim().isEmpty()){
            showSnackBar(R.string.empty_fields)
            return false
        }
        val licenceInt = licenceStr.toIntOrNull()
        if (licenceInt==null){
            showSnackBar(R.string.incorrect_licence_plate)
            return false
        }

        if((TimeHelper.inMiddle(mTime,"7:00","9:30")) || (TimeHelper.inMiddle(mTime,"16:00","19:30"))){
            isDeny = when(dayOfWeek){
                1-> { licenceInt in 1..2 }
                2-> { licenceInt in 3..4 }
                3-> { licenceInt in 5..6 }
                4-> { licenceInt in 7..8 }
                5-> { licenceInt==9 || licenceInt==0}
                else -> false
            }
        }
        labelWarning.text = getString(if(isDeny) R.string.not_circulate else R.string.circulate)
        labelWarning.setTextColor(
            if (isDeny) Color.RED else Color.rgb(0,153,0)
        )
        return true
    }

    private fun showSnackBar(idString:Int) {
        Snackbar.make(content,
            getString(idString),
            Snackbar.LENGTH_LONG
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun save() {
        if (!validateForm()) return
        val licencePlate= inputLicensePlate.text.toString()


        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)

        val data = Intent()
        data.putExtra(EXTRA_LICENCE_PLATE,licencePlate)
        data.putExtra(EXTRA_DATE,"$mDate $mTime")
        data.putExtra(EXTRA_CURRENT_TIME,formatted)
        data.putExtra(EXTRA_CONTRAVENTION,isDeny)
        setResult(RESULT_OK,data)
        finish()
    }
}
