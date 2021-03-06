package com.livecook.livecookapp.Adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.livecook.livecookapp.Activity.LiveuserActivityy;
import com.livecook.livecookapp.Activity.ResturantPageActivity;
import com.livecook.livecookapp.Model.AllFirebaseModel;
import com.livecook.livecookapp.Model.Constants;
import com.livecook.livecookapp.Model.RoundedCornersTransformation;
import com.livecook.livecookapp.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.livecook.livecookapp.Adapter.NotificationAdapter.getTimeAgo;


public class ResturantImagetopAdapter1 extends RecyclerView.Adapter<ResturantImagetopAdapter1.MovieHolder> {
    // ArrayList<TasksDTO> data;
    ArrayList<AllFirebaseModel> data;
    Activity activity;
    String firebase_type_str;







    public ResturantImagetopAdapter1(ArrayList<AllFirebaseModel> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ResturantImagetopAdapter1.MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View root = LayoutInflater.from(activity).inflate(R.layout.resturant_item_image_top,null,false);

        return new MovieHolder(root);
    }
    @Override
    public void onBindViewHolder(@NonNull ResturantImagetopAdapter1.MovieHolder movieHolder, final int i) {



        String url="";
        Locale locale = new Locale("ar");
        String dateValue =data.get(i).getCreated_at();
        if(dateValue!=null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",locale);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            long time = 0;
            try {

                //long time = sdf.parse("2016-01-24T16:00:00.000Z").getTime();

                time = sdf.parse(dateValue).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long now = System.currentTimeMillis();

            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            // Toast.makeText(activity, ""+ago, Toast.LENGTH_SHORT).show();


            movieHolder.livetime.setText(ago.toString());
        }





        if(url.matches("") || !url.startsWith("http")||url.isEmpty())
        {////https://image.flaticon.com/icons/svg/1055/1055672.svg
            Picasso.with(activity)
                    .load(data.get(i).getLiveImage())
                    .error(R.drawable.all_image2)
                    // .resize(100,100)

                    .into((movieHolder.resturantimage));
        }
        else {
            Picasso.with(activity).load(data.get(i).getLiveImage())
                    // .resize(100,100)
                    .error(R.drawable.all_image2)



                    .into((movieHolder.resturantimage));


        }


        YoYo.with(Techniques.SlideInDown)
                .duration(1000)

                .playOn(movieHolder.resturantimage);




        movieHolder.livetitle.setText(data.get(i).getTitle());
        boolean status=data.get(i).isStatus();
        if(status){

            movieHolder.live_label.setVisibility(View.VISIBLE);
            movieHolder.live_label.setText(R.string.liveword);
            movieHolder.refersh.setVisibility(View.GONE);
            movieHolder.livestuatus.setVisibility(View.GONE);
            movieHolder.livetitle.setText(data.get(i).getTitle());
            if(dateValue!=null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",locale);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                long time = 0;
                try {

                    //long time = sdf.parse("2016-01-24T16:00:00.000Z").getTime();

                    time = sdf.parse(dateValue).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long now = System.currentTimeMillis();

                CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
                // Toast.makeText(activity, ""+ago, Toast.LENGTH_SHORT).show();


                movieHolder.livetime.setText(ago.toString());
            }





            movieHolder.resturantimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity,LiveuserActivityy.class) ;
                    intent.putExtra("classFrom", "ResturantPageActivity");
                    intent.putExtra(Constants.WatchPath,data.get(i).getWatchPath());
                    intent.putExtra(Constants.RecordPath,data.get(i).getRecordPath());
                    intent.putExtra(Constants.Steam_path,data.get(i).getLivePath());
                    intent.putExtra(Constants.firebase_name,data.get(i).getName());
                    intent.putExtra(Constants.firebase_title,data.get(i).getTitle());
                    intent.putExtra(Constants.WatchPath, data.get(i).getWatchPath());
                    intent.putExtra(Constants.Steam_path, data.get(i).getLivePath());
                    intent.putExtra(Constants.firebase_name, data.get(i).getName());
                    intent.putExtra(Constants.firebase_title, data.get(i).getTitle());
                    intent.putExtra(Constants.firebase_type, firebase_type_str);
                    intent.putExtra(Constants.cooker_resturant_firebaseid, data.get(i).getId());
                    intent.putExtra(Constants.cooker_resturant_type, data.get(i).getType());
                    intent.putExtra(Constants.RecordPath, data.get(i).getRecordPath());
                    intent.putExtra(Constants.status, data.get(i).isStatus());
                    intent.putExtra(Constants.WatchPath,data.get(i).getWatchPath());
                    intent.putExtra(Constants.RecordPath,data.get(i).getRecordPath());
                    intent.putExtra(Constants.Steam_path,data.get(i).getLivePath());
                    intent.putExtra(Constants.firebase_name,data.get(i).getName());
                    intent.putExtra(Constants.firebase_title,data.get(i).getTitle());
                    intent.putExtra(Constants.firebase_type,firebase_type_str);



                    activity.startActivity(intent);


                }
            });

        }
        else {
            movieHolder.live_label.setVisibility(View.GONE);

            movieHolder.refersh.setVisibility(View.VISIBLE);
            movieHolder.livetitle.setText(data.get(i).getTitle());
            movieHolder.livestuatus.setVisibility(View.VISIBLE);
            movieHolder.resturantimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity,LiveuserActivityy.class) ;
                    intent.putExtra(Constants.Steam_path,data.get(i).getLivePath());
                    intent.putExtra(Constants.RecordPath,data.get(i).getRecordPath());
                    intent.putExtra(Constants.WatchPath,data.get(i).getWatchPath());
                    intent.putExtra(Constants.firebase_name,data.get(i).getName());
                    intent.putExtra(Constants.firebase_title,data.get(i).getTitle());
                    intent.putExtra(Constants.firebase_type,firebase_type_str);
                    intent.putExtra(Constants.WatchPath, data.get(i).getWatchPath());
                    intent.putExtra(Constants.Steam_path, data.get(i).getLivePath());
                    intent.putExtra(Constants.firebase_name, data.get(i).getName());
                    intent.putExtra(Constants.firebase_title, data.get(i).getTitle());
                    intent.putExtra(Constants.firebase_type, firebase_type_str);
                    intent.putExtra(Constants.cooker_resturant_firebaseid, data.get(i).getId());
                    intent.putExtra(Constants.cooker_resturant_type, data.get(i).getType());
                    intent.putExtra(Constants.RecordPath, data.get(i).getRecordPath());
                    intent.putExtra(Constants.status, data.get(i).isStatus());
                    intent.putExtra(Constants.WatchPath,data.get(i).getWatchPath());
                    intent.putExtra(Constants.RecordPath,data.get(i).getRecordPath());

                    intent.putExtra(Constants.Steam_path,data.get(i).getLivePath());
                    intent.putExtra(Constants.firebase_name,data.get(i).getName());
                    intent.putExtra(Constants.firebase_title,data.get(i).getTitle());
                    intent.putExtra(Constants.firebase_type,firebase_type_str);


                    activity.startActivity(intent);
                }
            });


        }




    }


    @Override
    public int getItemCount() {

        return data.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {


        public ImageView resturantimage;
        public ImageView refersh;
        public TextView livetitle;
        public TextView livetime;
        public TextView livestuatus;
        public TextView live_label;
        public CardView cardView;







        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            resturantimage = itemView.findViewById(R.id.resturantimage);
            refersh = itemView.findViewById(R.id.refersh);
            livetitle=itemView.findViewById(R.id.livetitle);
            livestuatus=itemView.findViewById(R.id.livestuatus);
            live_label=itemView.findViewById(R.id.live_label);
            livetime=itemView.findViewById(R.id.livetime);
            cardView=itemView.findViewById(R.id.cardView);





        }
    }


    public static String ConvertTimeStampintoAgo(Long timeStamp)
    {
        try
        {
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            Locale locale = new Locale("ar");

            cal.setTimeInMillis(timeStamp);
            String date = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", cal).toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",locale);
            Date dateObj = sdf.parse(date);
            PrettyTime p = new PrettyTime();
            return p.format(dateObj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}


