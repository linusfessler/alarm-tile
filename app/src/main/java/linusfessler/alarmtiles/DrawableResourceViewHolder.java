package linusfessler.alarmtiles;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;

public class DrawableResourceViewHolder extends RecyclerView.ViewHolder {

    @Getter
    private final ImageView imageView;

    public DrawableResourceViewHolder(@NonNull final ImageView imageView) {
        super(imageView);
        this.imageView = imageView;
    }

}
