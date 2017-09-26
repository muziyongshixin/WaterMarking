/*
 * Copyright 2015 Worldline.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.administrator.watermarking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import tools.FLAG;
import worldline.com.foldablelayout.FoldableLayout;

/**
 * TODO: Add a class header comment!
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private String[] mDataSet;
    private Map<Integer, Boolean> mFoldStates = new HashMap<>();
    private Context mContext;
    Handler mHandler;


    public PhotoAdapter(String[] dataSet, Context context, Handler handler) {
        mDataSet = dataSet;
        mContext = context;
        mHandler = handler;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(new FoldableLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        JSONObject tmpJson = null;
        Bitmap tmpMap = null;
        try {
            tmpJson = new JSONObject(mDataSet[position]);
            final String path = FLAG.picPathDir.getPath()+File.separator+"IMG_"+tmpJson.getString("pic_no")+".PNG";
            tmpMap = BitmapFactory.decodeStream(new FileInputStream(new File(path)));
            tmpMap = zoomBitmap(tmpMap,tmpMap.getWidth()/2,tmpMap.getHeight()/2);
            holder.mImageViewCover.setImageBitmap(tmpMap);
            holder.mImageViewDetail.setImageBitmap(tmpMap);
            StringBuffer assetContent = new StringBuffer();
            assetContent.append("资产编号\n"+ tmpJson.getString("asset_no") +"\n");
            assetContent.append("资产名称\n"+tmpJson.getString("asset_desc")+"\n");
            assetContent.append("资产价值\n"+tmpJson.getString("asset_money")+"\n");
            holder.mTextViewAsset.setText(assetContent.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Bind state
        if (mFoldStates.containsKey(position)) {
            if (mFoldStates.get(position) == Boolean.TRUE) {
                if (!holder.mFoldableLayout.isFolded()) {
                    holder.mFoldableLayout.foldWithoutAnimation();
                }
            } else if (mFoldStates.get(position) == Boolean.FALSE) {
                if (holder.mFoldableLayout.isFolded()) {
                    holder.mFoldableLayout.unfoldWithoutAnimation();
                }
            }
        } else {
            holder.mFoldableLayout.foldWithoutAnimation();
        }
        //修改这个资产
        holder.mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject tmp = new JSONObject(mDataSet[position]);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("type","add");
                    bundle.putString("asset_no", tmp.getString("asset_no"));
                    bundle.putString("asset_desc",tmp.getString("asset_desc"));
                    bundle.putString("asset_money",tmp.getString("asset_money"));
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(mContext,UploadAssert.class);
                    mContext.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //删除信息并进行提交
        holder.mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //TODO 删除信息并进行提交
                String[] newDataSet = new String[mDataSet.length - 1];
                int j = 0;
                for(int i = 0 ; i < mDataSet.length ; i++){
                    if(i != position){
                        newDataSet[j] = mDataSet[i];
                        j++;
                    }
                }
                String[] content = holder.mTextViewAsset.getText().toString().split("\n");
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("asset_no",content[1]);
                msg.setData(bundle);
                msg.what = 1;
                msg.obj = newDataSet;
                mHandler.sendMessage(msg);

            }
        });

        holder.mFoldableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mFoldableLayout.isFolded()) {
                    holder.mFoldableLayout.unfoldWithAnimation();
                } else {
                    holder.mFoldableLayout.foldWithAnimation();
                }
            }
        });
        holder.mFoldableLayout.setFoldListener(new FoldableLayout.FoldListener() {
            @Override
            public void onUnFoldStart() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(5);
                }
            }

            @Override
            public void onUnFoldEnd() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(0);
                }
                mFoldStates.put(holder.getAdapterPosition(), false);
            }

            @Override
            public void onFoldStart() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(5);
                }
            }

            @Override
            public void onFoldEnd() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(0);
                }
                mFoldStates.put(holder.getAdapterPosition(), true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    protected static class PhotoViewHolder extends RecyclerView.ViewHolder {

        protected FoldableLayout mFoldableLayout;

        @Bind(R.id.imageview_cover)
        protected ImageView mImageViewCover;

        @Bind(R.id.imageview_detail)
        protected ImageView mImageViewDetail;

        @Bind(R.id.asset_content)
        protected TextView mTextViewAsset;

        @Bind(R.id.delete_btn)
        protected Button mButtonDelete;

        @Bind(R.id.addNewPic_btn)
        protected Button mButtonAdd;

        public PhotoViewHolder(FoldableLayout foldableLayout) {
            super(foldableLayout);
            mFoldableLayout = foldableLayout;
            foldableLayout.setupViews(R.layout.list_item_cover, R.layout.list_item_detail, R.dimen.card_cover_height, itemView.getContext());
            ButterKnife.bind(this, foldableLayout);
        }
    }
    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }
}
