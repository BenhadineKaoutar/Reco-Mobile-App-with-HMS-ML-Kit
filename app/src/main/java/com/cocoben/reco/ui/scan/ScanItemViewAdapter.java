package com.cocoben.reco.ui.scan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cocoben.reco.R;

import java.util.List;

class ScanItemViewAdapter extends ArrayAdapter<ScanItem> {

    public ScanItemViewAdapter(Context context, List<ScanItem> services) {
        super(context, 0, services);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ingrediant_placeholder, parent, false);
        }

        ScanItemViewAdapter.ScanItemViewHolder viewHolder = (ScanItemViewAdapter.ScanItemViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ScanItemViewAdapter.ScanItemViewHolder();
            viewHolder.ingredient = convertView.findViewById(R.id.ingredient);
            viewHolder.details = convertView.findViewById(R.id.details);
            viewHolder.function = convertView.findViewById(R.id.function);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<ViewCar> cars
        ScanItem item = getItem(position);

        viewHolder.ingredient.setText(item.getIngredient());
        viewHolder.details.setText(item.getDetails());
        viewHolder.function.setText(item.getFunction());

        return convertView;
    }

    private class ScanItemViewHolder {

        public TextView ingredient;
        public TextView details;
        public TextView function;
    }

}
