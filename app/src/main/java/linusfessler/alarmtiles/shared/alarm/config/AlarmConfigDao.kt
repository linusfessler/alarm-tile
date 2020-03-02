package linusfessler.alarmtiles.shared.alarm.config

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Observable

@Dao
interface AlarmConfigDao {
    @Query("SELECT * FROM AlarmConfig")
    fun select(): Observable<AlarmConfig>

    @Insert
    fun insert(alarmConfig: AlarmConfig)

    @Update
    fun update(alarmConfig: AlarmConfig)

    @Query("SELECT COUNT(*) FROM AlarmConfig")
    fun count(): Int
}