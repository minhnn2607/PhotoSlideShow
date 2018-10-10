package vn.nms.hui.app.ui.gallery;

import android.os.Bundle;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import vn.nms.hui.app.Constant;
import vn.nms.hui.app.R;
import vn.nms.hui.app.base.BaseActivity;
import vn.nms.hui.app.data.entity.GalleryModel;

public class GalleryPreviewActivity extends BaseActivity {
    GalleryModel gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_preview);
        unbinder = ButterKnife.bind(this);

        if (getIntent().hasExtra(Constant.EXTRA_DATA)) {
            gallery = Parcels.unwrap(getIntent().getExtras().getParcelable(Constant.EXTRA_DATA));
        }
        if (gallery == null) {
            finish();
        }else{
            loadGallery();
        }
    }

    private void loadGallery() {

    }
}
