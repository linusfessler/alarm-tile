package linusfessler.alarmtiles;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DrawableResourceAdapter extends RecyclerView.Adapter<DrawableResourceViewHolder> {

    private final int[] resourceIds;

    public DrawableResourceAdapter(final int[] resourceIds) {
        this.resourceIds = resourceIds;
    }

    @NonNull
    @Override
    public DrawableResourceViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final ImageView imageView = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawable_resource_view, parent, false);

        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) imageView.getLayoutParams();
        params.height = parent.getHeight();
        params.width = parent.getHeight();
        imageView.setLayoutParams(params);

        return new DrawableResourceViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(final DrawableResourceViewHolder holder, final int position) {
        final int resourceId = resourceIds[position];
        holder.getImageView().setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        return resourceIds.length;
    }

}
