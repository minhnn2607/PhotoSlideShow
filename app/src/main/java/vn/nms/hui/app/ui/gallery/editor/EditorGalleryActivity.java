package vn.nms.hui.app.ui.gallery.editor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vn.nms.hui.app.Constant;
import vn.nms.hui.app.R;
import vn.nms.hui.app.base.BaseActivity;
import vn.nms.hui.app.base.BaseApp;
import vn.nms.hui.app.data.entity.GalleryModel;
import vn.nms.hui.app.services.local.AppDataBase;
import vn.nms.hui.app.ui.gallery.list.GalleryActivity;
import vn.nms.hui.app.ui.gallery.list.GalleryAdapter;
import vn.nms.hui.app.ui.widget.ItemDecorationAlbumColumns;

public class EditorGalleryActivity extends BaseActivity {
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etDescription)
    EditText etDescription;
    private GalleryAdapter mAdapter;
    GalleryModel editGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_gallery);
        initHeader();
        unbinder = ButterKnife.bind(this);
        mAdapter = new GalleryAdapter();
        mAdapter.isLock = true;
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
                getResources().getDimensionPixelSize(R.dimen.photos_spacing),
                getResources().getInteger(R.integer.photo_column)));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        if (getIntent().hasExtra(Constant.EXTRA_DATA)) {
            editGallery = Parcels.unwrap(getIntent().getExtras().getParcelable(Constant.EXTRA_DATA));
            if (editGallery != null) {
                loadOldGallery(editGallery);
            }
        }
    }

    private void loadOldGallery(GalleryModel gallery) {
        if (gallery.getTitle() != null) {
            etTitle.setText(gallery.getTitle());
        }
        if (gallery.getDescription() != null) {
            etDescription.setText(gallery.getDescription());
        }
        if (gallery.getPhotos() != null) {
            mAdapter.resetData();
            mAdapter.addAll(gallery.getPhotos());
        }
    }

    @OnClick(R.id.btnAddPhoto)
    public void addGallery() {
        Intent i = new Intent(this, GalleryActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(i, Constant.ADD_PHOTO_REQUEST_CODE);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onHeaderRightTextPressed() {
        super.onHeaderRightTextPressed();
        if (validate()) {
            GalleryModel gallery = new GalleryModel();
            if (editGallery != null) {
                gallery.setId(editGallery.getId());
                gallery.setCreatedDate(editGallery.getCreatedDate());
                gallery.setUpdatedDate(System.currentTimeMillis());
            } else {
                gallery.setCreatedDate(System.currentTimeMillis());
            }
            gallery.setTitle(etTitle.getText().toString().trim());
            gallery.setDescription(etDescription.getText().toString().trim());
            gallery.setPhotos(mAdapter.getData());
            Flowable.fromCallable(() -> AppDataBase.getAppDatabase(BaseApp.getAppContext())
                    .galleryDao().insert(gallery))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                                if (result > 0) {
                                    showToast("Save slideshow success.");
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }, error -> showToast("Save slideshow error")
                    );
        }
    }

    public boolean validate() {
        String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            showMessageDialog("Please input gallery title");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.ADD_PHOTO_REQUEST_CODE:
                    GalleryModel gallery =
                            Parcels.unwrap(data.getExtras().getParcelable(Constant.EXTRA_DATA));
                    mAdapter.resetData();
                    mAdapter.addAll(gallery.getPhotos());
                    break;
            }
        }
    }
}
