package com.example.profolio.adapterfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profolio.R;
import com.example.profolio.modelfragment.OrganisasiModel;

import java.util.List;

public class AdapterCVOrganisasi extends RecyclerView.Adapter<AdapterCVOrganisasi.CVViewHolder> {
    private List<OrganisasiModel> organisasiItems;
    private Context context;

    public AdapterCVOrganisasi(List<OrganisasiModel> organisasiItems, Context context) {
        this.organisasiItems = organisasiItems;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterCVOrganisasi.CVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cvView = inflater.inflate(R.layout.list_experience_organisasi, parent, false);
        return new CVViewHolder(cvView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCVOrganisasi.CVViewHolder holder, int position) {
        OrganisasiModel organisasiModel = organisasiItems.get(position);
        holder.title.setText(organisasiModel.getNamaOrganisasi());
        holder.jabatan.setText(organisasiModel.getJabatanOrganisasi());
        holder.periode.setText(organisasiModel.getTahunMulaiOrganisasi() + " - " + organisasiModel.getTahunSelesaiOrganisasi());
        holder.deskripsi.setText(organisasiModel.getDeskripsiOrganisasi());

    }

    @Override
    public int getItemCount() {
        return organisasiItems.size();
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
