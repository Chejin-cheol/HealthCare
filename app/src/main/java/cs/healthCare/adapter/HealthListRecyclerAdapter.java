package cs.healthCare.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;

import cs.healthCare.R;
import cs.healthCare.model.HealthListData;

public class HealthListRecyclerAdapter extends RecyclerView.Adapter<HealthListRecyclerAdapter.HealthItemViewHolder> {
    private ArrayList<HealthListData> listData = new ArrayList();

    //추가
    private HealthListRecyclerAdapter.ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view, int position);
    }
    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(HealthListRecyclerAdapter.ItemClick itemClick) {
        this.itemClick = itemClick;
    }
    public void addItem(HealthListData data){
        listData.add(data);
    }

    @NonNull
    @Override
    public HealthListRecyclerAdapter.HealthItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ex_list_item,parent,false);
        return new HealthItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthListRecyclerAdapter.HealthItemViewHolder holder, final int position) {
        Log.i("Health","Health");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onClick(v, position);
            }
        });
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class HealthItemViewHolder extends  RecyclerView.ViewHolder
    {
        private TextView textView1;
        private TextView textView2;
        private ImageView imageView;
        View view ;
        HealthItemViewHolder(View itemView) {
            super(itemView);
            view  = itemView;
            textView1 = itemView.findViewById(R.id.title);
            textView2 = itemView.findViewById(R.id.content);
            imageView = itemView.findViewById(R.id.liv);
        }

        void onBind(HealthListData data) {
            textView1.setText(data.getTitle());
            view.setTag(data.getResld());

            File imgFile = new  File(itemView.getContext().getFilesDir() + "/images/"   +data.getResld()+".jpg");
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            }
            else
            {
                Drawable drawable    = view.getResources().getDrawable( R.drawable.daelim );
                imageView.setImageDrawable(drawable);
            }


        }

        }
    }


