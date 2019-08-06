package com.sergio.testpicoplaca.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContraventionDao {
    @Insert
    fun insert(contravention: Contravention)

    @Query("SELECT * FROM contravention ORDER BY id DESC")
    fun getAll(): LiveData<List<Contravention>>

    @Query("SELECT * FROM contravention WHERE licensePlate LIKE :licensePlate")
    fun getFindBy(licensePlate:String?): List<Contravention>
}