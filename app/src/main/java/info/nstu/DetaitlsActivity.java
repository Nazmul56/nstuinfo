package info.nstu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import info.androidhive.recyclerview.R;

public class DetaitlsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaitls);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String Title= getIntent().getStringExtra("title");
        toolbar.setTitle(Title);
        setSupportActionBar(toolbar);




        TextView URLtv = (TextView)findViewById(R.id.urltv);
        WebView detatils_wv = (WebView)findViewById(R.id.webview);

        detatils_wv.getSettings().setJavaScriptEnabled(true);
        String UrlFrom_MainActivity= getIntent().getStringExtra("url");
       // URLtv.setText(UrlFrom_MainActivity);

       // detatils_wv.set

        detatils_wv.loadDataWithBaseURL(null, UrlFrom_MainActivity, "text/html", "utf-8","about:blank");

        URLtv.setText(Html.fromHtml(UrlFrom_MainActivity));

        // Making url clickable
        URLtv.setMovementMethod(LinkMovementMethod.getInstance());
        URLtv.setVisibility(View.VISIBLE);

      /**  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
      .setAction("Action", null).show();
      }
      });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
