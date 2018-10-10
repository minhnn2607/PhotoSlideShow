package vn.nms.hui.app.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import vn.nms.hui.app.data.entity.GalleryModel;

@Dao
public interface GalleryDao {

    @Query("SELECT * FROM GalleryModel")
    Flowable<List<GalleryModel>> getGallery();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(GalleryModel gallery);

    @Delete
    int delete(GalleryModel gallery);
}
