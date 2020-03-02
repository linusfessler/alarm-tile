package linusfessler.alarmtiles.shared.alarm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Single

@Dao
interface AlarmDao {
    @Query("SELECT * FROM Alarm WHERE id = :id")
    fun select(id: Long): Single<Alarm>

    @Insert
    fun insert(alarm: Alarm)

    @Update
    fun update(alarm: Alarm)
}