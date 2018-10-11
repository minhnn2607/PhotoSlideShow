package vn.nms.photo.app.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import vn.nms.photo.app.BuildConfig;
import vn.nms.photo.app.ui.home.HomeActivity;
import vn.nms.photo.app.R;
import vn.nms.photo.app.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ((TextView)findViewById(R.id.tvVersion)).setText("Version: " + BuildConfig.VERSION_NAME);
        new Handler().postDelayed(() -> {
            HomeActivity.start(SplashActivity.this);
            finish();

        }, 1000);
    }
}
