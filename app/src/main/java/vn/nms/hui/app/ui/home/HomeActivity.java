package vn.nms.hui.app.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import vn.nms.hui.app.Constant;
import vn.nms.hui.app.R;
import vn.nms.hui.app.base.BaseActivity;
import vn.nms.hui.app.base.BaseApp;
import vn.nms.hui.app.data.entity.GalleryModel;
import vn.nms.hui.app.services.local.AppDataBase;
import vn.nms.hui.app.ui.gallery.editor.EditorGalleryActivity;
import vn.nms.hui.app.ui.gallery.preview.GalleryPreviewActivity;
import vn.nms.hui.app.ui.widget.HeaderView;

public class HomeActivity extends BaseActivity implements HomeAdapter.HomeListener {

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.searchView)
    SearchView searchView;

    HomeAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);
        initHeader();
        mAdapter = new HomeAdapter();
        mAdapter.setListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        searchView.setOnCloseListener(() -> {
            ((HeaderView) findViewById(R.id.headerView)).setTitle(getString(R.string.home_title));
            mAdapter.getFilter().filter("");
            return false;
        });
        searchView.setOnSearchClickListener(
                view -> ((HeaderView) findViewById(R.id.headerView))
                        .setTitle(""));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        loadGallery();
    }

    @SuppressLint("CheckResult")
    public void loadGallery() {
        mAdapter.resetData();
        AppDataBase.getAppDatabase(BaseApp.getAppContext()).galleryDao().getGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> mAdapter.addAll(result),
                        error -> Timber.d("Error get data:%s", error.getMessage()));
    }

    public static void start(Activity activity) {
        Intent starter = new Intent(activity, HomeActivity.class);
        activity.startActivity(starter);
    }

    @OnClick(R.id.btnAdd)
    public void addGallery() {
        Intent i = new Intent(this, EditorGalleryActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(i, Constant.CREATE_GALLERY_REQUEST_CODE);
    }

    @Override
    public void onHeaderRightImagePressed() {
        super.onHeaderRightImagePressed();
        searchView.setVisibility(View.VISIBLE);
        findViewById(R.id.headerView).setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.CREATE_GALLERY_REQUEST_CODE:
                    loadGallery();
                    break;
            }
        }
    }

    @Override
    public void onEdit(GalleryModel item, int position) {
        Intent i = new Intent(this, EditorGalleryActivity.class);
        i.putExtra(Constant.EXTRA_DATA, Parcels.wrap(item));
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(i, Constant.CREATE_GALLERY_REQUEST_CODE);
    }

    @Override
    public void onDelete(GalleryModel item, int position) {
        showMessageDialog("Warning", "Do you want to delete this item?",
                "Yes", "No",
                (dialogInterface, i) -> Flowable.fromCallable(()
                        -> AppDataBase.getAppDatabase(BaseApp.getAppContext())
                        .galleryDao().delete(item)).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(result -> {
                            if (result > 0) {
                                showToast("Delete successful");
                                mAdapter.remove(position);
                            }

                        }, error -> showToast("Can not delete, please try again later.")), (dialogInterface, i) -> {

                });
    }

    @Override
    public void onOpenItem(GalleryModel item, int position) {
        Intent i = new Intent(this, GalleryPreviewActivity.class);
        i.putExtra(Constant.EXTRA_DATA, Parcels.wrap(item));
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
}
