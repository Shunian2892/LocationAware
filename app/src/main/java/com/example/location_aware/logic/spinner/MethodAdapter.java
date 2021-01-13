package com.example.location_aware.logic.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.location_aware.R;

import java.util.ArrayList;

/**
 * Adapter for handling (clicked) items in the combobox in the MainActivity.
 */
public class MethodAdapter extends ArrayAdapter<MethodItem> {

    public MethodAdapter(Context context, ArrayList<MethodItem> methodList){
        super(context, 0, methodList);
    }

    //getView and getDropDownView both need to return the same view. Therefor a new method "initView" which returns the desired view such that the getView and getDropDownView only need to cal initView.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        //Check if convertView is null, if so create and inflate layout with the layout of the spinner.
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.method_spinner, parent, false
            );
        }

        ImageView methodImage = convertView.findViewById(R.id.method_image);
        TextView methodText = convertView.findViewById(R.id.method_text);

        MethodItem currentItem = getItem(position);

        if(currentItem != null){
            methodImage.setImageResource(currentItem.getMethodImage());
            methodText.setText(currentItem.getMethodName());
        }

        return convertView;
    }
}
