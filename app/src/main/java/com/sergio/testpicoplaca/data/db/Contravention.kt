package com.sergio.testpicoplaca.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="contravention")
data class Contravention(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    var date: String,
    var dateRegister: String,
    var licensePlate: String,
    var contravention: Boolean
){
    constructor():this(null,"","","",false)
}