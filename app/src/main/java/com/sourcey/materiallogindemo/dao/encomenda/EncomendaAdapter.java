package com.sourcey.materiallogindemo.dao.encomenda;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.model.Encomenda;

import java.util.List;

public class EncomendaAdapter extends RecyclerView.Adapter<EncomendaAdapter.ViewHolder>{
    private List<Encomenda> encomendas;
    private Context context;

    public EncomendaAdapter(List<Encomenda> encomendas, Context context) {
        this.encomendas = encomendas;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.encomendas, parent,false);
        return new ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvProduto.setText(encomendas.get(position).getDescricaoProduto());
        holder.tvUnidadeMedida.setText("U.M: "+encomendas.get(position).getUnidadeMedida());
        holder.tvQtd.setText("QTD: "+encomendas.get(position).getQuantidade()+"");
        holder.delete.setImageResource(R.drawable.remove);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            private EncomendaDatabaseHelper encomendaDatabaseHelper;
            private int pos;
            @Override
            public void onClick(View v) {
                encomendaDatabaseHelper = new EncomendaDatabaseHelper(context);
                pos = position;
                encomendaDatabaseHelper.deleteEncomendas(encomendas.get(pos).getId());

                Toast.makeText(context, encomendas.get(pos).getDescricaoProduto()+" "+encomendas.get(pos).getId()+" removido!", Toast.LENGTH_LONG).show();

                encomendas.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, encomendas.size());
               // holder.itemView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return encomendas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProduto, tvQtd, tvUnidadeMedida;
        private ImageView delete;
        public ViewHolder(View itemView) {
            super(itemView);
            tvProduto = (TextView) itemView.findViewById(R.id.textViewProduto);
            tvQtd = (TextView) itemView.findViewById(R.id.textViewQtd);
            tvUnidadeMedida = (TextView) itemView.findViewById(R.id.textViewUm);
            delete = (ImageView) itemView.findViewById(R.id.removeProduct);
        }
    }


}
