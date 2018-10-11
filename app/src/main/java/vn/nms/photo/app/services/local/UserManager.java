package vn.nms.photo.app.services.local;

import vn.nms.photo.app.Constant;
import vn.nms.photo.app.data.entity.User;

public class UserManager {
    private static final UserManager instance = new UserManager();

    public static UserManager getInstance() {
        return instance;
    }

    private User userInfo;

    private UserManager() {
        userInfo = SharedPrefs.getInstance()
                .get(Constant.PREF_USER_INFO, User.class);
    }

    public User getUserInfo() {
        if (userInfo == null) {
            userInfo = SharedPrefs.getInstance()
                    .get(Constant.PREF_USER_INFO, User.class);
        }
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        if (userInfo != null) {
            this.userInfo = userInfo;
            SharedPrefs.getInstance()
                    .put(Constant.PREF_USER_INFO, userInfo);
        }
    }
}
