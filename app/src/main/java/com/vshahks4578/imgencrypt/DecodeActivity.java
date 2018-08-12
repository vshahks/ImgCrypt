package com.vshahks4578.imgencrypt;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.ActionBar;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class DecodeActivity extends AppCompatActivity {
    private static final String LOG_TAG = DecodeActivity.class.getSimpleName();
    AppCompatButton uploadbutton;
    private File image;
    private File decoded;
    private ImageView decodedView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
        mToolbar = findViewById(R.id.toolbar_main);
        uploadbutton=findViewById(R.id.uploadbutton);
        setSupportActionBar(mToolbar);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        decoded = new File(getCacheDir(), "temp.png");
        decodedView =  findViewById(R.id.imageViewDecode);
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
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

    public void decode(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            }
            image = new File(selectedImagePath);
            uploadbutton.setVisibility(View.GONE);
            decodedView.setImageBitmap(EncodeActivity.decodeBitmapScaledDown(decodedView, selectedImagePath));
        }
    }
    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public static final String DECODED_FILE_PATH = "ENCODED FILE";

    public void nextPage(View v) {
        if (image == null) {
            Toast.makeText(getApplicationContext(), "You need to pick an image", Toast
                    .LENGTH_SHORT).show();
        } else {
            try {
                ProgressDialog progress = new ProgressDialog(this);
                progress.setTitle("Decoding");
                progress.setMessage("This may take a few minutes for large files...");
                progress.show();
                new DecoderTask(this, progress).execute(image, decoded);
            } catch (OutOfMemoryError e) {
                Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast
                        .LENGTH_SHORT);
                toast.show();
            }
        }
    }


}

