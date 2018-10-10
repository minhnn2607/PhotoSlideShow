package vn.nms.hui.app.ui.splash;

import android.os.Bundle;
import android.os.Handler;

import vn.nms.hui.app.ui.home.HomeActivity;
import vn.nms.hui.app.R;
import vn.nms.hui.app.base.BaseActivity;
import vn.nms.hui.app.ui.gallery.GalleryActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            HomeActivity.start(SplashActivity.this);
            finish();

        }, 500);
    }
}
