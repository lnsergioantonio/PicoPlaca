package com.sergio.testpicoplaca.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.sergio.testpicoplaca.data.db.Contravention

import com.sergio.testpicoplaca.data.db.ContraventionDao
import kotlinx.coroutines.*

class ContraventionRepository(private val contraventionDao: ContraventionDao){

    var allContravaction:LiveData<List<Contravention>> = contraventionDao.getAll()

    fun insert(contravention: Contravention) = contraventionDao.insert(contravention)

    fun getByField(licensePlate:String) = contraventionDao.getFindBy(licensePlate)
}