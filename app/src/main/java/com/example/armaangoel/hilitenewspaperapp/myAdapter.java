package com.example.armaangoel.hilitenewspaperapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by armaangoel on 4/21/18.
 */

public class myAdapter extends ArrayAdapter<String> {


    ArrayList<Reader.Message> messages = new ArrayList<Reader.Message>();


    Context mContext;
    public myAdapter(@NonNull Context context, ArrayList<Reader.Message> messages) {
        super(context, R.layout.listview_item);

        this.mContext = context;
        this.messages = messages;

    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();

        if (convertView==null) {
            LayoutInflater mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflator.inflate(R.layout.listview_item, parent, false);

            mViewHolder.thumb = (ImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.headline = (TextView) convertView.findViewById(R.id.headline);
            mViewHolder.excerpt = (TextView) convertView.findViewById(R.id.excerpt);
            mViewHolder.date = (TextView) convertView.findViewById(R.id.date);


            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.headline.setText(decode(messages.get(position).title));
        mViewHolder.excerpt.setText(decode(messages.get(position).excerpt));
        if (messages.get(position).thumbnail != null)  {
            mViewHolder.thumb.setVisibility(View.VISIBLE);
            mViewHolder.thumb.setImageBitmap(messages.get(position).thumbnail);
        } else {
            mViewHolder.thumb.setImageBitmap(null);
            mViewHolder.thumb.setVisibility(View.GONE);
        }
        mViewHolder.date.setText(messages.get(position).date);


        return convertView;
    }

    public String decode (String excerpt) {
        return Html.fromHtml(excerpt).toString();
    }

    static class ViewHolder {
        ImageView thumb;
        TextView headline;
        TextView excerpt;
        TextView date;
    }
}
