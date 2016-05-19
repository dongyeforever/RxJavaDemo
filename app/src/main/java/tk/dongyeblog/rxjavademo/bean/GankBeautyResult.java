package tk.dongyeblog.rxjavademo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * description:
 * author： dongyeforever@gmail.com
 * date: 2016-05-18 23:00
 */
public class GankBeautyResult {
    public boolean error;
    public @SerializedName("results")
    List<GankBeauty> beauties;
}
