package com.example.myappnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myappnews.R;
import com.example.myappnews.model.Article;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Article> articleList;
    Context context;

    static final int TYPE_CELL = 0;

    public RecyclerViewAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articleList = articles;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_CELL;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false);
                return new ViewHolderCardBig(view) {
                };
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Article article = articleList.get(position);
        if (article != null){
            switch (getItemViewType(position)) {
                case TYPE_CELL:
                    List <Long> time = new ArrayList<>();
                    //Format the data, and display the hours
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    try {
                        Date date1 = simpleDateFormat.parse(article.getWebPublicationDate());
                        Date date2 = Calendar.getInstance().getTime();
                        time = printDifference(date1, date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < time.size(); i++) {
                        if (time.get(0) == 0) {
                            ((ViewHolderCardBig) holder).textViewData.setText("Publicado a " + time.get(1) + " horas");
                        }else if (time.get(0) == 1){
                            ((ViewHolderCardBig) holder).textViewData.setText("Publicado a " + time.get(0) + " dia");
                        }
                        else if ((time.get(0) > 1)){
                            ((ViewHolderCardBig) holder).textViewData.setText("Publicado a " + time.get(0) + " dias");
                        }
                    }

                    //The title and subtile textview
                    ((ViewHolderCardBig)holder).textViewTitle.setText(article.getWebTitle());
                    ((ViewHolderCardBig)holder).textViewSubtitle.setText(article.getSectionName());

                    //The click to open the url
                    ((ViewHolderCardBig)holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Convert the String URL into a URI object (to pass into the Intent constructor)
                            Uri earthquakeUri = Uri.parse(article.getWebUrl());
                            // Create a new intent to view the earthquake URI
                            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                            // Send the intent to launch a new activity
                            context.startActivity(websiteIntent);
                        }
                    });

                    //The cicle color
                    Random rnd = new Random();
                    int color = rnd.nextInt(R.array.random_color);
                    GradientDrawable background = (GradientDrawable) ((ViewHolderCardBig)holder).imageView.getBackground();
                    background.setColor(color);

                    break;
            }
        }
    }

    public class ViewHolderCardBig extends RecyclerView.ViewHolder{
        TextView textViewData, textViewTitle, textViewSubtitle;
        LinearLayout linearLayout;
        ImageView imageView;

        public ViewHolderCardBig(@NonNull View itemView) {
            super(itemView);
            textViewData = itemView.findViewById(R.id.textview_data);
            textViewTitle = itemView.findViewById(R.id.textview_title);
            textViewSubtitle = itemView.findViewById(R.id.textview_subtitle);
            linearLayout = itemView.findViewById(R.id.linear_layout_clickable);
            imageView = itemView.findViewById(R.id.imageview_details);
        }
    }

    public List<Long> printDifference(Date startDate, Date endDate) {
        List<Long> time = new ArrayList<>();
        //milliseconds
        long diff = endDate.getTime() - startDate.getTime();

        if (diff < 0)
        {
            diff = (24 * 60 * 60 * 1000) + diff;
        }

        // Calculate difference in seconds
        long diffSeconds = diff / 1000;

        // Calculate difference in minutes
        long diffMinutes = diff / (60 * 1000);

        // Calculate difference in hours
        long diffHours = diff / (60 * 60 * 1000);

        // Calculate difference in days
        long diffDays = diff / (24 * 60 * 60 * 1000);

        time.add(diffDays);
        time.add(diffHours);
        time.add(diffMinutes);
        time.add(diffSeconds);

        Log.i("TAGG", ""+diffDays+" dias "+diffHours+" horas "+diffMinutes+" minutos "+diffSeconds+" segundos");

        return time;
    }
}