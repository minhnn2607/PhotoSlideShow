package vn.nms.photo.app.ui.gallery.preview;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.nms.photo.app.Constant;
import vn.nms.photo.app.R;
import vn.nms.photo.app.base.BaseActivity;
import vn.nms.photo.app.data.entity.GalleryModel;
import vn.nms.photo.app.utils.DateTimeUtils;

public class GalleryPreviewActivity extends BaseActivity {
    GalleryModel gallery;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tvTime)
    TextView tvTime;
    PreviewAdapter mAdapter;

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
        } else {
            loadGallery();
        }
    }

    @OnClick(R.id.imgBack)
    public void onBack() {
        finish();
    }

    private void loadGallery() {
        if (gallery.getTitle() != null) {
            tvTitle.setText(gallery.getTitle());
        }

        if (gallery.getDescription() != null) {
            tvDescription.setText(gallery.getDescription());
        }
        tvTime.setText(DateTimeUtils.getDateFromTime(gallery.getCreatedDate()));
        tvTime.setTypeface(tvTime.getTypeface(), Typeface.ITALIC);
        tvTime.setPaintFlags(tvTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvDescription.setTypeface(tvDescription.getTypeface(), Typeface.ITALIC);
        if (gallery.getPhotos() != null) {
            mAdapter = new PreviewAdapter(getSupportFragmentManager(), gallery.getPhotos());
            viewPager.setAdapter(mAdapter);
            viewPager.setOffscreenPageLimit(3);
            viewPager.setCurrentItem(gallery.getPhotos().size() * Constant.CENTER_INDEX);
        }
    }

}
