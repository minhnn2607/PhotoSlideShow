package vn.nms.photo.app.ui.home;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ru.rambler.libs.swipe_layout.SwipeLayout;
import vn.nms.photo.app.R;
import vn.nms.photo.app.base.BaseAdapter;
import vn.nms.photo.app.base.BaseViewHolder;
import vn.nms.photo.app.data.entity.GalleryModel;
import vn.nms.photo.app.utils.DateTimeUtils;

public class HomeAdapter extends BaseAdapter<GalleryModel> implements Filterable {
    private List<GalleryModel> filterList;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeVH(parent, R.layout.slideshow_item);
    }

    @Override
    public int getItemCount() {
        return filterList == null || filterList.isEmpty() ? 0 : filterList.size();
    }

    @Override
    public void addAll(List<GalleryModel> items) {
        getData().addAll(items);
        this.filterList = items;
        notifyDataSetChanged();
    }

    @Override
    public GalleryModel getItemAtPosition(int position) {
        return filterList == null
                || position < 0
                || filterList.size() == 0
                || filterList.size() < position
                ? null : filterList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(filterList.get(position));
    }

    @Override
    public void remove(int position) {
        GalleryModel item = getItemAtPosition(position);
        if (item != null) {
            int removeId = item.getId();
            for (GalleryModel gallery : getData()) {
                if (gallery.getId() == removeId) {
                    getData().remove(gallery);
                    break;
                }
            }
        }
        filterList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
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
                            .fit().centerCrop().into(imgView);
                } else {
                    imgView.setImageResource(R.drawable.ic_photo_ph);
                }
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterList = getData();
                } else {
                    List<GalleryModel> filter = new ArrayList<>();
                    for (GalleryModel row : getData()) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())
                                || row.getDescription().contains(charSequence)) {
                            filter.add(row);
                        }
                    }
                    filterList = filter;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (ArrayList<GalleryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
