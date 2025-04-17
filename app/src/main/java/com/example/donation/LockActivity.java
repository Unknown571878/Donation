package com.example.donation;

import android.os.Bundle;
import android.widget.SeekBar;
import com.example.donation.etc.AppCompat;

public class LockActivity extends AppCompat {
    SeekBar my_seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lock_activity);

        my_seek = findViewById(R.id.myseek);
        my_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() > 75) {
                } else if (seekBar.getProgress() < 25) {
                } else {
                    seekBar.setProgress(50);
                    seekBar.setThumb(getResources().getDrawable(R.drawable.baseline_radio_button_unchecked_24));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (progress > 75) {
                    seekBar.setProgress(92);
                    seekBar.setThumb(getResources().getDrawable(R.drawable.baseline_circle_24));
                } else if (progress < 25) {
                    seekBar.setProgress(8);
                    seekBar.setThumb(getResources().getDrawable(R.drawable.baseline_circle_24));
                } else {
                    seekBar.setThumb(getResources().getDrawable(R.drawable.baseline_radio_button_unchecked_24));
                }
            }
        });
    }
}
