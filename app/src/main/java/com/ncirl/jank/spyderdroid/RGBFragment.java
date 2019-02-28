package com.ncirl.jank.spyderdroid;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Author: Jan Kowal
 * RGB fragment extending Fragment
 * This class will handle all Messages to display on the LCD screen
 */
public class RGBFragment extends Fragment {
    //private properties
    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;
    //database ref
    private DatabaseReference rgbRef;

    public RGBFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate layout to the view
        View view = inflater.inflate(R.layout.fragment_rgb, container, false);
        //init GUI elements
        red = view.findViewById(R.id.seek_red);
        green = view.findViewById(R.id.seek_green);
        blue = view.findViewById(R.id.seek_blue);
        //database reference
        rgbRef = FirebaseDatabase.getInstance().getReference("display");

        //get the red seekbar progress drawable object
        LayerDrawable progressDrawable = (LayerDrawable) red.getProgressDrawable();
        //get the layer
        Drawable processDrawable = progressDrawable.findDrawableByLayerId(android.R.id.progress);

        //set the color filter to drawable progress and thumb to make it red
        processDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        red.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        red.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        //get green drawable progress bar and apply color mods
        processDrawable = (LayerDrawable) green.getProgressDrawable();
        processDrawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        green.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        green.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

        //get blue drawable progress bar and apply color mode
        processDrawable = (LayerDrawable) blue.getProgressDrawable();
        processDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        blue.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        blue.getThumb().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        //listen to red seekbar changes and sync with firebase
        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rgbRef.child("red").setValue(progress);
            }

            @Override
            //required - empty
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            //required - empty
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        //set event listener for green seekbar and sync with firebase
        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rgbRef.child("green").setValue(progress);
            }

            @Override
            //required - empty
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            //required - empty
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // listen to blue seekbar
        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rgbRef.child("blue").setValue(progress);
            }

            @Override
            //empty
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            //empty
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        //listen to firebase changes
        rgbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((int)(long) dataSnapshot.child("init").getValue() == 1) {
                    red.setProgress((int)(long) dataSnapshot.child("red").getValue());
                    green.setProgress((int)(long) dataSnapshot.child("green").getValue());
                    blue.setProgress((int)(long) dataSnapshot.child("blue").getValue());
                    rgbRef.child("init").setValue(0);
                }
            }

            @Override
            //display an error
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Problem with connection to DB.");
            }
        });

        //return view
        return view;
    }
}
