package anonymous.automata.Activities;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import anonymous.automata.CustomUI.Settings_Dialog;
import anonymous.automata.Fragments.About;
import anonymous.automata.Fragments.ControlCenter;
import anonymous.automata.Fragments.Monitor;
import anonymous.automata.R;
import cz.msebera.android.httpclient.Header;


/**
 * Created by affan on 20/8/17.
 */

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            // on first time to display view for first navigation item based on the number
            navigationView.getMenu().getItem(0).setChecked(true);
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            // Reading from SharedPreferences
            final SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
            final String value_mode = settings.getString("auto_mode", "");
            final String value_ip = settings.getString("server_ip", "");
            Settings_Dialog sd = new Settings_Dialog(this);
            sd.show();
            sd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    // Reading from SharedPreferences
                    String valueMode_new = settings.getString("auto_mode", "");
                    String valueIP_new = settings.getString("server_ip", "");
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    if (getCheckedItem(navigationView) == 0 & ! value_mode.equals(valueMode_new)) {
                        navigationView.getMenu().getItem(0).setChecked(true);
                        onNavigationItemSelected(navigationView.getMenu().getItem(0));
                    }
                    if (!value_ip.equals(valueIP_new)) {
                        //Update UI
                        navigationView.getMenu().getItem(getCheckedItem(navigationView)).setChecked(true);
                        onNavigationItemSelected(navigationView.getMenu().getItem(getCheckedItem(navigationView)));
                        //Update Mode On Server
                        AsyncHttpClient client = new AsyncHttpClient();
                        if(valueMode_new.equals("1"))
                            client.get(valueIP_new+":3000/automatic",new AsyncHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                }
                            });
                        else
                            client.get(valueIP_new+":3000/manual",new AsyncHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                }
                            });
                    }
                }
            });
        } else if (id == R.id.voice) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

            final SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
            final String value_mode = settings.getString("auto_mode", "");

            if(value_mode.equals("1"))
                MDToast.makeText(this,"Can't use voice command in automatic mode!!",MDToast.LENGTH_LONG,MDToast.TYPE_INFO).show();
            else if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 10);
            } else {
                MDToast.makeText(this,"Your Device Don't Support Speech Input",MDToast.LENGTH_LONG,MDToast.TYPE_INFO).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private int getCheckedItem(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isChecked()) {
                return i;
            }
        }

        return -1;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();

        if (id == R.id.cc) {
            fm.beginTransaction()
                    .replace(R.id.content_frame , new ControlCenter())
                    .commit();
        }else if (id == R.id.mon) {
            fm.beginTransaction()
                    .replace(R.id.content_frame , new Monitor())
                    .commit();
        }else if (id == R.id.about) {
            fm.beginTransaction()
                    .replace(R.id.content_frame , new About())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        final String value_ip = settings.getString("server_ip", "");

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    String cmd = result.get(0).toLowerCase();
                    if ( cmd.contains("fan") ) {
                        if( cmd.contains("off") ) {
                            // Off
                            MDToast.makeText(this,"Turning off fan...",MDToast.LENGTH_LONG,MDToast.TYPE_INFO).show();

                            AsyncHttpClient client = new AsyncHttpClient();
                            client.get(value_ip+":3000/fan/1",new AsyncHttpResponseHandler(){

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    if (getCheckedItem(navigationView) == 0) {
                                        try {
                                            TimeUnit.SECONDS.sleep(2);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        navigationView.getMenu().getItem(0).setChecked(true);
                                        onNavigationItemSelected(navigationView.getMenu().getItem(0));
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                }
                            });

                        } else {
                            // On
                            MDToast.makeText(this,"Turning on fan...",MDToast.LENGTH_LONG,MDToast.TYPE_INFO).show();
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.get(value_ip+":3000/fan/0",new AsyncHttpResponseHandler(){

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    if (getCheckedItem(navigationView) == 0) {
                                        try {
                                            TimeUnit.SECONDS.sleep(2);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        navigationView.getMenu().getItem(0).setChecked(true);
                                        onNavigationItemSelected(navigationView.getMenu().getItem(0));
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                }
                            });
                        }

                    } else if ( cmd.contains("light") ) {
                        if( cmd.contains("off") ) {
                            // Off
                            MDToast.makeText(this,"Turning off lights...",MDToast.LENGTH_LONG,MDToast.TYPE_INFO).show();
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.get(value_ip+":3000/light/1",new AsyncHttpResponseHandler(){

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    if (getCheckedItem(navigationView) == 0) {
                                        try {
                                            TimeUnit.SECONDS.sleep(2);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        navigationView.getMenu().getItem(0).setChecked(true);
                                        onNavigationItemSelected(navigationView.getMenu().getItem(0));
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                }
                            });

                        } else {
                            // On
                            MDToast.makeText(this,"Turning on lights...",MDToast.LENGTH_LONG,MDToast.TYPE_INFO).show();
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.get(value_ip+":3000/light/0",new AsyncHttpResponseHandler(){

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    if (getCheckedItem(navigationView) == 0) {
                                        try {
                                            TimeUnit.SECONDS.sleep(2);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        navigationView.getMenu().getItem(0).setChecked(true);
                                        onNavigationItemSelected(navigationView.getMenu().getItem(0));
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                }
                            });
                        }

                    }


                }
                break;
        }
    }
}
