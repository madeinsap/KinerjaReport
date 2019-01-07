package com.project.solomode.kinerjareport.HomeActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.project.solomode.kinerjareport.DatabaseSetup.Kegiatan;
import com.project.solomode.kinerjareport.R;

import java.util.List;

/**
 * Created by madeinsap on 3/12/2018.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder>{

    Context context;
    List<Kegiatan> kegiatanList;
    MyAdapterListener onClickListener;

    public DataAdapter(Context context, List<Kegiatan> kegiatanList) {

    }

    public DataAdapter(Context context, List<Kegiatan> kegiatanList, MyAdapterListener listener) {
        this.context = context;
        this.kegiatanList = kegiatanList;
        onClickListener = listener;
    }


    public interface MyAdapterListener {
        void editOnClick(View v, int position);
        void hapusOnClick(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTanggal;
        public TextView txtKegiatan;
        public TextView txtVolume;
        public TextView txtSatuan;
        public TextView txtOutput;
        public TextView txtKeterangan;
        public Button btnUbah;
        public Button btnHapus;

        public MyViewHolder(View view) {
            super(view);
            txtTanggal = view.findViewById(R.id.txt_tanggal);
            txtKegiatan = view.findViewById(R.id.txt_kegiatan);
            txtVolume = view.findViewById(R.id.txt_volume);
            txtSatuan = view.findViewById(R.id.txt_satuan);
            txtOutput = view.findViewById(R.id.txt_output);
            txtKeterangan = view.findViewById(R.id.txt_keterangan);
            btnUbah = view.findViewById(R.id.btn_edit);
            btnHapus = view.findViewById(R.id.btn_delete);

            btnUbah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.editOnClick(v, getAdapterPosition());
                }
            });

            btnHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.hapusOnClick(v, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_kinerja, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Kegiatan kegiatan = kegiatanList.get(position);

        holder.txtTanggal.setText(kegiatan.getTanggal());
        holder.txtKegiatan.setText(kegiatan.getKegiatan());
        holder.txtVolume.setText(kegiatan.getVolume());
        holder.txtSatuan.setText(kegiatan.getSatuan());
        holder.txtOutput.setText(kegiatan.getOutput());
        holder.txtKeterangan.setText(kegiatan.getKeterangan());

    }

    @Override
    public int getItemCount() {
        return kegiatanList.size();
    }

}
