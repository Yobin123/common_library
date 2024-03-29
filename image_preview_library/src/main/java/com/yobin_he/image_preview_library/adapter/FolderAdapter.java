package com.yobin_he.image_preview_library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yobin_he.image_preview_library.R;
import com.yobin_he.image_preview_library.entry.Folder;
import com.yobin_he.image_preview_library.entry.Image;

import java.io.File;
import java.util.ArrayList;


public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Folder> mFolders;
    private LayoutInflater mInflater;
    private int mSelectItem;
    private OnFolderSelectListener mListener;

    public FolderAdapter(Context context, ArrayList<Folder> folders) {
        mContext = context;
        mFolders = folders;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Folder folder = mFolders.get(position);
        ArrayList<Image> images = folder.getImages();
        holder.tvFolderName.setText(folder.getName());
        holder.ivSelect.setVisibility(mSelectItem == position ? View.VISIBLE : View.GONE);
        if (images != null && !images.isEmpty()) {
            holder.tvFolderSize.setText(images.size() + "张");
            Glide.with(mContext).load(new File(images.get(0).getPath())).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.ivImage);
            /*Glide.with(mContext).load(new File(images.get(0).getPath()))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(holder.ivImage);*/
        } else {
            holder.tvFolderSize.setText("0张");
            holder.ivImage.setImageBitmap(null);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectItem = holder.getAdapterPosition();
                notifyDataSetChanged();
                if (mListener != null) {
                    mListener.OnFolderSelect(folder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFolders == null ? 0 : mFolders.size();
    }

    public void setOnFolderSelectListener(OnFolderSelectListener listener) {
        this.mListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        ImageView ivSelect;
        TextView tvFolderName;
        TextView tvFolderSize;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivSelect = itemView.findViewById(R.id.iv_select);
            tvFolderName = itemView.findViewById(R.id.tv_folder_name);
            tvFolderSize = itemView.findViewById(R.id.tv_folder_size);
        }
    }

    public interface OnFolderSelectListener {
        void OnFolderSelect(Folder folder);
    }

}
