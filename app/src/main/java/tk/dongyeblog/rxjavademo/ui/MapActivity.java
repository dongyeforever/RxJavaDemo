package tk.dongyeblog.rxjavademo.ui;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import tk.dongyeblog.rxjavademo.R;
import tk.dongyeblog.rxjavademo.adapter.ItemListAdapter;
import tk.dongyeblog.rxjavademo.base.BaseActivity;
import tk.dongyeblog.rxjavademo.bean.GankBeauty;
import tk.dongyeblog.rxjavademo.bean.GankBeautyResult;
import tk.dongyeblog.rxjavademo.bean.Item;
import tk.dongyeblog.rxjavademo.network.Network;
import tk.dongyeblog.rxjavademo.util.LogUtil;
import tk.dongyeblog.rxjavademo.util.ToastUtil;

/**
 * description:
 * author： dongyeforever@gmail.com
 * date: 2016-05-18 22:26
 */
public class MapActivity extends BaseActivity {
    private int page = 0;

    @Bind(R.id.pageTv)
    TextView pageTv;
    @Bind(R.id.previousPageBt)
    Button previousPageBt;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gridRv)
    RecyclerView gridRv;

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
            pageTv.setText("第" + page + "页");
            adapter.setItems(items);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @OnClick(R.id.previousPageBt)
    void previousPage() {
        loadPage(--page);
        if (page == 1) {
            previousPageBt.setEnabled(false);
        }
    }

    @OnClick(R.id.nextPageBt)
    void nextPage() {
        loadPage(++page);
        if (page == 2) {
            previousPageBt.setEnabled(true);
        }
    }

    private void loadPage(int page) {
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        subscription = Network.getGankApi()
                .getBeauties(10, page)
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
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    protected void initView() {
        gridRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
    }
}
