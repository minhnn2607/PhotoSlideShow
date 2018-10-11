package vn.nms.photo.app.ui.gallery.list;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.nms.photo.app.Constant;
import vn.nms.photo.app.R;
import vn.nms.photo.app.base.BaseActivity;
import vn.nms.photo.app.data.entity.GalleryModel;
import vn.nms.photo.app.ui.widget.ItemDecorationAlbumColumns;

public class GalleryActivity extends BaseActivity<GalleryPresenter> implements GalleryView {

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    private GalleryAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        unbinder = ButterKnife.bind(this);
        initHeader();
        init();
    }

    @SuppressLint("CheckResult")
    public void init() {
        mPresenter = new GalleryPresenter();
        mPresenter.attachView(this);
        mAdapter = new GalleryAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
                getResources().getDimensionPixelSize(R.dimen.photos_spacing),
                getResources().getInteger(R.integer.photo_column)));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(isGrant -> {
                    if (isGrant) {
                        mPresenter.loadPhotos();
                    }
                });
    }


    public static void start(Activity activity) {
        Intent starter = new Intent(activity, GalleryActivity.class);
        activity.startActivity(starter);
    }

    @Override
    public void onLoadPhotoSuccess(List<String> photos) {
        if (mAdapter != null) {
            mAdapter.addAll(photos);
        }
    }

    @Override
    public void onHeaderRightTextPressed() {
        super.onHeaderRightTextPressed();
        GalleryModel gallery = new GalleryModel();
        gallery.setPhotos(mAdapter.getSelectedPhoto());
        Intent i = new Intent();
        i.putExtra(Constant.EXTRA_DATA, Parcels.wrap(gallery));
        setResult(RESULT_OK, i);
        finish();
    }
}
