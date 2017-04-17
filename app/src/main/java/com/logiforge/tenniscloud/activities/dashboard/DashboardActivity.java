package com.logiforge.tenniscloud.activities.dashboard;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.editleaguematch.EditLeagueMatchActivity;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
            MatchListFragment.OnFragmentInteractionListener,
            LeagueListFragment.OnFragmentInteractionListener,
            View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // pager setup
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new DashboardPagerAdapter(getSupportFragmentManager(),
               DashboardActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        /*
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab(), true);
        tabLayout.addTab(tabLayout.newTab(), false);
        int tabCount = tabLayout.getTabCount();
        for(int i = 0; i < tabCount; i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.act_dashboard_tab);
            View customView = tab.getCustomView();
            TextView textView = (TextView)customView.findViewById(R.id.text1);
            textView.setText(""+i*10);

            ImageView staticImgView = (ImageView)customView.findViewById(R.id.static_image);
            ColorStateList colours = getResources()
                    .getColorStateList(R.color.tab_icon_colors);
            Drawable d = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_menu_camera)); // DrawableCompat.wrap(staticImgView.getDrawable());
            DrawableCompat.setTintList(d.mutate(), colours);
            staticImgView.setImageDrawable(d);
            if(i==0) {
                customView.setSelected(true);
            }

            ImageView dynImgView = (ImageView)customView.findViewById(R.id.dynamic_image);

            dynImgView.post(new MyRunnable(dynImgView) );
        }
        */
    }

    /*
    static class MyRunnable implements Runnable {
        ImageView dynImgView;
        public MyRunnable(ImageView dynImgView) {
            this.dynImgView = dynImgView;
        }

        public void run() {
            int w = dynImgView.getMeasuredWidth();
            int h = dynImgView.getMeasuredHeight();
            if(w != 0 || h != 0) {
                Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                canvas.drawCircle(w / 2, h / 2, (int) (0.7 * (Math.min(h, w) / 2)), paint);
                dynImgView.setImageBitmap(bitmap);
            }
        }
    }
    */

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
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMatchListInteraction(Uri uri) {

    }

    @Override
    public void onSeasonListInteraction(Uri uri) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab) {
            FragmentManager fm = getSupportFragmentManager();
            MatchTypeDlg matchTypeDlg = MatchTypeDlg.newInstance();
            matchTypeDlg.show(fm, "act_dashboard_dlg_add");
        } else if(view.getId() == R.id.new_league_match) {
            DialogFragment matchTypeDlg = (DialogFragment)getSupportFragmentManager().findFragmentByTag("act_dashboard_dlg_add");
            if(matchTypeDlg != null) {
                matchTypeDlg.dismiss();
            }
            Intent intent = new Intent(this, EditLeagueMatchActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.new_friendly_match) {
            DialogFragment matchTypeDlg = (DialogFragment)getSupportFragmentManager().findFragmentByTag("act_dashboard_dlg_add");
            if(matchTypeDlg != null) {
                matchTypeDlg.dismiss();
            }
        }
    }
}
