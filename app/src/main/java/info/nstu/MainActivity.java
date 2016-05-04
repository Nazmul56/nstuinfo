package info.nstu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import info.androidhive.recyclerview.R;

public class MainActivity extends AppCompatActivity {
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;

    EditText etResponse;
    TextView tvIsConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);


         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
         fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();
            Intent i = new Intent(getApplicationContext(), ImageViewTest.class);
            startActivity(i);
        }
        });


        // check if you are connected or not

        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        }
        else{
            tvIsConnected.setText("You are NOT conncted");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(movieList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                Intent details = new Intent(MainActivity.this, DetaitlsActivity.class);
                details.putExtra("url", movie.getUrl());
                startActivity(details);


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        if(isConnected() ){
            prepareMovieData();
        }
        else{
            OffLineData();
        }


    }


    /** Check Internet Connection*/
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void prepareMovieData() {

        new HttpAsyncTask().execute("http://nazmul56.github.io/nget.json");

   /*     Movie movie = new Movie("Mad Max: Fury Road", "Action & Adventure", "2015");
        movieList.add(movie);

       */

    }


    /**JSON Step 2 */
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    /** JSON Step 2 InputStrream to String Converter*/
    /** Parse Data Form Input Stream */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result; ///this feed pass the result string to onPostExecute.

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                JSONObject json = new JSONObject(result);
                String str = "";

                String file_name = "json_string";
                String data = "\n" +
                        "{\n" +
                        "  \"articleList\": [\n" +
                        "    {\n" +
                        "      \"title\": \"Android Simple Html view\",\n" +
                        "      \"url\": \"<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<title>Page Title</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>This is a Heading</h1>\n" +
                        "<p>This is a paragraph.</p>\n" +
                        "</body>\n" +
                        "</html>\",\n" +
                        "      \"categories\": [\n" +
                        "        \"Android\"\n" +
                        "      ],\n" +
                        "      \"tags\": [\n" +
                        "        \"android\",\n" +
                        "        \"httpclient\",\n" +
                        "        \"internet\"\n" +
                        "      ]\n" +
                        "    },\n" +
                        "     {\n" +
                        "      \"title\": \"Android Html view with CSS, Java Script\",\n" +
                        "      \"url\": \"<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<body>\n" +
                        "<p id=\\\"demo\\\">Click the button to change the layout of this paragraph</p>\n" +
                        "<script>\n" +
                        "function myFunction() \\{\n" +
                        "    var x = document.getElementById(\\\"demo\\\");\n" +
                        "    x.style.fontSize = \\\"25px\\\"; \n" +
                        "    x.style.color = \\\"red\\\"; \n" +
                        "\\}\n" +
                        "</script>\n" +
                        "<button onclick=\\\"myFunction()\\\">Click Me!</button>\n" +
                        "</body>\n" +
                        "</html>\",\n" +
                        "      \"categories\": [\n" +
                        "        \"Android\"\n" +
                        "      ],\n" +
                        "      \"tags\": [\n" +
                        "        \"android\",\n" +
                        "        \"httpclient\",\n" +
                        "        \"internet\"\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"title\": \" Android Html View with CSS \",\n" +
                        "      \"url\": \"<html>\n" +
                        "<head>\n" +
                        "<style>\n" +
                        "body {\n" +
                        "    background-color: #d0e4fe;\n" +
                        "}\n" +
                        "h1 {\n" +
                        "    color: orange;\n" +
                        "    text-align: center;\n" +
                        "}\n" +
                        "p {\n" +
                        "    font-family: \\\"Times New Roman\\\";\n" +
                        "    font-size: 20px;\n" +
                        "}\n" +
                        "</style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>My First CSS Example</h1>\n" +
                        "<p>This is a paragraph.</p>\n" +
                        "</body>\n" +
                        "</html>\n" +
                        "\",\n" +
                        "      \"categories\": [\n" +
                        "        \"Android\"\n" +
                        "      ],\n" +
                        "      \"tags\": [\n" +
                        "        \"android\",\n" +
                        "        \"camera\"\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"title\": \"Message From Developer\",\n" +
                        "      \"url\": \"<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "    <head>\n" +
                        "        <meta charset=\\\"ISO-8859-1\\\"> \n" +
                        "<style type=\\\"text/css\\\">.infoclass\\{ font-size:15px;padding:8px; margin-bottom:3px; border-radius: 3px;  background-color:white;-webkit-box-shadow: inset 0px 0px 2px 0px rgba(49, 50, 50, 0.67);\n" +
                        "-moz-box-shadow:    inset 0px 0px 2px 0px rgba(49, 50, 50, 0.67);\n" +
                        "box-shadow:         inset 0px 0px 2px 0px rgba(49, 50, 50, 0.67);\\}\n" +
                        "body\\{background-color:#f5f5f5; font-family:serif; line-height:1.3em; border:3px solid #29b6f6; \tborder-radius: 6px; padding:3px; padding-top:8px \\}\n" +
                        "p\\{font-size:15px\\}\n" +
                        ".headertitle\\{width:100%; font-size: 17px; background:#29b6f6; color:#FFFFFF; border-radius: 3px; text-align: center\\}\n" +
                        ".maintitle\\{font-weight:bold;width:100%; font-size: 17px; background:#039be5; color:#FFFFFF; border-radius: 15px;padding-top:10px; padding-bottom:10px; text-align: center; margin-bottom:10px\\}\n" +
                        ".telephone \\{\n" +
                        "    background-image: url(\\\"http://i61.tinypic.com/n6xtmw.png\\\");\n" +
                        "    background-position: 0 0px;\n" +
                        "    background-repeat: no-repeat;\n" +
                        "    background-size: 20px auto;\n" +
                        "    color: black;\n" +
                        "    padding-bottom: 3px;\n" +
                        "    padding-left: 22px;\n" +
                        "    text-decoration: none;\n" +
                        "    padding-top: 2px;\n" +
                        "\\}\n" +
                        ".telephone:hover\\{color:#29b6f6\\}\n" +
                        ".email \\{\n" +
                        "    background-image: url(\\\"http://i60.tinypic.com/dqm2cy.png\\\");\n" +
                        "    background-position: 3px 3px;\n" +
                        "    background-repeat: no-repeat;\n" +
                        "    background-size: 18px auto;\n" +
                        "    color: black;\n" +
                        "    padding-bottom: 3px;\n" +
                        "    padding-left: 22px;\n" +
                        "    padding-top: 2px;\n" +
                        "    text-decoration: none;\n" +
                        "\\}\n" +
                        ".email:hover\\{color:#29b6f6\\}\n" +
                        "</style>\n" +
                        "    </head>\n" +
                        "    <body>\n" +
                        "<div class=\\\"maintitle\\\">Conclusion</div>\n" +
                        "<div class=\\\"infoclass\\\">শিক্ষকের একান্ত সদিচ্ছা থাকার কারনে NSTUinfo আবার আপডেট করা হল। এবারো ২০১৫ সালের ডায়রী অনুসারে আপডেট করা হয়েছে। তবুও নতুন যেসব পদে পরিবর্তন হয়েছে সেগুলোও আপডেট করার চেষ্টা করা হয়েছে। আজ যদি অ্যাপ পাবলিশও হয় চেক করলে দেখা যাবে গত ১ সপ্তাহেই অনেক পরিবর্তন হয়ে গিয়েছে। একটা ভার্সিটিতে এরকম পরিবর্তন হবেই। আন-অফিসিয়ালভাবে এত কিছুর ট্র্যাক রাখা অনেক কষ্টকর। ধন্যবাদ সবাইকে।&nbsp;আর এই অ্যাপ এ ভুল থাকতে পারে এটা স্বাভাবিক। কোন ভুল ধরা পড়লে nstuinfoandroid@gmail.com এ জানানোর অনুরোধ রইল।</div>\n" +
                        "<div class=\\\"headertitle\\\">Contribute on this project</div>\n" +
                        "<p>&nbsp;</p>\n" +
                        "<div class=\\\"infoclass\\\">এবার সোর্সকোড আর ওপেন দেয়া হচ্ছে না। যারা আগ্রহী তারা গুগলে একটু সার্চ করলেই খুজে পাবেন। গিটহাবে রাখা আছে। কেউ ফর্ক করে পুল রিকোয়েস্ট পাঠালে একসেপ্ট করব কথা দিলাম।</div>\n" +
                        "<div class=\\\"infoclass\\\"><strong>Favorite Quotes:</strong> অল্পবিদ্যা ভয়ংকর। আপনি ঠিকমত জানেনই না কিভাবে একটা কিছু নিয়ে কাজ করতে হয়, তার আগেই শুরু করে দিলেন সমালোচনা, দিন শেষে আপনিই ঠকলেন। কারন সারাটা জীবন আপনার অন্যের সমালোচনা করতে করতেই কাটবে। নিজের জন্য কিছু করা হবে না। নিজেকে এত জ্ঞানী না ভেবে সমালোচনা করার আগে কোন একটা কাজ দেখে বুঝতে চেষ্টা করুন আপনি নিজে কি আসলেই পারবেন কিনা এ পর্যন্ত করতে? মনে রাখবেন অন্যের সমালোচনাকারীরা সকল কাজকে সহজ ভেবে, কোন কাজ কমপ্লিট করতে আগেও কখনও পারে নি। ভবিষৎতেও পারবে না।</div>\n" +
                        "            \n" +
                        "    </body>\n" +
                        "</html>\",\n" +
                        "      \"categories\": [\n" +
                        "        \"Android\"\n" +
                        "      ],\n" +
                        "      \"tags\": [\n" +
                        "        \"android\",\n" +
                        "        \"httpclient\",\n" +
                        "        \"internet\"\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";

                // str=json.toString();

                try {
                    new ReadWriteJsonFileUtils(getBaseContext()).createJsonFileData(file_name, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String jsonString = new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name);



                //JSONObject json = new JSONObject(jsonString);

               // JSONArray article_storage = json_storage.getJSONArray("articleList");




                JSONArray articles = json.getJSONArray("articleList");
                str += "articles length = "+json.getJSONArray("articleList").length();//This section find the articleList
                str += "\n--------\n";
                str += "names: "+articles.getJSONObject(0).names();
                str += "\n--------\n";
                str += "url: "+articles.getJSONObject(1).getString("url");

                int jasonObjecLenth =json.getJSONArray("articleList").length();
                for(int i = 0; i<jasonObjecLenth;i++) {

                    Movie movie = new Movie(articles.getJSONObject(i).getString("title") , articles.getJSONObject(i).getString("categories"), "", articles.getJSONObject(i).getString("url"));
                    movieList.add(movie);

                }

                mAdapter.notifyDataSetChanged();
               // etResponse.setText(str);
                //etResponse.setText(json.toString(1));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void OffLineData(){

        Toast.makeText(getBaseContext(), "OffLine Data!", Toast.LENGTH_LONG).show();
        try {

            String str = "";

            String file_name = "json_string";
            String data = "\n" +
                    "{\n" +
                    "  \"articleList\": [\n" +
                    "    {\n" +
                    "      \"title\": \"Android Simple Html view\",\n" +
                    "      \"url\": \"<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>Page Title</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>This is a Heading</h1>\n" +
                    "<p>This is a paragraph.</p>\n" +
                    "</body>\n" +
                    "</html>\",\n" +
                    "      \"categories\": [\n" +
                    "        \"Android\"\n" +
                    "      ],\n" +
                    "      \"tags\": [\n" +
                    "        \"android\",\n" +
                    "        \"httpclient\",\n" +
                    "        \"internet\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "     {\n" +
                    "      \"title\": \"Android Html view with CSS, Java Script\",\n" +
                    "      \"url\": \"<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "<p id=\\\"demo\\\">Click the button to change the layout of this paragraph</p>\n" +
                    "<script>\n" +
                    "function myFunction() \\{\n" +
                    "    var x = document.getElementById(\\\"demo\\\");\n" +
                    "    x.style.fontSize = \\\"25px\\\"; \n" +
                    "    x.style.color = \\\"red\\\"; \n" +
                    "\\}\n" +
                    "</script>\n" +
                    "<button onclick=\\\"myFunction()\\\">Click Me!</button>\n" +
                    "</body>\n" +
                    "</html>\",\n" +
                    "      \"categories\": [\n" +
                    "        \"Android\"\n" +
                    "      ],\n" +
                    "      \"tags\": [\n" +
                    "        \"android\",\n" +
                    "        \"httpclient\",\n" +
                    "        \"internet\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"title\": \" Android Html View with CSS \",\n" +
                    "      \"url\": \"<html>\n" +
                    "<head>\n" +
                    "<style>\n" +
                    "body {\n" +
                    "    background-color: #d0e4fe;\n" +
                    "}\n" +
                    "h1 {\n" +
                    "    color: orange;\n" +
                    "    text-align: center;\n" +
                    "}\n" +
                    "p {\n" +
                    "    font-family: \\\"Times New Roman\\\";\n" +
                    "    font-size: 20px;\n" +
                    "}\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>My First CSS Example</h1>\n" +
                    "<p>This is a paragraph.</p>\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "\",\n" +
                    "      \"categories\": [\n" +
                    "        \"Android\"\n" +
                    "      ],\n" +
                    "      \"tags\": [\n" +
                    "        \"android\",\n" +
                    "        \"camera\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"title\": \"Message From Developer\",\n" +
                    "      \"url\": \"<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <meta charset=\\\"ISO-8859-1\\\"> \n" +
                    "<style type=\\\"text/css\\\">.infoclass\\{ font-size:15px;padding:8px; margin-bottom:3px; border-radius: 3px;  background-color:white;-webkit-box-shadow: inset 0px 0px 2px 0px rgba(49, 50, 50, 0.67);\n" +
                    "-moz-box-shadow:    inset 0px 0px 2px 0px rgba(49, 50, 50, 0.67);\n" +
                    "box-shadow:         inset 0px 0px 2px 0px rgba(49, 50, 50, 0.67);\\}\n" +
                    "body\\{background-color:#f5f5f5; font-family:serif; line-height:1.3em; border:3px solid #29b6f6; \tborder-radius: 6px; padding:3px; padding-top:8px \\}\n" +
                    "p\\{font-size:15px\\}\n" +
                    ".headertitle\\{width:100%; font-size: 17px; background:#29b6f6; color:#FFFFFF; border-radius: 3px; text-align: center\\}\n" +
                    ".maintitle\\{font-weight:bold;width:100%; font-size: 17px; background:#039be5; color:#FFFFFF; border-radius: 15px;padding-top:10px; padding-bottom:10px; text-align: center; margin-bottom:10px\\}\n" +
                    ".telephone \\{\n" +
                    "    background-image: url(\\\"http://i61.tinypic.com/n6xtmw.png\\\");\n" +
                    "    background-position: 0 0px;\n" +
                    "    background-repeat: no-repeat;\n" +
                    "    background-size: 20px auto;\n" +
                    "    color: black;\n" +
                    "    padding-bottom: 3px;\n" +
                    "    padding-left: 22px;\n" +
                    "    text-decoration: none;\n" +
                    "    padding-top: 2px;\n" +
                    "\\}\n" +
                    ".telephone:hover\\{color:#29b6f6\\}\n" +
                    ".email \\{\n" +
                    "    background-image: url(\\\"http://i60.tinypic.com/dqm2cy.png\\\");\n" +
                    "    background-position: 3px 3px;\n" +
                    "    background-repeat: no-repeat;\n" +
                    "    background-size: 18px auto;\n" +
                    "    color: black;\n" +
                    "    padding-bottom: 3px;\n" +
                    "    padding-left: 22px;\n" +
                    "    padding-top: 2px;\n" +
                    "    text-decoration: none;\n" +
                    "\\}\n" +
                    ".email:hover\\{color:#29b6f6\\}\n" +
                    "</style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "<div class=\\\"maintitle\\\">Conclusion</div>\n" +
                    "<div class=\\\"infoclass\\\">শিক্ষকের একান্ত সদিচ্ছা থাকার কারনে NSTUinfo আবার আপডেট করা হল। এবারো ২০১৫ সালের ডায়রী অনুসারে আপডেট করা হয়েছে। তবুও নতুন যেসব পদে পরিবর্তন হয়েছে সেগুলোও আপডেট করার চেষ্টা করা হয়েছে। আজ যদি অ্যাপ পাবলিশও হয় চেক করলে দেখা যাবে গত ১ সপ্তাহেই অনেক পরিবর্তন হয়ে গিয়েছে। একটা ভার্সিটিতে এরকম পরিবর্তন হবেই। আন-অফিসিয়ালভাবে এত কিছুর ট্র্যাক রাখা অনেক কষ্টকর। ধন্যবাদ সবাইকে।&nbsp;আর এই অ্যাপ এ ভুল থাকতে পারে এটা স্বাভাবিক। কোন ভুল ধরা পড়লে nstuinfoandroid@gmail.com এ জানানোর অনুরোধ রইল।</div>\n" +
                    "<div class=\\\"headertitle\\\">Contribute on this project</div>\n" +
                    "<p>&nbsp;</p>\n" +
                    "<div class=\\\"infoclass\\\">এবার সোর্সকোড আর ওপেন দেয়া হচ্ছে না। যারা আগ্রহী তারা গুগলে একটু সার্চ করলেই খুজে পাবেন। গিটহাবে রাখা আছে। কেউ ফর্ক করে পুল রিকোয়েস্ট পাঠালে একসেপ্ট করব কথা দিলাম।</div>\n" +
                    "<div class=\\\"infoclass\\\"><strong>Favorite Quotes:</strong> অল্পবিদ্যা ভয়ংকর। আপনি ঠিকমত জানেনই না কিভাবে একটা কিছু নিয়ে কাজ করতে হয়, তার আগেই শুরু করে দিলেন সমালোচনা, দিন শেষে আপনিই ঠকলেন। কারন সারাটা জীবন আপনার অন্যের সমালোচনা করতে করতেই কাটবে। নিজের জন্য কিছু করা হবে না। নিজেকে এত জ্ঞানী না ভেবে সমালোচনা করার আগে কোন একটা কাজ দেখে বুঝতে চেষ্টা করুন আপনি নিজে কি আসলেই পারবেন কিনা এ পর্যন্ত করতে? মনে রাখবেন অন্যের সমালোচনাকারীরা সকল কাজকে সহজ ভেবে, কোন কাজ কমপ্লিট করতে আগেও কখনও পারে নি। ভবিষৎতেও পারবে না।</div>\n" +
                    "            \n" +
                    "    </body>\n" +
                    "</html>\",\n" +
                    "      \"categories\": [\n" +
                    "        \"Android\"\n" +
                    "      ],\n" +
                    "      \"tags\": [\n" +
                    "        \"android\",\n" +
                    "        \"httpclient\",\n" +
                    "        \"internet\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            // str=json.toString();

            try {
                new ReadWriteJsonFileUtils(getBaseContext()).createJsonFileData(file_name, data);
            } catch (Exception e) {
                e.printStackTrace();
            }


            String jsonString = new ReadWriteJsonFileUtils(getBaseContext()).readJsonFileData(file_name);



            JSONObject json = new JSONObject(jsonString);

            // JSONArray article_storage = json_storage.getJSONArray("articleList");




            JSONArray articles = json.getJSONArray("articleList");
            str += "articles length = "+json.getJSONArray("articleList").length();//This section find the articleList
            str += "\n--------\n";
            str += "names: "+articles.getJSONObject(0).names();
            str += "\n--------\n";
            str += "url: "+articles.getJSONObject(1).getString("url");

            int jasonObjecLenth =json.getJSONArray("articleList").length();
            for(int i = 0; i<jasonObjecLenth;i++) {

                Movie movie = new Movie(articles.getJSONObject(i).getString("title") , articles.getJSONObject(i).getString("categories"), "", articles.getJSONObject(i).getString("url"));
                movieList.add(movie);

            }

            mAdapter.notifyDataSetChanged();
            // etResponse.setText(str);
            //etResponse.setText(json.toString(1));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }





    /** Store Data into file*/
    public class ReadWriteJsonFileUtils {
        Activity activity;
        Context context;

        public ReadWriteJsonFileUtils(Context context) {
            this.context = context;
        }

        public void createJsonFileData(String filename, String mJsonResponse) {
            try {
                File checkFile = new File(context.getApplicationInfo().dataDir + "/new_directory_name/");
                if (!checkFile.exists()) {
                    checkFile.mkdir();
                }
                FileWriter file = new FileWriter(checkFile.getAbsolutePath() + "/" + filename);
                file.write(mJsonResponse);
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String readJsonFileData(String filename) {
            try {
                File f = new File(context.getApplicationInfo().dataDir + "/new_directory_name/" + filename);
                if (!f.exists()) {
                   // onNoResult();
                    return null;
                }
                FileInputStream is = new FileInputStream(f);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                return new String(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
           // onNoResult();
            return null;
        }

        public void deleteFile() {
            File f = new File(context.getApplicationInfo().dataDir + "/new_directory_name/");
            File[] files = f.listFiles();
            for (File fInDir : files) {
                fInDir.delete();
            }
        }

        public void deleteFile(String fileName) {
            File f = new File(context.getApplicationInfo().dataDir + "/new_directory_name/" + fileName);
            if (f.exists()) {
                f.delete();
            }
        }
    }

    private String getHtmlData(Context context, String data)
    {
        String head = "<head><style>@font-face {font-family: 'verdana';src: url('file:///android_asset/fonts/verdana.ttf');}body {width=600;height=1024;margin:10px;font-family:'verdana';font-size:12px}</style></head>";
        String htmlData= "<html>"+head+"<body>"+data+"</body></html>" ;
        return htmlData;
    }





    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }






}
