package norapol.saowarak.narubeth.rmutr.broadcastertest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import norapol.saowarak.narubeth.rmutr.broadcastertest.utility.SessionManager;
import norapol.saowarak.narubeth.rmutr.broadcastertest.utility.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowVideoActivity extends AppCompatActivity {

    //Explicit ประกาศตัวแปร
    private TextView titleTextView;
    private VideoView showVideoView;
    private String strTitle, detailString;
    private String fileName;
    private SessionManager sessionManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);

        fileName = getIntent().getStringExtra("Video");


        //Show Title
        showTitle();

        //Show Video
        showVideo();

    }   // Main Method

    private void showVideo() {

        showVideoView = (VideoView) findViewById(R.id.videoView);
        //String strSourceVideo = "android.resource://" + getPackageName() + "/" + R.raw.talkname1;


        File mydir = getApplicationContext().getDir(Utility.DIRECTORY_NAME, Context.MODE_PRIVATE); //Creating an internal dir;
        String path = mydir.getPath();

        MediaController objMediaController = new MediaController(this);
        objMediaController.setAnchorView(showVideoView);
        showVideoView.setMediaController(objMediaController);
        showVideoView.setVideoPath(path + "/" + fileName);
        showVideoView.start();

    }   // showVideo

    private void showTitle() {

        strTitle = getIntent().getStringExtra("Title");
        titleTextView = (TextView) findViewById(R.id.txtShowTitleVideo);
        titleTextView.setText(strTitle);

    }

    public void clickBackShowVideo(View view) {
        finish();
    }

    public void clickTest(View view) {

        showVideoView.stopPlayback();

        detailString = getIntent().getStringExtra("Detail");


        sessionManager = new SessionManager(getApplicationContext());
        String strName = sessionManager.getUserInformation();

        if (strName.isEmpty()) {

            showDialogName();

        } else {

            Intent intent = new Intent(ShowVideoActivity.this, TestActivity.class);
            intent.putExtra("Title", strTitle);
            intent.putExtra("Detail", detailString);
            intent.putExtra("Name", strName);
            intent.putExtra("Video", fileName);
            startActivity(intent);

        }

//        Intent intent = new Intent(ShowVideoActivity.this, NameActivity.class);
//        intent.putExtra("Title", strTitle);
//        intent.putExtra("Detail", detailString);
//        startActivity(intent);

    }

    private void showDialogName() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ShowVideoActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_custom_name, null);
        builder.setView(view);

        final EditText username = (EditText) view.findViewById(R.id.username);

        builder.setTitle("กรุณากรอกชื่อของคุณ");
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Check username password

                String strName = username.getText().toString();

                if (!strName.isEmpty()) {

                    sessionManager.createUserInformation(strName);

                    Intent intent = new Intent(ShowVideoActivity.this, TestActivity.class);
                    intent.putExtra("Title", strTitle);
                    intent.putExtra("Detail", detailString);
                    intent.putExtra("Name", strName);
                    intent.putExtra("Video", fileName);
                    startActivity(intent);
                } else {

                    Toast.makeText(ShowVideoActivity.this, "กรุณากรอกชื่อของคุณ !", Toast.LENGTH_LONG).show();

                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        builder.show();
    }

}   // Main Class