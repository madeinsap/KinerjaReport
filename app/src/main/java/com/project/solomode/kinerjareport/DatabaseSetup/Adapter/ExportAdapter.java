package com.project.solomode.kinerjareport.DatabaseSetup.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.solomode.kinerjareport.DatabaseSetup.Models.Data;
import com.project.solomode.kinerjareport.R;

import java.util.List;

public class ExportAdapter extends RecyclerView.Adapter<ExportAdapter.MyViewHolder>{

    private Context context;
    private List<Data> dataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kegiatan;
        public TextView volume;
        public TextView satuan;
        public TextView output;
        public TextView keterangan;

        public MyViewHolder(View view) {
            super(view);
            kegiatan = view.findViewById(R.id.txt_kegiatan);
            volume = view.findViewById(R.id.txt_volume);
            satuan = view.findViewById(R.id.txt_satuan);
            output = view.findViewById(R.id.txt_output);
            keterangan = view.findViewById(R.id.txt_keterangan);
        }
    }

    public ExportAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_kinerja_export, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Data data = dataList.get(position);

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
