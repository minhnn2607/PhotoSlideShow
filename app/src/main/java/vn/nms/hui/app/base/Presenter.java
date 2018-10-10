package vn.nms.hui.app.base;

interface Presenter<T extends MvpView> {
    void attachView(T mvpView);

    void detachView();
}