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
    private LayoutInflater inflater;

    @NonNull
    private List<AlarmTile> alarmTiles;

    @Override
    public int getCount() {
        return alarmTiles.size();
    }

    @Override
    public AlarmTile getItem(int position) {
        return alarmTiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.alarm_tile, parent, false);
        }

        AlarmTile alarmTile = getItem(position);

        ImageView iconView = convertView.findViewById(R.id.icon);
        iconView.setImageResource(alarmTile.getBasicSettings().getIconResourceId());

        TextView nameView = convertView.findViewById(R.id.name);
        nameView.setText(alarmTile.getBasicSettings().getName());

        return convertView;
    }

}
