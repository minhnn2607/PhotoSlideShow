package vn.nms.photo.app.ui.gallery.list;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vn.nms.photo.app.base.BaseApp;
import vn.nms.photo.app.base.BasePresenter;

public class GalleryPresenter extends BasePresenter<GalleryView> {
    public void loadPhotos() {
        getDisposable().add(Flowable.fromCallable(() -> getCameraImages(BaseApp.getAppContext())
        ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    if (isViewAttached()) {
                        if (result != null)
                            mMvpView.onLoadPhotoSuccess(result);
                    }
                }, error -> {
                    if (isViewAttached()) {
                        mMvpView.showMessageDialog("Can not load photo from your device");
                    }
                }));
    }

    public static List<String> getCameraImages(Context context) {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = context.getContentResolver().query(uri, projection, null,
                null, MediaStore.MediaColumns.DATE_ADDED + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(PathOfImage);
        }
        cursor.close();
        return listOfAllImages;
    }
}
