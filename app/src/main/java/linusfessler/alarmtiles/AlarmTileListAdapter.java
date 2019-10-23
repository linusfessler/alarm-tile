package linusfessler.alarmtiles;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import linusfessler.alarmtiles.model.AlarmTile;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlarmTileListAdapter extends BaseAdapter {

    private final Context context;
    private final AppDatabase db;
    private final LayoutInflater inflater;
    private final AlertDialog deleteDialog;

    private List<AlarmTile> alarmTiles = Collections.emptyList();


    public AlarmTileListAdapter(final Context context) {
        this.context = context.getApplicationContext();
        db = AppDatabase.getInstance(context);
        inflater = LayoutInflater.from(context);

        deleteDialog = new AlertDialog.Builder(context)
                .setMessage(R.string.dialog_delete_message)
                .setNegativeButton(R.string.dialog_no, (dialog, which) -> {
                })
                .create();
    }

    @Override
    public int getCount() {
        return alarmTiles.size();
    }

    @Override
    public AlarmTile getItem(final int position) {
        return alarmTiles.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.alarm_tile, parent, false);
        }

        final AlarmTile alarmTile = getItem(position);

        final ImageView iconView = convertView.findViewById(R.id.selected_icon);
        iconView.setImageResource(alarmTile.getGeneralSettings().getIconResourceId());

        final TextView nameView = convertView.findViewById(R.id.name);
        nameView.setText(alarmTile.getGeneralSettings().getName());

        final ImageView deleteView = convertView.findViewById(R.id.delete);
        deleteView.setOnClickListener(v -> {
            final String title = alarmTile.getGeneralSettings().getName();
            deleteDialog.setTitle(title);

            final String dialogYes = context.getString(R.string.dialog_yes);
            deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, dialogYes, (dialog, which) ->
                    Executors.newSingleThreadExecutor().submit(() -> db.alarmTiles().delete(alarmTile)));

            deleteDialog.show();
        });

        return convertView;
    }

    public void setAlarmTiles(final List<AlarmTile> alarmTiles) {
        this.alarmTiles = alarmTiles;
        notifyDataSetChanged();
    }

}
