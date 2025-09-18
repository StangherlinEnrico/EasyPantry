package com.stanga.easypantry;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.stanga.easypantry.databinding.ActivityMainBinding;
import com.stanga.easypantry.enums.LanguageMode;
import com.stanga.easypantry.enums.ThemeMode;
import com.stanga.easypantry.utils.LanguageHelper;
import com.stanga.easypantry.utils.ThemeHelper;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.createLanguageContext(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply saved theme before calling super.onCreate()
        ThemeMode savedTheme = ThemeHelper.getSavedTheme(this);
        ThemeHelper.applyTheme(savedTheme);

        // Apply saved language
        LanguageMode savedLanguage = LanguageHelper.getSavedLanguage(this);
        LanguageHelper.applyLanguage(this, savedLanguage);

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_pantry, R.id.nav_products, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}