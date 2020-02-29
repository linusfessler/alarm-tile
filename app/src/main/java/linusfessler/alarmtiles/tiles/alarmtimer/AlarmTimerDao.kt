package linusfessler.alarmtiles.tiles.alarmtimer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Observable

@Dao
interface AlarmTimerDao {
    @Query("SELECT * FROM AlarmTimer")
    fun select(): Observable<AlarmTimer>

    @Insert
    fun insert(alarmTimer: AlarmTimer)

    @Update
    fun update(alarmTimer: AlarmTimer)

    @Query("SELECT COUNT(*) FROM AlarmTimer")
    fun count(): Int
}