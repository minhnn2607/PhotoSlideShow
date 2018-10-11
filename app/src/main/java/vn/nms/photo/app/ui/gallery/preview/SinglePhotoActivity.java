package vn.nms.photo.app.ui.gallery.preview;

import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.nms.photo.app.Constant;
import vn.nms.photo.app.R;
import vn.nms.photo.app.base.BaseActivity;

public class SinglePhotoActivity extends BaseActivity {
    @BindView(R.id.photoView)
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_photo);
        unbinder = ButterKnife.bind(this);
        photoView.setMinimumScale(0.5f);
        initHeader();
        if (getIntent().hasExtra(Constant.EXTRA_DATA)) {
            String photo = getIntent().getExtras().getString(Constant.EXTRA_DATA);
            Picasso.get().load("file://" + photo).centerInside().fit().into(photoView);
        } else {
            finish();
        }
    }

    @OnClick(R.id.imgBack)
    public void onBack() {
        finish();
    }
}
