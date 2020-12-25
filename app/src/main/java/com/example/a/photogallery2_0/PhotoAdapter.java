package com.example.a.photogallery2_0;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a.photogallery2_0.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private final List<Photo> values;
    private OnLongClickPhotoListener onLongClickPhotoListener;

    public PhotoAdapter(List<Photo> items) {
        values = items;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Photo photo = values.get(position);
        Picasso.get().load(photo.getUrlS()).into(holder.image);
        holder.itemView.setTag(photo);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public void setOnLongClickPhotoListener(OnLongClickPhotoListener listener) {
        this.onLongClickPhotoListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;

        ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            image.setOnLongClickListener(v -> {
                onLongClickPhotoListener.onLongClick(values.get(this.getAdapterPosition()));
                return true;
            });
        }
    }

}
