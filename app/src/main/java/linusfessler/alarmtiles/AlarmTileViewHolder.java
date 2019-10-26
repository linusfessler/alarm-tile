package linusfessler.alarmtiles;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

public class AlarmTileViewHolder extends RecyclerView.ViewHolder {

    private final MaterialTextView nameView;
    private final ImageView iconView;

    public AlarmTileViewHolder(final View view) {
        super(view);
        nameView = view.findViewById(R.id.name);
        iconView = view.findViewById(R.id.icon);
    }

    public void setName(final String name) {
        nameView.setText(name);
    }

    public void setIconResourceId(final int iconResourceId) {
        iconView.setImageResource(iconResourceId);
    }

}
