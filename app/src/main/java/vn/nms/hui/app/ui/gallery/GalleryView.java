package vn.nms.hui.app.ui.gallery;

import java.util.List;

import vn.nms.hui.app.base.MvpView;

public interface GalleryView extends MvpView {
    void onLoadPhotoSuccess(List<String> photos);
}
