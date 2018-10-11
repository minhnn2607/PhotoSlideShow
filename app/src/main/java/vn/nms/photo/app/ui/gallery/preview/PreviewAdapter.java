package vn.nms.photo.app.ui.gallery.preview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

class PreviewAdapter extends FragmentStatePagerAdapter {

    List<String> photo;

    PreviewAdapter(FragmentManager fm, List<String> photo) {
        super(fm);
        this.photo = photo;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Fragment getItem(int position) {
        int index = position % photo.size();
        return PreviewFragment.newInstance(photo.get(index));
    }

}