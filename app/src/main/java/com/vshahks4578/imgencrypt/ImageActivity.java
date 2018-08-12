package com.vshahks4578.imgencrypt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

public class ImageActivity extends AppCompatActivity {

    private static final String LOG_TAG = ImageActivity.class.getSimpleName();
    File tempFile;
    private Toolbar mToolbar;
    private String task;
    private Bitmap bitmap;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String displayPath = intent.getStringExtra(EncodeActivity.EXTRA_FILE_TAG);
         task=intent.getStringExtra("TASK");
        ImageView imageView = findViewById(R.id.imageView);
         bitmap = BitmapFactory.decodeFile(displayPath);
        imageView.setImageBitmap(bitmap);
        Calendar calendar = Calendar.getInstance();
         name = "" + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get
                (Calendar.DATE) + calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE)
                + calendar.get(Calendar.SECOND);
      String sucessmsg=null;
        if(task.equals("Encode")){
         tempFile = new File(Environment.getExternalStorageDirectory().getPath() +
                "/Pictures/ImgEnCrypt/" + name + ".png");
            sucessmsg="Image is successfully saved into your ImgEncrypt folder of pictures directory ";
        }

        try { if(task.equals("Encode")) {
            tempFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            Toast.makeText(this, sucessmsg, Toast.LENGTH_LONG).show();
            fos.close();
        }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        File encode = new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/ImgEnCrypt");

        if(!encode.exists()) {
            encode.mkdirs();
        }
    }



    public void shareImage(View view) {
        if(task.equals("Encode")) {
            String path=Environment.getExternalStorageDirectory().getPath() +
                    "/Pictures/ImgEnCrypt/" + name + ".png";
            Uri uri = Uri.parse(path);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM,uri);
            intent.setType("image/*");
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Can't share this image because this image is message", Toast.LENGTH_LONG).show();
        }
    }
}
