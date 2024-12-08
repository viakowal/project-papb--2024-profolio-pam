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
import com.example.profolio.modelfragment.OrganisasiModel;

import java.util.List;

public class AdapterCVKepanitiaan extends RecyclerView.Adapter<AdapterCVKepanitiaan.CVViewHolder> {
    private List<KepanitiaanModel> kepanitiaanItems;
    private Context context;

    public AdapterCVKepanitiaan(List<KepanitiaanModel> kepanitiaanItems, Context context) {
        this.kepanitiaanItems = kepanitiaanItems;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterCVKepanitiaan.CVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View kepanitiaanCV = inflater.inflate(R.layout.list_experience_kepanitiaan, parent, false);
        return new CVViewHolder(kepanitiaanCV);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCVKepanitiaan.CVViewHolder holder, int position) {
        KepanitiaanModel kepanitiaanModel = kepanitiaanItems.get(position);
        holder.title.setText(kepanitiaanModel.getNamaKepanitiaan());
        holder.jabatan.setText(kepanitiaanModel.getJabatanKepanitiaan());
        holder.periode.setText(kepanitiaanModel.getTahunKepanitiaan());
        holder.deskripsi.setText(kepanitiaanModel.getDeskripsiKepanitiaan());
    }

    @Override
    public int getItemCount() {
        return kepanitiaanItems.size();
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
