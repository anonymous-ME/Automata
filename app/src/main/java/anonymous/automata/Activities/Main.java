package anonymous.automata.Activities;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import anonymous.automata.CustomUI.Settings_Dialog;
import anonymous.automata.Fragments.*;
import anonymous.automata.R;


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
            final String value = settings.getString("auto_mode", "");
            Settings_Dialog sd = new Settings_Dialog(this);
            sd.show();
            sd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    // Reading from SharedPreferences
                    String value_new = settings.getString("auto_mode", "");
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    if (getCheckedItem(navigationView) == 0 & ! value.equals(value_new)) {
                        navigationView.getMenu().getItem(0).setChecked(true);
                        onNavigationItemSelected(navigationView.getMenu().getItem(0));
                    }
                }
            });
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
}
