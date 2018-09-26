package com.dermabalance.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.dermabalance.R;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkFilePermissions();
    }

    /**Splash animation.*/
    public void showAnimation() {
        final AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });

        animation.setDuration(2000);
        animation.setFillAfter(true);
        findViewById(R.id.logo).startAnimation(animation);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final String permissions[], final
                                           int[] grantResults) {
        showAnimation();
    }

    /**
     * Check and request permissions.
     */
    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                permissionCheck = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            permissionCheck += this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            } else {
                showAnimation();
            }
        } else {
            showAnimation();
        }
    }
}
