package vn.nms.photo.app.ui.gallery.list;

import java.util.List;

import vn.nms.photo.app.base.MvpView;

public interface GalleryView extends MvpView {
    void onLoadPhotoSuccess(List<String> photos);
}
