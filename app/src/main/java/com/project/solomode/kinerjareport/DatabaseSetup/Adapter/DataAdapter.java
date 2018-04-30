package com.project.solomode.kinerjareport.DatabaseSetup.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.project.solomode.kinerjareport.DatabaseSetup.Models.Data;
import com.project.solomode.kinerjareport.R;

import java.util.List;

/**
 * Created by madeinsap on 3/12/2018.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder>{

    private Context context;
    private List<Data> dataList;
    private MyAdapterListener onClickListener;

    public interface MyAdapterListener {
        void editOnClick(View v, int position);
        void hapusOnClick(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tanggal;
        public TextView kegiatan;
        public TextView volume;
        public TextView satuan;
        public TextView output;
        public TextView keterangan;
        public Button ubah;
        public Button hapus;

        public MyViewHolder(View view) {
            super(view);
            tanggal = view.findViewById(R.id.txt_tanggal);
            kegiatan = view.findViewById(R.id.txt_kegiatan);
            volume = view.findViewById(R.id.txt_volume);
            satuan = view.findViewById(R.id.txt_satuan);
            output = view.findViewById(R.id.txt_output);
            keterangan = view.findViewById(R.id.txt_keterangan);
            ubah = view.findViewById(R.id.btn_edit);
            hapus = view.findViewById(R.id.btn_delete);

            ubah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.editOnClick(v, getAdapterPosition());
                }
            });

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.hapusOnClick(v, getAdapterPosition());
                }
            });
        }
    }

    public DataAdapter(Context context, List<Data> dataList, MyAdapterListener listener) {
        this.context = context;
        this.dataList = dataList;
        onClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_kinerja, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Data data = dataList.get(position);

        holder.tanggal.setText(data.getTanggal());
        holder.kegiatan.setText(data.getKegiatan());
        holder.volume.setText(data.getVolume());
        holder.satuan.setText(data.getSatuan());
        holder.output.setText(data.getOutput());
        holder.keterangan.setText(data.getKeterangan());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
