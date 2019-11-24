package linusfessler.alarmtiles.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import linusfessler.alarmtiles.model.AlarmTile;

@Dao
public interface AlarmDao {

    @Query("SELECT * FROM AlarmTile")
    LiveData<AlarmTile> select();

    @Insert
    void insert(AlarmTile alarmTile);

    @Update
    void update(AlarmTile alarmTile);

    @Query("SELECT COUNT(*) FROM AlarmTile")
    int count();
}
