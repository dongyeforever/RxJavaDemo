package tk.dongyeblog.rxjavademo.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.dongyeblog.rxjavademo.R;
import tk.dongyeblog.rxjavademo.bean.ZhuangbiImage;

/**
 * description:
 * author： dongyeforever@gmail.com
 * date: 2016-05-17 16:10
 */
public class ZhuangbiListAdapter extends RecyclerView.Adapter<ZhuangbiListAdapter.DebounceViewHolder> {
    List<ZhuangbiImage> images;

    @Override
    public DebounceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new DebounceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DebounceViewHolder holder, int position) {
        ZhuangbiImage image = images.get(position);
        holder.imageIv.setImageURI(Uri.parse(image.image_url));
        holder.descriptionTv.setText(image.description);
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 :images.size();
    }

    public void setImages(List<ZhuangbiImage> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    static class DebounceViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageIv)
        ImageView imageIv;
        @Bind(R.id.descriptionTv)
        TextView descriptionTv;
        public DebounceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
