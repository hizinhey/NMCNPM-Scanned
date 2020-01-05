package com.johnnghia.scanned.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.johnnghia.scanned.R;
import com.johnnghia.scanned.models.objects.TextFile;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<TextFile> textFiles;
    private OnItemClickListener listener;


    public interface OnItemClickListener{
        void onShareClick(int position);
        void onItemClick(int position);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MyAdapter() {
        textFiles = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        TextView tvTitle = holder.tvTitle;
        TextView tvDate = holder.tvDate;
        ImageButton ibShare = holder.ibShare;

        TextFile textFile = textFiles.get(position);

        tvTitle.setText(textFile.getTitle());
        String date = DateFormat.getDateInstance().format(textFile.getDate());
        tvDate.setText(date);
        ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShareClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return textFiles == null? 0 : textFiles.size();
    }

    //helper
    public void add(TextFile textFile){
        textFiles.add(textFile);
        notifyItemInserted(textFiles.size() - 1);
    }

    public void addAll(List<TextFile> textFileList){
        for (TextFile textFile : textFileList){
            add(textFile);
        }
    }

    public void remove(TextFile textFile) {
        int position = textFiles.indexOf(textFile);
        if (position > -1){
            textFiles.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear(){
        while (getItemCount() > 0){
            remove(getItem(0));
        }
    }

    public boolean isEmpty(){
        return getItemCount() == 0;
    }
    public TextFile getItem(int position){
        return textFiles.get(position);
    }
    
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvDate;
        private ImageButton ibShare;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
            ibShare = itemView.findViewById(R.id.ib_share);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
