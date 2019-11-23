package linusfessler.alarmtiles.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import linusfessler.alarmtiles.model.SleepTimerTile;

@Dao
public interface SleepTimerTileDao {

    @Query("SELECT * FROM SleepTimerTile")
    LiveData<SleepTimerTile> select();

    @Insert
    void insert(SleepTimerTile sleepTimerTile);

    @Update
    void update(SleepTimerTile sleepTimerTile);

    @Query("SELECT COUNT(*) FROM SleepTimerTile")
    int count();
}
