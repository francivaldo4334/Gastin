package br.com.fcr.gastin.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "TB_CATEGORIA")
class Categoria (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var Id:Int = 0,
    @ColumnInfo(name = "NAME")
    var Name:String = "",
    @ColumnInfo(name = "DESCRIPTION")
    var Description:String = "",
    @ColumnInfo(name = "COLOR")
    var Color:Long = 0,
    @ColumnInfo(name = "CREATE_AT")
    var CreateAT: Date = Date(),
    @ColumnInfo(name = "TOTAL")
    var total:Int = 0,
    @ColumnInfo(name = "IS_RECURRENT")
    var isRecurrent: Boolean = false,
    @ColumnInfo(name = "IS_EVER_DAYS")
    var isEverDays: Boolean = false,
    @ColumnInfo(name = "START_DATE")
    var startDate: Date? = null,
    @ColumnInfo(name = "END_DATE")
    var endDate: Date? = null
)