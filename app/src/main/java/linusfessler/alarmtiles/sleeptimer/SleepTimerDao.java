package linusfessler.alarmtiles.sleeptimer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SleepTimerDao {

    @Query("SELECT * FROM SleepTimer")
    LiveData<SleepTimer> select();

    @Insert
    void insert(SleepTimer sleepTimer);

    @Update
    void update(SleepTimer sleepTimer);

    @Query("SELECT COUNT(*) FROM SleepTimer")
    int count();
}
