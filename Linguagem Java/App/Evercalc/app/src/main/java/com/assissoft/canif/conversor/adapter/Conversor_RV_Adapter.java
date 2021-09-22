package com.assissoft.canif.conversor.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.assissoft.canif.R;
import com.assissoft.canif.conversor.model.Conversor;
import com.assissoft.canif.conversor.ui.RecyclerViewOnClickListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcos on 01/12/2016.
 *
 */
public class Conversor_RV_Adapter extends RecyclerView.Adapter<Conversor_RV_Adapter.MyViewHolder>
{
    private final LayoutInflater inflater;
    private List<Conversor> mList;
    private RecyclerViewOnClickListener mRecyclerViewOnClickListener;

    public Conversor_RV_Adapter(Context context, List<Conversor> l) {
        mList = l;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.conversor_list_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewholder, int position) {
        myViewholder.tituloConversor.setText(mList.get(position).getTitulo());
        myViewholder.descricaoConversor.setText(mList.get(position).getDescricao());
        myViewholder.imagemConversor.setImageResource(mList.get(position).getImagem());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener r) {
        mRecyclerViewOnClickListener = r;
    }

    public void setFilter(List<Conversor> newList) {
        mList = new ArrayList<>();
        mList.addAll(newList);
        notifyDataSetChanged();
    }

    public List<Conversor> getList() {
        return mList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tituloConversor;
        final TextView descricaoConversor;
        final ImageView imagemConversor;

        MyViewHolder(View itemView) {
            super(itemView);

            tituloConversor = (TextView) itemView.findViewById(R.id.tv_title);
            descricaoConversor = (TextView) itemView.findViewById(R.id.tv_description);
            imagemConversor = (ImageView) itemView.findViewById(R.id.iv_conversor);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mRecyclerViewOnClickListener != null){
                mRecyclerViewOnClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

}
