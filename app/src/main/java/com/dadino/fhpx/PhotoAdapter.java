package com.dadino.fhpx;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends ArrayAdapter<Photo> {

    private final LayoutInflater layoutInflater;
    private List<Photo> photos;

    public PhotoAdapter(Context context, List<Photo> objects) {
        super(context, 0, objects);
        this.photos = objects;
        layoutInflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        PhotoHolder holder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_media, null);
            holder = new PhotoHolder();
            holder.image = (ImageView) view.findViewById(R.id.image);

            view.setTag(holder);
        } else {
            holder = (PhotoHolder) view.getTag();
        }
        Photo media = getItem(position);

        int width = parent.getWidth()/getContext().getResources().getInteger(R.integer.num_columns);

        Picasso picasso = Picasso.with(getContext());
        picasso.setIndicatorsEnabled(true);
        picasso.load(media.image_url)
                .placeholder(R.drawable.ic_500)
                .resize(width, width)
                .centerCrop()
                .into(holder.image);
        return view;
    }

    public void setPhotos(List<Photo> medias) {
        photos = medias;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        try {
            return this.getItem(position).id;
        } catch (Exception e) {
            return 0;
        }

    }

    @Override
    public Photo getItem(int position) {
        return photos.get(position);
    }

    @Override
    public int getCount() {
        if (photos != null) return photos.size();
        else return 0;
    }

    private class PhotoHolder {
        ImageView image;
    }
}
