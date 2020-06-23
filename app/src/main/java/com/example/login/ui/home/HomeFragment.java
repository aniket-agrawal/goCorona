package com.example.login.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.login.R;
import com.github.chrisbanes.photoview.PhotoView;

import org.jsoup.Jsoup;

public class HomeFragment extends Fragment {

    private PhotoView i1,i2,i4,i3,i5;
    private Button b;
    private Fragment fragment;
    private TextView cnfcases,deaths,recovery,active;

    private String url = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/India_COVID-19_cases_density_map.svg/768px-India_COVID-19_cases_density_map.svg.png";
    private String url1 = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/COVID-19_India_Total_Cases_Animated_Map.gif/800px-COVID-19_India_Total_Cases_Animated_Map.gif";
    private String url2 = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/India_COVID-19_confirmed_cases_map.svg/768px-India_COVID-19_confirmed_cases_map.svg.png";
    private String url3 = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/India_COVID-19_deaths_map.svg/800px-India_COVID-19_deaths_map.svg.png";
    private String url4 = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/India_COVID-19_active_cases_map.svg/800px-India_COVID-19_active_cases_map.svg.png";
    private String url5 = "https://www.deccanherald.com/sites/dh/files/article_images/2020/05/21/photo_2020-05-21_12-48-09.jpg";
    private String s1 = "https://en.wikipedia.org/wiki/Template:COVID-19_pandemic_data/India_medical_cases_by_state_and_union_territory#Total_confirmed_cases,_active_cases,_recoveries_and_deaths";

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        i1 = root.findViewById(R.id.imageView1);
        b = (Button)getActivity().findViewById(R.id.button3);
        cnfcases=(TextView)root.findViewById(R.id.cnfcases);
        deaths=(TextView)root.findViewById(R.id.deaths);
        recovery=(TextView)root.findViewById(R.id.recovery);
        active=(TextView)root.findViewById(R.id.active);
        new doit().execute();
        b.setVisibility(View.VISIBLE);
        fragment = this;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
            }
        });
        i2 = root.findViewById(R.id.imageView2);
        i3 = root.findViewById(R.id.imageView3);
        i4 = root.findViewById(R.id.imageView4);
        i5 = root.findViewById(R.id.imageView);
        Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.NONE ).skipMemoryCache(true).into(i1);
        Glide.with(getContext()).load(url4).diskCacheStrategy(DiskCacheStrategy.NONE ).skipMemoryCache(true).into(i3);
        Glide.with(getContext()).load(url1).diskCacheStrategy(DiskCacheStrategy.NONE ).skipMemoryCache(true).into(i2);
        Glide.with(getContext()).load(url3).diskCacheStrategy(DiskCacheStrategy.NONE ).skipMemoryCache(true).into(i4);
        Glide.with(getContext()).load(url5).diskCacheStrategy(DiskCacheStrategy.NONE ).skipMemoryCache(true).into(i5);
        return root;
    }

    public void load(View view) {
        new doit().execute();
    }

    public class doit extends AsyncTask<Void,Void,Void> {

        String words;
        String w="",d="",r="",a="";

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                org.jsoup.nodes.Document doc = (org.jsoup.nodes.Document) Jsoup.connect(s1).get();
                words = doc.text();
                int x= words.indexOf("Kerala")+7;
                while(words.charAt(x)!=' '){
                    if(words.charAt(x)=='['){
                        x+=3;
                        break;
                    }
                    w+=words.charAt(x);
                    x++;
                }
                x++;
                while(words.charAt(x)!=' '){
                    if(words.charAt(x)=='['){
                        x+=3;
                        break;
                    }
                    d+=words.charAt(x);
                    x++;
                }
                x++;
                while(words.charAt(x)!=' '){
                    if(words.charAt(x)=='['){
                        x+=3;
                        break;
                    }
                    r+=words.charAt(x);
                    x++;
                }
                x++;
                while(words.charAt(x)!=' '){
                    if(words.charAt(x)=='['){
                        x+=3;
                        break;
                    }
                    a+=words.charAt(x);
                    x++;
                }
                x++;
            }catch (Exception e){e.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cnfcases.setText(w);
            deaths.setText(d);
            recovery.setText(r);
            active.setText(a);

        }
    }

}