package vn.nms.photo.app.ui.gallery.preview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import vn.nms.photo.app.Constant;
import vn.nms.photo.app.R;
import vn.nms.photo.app.base.BaseFragment;

public class PreviewFragment extends BaseFragment {
    String photo = "";
    final int BLUR_RADIUS = 25;
    BlurTransformation blurTransformation;
    GestureDetector gestureDetector;

    public PreviewFragment() {
    }

    public static PreviewFragment newInstance(String photo) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_DATA, photo);
        PreviewFragment fragment = new PreviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.imgView)
    ImageView imgView;

    @BindView(R.id.imgBg)
    ImageView imgBg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_photo_page, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photo = getArguments().getString(Constant.EXTRA_DATA);
        if (photo != null) {
            blurTransformation = new BlurTransformation(getContext(), BLUR_RADIUS);
            gestureDetector = new GestureDetector(getContext(), new GestureListener());
            imgView.setOnTouchListener((v, motionEvent) -> gestureDetector.onTouchEvent(motionEvent));
            Picasso.get().load("file://" + photo)
                    .transform(blurTransformation).fit().into(imgBg);
            Picasso.get().load("file://" + photo).centerCrop().fit().into(imgView);
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Intent i = new Intent(getContext(), SinglePhotoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra(Constant.EXTRA_DATA, photo);
            startActivity(i);
            return true;
        }
    }
}
