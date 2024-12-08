package com.example.profolio.adapterfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profolio.R;
import com.example.profolio.modelfragment.KepanitiaanModel;
import com.example.profolio.modelfragment.PrestasiModel;

import java.util.List;

public class AdapterCVPrestasi extends RecyclerView.Adapter<AdapterCVPrestasi.CVViewHolder> {
    private List<PrestasiModel> prestasiItems;
    private Context context;

    public AdapterCVPrestasi(List<PrestasiModel> prestasiItems, Context context) {
        this.prestasiItems = prestasiItems;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterCVPrestasi.CVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View prestasiCV = inflater.inflate(R.layout.list_experience_prestasi, parent, false);
        return new CVViewHolder(prestasiCV);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCVPrestasi.CVViewHolder holder, int position) {
        PrestasiModel prestasiModel = prestasiItems.get(position);
        holder.title.setText(prestasiModel.getNamaPrestasi());
        holder.jabatan.setText(prestasiModel.getJabatanPrestasi());
        holder.periode.setText(prestasiModel.getTahunPrestasi());
        holder.deskripsi.setText(prestasiModel.getDeskripsiPrestasi());
    }

    @Override
    public int getItemCount() {
        return prestasiItems.size();
    }

    public class CVViewHolder extends RecyclerView.ViewHolder {
        TextView title, jabatan, periode, deskripsi;
        public CVViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitleCV);
            jabatan = itemView.findViewById(R.id.tvJabatanCV);
            periode = itemView.findViewById(R.id.tvPeriodeCV);
            deskripsi = itemView.findViewById(R.id.tvDeskripsiCV);
        }
    }
}
