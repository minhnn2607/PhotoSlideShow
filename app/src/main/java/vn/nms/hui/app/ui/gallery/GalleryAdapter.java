package vn.nms.hui.app.ui.gallery;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import vn.nms.hui.app.R;
import vn.nms.hui.app.base.BaseAdapter;
import vn.nms.hui.app.base.BaseViewHolder;

public class GalleryAdapter extends BaseAdapter<String> {
    public List<Integer> selectedList = new ArrayList<>();
    public boolean isLock = false;

    public List<String> getSelectedPhoto() {
        List<String> result = new ArrayList<>();
        for (Integer position : selectedList) {
            result.add(getItemAtPosition(position));
        }
        return result;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeVH(parent, R.layout.photo_item);
    }

    public class HomeVH extends BaseViewHolder<String> {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.imgSelected)
        ImageView imgSelected;
        @BindView(R.id.imgCheck)
        ImageView imgCheck;


        protected HomeVH(ViewGroup parent, int itemLayoutId) {
            super(parent, itemLayoutId);
        }

        @Override
        public void onBind(String s) {
            Picasso.get().load(new File(s)).centerCrop().noFade().fit().into(imageView);
            int position = getAdapterPosition();
            if (selectedList.contains(position)) {
                imgSelected.setVisibility(View.VISIBLE);
                imgCheck.setVisibility(View.VISIBLE);
            } else {
                imgSelected.setVisibility(View.GONE);
                imgCheck.setVisibility(View.GONE);
            }
        }

        @OnClick(R.id.imageView)
        public void selectPhoto() {
            if(!isLock) {
                int position = getAdapterPosition();
                if (position != -1) {
                    if (selectedList.contains(position)) {
                        selectedList.remove((Integer) position);
                    } else {
                        selectedList.add(position);
                    }
                    notifyItemChanged(position);
                }
            }
        }
    }
}
