package tk.dongyeblog.rxjavademo.ui;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tk.dongyeblog.rxjavademo.R;
import tk.dongyeblog.rxjavademo.adapter.ZhuangbiListAdapter;
import tk.dongyeblog.rxjavademo.base.App;
import tk.dongyeblog.rxjavademo.base.BaseActivity;
import tk.dongyeblog.rxjavademo.bean.ZhuangbiImage;
import tk.dongyeblog.rxjavademo.network.Network;
import tk.dongyeblog.rxjavademo.util.ToastUtil;

public class MainActivity extends BaseActivity {
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gridRv)
    RecyclerView gridRv;
    ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();
    Observer<List<ZhuangbiImage>> observer = new Observer<List<ZhuangbiImage>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showToast("数据加载失败");
        }

        @Override
        public void onNext(List<ZhuangbiImage> images) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setImages(images);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_elementary;
    }

    @Override
    protected void initView() {
        gridRv.setLayoutManager(new GridLayoutManager(this, 2));
        gridRv.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
    }

    @OnCheckedChanged({R.id.searchRb1, R.id.searchRb2, R.id.searchRb3, R.id.searchRb4})
    void onTagChecked(RadioButton searchRb, boolean checked) {
        if (checked) {
//            unsubscribe();
//            adapter.setImages(null);
            swipeRefreshLayout.setRefreshing(true);
            search(searchRb.getText().toString());
        }
    }

    private void search(String str) {
        subscription = Network.getZhuangbiApi()
                .search(str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
