package cs.healthCare.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import cs.healthCare.R;
import cs.healthCare.model.Data;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    //adapter에 들어갈 list
    private ArrayList<Data> listData = new ArrayList();
    private ItemClickListener mClcikListener = null;
    public interface ItemClickListener
    {
        void OnClick(View view, int position);
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ex_part_item,parent,false);
        return  new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position){
        holder.onBind(listData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClcikListener.OnClick(v , position);
            }
        });
    }

    @Override
    public int getItemCount(){
        //RecyclerView의 총 개수
        return listData.size();
    }

    public void addItem(Data data){
        listData.add(data);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        mClcikListener = listener;
    }


    //RecycleView의 핵심인 ViewHolder
    //여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView1;
        private TextView textView2;
        private View view;
        ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);

        }
        void onBind(Data data) {
            textView1.setText(data.getTitle());
            view.setTag(data.getResld());

        }
    }
}
