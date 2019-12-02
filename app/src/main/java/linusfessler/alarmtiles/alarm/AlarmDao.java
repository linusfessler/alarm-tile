package linusfessler.alarmtiles.alarm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AlarmDao {

    @Query("SELECT * FROM Alarm")
    LiveData<Alarm> select();

    @Insert
    void insert(Alarm alarm);

    @Update
    void update(Alarm alarm);

    @Query("SELECT COUNT(*) FROM Alarm")
    int count();
}
