package linusfessler.alarmtiles.alarm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Observable

@Dao
interface AlarmDao {
    @Query("SELECT * FROM Alarm")
    fun select(): Observable<Alarm>

    @Insert
    fun insert(alarm: Alarm)

    @Update
    fun update(alarm: Alarm)

    @Query("SELECT COUNT(*) FROM Alarm")
    fun count(): Int
}