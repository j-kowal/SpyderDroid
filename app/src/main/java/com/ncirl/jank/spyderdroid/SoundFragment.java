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
 * MessageFragment extending Fragment
 * This class will handle all Messages to display on the LCD screen
 */
public class SoundFragment extends Fragment {
    //private properties
    private TextView soundVal;
    private Switch soundSwitch;
    private DatabaseReference soundRef;

    public SoundFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate layout to the view
        View view = inflater.inflate(R.layout.fragment_sound, container, false);
        //db ref
        soundRef = FirebaseDatabase.getInstance().getReference("sensors/sound");
        //gui elements init
        soundVal = view.findViewById(R.id.ranger_distance);
        soundSwitch = view.findViewById(R.id.ranger_switch);
        //listen to switch button and sync with firebase
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                soundRef.child("is_on").setValue(isChecked ? 1 : 0);
                if(isChecked){
                    soundVal.setVisibility(View.VISIBLE);
                } else {
                    soundVal.setVisibility(View.GONE);
                }
            }
        });
        //listen to firebase db and sync
        soundRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((int) (long) dataSnapshot.child("is_on").getValue() == 1){
                    soundVal.setText(String.valueOf(dataSnapshot.child("value").getValue()));
                    soundSwitch.setChecked(true);
                }
                else {
                    soundSwitch.setChecked(false);
                }
            }

            @Override
            //show the error
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("error with DB connection");
            }
        });
        //return view
        return view;
    }
}
