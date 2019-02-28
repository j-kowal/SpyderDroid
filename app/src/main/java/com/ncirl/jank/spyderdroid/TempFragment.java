package com.ncirl.jank.spyderdroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Author: Jan Kowal
 * TempFragment extending Fragment
 * This class will handle all temperature's sensor activity
 */
public class TempFragment extends Fragment {
    //private properties
    private TextView tempVal;
    private Switch tempSwitch;
    private DatabaseReference tempRef;

    public TempFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temp, container, false);
        //init db ref
        tempRef = FirebaseDatabase.getInstance().getReference("sensors/temp");
        //init GUI elements
        tempVal = view.findViewById(R.id.ranger_distance);
        tempSwitch = view.findViewById(R.id.ranger_switch);
        //set switch on/off to position from firebase
        tempSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tempRef.child("is_on").setValue(isChecked ? 1 : 0);
                if(isChecked){
                    tempVal.setVisibility(View.VISIBLE);
                } else {
                    tempVal.setVisibility(View.GONE);
                }
            }
        });
        //add db listener
        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //sync values with firebase
                if ((int) (long) dataSnapshot.child("is_on").getValue() == 1){
                    tempVal.setText(String.valueOf(dataSnapshot.child("value").getValue() + "°C"));
                    tempSwitch.setChecked(true);
                }
                else {
                    tempSwitch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Problems with DB");
            }
        });
        //return view
        return view;
    }

}
