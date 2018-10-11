package vn.nms.hui.app.ui.gallery.preview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import vn.nms.hui.app.R;

public class PreviewAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<String> photos;
    final int BLUR_RADIUS = 25;
    BlurTransformation blurTransformation;
    GestureDetector gestureDetector;

    public PreviewAdapter(Context context, List<String> photos) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.photos = photos;
        blurTransformation = new BlurTransformation(mContext, BLUR_RADIUS);
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.preview_photo_page, container, false);
        ImageView imageView = itemView.findViewById(R.id.imgView);
        ImageView imgBg = itemView.findViewById(R.id.imgBg);
        imageView.setOnTouchListener((view, motionEvent) -> gestureDetector.onTouchEvent(motionEvent));
        Picasso.get().load("file://" + photos.get(position))
                .transform(blurTransformation).fit().into(imgBg);
        Picasso.get().load("file://" + photos.get(position)).centerCrop().fit().into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    private PreViewListener listener;

    public void setListener(PreViewListener listener) {
        this.listener = listener;
    }

    public interface PreViewListener {
        void onPreviewPhoto();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (listener != null) {
                listener.onPreviewPhoto();
            }
            return true;
        }
    }
}