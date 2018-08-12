package com.vshahks4578.imgencrypt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SteganographyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    private Toolbar mToolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steganography);
        drawerLayout=findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,mToolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,Login.class));

        }

        return super.onOptionsItemSelected(item);
    }

    public void encode(View v) {
        startActivity(new Intent(this, EncodeActivity.class));
    }

    public void decode(View v) {
        startActivity(new Intent(this, DecodeActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_send) {
            Intent intent=new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:vktechies@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT," Need help regarding Your ImgCrypt App");
            startActivity(intent);
        }
        else if(id==R.id.nav_share){
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "ImgCrypt");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
