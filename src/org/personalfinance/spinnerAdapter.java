package org.personalfinance;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class spinnerAdapter extends ArrayAdapter<String>{
    private static Integer[] imageIconDatabase = {R.drawable.general,
    	R.drawable.clothes, R.drawable.food, R.drawable.bar, 
        R.drawable.gift, R.drawable.hobbies, R.drawable.household,
        R.drawable.car, R.drawable.personal, R.drawable.shopping, R.drawable.travel};
    // stores the image database names
    private String[] imageNameDatabase = {"General","Clothes","Food","Bar","Gift","Hobbies","HouseHold"
    		,"Car","Personal","Shopping","Travel"};
    
        public spinnerAdapter(Context context, int textViewResourceId,   String[] objects) {
            super(context, textViewResourceId, objects);
        }
 
        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
 
        public View getCustomView(int position, View convertView, ViewGroup parent) {
        	
            //LayoutInflater inflater=getLayoutInflater();
        	LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.company);
            label.setText(imageNameDatabase[position]);
 
            ImageView icon=(ImageView)row.findViewById(R.id.image);
            icon.setImageResource(imageIconDatabase[position]);
 
            return row;
            }
        
}
