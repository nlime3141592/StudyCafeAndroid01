package study.customer.gui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;


import com.example.mysecondproject.R;
import com.google.android.material.navigation.NavigationBarView;

import study.customer.gui.HomeFragment;
import study.customer.gui.InfoFragment;
import study.customer.gui.ReservationFragment;

public class MainActivity extends AppCompatActivity {
    private HomeFragment homeFragment;
    private InfoFragment infoFragment;
    private ReservationFragment reservationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        infoFragment = new InfoFragment();
        reservationFragment   = new ReservationFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.setting) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, reservationFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.info) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, infoFragment).commit();
                    return true;
                }

                return false;
            }
        });
    }
}