package com.notsatria.posapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.notsatria.posapp.data.local.entity.TransactionEntity
import com.notsatria.posapp.utils.Converters

@Database(entities = [TransactionEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun dao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): TransactionDatabase {
            if (INSTANCE == null) {
                synchronized(TransactionDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TransactionDatabase::class.java,
                        "transaction.db"
                    )
                        .build()
                }
            }
            return INSTANCE as TransactionDatabase
        }
    }

}