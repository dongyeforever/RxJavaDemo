package tk.dongyeblog.rxjavademo.ui;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observer;
import tk.dongyeblog.rxjavademo.R;
import tk.dongyeblog.rxjavademo.adapter.ItemListAdapter;
import tk.dongyeblog.rxjavademo.base.BaseActivity;
import tk.dongyeblog.rxjavademo.bean.Data;
import tk.dongyeblog.rxjavademo.bean.Item;
import tk.dongyeblog.rxjavademo.util.ToastUtil;

/**
 * description:
 * author： dongyeforever@gmail.com
 * date: 2016-05-19 17:28
 */
public class BehaviorSubjectAty extends BaseActivity {
    @Bind(R.id.loadingTimeTv)
    TextView loadingTimeTv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.cacheRv)
    RecyclerView cacheRv;
    ItemListAdapter adapter = new ItemListAdapter();
    private long startingTime;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cache;
    }

    @Override
    protected void initView() {
        cacheRv.setLayoutManager(new GridLayoutManager(this, 2));
        cacheRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
    }

    @OnClick(R.id.loadBt)
    void load() {
        swipeRefreshLayout.setRefreshing(true);
        startingTime = System.currentTimeMillis();
        unsubscribe();
        subscription = Data.getInstance()
                .subscribeData(new Observer<List<Item>>() {
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
                        int loadingTime = (int) (System.currentTimeMillis() - startingTime);
                        loadingTimeTv.setText(getString(R.string.loading_time_and_source, loadingTime, Data.getInstance().getDataSourceText()));
                        adapter.setItems(items);
                    }
                });
    }

    @OnClick(R.id.clearMemoryCacheBt)
    void clearMemoryCache() {
        Data.getInstance().clearMemoryCache();
        adapter.setItems(null);
        ToastUtil.showToast(R.string.memory_cache_cleared);
    }

    @OnClick(R.id.clearMemoryAndDiskCacheBt)
    void clearMemoryAndDiskCache() {
        Data.getInstance().clearMemoryAndDiskCache();
        adapter.setItems(null);
        ToastUtil.showToast(R.string.memory_and_disk_cache_cleared);
    }
}
