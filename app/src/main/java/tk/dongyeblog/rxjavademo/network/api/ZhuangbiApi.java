// (c)2016 Flipboard Inc, All Rights Reserved.

package tk.dongyeblog.rxjavademo.network.api;


import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import tk.dongyeblog.rxjavademo.bean.ZhuangbiImage;

public interface ZhuangbiApi {
    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);
}
