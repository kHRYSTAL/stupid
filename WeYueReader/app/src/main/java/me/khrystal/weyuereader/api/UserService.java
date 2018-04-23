package me.khrystal.weyuereader.api;

import com.allen.library.bean.BaseData;

import java.util.List;

import io.reactivex.Observable;
import me.khrystal.weyuereader.db.entity.UserBean;
import me.khrystal.weyuereader.model.AppUpdateBean;
import me.khrystal.weyuereader.model.BookBean;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public interface UserService {

    /**
     * 用户注册
     */
    @POST(ModelPath.USER + "／register")
    @FormUrlEncoded
    Observable<BaseData<String>> register(@Field("name") String username,
                                          @Field("password") String password);

    /**
     * 用户登录
     */
    @GET(ModelPath.USER + "/login")
    Observable<BaseData<UserBean>> login(@Query("name") String username,
                                         @Query("password") String password);

    /**
     * 修改用户密码
     */
    @PUT(ModelPath.USER + "/password")
    @FormUrlEncoded
    Observable<BaseData<String>> updatePassword(@Field("password") String password);


    /**
     * 修改用户信息
     */
    @PUT(ModelPath.USER + "/userinfo")
    @FormUrlEncoded
    Observable<BaseData<String>> updateUserInfo(@Field("nickname") String nickname,
                                                @Field("brief") String brief);

    /**
     * 获取用户信息
     */
    @GET(ModelPath.USER + "/userinfo")
    Observable<BaseData<UserBean>> getUserInfo();

    /**
     * 更换用户头像
     */
    @Multipart
    @POST(ModelPath.USER + "/uploadavatar")
    Observable<BaseData<String>> avatar(@Part MultipartBody.Part part);

    /**
     * 获取服务器书架信息
     */
    @GET(ModelPath.USER + "/bookshelf")
    Observable<BaseData<List<BookBean>>> getBookShelf();

    /**
     * 添加书架
     */
    @POST(ModelPath.USER + "/bookshelf")
    @FormUrlEncoded
    Observable<BaseData<String>> addBookShelf(@Field("bookid") String bookid);

//    /**
//     * 移除书架
//     *
//     * @return
//     */
////    @DELETE(ModelPath.USER + "/bookshelf")
//    @HTTP(method = "DELETE", path = ModelPath.USER + "/bookshelf", hasBody = true)
////    @FormUrlEncoded
//    Observable<BaseData<String>> deleteBookShelf(@Body DeleteBookBean bean);

    /**
     * 用户反馈
     */
    @POST(ModelPath.API + "/feedback")
    @FormUrlEncoded
    Observable<BaseData<String>> userFeedBack(@Field("qq") String qq, @Field("feedback") String feedback);


    /**
     * App更新
     */
    @GET(ModelPath.API + "/appupdate")
    Observable<BaseData<AppUpdateBean>> appUpdate();


}
