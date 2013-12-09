package ca.sulli.eriel;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ErielLauncher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eriel_launcher);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eriel_launcher, menu);
        return true;
    }
    
}
