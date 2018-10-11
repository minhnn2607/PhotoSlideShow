package vn.nms.photo.app.services.network;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import vn.nms.photo.app.data.entity.User;
import vn.nms.photo.app.data.response.BaseResponse;

public interface ApiInterface {
    @FormUrlEncoded
    @POST(ApiEndPoint.LOGIN_API)
    Observable<BaseResponse<User>> login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType);
}
