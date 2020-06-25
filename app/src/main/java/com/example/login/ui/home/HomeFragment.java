package com.example.login.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.login.R;
import com.github.chrisbanes.photoview.PhotoView;

import org.jsoup.Jsoup;

public class HomeFragment extends Fragment {

    private PhotoView i1,i2,i4,i3,i5,i6;
    private Button b;
    private Fragment fragment;
    private TextView cnfcases,deaths,recovery,active,st;
    private String state;
    private Spinner spinner;

    private String url = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/India_COVID-19_cases_density_map.svg/768px-India_COVID-19_cases_density_map.svg.png";
    private String url1 = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/COVID-19_India_Total_Cases_Animated_Map.gif/800px-COVID-19_India_Total_Cases_Animated_Map.gif";
    private String url2 = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/India_COVID-19_confirmed_cases_map.svg/768px-India_COVID-19_confirmed_cases_map.svg.png";
    private String url3 = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/India_COVID-19_deaths_map.svg/800px-India_COVID-19_deaths_map.svg.png";
    private String url4 = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/India_COVID-19_active_cases_map.svg/800px-India_COVID-19_active_cases_map.svg.png";
    private String url5 = "https://www.deccanherald.com/sites/dh/files/article_images/2020/05/21/photo_2020-05-21_12-48-09.jpg";
    private String s1 = "https://en.wikipedia.org/wiki/Template:COVID-19_pandemic_data/India_medical_cases_by_state_and_union_territory#Total_confirmed_cases,_active_cases,_recoveries_and_deaths";
    private String url6 = "https://en.wikipedia.org/api/rest_v1/page/graph/png/COVID-19_pandemic_in_India/0/e94488d426b09c386768e02269cc209c54b1837e.png";

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        i1 = root.findViewById(R.id.imageView1);
        b = (Button)getActivity().findViewById(R.id.button3);
        st=(TextView)root.findViewById(R.id.textView);
        cnfcases=(TextView)root.findViewById(R.id.cnfcases);
        deaths=(TextView)root.findViewById(R.id.deaths);
        recovery=(TextView)root.findViewById(R.id.recovery);
        active=(TextView)root.findViewById(R.id.active);

        spinner = (Spinner) root.findViewById(R.id.spinner_state);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.stateList));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {


                state = parent.getItemAtPosition(position).toString();
                if(state.equals("Select Your Region")){
                    cnfcases.setText("");
                    deaths.setText("");
                    recovery.setText("");
                    active.setText("");
                }
                else{
                    if(state.equals("India")){
                        state = "36";
                    }
                    new doit().execute();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "Please select the State", Toast.LENGTH_SHORT).show();
            }
        });












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
        i6 = root.findViewById(R.id.imageView6);
        Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.NONE ).skipMemoryCache(true).into(i1);
        Glide.with(getContext()).load(url4).diskCacheStrategy(DiskCacheStrategy.NONE ).skipMemoryCache(true).into(i3);
        Glide.with(getContext()).load(url1).diskCacheStrategy(DiskCacheStrategy.NONE ).skipMemoryCache(true).into(i2);
        Glide.with(getContext()).load(url3).diskCacheStrategy(DiskCacheStrategy.NONE ).skipMemoryCache(true).into(i4);
        Glide.with(getContext()).load(url5).diskCacheStrategy(DiskCacheStrategy.NONE ).skipMemoryCache(true).into(i5);
        //Glide.with(getContext()).load(url6).diskCacheStrategy(DiskCacheStrategy.NONE ).skipMemoryCache(true).into(i6);
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
                int x= words.indexOf(state)+state.length()+1;
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

    public String getState(String state){
        int x = state.length();
        x-=15;
        String res="";
        while(state.charAt(x)!=','){
            res = state.charAt(x) + res;
            x--;
        }
        return res.substring(1);
    }

}