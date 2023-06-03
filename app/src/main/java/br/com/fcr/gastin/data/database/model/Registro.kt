package br.com.fcr.gastin.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "TB_REGISTRO",
)
class Registro (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var Id:Int = 0,
    @ColumnInfo(name = "VALUE")
    var Value:Int = 0,
    @ColumnInfo(name = "DESCRIPTION")
    var Description:String = "",
    @ColumnInfo(name = "CATEGORIA_FK")
    var CategoriaFk:Int = 0,
    @ColumnInfo(name = "CREATE_AT")
    var CreateAT:Date = Date(),
    @ColumnInfo(name = "UPDATE_AT")
    var UpdateAT:Date = Date(),
    @ColumnInfo(name = "IS_DEPESA")
    var IsDespesa:Boolean = true
)