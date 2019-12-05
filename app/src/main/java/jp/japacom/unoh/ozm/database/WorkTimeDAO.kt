package jp.japacom.unoh.ozm.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface WorkTimeDAO {

    // シンプルなSELECTクエリ
    @Query("SELECT * FROM worktime")
    fun getAll(): List<WorkTime>

    // データモデルのクラスを引数に渡すことで、データの作成ができる。
    @Insert
    fun insert(worktime: WorkTime)

    // データモデルのクラスを引数に渡すことで、データの削除ができる。主キーでデータを検索して削除する場合。
    @Delete
    fun delete(worktime: WorkTime)

}