package tk.dongyeblog.rxjavademo.network.api;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import tk.dongyeblog.rxjavademo.bean.GankBeautyResult;

/**
 * description:
 * author： dongyeforever@gmail.com
 * date: 2016-05-18 22:59
 */
public interface GankApi {
    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult> getBeauties(@Path("number") int number, @Path("page") int page);
}
