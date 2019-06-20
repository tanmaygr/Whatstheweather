package com.example.tanmay.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText e1,e2;
    TextView t;
    public void go(View view){

        String result = "";
        Download download = new Download();
        InputMethodManager mgr= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(e2.getWindowToken(),0);
        try {
            result=download.execute("https://api.darksky.net/forecast/dc35c400c7cf154d42c36107d0c069ec/"+e1.getText().toString()+","+e2.getText().toString()).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class Download extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result="";
            URL url;
            HttpURLConnection httpURLConnection=null;
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data=inputStreamReader.read();
                while(data!=-1) {
                    char c = (char) data;
                    result+=c;
                    data=inputStreamReader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                String message="";
                JSONObject jsonObject=new JSONObject(result);
                String currently = jsonObject.getString("currently");
                //  Log.i("Website Content",currently);
                JSONObject a =new JSONObject(currently);
                String summary= a.getString("summary");
                String icon= a.getString("icon");
                String temp = a.getString("temperature");

                Log.i("icon",summary+" "+icon);
                t.setText(summary+" :  "+icon+"\n"+temp+" °F");

//                if(summary!="" && icon!="") {
//                    message+=summary+": "+icon+"\r\n";
//                }
//
//                if(message!=""){
//                    t.setText(message);
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1=findViewById(R.id.editText2);
        e2=findViewById(R.id.editText3);
        t=findViewById(R.id.resultTV);
    }
}
