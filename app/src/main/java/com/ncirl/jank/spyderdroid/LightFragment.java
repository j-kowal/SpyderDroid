package com.ncirl.jank.spyderdroid;

//imports
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
 * LightFragment extending Fragment
 * This class will handle all Light sensor's activities
 */
public class LightFragment extends Fragment {
    //private GUI elements
    private TextView lightVal;
    private Switch lightSwitch;
    //Private Database ref
    private DatabaseReference lightRef;

    public LightFragment() {
        // Required empty public constructor
    }

    @Override
    //on create view...
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get the layout and inflate it into container
        View view = inflater.inflate(R.layout.fragment_light, container, false);
        //init db ref
        lightRef = FirebaseDatabase.getInstance().getReference("sensors/light");
        //init gui elements
        lightVal = view.findViewById(R.id.ranger_distance);
        lightSwitch = view.findViewById(R.id.ranger_switch);
        //listen to on/off switch sync with firebase and show/hide GUI elements
        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lightRef.child("is_on").setValue(isChecked ? 1 : 0);
                if(isChecked){
                    lightVal.setVisibility(View.VISIBLE);
                } else {
                    lightVal.setVisibility(View.GONE);
                }
            }
        });
        //add an event listener to db ref
        lightRef.addValueEventListener(new ValueEventListener() {
            @Override
            //whenever data changes in DB
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check if on
                if ((int) (long) dataSnapshot.child("is_on").getValue() == 1){
                    //get firebase value and set switch to 'on' position
                    lightVal.setText(String.valueOf(dataSnapshot.child("value").getValue() + " lm"));
                    lightSwitch.setChecked(true);
                }
                else {
                    //set to 'off' position
                    lightSwitch.setChecked(false);
                }
            }

            @Override
            //if failed display the error
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Problems with DB");
            }
        });
        //return the view
        return view;
    }

}
