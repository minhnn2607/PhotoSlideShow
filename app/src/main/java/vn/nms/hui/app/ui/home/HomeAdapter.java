package vn.nms.hui.app.ui.home;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import ru.rambler.libs.swipe_layout.SwipeLayout;
import vn.nms.hui.app.R;
import vn.nms.hui.app.base.BaseAdapter;
import vn.nms.hui.app.base.BaseViewHolder;
import vn.nms.hui.app.data.entity.GalleryModel;
import vn.nms.hui.app.utils.DateTimeUtils;

public class HomeAdapter extends BaseAdapter<GalleryModel> {

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeVH(parent, R.layout.slideshow_item);
    }

    public class HomeVH extends BaseViewHolder<GalleryModel> {
        @BindView(R.id.imgView)
        ImageView imgView;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.slContainer)
        SwipeLayout slContainer;

        protected HomeVH(ViewGroup parent, int itemLayoutId) {
            super(parent, itemLayoutId);
        }

        @OnClick(R.id.tvDelete)
        public void delete() {
            if (listener != null) {
                slContainer.animateReset();
                GalleryModel item = getItemAtPosition(getAdapterPosition());
                if (item != null) {
                    listener.onDelete(item, getAdapterPosition());
                }
            }
        }

        @OnClick(R.id.tvEdit)
        public void edit() {
            if (listener != null) {
                slContainer.animateReset();
                GalleryModel item = getItemAtPosition(getAdapterPosition());
                if (item != null) {
                    listener.onEdit(item, getAdapterPosition());
                }
            }
        }

        @OnClick(R.id.llContainer)
        public void open() {
            if (listener != null) {
                slContainer.animateReset();
                GalleryModel item = getItemAtPosition(getAdapterPosition());
                if (item != null) {
                    listener.onOpenItem(item, getAdapterPosition());
                }
            }
        }

        @Override
        public void onBind(GalleryModel galleryModel) {
            if (galleryModel != null) {
                if (galleryModel.getTitle() != null) {
                    tvTitle.setText(galleryModel.getTitle());
                }
                if (galleryModel.getDescription() != null) {
                    tvDescription.setText(galleryModel.getDescription());
                }

                tvDate.setText(DateTimeUtils.getDateFromTime(galleryModel.getCreatedDate()));
                if (galleryModel.getPhotos() != null && galleryModel.getPhotos().size() > 0) {
                    Picasso.get().load("file://" + galleryModel.getPhotos().get(0))
                            .fit().noFade().into(imgView);
                } else {
                    imgView.setImageResource(R.drawable.ic_photo_ph);
                }
            }
        }
    }

    private HomeListener listener;

    void setListener(HomeListener listener) {
        this.listener = listener;
    }

    public interface HomeListener {
        void onEdit(GalleryModel item, int position);

        void onDelete(GalleryModel item, int position);

        void onOpenItem(GalleryModel item, int position);
    }
}
