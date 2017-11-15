package ru.kuzmiankou.camera;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUWAR_CODE = 100;
    File pictureDir = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), "CameraDemo");
    private static final String FILE_NAME = "image01.jpg";
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!pictureDir.exists()) {
            pictureDir.mkdir();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camere:
                showCamera();
                return true;
            case R.id.action_email:
                emailPicture();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File image = new File(pictureDir, FILE_NAME);
        fileUri = Uri.fromFile(image);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUWAR_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUWAR_CODE) {
            if(resultCode == RESULT_OK) {
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                File image = new File (pictureDir, FILE_NAME);
                fileUri = Uri.fromFile(image);
                imageView.setImageURI(fileUri);
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action cancelled", Toast.LENGTH_LONG).show();

            }
            else {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void emailPicture() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("aplication/image");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"me@example.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New photo");
        emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}
