package tk.dongyeblog.rxjavademo.ui;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import tk.dongyeblog.rxjavademo.R;
import tk.dongyeblog.rxjavademo.adapter.ItemListAdapter;
import tk.dongyeblog.rxjavademo.base.BaseActivity;
import tk.dongyeblog.rxjavademo.bean.GankBeauty;
import tk.dongyeblog.rxjavademo.bean.GankBeautyResult;
import tk.dongyeblog.rxjavademo.bean.Item;
import tk.dongyeblog.rxjavademo.bean.ZhuangbiImage;
import tk.dongyeblog.rxjavademo.network.Network;
import tk.dongyeblog.rxjavademo.util.ToastUtil;

/**
 * description:
 * author： dongyeforever@gmail.com
 * date: 2016-05-19 08:07
 */
public class ZipActivity extends BaseActivity {
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gridRv)
    RecyclerView gridRv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zip;
    }

    ItemListAdapter adapter = new ItemListAdapter();
    Observer<List<Item>> observer = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showToast("数据加载失败");
        }

        @Override
        public void onNext(List<Item> items) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setItems(items);
        }
    };

    @Override
    protected void initView() {
        gridRv.setLayoutManager(new GridLayoutManager(this, 2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
    }

    @OnClick(R.id.zipLoadBt)
    void load() {
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        subscription = Observable.zip(Network.getGankApi().getBeauties(100, 1)
                        .map(new Func1<GankBeautyResult, List<Item>>() {
                            @Override
                            public List<Item> call(GankBeautyResult gankBeautyResult) {
                                List<GankBeauty> gankBeauties = gankBeautyResult.beauties;
                                List<Item> items = new ArrayList<>(gankBeauties.size());
                                for (GankBeauty gankBeauty : gankBeauties) {
                                    Item item = new Item();
                                    item.description = gankBeauty.createdAt;
                                    item.imageUrl = gankBeauty.url;
                                    items.add(item);
                                }
                                return items;
                            }
                        }), Network.getZhuangbiApi().search("装逼"),
                new Func2<List<Item>, List<ZhuangbiImage>, List<Item>>() {
                    @Override
                    public List<Item> call(List<Item> gankItems, List<ZhuangbiImage> zhuangbiImages) {
                        List<Item> items = new ArrayList<>();
                        for (int i = 0; i < gankItems.size() / 2 && i < zhuangbiImages.size(); i++) {
                            items.add(gankItems.get(i * 2));
                            items.add(gankItems.get(i * 2 + 1));
                            Item zhuangbiItem = new Item();
                            ZhuangbiImage zhuangbiImage = zhuangbiImages.get(i);
                            zhuangbiItem.description = zhuangbiImage.description;
                            zhuangbiItem.imageUrl = zhuangbiImage.image_url;
                            items.add(zhuangbiItem);
                        }
                        return items;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
