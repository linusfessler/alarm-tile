package linusfessler.alarmtiles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import linusfessler.alarmtiles.model.AlarmTile;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlarmTileListAdapter extends BaseAdapter {

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private final List<AlarmTile> alarmTiles;

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

        return convertView;
    }

}
