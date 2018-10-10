package vn.nms.hui.app.services.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import vn.nms.hui.app.Constant;
import vn.nms.hui.app.data.dao.GalleryDao;
import vn.nms.hui.app.data.dao.UserDao;
import vn.nms.hui.app.data.entity.ConverterList;
import vn.nms.hui.app.data.entity.GalleryModel;
import vn.nms.hui.app.data.entity.User;

@Database(entities = {User.class, GalleryModel.class}, version = 1, exportSchema = false)
@TypeConverters(ConverterList.class)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;

    public abstract UserDao userDao();

    public abstract GalleryDao galleryDao();

    public static AppDataBase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class, Constant.DB_NAME).build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}