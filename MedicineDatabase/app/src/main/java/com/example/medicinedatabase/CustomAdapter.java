package com.example.medicinedatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    ArrayList<String> data;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.data = objects;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_adapter, parent, false);
        TextView med = convertView.findViewById(R.id.showMed);
        TextView date = convertView.findViewById(R.id.showDate);
        TextView time = convertView.findViewById(R.id.showTime);

        String bundle = getItem(position);
        assert bundle != null;
        String[] bundleArr = bundle.split(" ");

        med.setText(bundleArr[0]);
        date.setText(bundleArr[1]);
        time.setText(bundleArr[2]);

        return convertView;

    }
}
