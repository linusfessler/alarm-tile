package linusfessler.alarmtiles.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import linusfessler.alarmtiles.model.AlarmTile;

@Dao
public interface AlarmTileDao {

    @Query("SELECT * FROM AlarmTile")
    LiveData<List<AlarmTile>> selectAll();

    @Insert
    void insert(AlarmTile alarmTile);

    @Insert
    void insert(List<AlarmTile> alarmTiles);

    @Update
    void update(AlarmTile alarmTile);

    @Delete
    void delete(AlarmTile alarmTile);

    @Query("SELECT COUNT(*) FROM AlarmTile")
    int count();

}
