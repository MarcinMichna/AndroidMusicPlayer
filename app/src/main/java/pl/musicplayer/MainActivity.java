package pl.musicplayer;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.musicplayer.fragments.PlayerFragment;
import pl.musicplayer.fragments.SearchFragment;
import pl.musicplayer.fragments.SongListFragment;
import pl.musicplayer.services.MusicService;

import static pl.musicplayer.fragments.SearchFragment.searchPhrase;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeView();
        getSupportActionBar().hide();


        startService();
        requestFileReadWritePermissions();
//        ActivityCompat.requestPermissions(MainActivity.this,
//                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
//                    List<Song> tmp = SongRepository.songs;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void requestFileReadWritePermissions() {
        System.out.println("REQ");
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("IF 1");
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("IF 2");
            finish();
            System.exit(0);
        }

    }

    public void startService()
    {
        if(!isServiceRunning())
        {
            Intent serviceIntent = new Intent(this, MusicService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }

    private boolean isServiceRunning()
    {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if("pl.musicplayer.services.MusicService".equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

    public void initializeView()
    {
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            loadFragment(R.id.fragment_container, new PlayerFragment());
            setContentView(R.layout.activity_main);

            BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
            navigation.setOnNavigationItemSelectedListener(this);
        } else
        {
            loadFragment(R.id.fragment_container_player, new PlayerFragment());
            loadFragment(R.id.fragment_container_search, new SearchFragment());
            loadFragment(R.id.fragment_container_list, new SongListFragment());
            setContentView(R.layout.activity_main_horizontal);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        initializeView();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Fragment fragment = null;
        int orientation = getResources().getConfiguration().orientation;

        switch(item.getItemId())
        {
            case R.id.player:
                fragment = new PlayerFragment();
                break;

            case R.id.songs:
                fragment = new SongListFragment();
                searchPhrase = null;
                break;

            case R.id.search:
                fragment = new SearchFragment();
                break;
        }
        if(orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            return loadFragment(R.id.fragment_container, fragment);
        } else
        {
            return loadFragment(R.id.fragment_container_player, fragment);
        }
    }

    private boolean loadFragment(int container, Fragment fragment)
    {
        int orientation = getResources().getConfiguration().orientation;

        if(fragment != null)
        {
            if(orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(container, fragment)
                        .commit();
            } else
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(container, fragment)
                        .commit();
            }
            return true;
        }
        return false;
    }
}
