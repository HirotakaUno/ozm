package jp.japacom.unoh.ozm.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(WorkTime::class) , version = 1)
abstract class  WorkTimeDatabase : RoomDatabase() {
    abstract fun worktimeDao(): WorkTimeDAO

    companion object {
        private var INSTANCE: WorkTimeDatabase? = null

        fun getInstance(context: Context): WorkTimeDatabase? {
            if (INSTANCE == null) {
                synchronized(WorkTimeDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        WorkTimeDatabase::class.java, "Databases.db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}