package linusfessler.alarmtiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import linusfessler.alarmtiles.model.AlarmTile;

public class AlarmTileRecyclerViewAdapter extends RecyclerView.Adapter<AlarmTileViewHolder> {

    private final AlarmTilePageListener listener;
    private final Context context;

    private List<AlarmTile> alarmTiles = Collections.emptyList();

    public AlarmTileRecyclerViewAdapter(final Context context, final AlarmTilePageListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlarmTileViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View alarmTileView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_alarm_tile, parent, false);
        return new AlarmTileViewHolder(alarmTileView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlarmTileViewHolder viewHolder, final int position) {
        final AlarmTile alarmTile = alarmTiles.get(position);

        final int iconResourceId = context.getResources().getIdentifier(alarmTile.getGeneralSettings().getIconResourceName(), "drawable", context.getPackageName());

        viewHolder.setName(alarmTile.getGeneralSettings().getName());
        viewHolder.setIconResourceId(iconResourceId);

        viewHolder.itemView.setOnClickListener(v -> listener.onAlarmTileClicked(alarmTile));
        viewHolder.itemView.setOnLongClickListener(v -> {
            listener.onAlarmTileLongClicked(alarmTile);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return alarmTiles.size();
    }

    public void setAlarmTiles(final List<AlarmTile> alarmTiles) {
        this.alarmTiles = alarmTiles;
        notifyDataSetChanged();
    }

}
