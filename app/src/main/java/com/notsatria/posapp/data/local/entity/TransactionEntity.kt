package com.notsatria.posapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(tableName = "transaction")
@Parcelize
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "time")
    var time: String? = null,

    @ColumnInfo(name = "to")
    var to: String? = null,

    @ColumnInfo(name = "from")
    var from: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "amount")
    var amount: Int = 0,

    @ColumnInfo(name = "date")
    var date: Long? = null,

    @ColumnInfo(name = "type")
    var type: String? = null,
) : Parcelable
