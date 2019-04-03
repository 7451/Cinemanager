package net.lzzy.cinemanager.activities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.view.Window;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.CinemasOrderFragment;

/**
 * @author Administrator
 */
public class CinemasOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cinemas_order);
        String cinemaId=getIntent().getStringExtra(MainActivity.EXTRA_CINEMA_ID);
        FragmentManager manager=getSupportFragmentManager();
        Fragment fragment=manager.findFragmentById(R.id.cinema_orders_container);
        if (fragment==null){
            fragment=CinemasOrderFragment.newInstance(cinemaId);
            manager.beginTransaction().add(R.id.cinema_orders_container,fragment).commit();
        }


    }

}
