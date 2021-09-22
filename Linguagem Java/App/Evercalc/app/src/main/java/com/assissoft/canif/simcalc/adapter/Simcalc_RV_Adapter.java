package com.assissoft.canif.simcalc.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.assissoft.canif.R;
import com.assissoft.canif.simcalc.model.Calculo;
import com.assissoft.canif.simcalc.ui.RecyclerViewOnClickListener;
import java.util.List;

/**
 * Created by Marcos on 30/11/2016.
 *
 */
public class Simcalc_RV_Adapter extends RecyclerView.Adapter<Simcalc_RV_Adapter.MyViewHolder>{

    private final LayoutInflater inflater;
    private final List<Calculo> mList;
    private RecyclerViewOnClickListener mRecyclerViewOnClickListener;

    public Simcalc_RV_Adapter(Context context, List<Calculo> l) {
        mList = l;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.simcalc_list_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewholder, int position) {
        myViewholder.tituloCalculo.setText(mList.get(position).getTitulo());
        myViewholder.descricaoCalculo.setText(mList.get(position).getDescricao());
        myViewholder.imagemCalculo.setImageResource(mList.get(position).getImagem());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener r) {
        mRecyclerViewOnClickListener = r;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tituloCalculo;
        final TextView descricaoCalculo;
        final ImageView imagemCalculo;

        MyViewHolder(View itemView) {
            super(itemView);

            tituloCalculo = (TextView) itemView.findViewById(R.id.tv_title);
            descricaoCalculo = (TextView) itemView.findViewById(R.id.tv_description);
            imagemCalculo = (ImageView) itemView.findViewById(R.id.iv_simcalc);

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
