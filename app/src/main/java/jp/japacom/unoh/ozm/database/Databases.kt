package jp.japacom.unoh.ozm.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(WorkTime::class) , version = 1)
abstract class  Databases : RoomDatabase() {
    abstract fun worktimeDao(): WorkTimeDAO

    companion object {
        private var INSTANCE: Databases? = null

        fun getInstance(context: Context): Databases? {
            if (INSTANCE == null) {
                synchronized(Databases::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        Databases::class.java, "Databases.db")
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