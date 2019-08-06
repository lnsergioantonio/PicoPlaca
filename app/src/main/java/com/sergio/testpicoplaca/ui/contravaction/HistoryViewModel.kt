package com.sergio.testpicoplaca.ui.contravaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sergio.testpicoplaca.data.db.ContraventionDatabase
import com.sergio.testpicoplaca.data.ContraventionRepository
import com.sergio.testpicoplaca.data.db.Contravention
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryViewModel (application: Application) : AndroidViewModel(application) {

    private var repository: ContraventionRepository
    val allData:LiveData<List<Contravention>>
    val _allDataFilter = MutableLiveData<List<Contravention>>()
    val allDataFilter:LiveData<List<Contravention>> get()= _allDataFilter

    init {
        val contravactionDao = ContraventionDatabase.getAppDatabase(application).contravactionDao()
        repository = ContraventionRepository(contravactionDao)
        allData = repository.allContravaction
    }

    fun insert(contravaction: Contravention) {
        GlobalScope.launch {
            repository.insert(contravaction)
        }
    }
    fun getByField(licensePlate:String) {
        GlobalScope.launch {
            _allDataFilter.postValue(repository.getByField(licensePlate))
        }
    }
}