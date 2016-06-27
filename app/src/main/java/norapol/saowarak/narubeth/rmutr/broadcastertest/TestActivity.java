package norapol.saowarak.narubeth.rmutr.broadcastertest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import norapol.saowarak.narubeth.rmutr.broadcastertest.soundfile.SoundFile;
import norapol.saowarak.narubeth.rmutr.broadcastertest.utility.FrameGains;
import norapol.saowarak.narubeth.rmutr.broadcastertest.utility.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class TestActivity extends AppCompatActivity {

    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final int AUDIO_OUTPUT_FILE_EXT_MP4 = MediaRecorder.OutputFormat.MPEG_4;
    private static final String TAG = "TestActivity";

    private MediaRecorder recorder = null;
    private String folderSessionName;
    private VideoView showVideoTest;
    private String pathFileName;
    private int[] frameGainsOrigenal;
    private int[] frameGainsDestination;
    private String recFileName;

    private boolean recordedToDB = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Create folder Session on test (1 test to 1 folder session) and destroy folder in onDestroy Activity
        folderSessionName = getCurrentTime();
        createDirectory();

//        initial Vedio View
        initVedio();

        new FrameGains(pathFileName){
            @Override
            protected void onPostExecute(SoundFile mSoundFile) {
                super.onPostExecute(mSoundFile);

                Log.e(TAG, "NumFrames : "+ mSoundFile.getNumFrames());


                frameGainsOrigenal = mSoundFile.getFrameGains();

                Log.e(TAG, "frameGains : "+ Arrays.toString(frameGainsOrigenal));

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);




        ImageView imageView = (ImageView) findViewById(R.id.imageButton);
        imageView.setClickable(true);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN :

                        Log.e("imageView", "ACTION_DOWN");
                        Log.e(TAG, "Start Recording");

                        if (recorder == null) {

                            initRecoder();
                            Log.e("recorder", "IS NULL");
                        } else {
                            Log.e("recorder", "Have Exits");
                        }

                        startRecording();
                        showVideoTest.start();
                        break;

                    case MotionEvent.ACTION_UP:

                        Log.e("imageView", "ACTION_UP");
                        Log.e(TAG, "Pause Recording");

                        stopRecording();
                        showVideoTest.pause();

                        getListSound();
                        break;
                }

                return false;
            }
        });
    }

    private void initVedio() {

        showVideoTest = (VideoView) findViewById(R.id.videoView);
        //String strSourceVideo = "android.resource://" + getPackageName() + "/" + R.raw.talkname1;

            String fileName = getIntent().getStringExtra("Video");
//        String fileName = "talkname1.mp4";


        File mydir = getApplicationContext().getDir(Utility.DIRECTORY_NAME, Context.MODE_PRIVATE); //Creating an internal dir;
        String path = mydir.getPath();

        pathFileName = path + "/" + fileName;

        MediaController objMediaController = new MediaController(this);

        objMediaController.setAnchorView(showVideoTest);
        showVideoTest.setMediaController(objMediaController);
        showVideoTest.setZOrderOnTop(true);
        showVideoTest.setVideoPath(pathFileName);
        showVideoTest.setOnPreparedListener(PreparedListener);
        showVideoTest.setOnCompletionListener(CompletionListener);

    }

    MediaPlayer.OnPreparedListener PreparedListener = new MediaPlayer.OnPreparedListener(){

        @Override
        public void onPrepared(MediaPlayer m) {
            Log.e(TAG, "PreparedListener");

            try {
                if (m.isPlaying()) {
                    m.stop();
                    m.release();
                    m = new MediaPlayer();
                }
                m.setVolume(0f, 0f);
                m.setLooping(false);
                m.start();
                m.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    MediaPlayer.OnCompletionListener CompletionListener = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mp) {

            if(null != recorder){

                Log.e(TAG, "CompletionListener");

                stopRecording();

                clickShowScore();

            }





        }
    };

    public void clickShowScore(View view){
        Log.e(TAG, "clickShowScore view");

        clickShowScore();

    }

    private void clickShowScore() {

        Log.e(TAG, "clickShowScore");
        File mydir = getApplicationContext().getDir(folderSessionName, Context.MODE_PRIVATE); //Creating an internal dir;
        System.out.println(mydir.getPath());

        String outputVideo = mydir.getPath() + "/audio_output.mp4";
        System.out.println(outputVideo);



        File output = new File(outputVideo);
        if (output.exists()) output.delete();


        // lists all the files into an array
        File[] dirFiles =  mydir.listFiles();

        try {
            Movie[] inMovies = new Movie[dirFiles.length];

            if (dirFiles.length != 0) {
                // loops through the array of files, outputing the name to console
                for (int ii = 0; ii < dirFiles.length; ii++) {

                    String fileOutput = dirFiles[ii].toString();

                    inMovies[ii] = MovieCreator.build(fileOutput);

                    System.out.println(fileOutput);
                }

                List<Track> audioTracks = new LinkedList<Track>();

                for (Movie m : inMovies) {
                    for (Track t : m.getTracks()) {
                        if (t.getHandler().equals("soun")) {
                            audioTracks.add(t);
                        }
                    }
                }

                Movie result = new Movie();

                if (audioTracks.size() > 0) {
                    result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
                }



                Container out = new DefaultMp4Builder().build(result);
                out.writeContainer(new FileOutputStream(outputVideo).getChannel());

//            audioPlayer(outputVideo);  play sound after marge all file

                new FrameGains(outputVideo){
                    @Override
                    protected void onPostExecute(SoundFile mSoundFile) {
                        super.onPostExecute(mSoundFile);

                        Log.e(TAG, "NumFrames : "+ mSoundFile.getNumFrames());


                        frameGainsDestination = mSoundFile.getFrameGains();

                        Log.e(TAG, "frameGains : "+ Arrays.toString(frameGainsDestination));

                        getCompairSound(frameGainsOrigenal, frameGainsDestination);

                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } else { //ไม่พบไฟล์เสียง
                AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
                builder.setMessage(R.string.test_file_found)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                            }
                        }).show();
            }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getCompairSound(int[] frameGainsOrigenal, int[] frameGainsDestination) {

        int frameTrue = 0;
        int shotLenght = frameGainsOrigenal.length;

        if (frameGainsDestination.length < shotLenght) shotLenght = frameGainsDestination.length;

        Log.e(TAG, "shotLenght : "+ shotLenght);


        int sumScalDestination = 0;
        for (int i = 0; i < shotLenght; i++) {
            boolean flag = Math.abs(frameGainsOrigenal[i] - frameGainsDestination[i]) < Utility.Dislocation;

            Log.e(TAG, "Origenal : " +frameGainsOrigenal[i] +", Destination : " + frameGainsDestination[i] +
                    ", ABS = " + Math.abs(frameGainsOrigenal[i] - frameGainsDestination[i]) + ", Flag = " + flag );

            if (flag) frameTrue++;

            sumScalDestination += frameGainsDestination[i];
        }

        Log.e(TAG, "Frame True : "+ frameTrue);
        double score = ( (double)frameTrue / (double)shotLenght) * 100;
        Log.e(TAG, "Percent True : "+ String.valueOf(score));

        Log.e(TAG, ""+sumScalDestination / shotLenght);
        if (sumScalDestination / shotLenght >= Utility.MinAVGScalAccept) {
            showDialogScore((int) score);
        } else { //ค่าเฉลี่ยของเสียงน้อยกว่าที่ยอมรับได้
            Log.e(TAG,"sound_bad");
            AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
            builder.setMessage(R.string.sound_bad)
                    .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    }).show();
        }
    }

    public void audioPlayer(String path){
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();

        try {
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File[] getListSound() {

        File mydir = getApplicationContext().getDir(folderSessionName, Context.MODE_PRIVATE); //Creating an internal dir;
        System.out.println(mydir.getPath());

        // lists all the files into an array
        return mydir.listFiles();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        File mydir = getApplicationContext().getDir(folderSessionName, Context.MODE_PRIVATE); //Create object;
        mydir.delete();

        Log.e(TAG, "folderSessionName deleted");

    }

    private void initRecoder() {

        recFileName = getFilename();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(AUDIO_OUTPUT_FILE_EXT_MP4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(recFileName);
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

    }

    private String getFilename(){


        File mydir = getApplicationContext().getDir(folderSessionName, Context.MODE_PRIVATE); //Creating an internal dir;


        System.out.println(mydir.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_MP4);

        return (mydir.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_MP4);
    }

    private void createDirectory() {

        File mydir = getApplicationContext().getDir(folderSessionName, Context.MODE_PRIVATE); //Creating an internal dir;

        if (!mydir.exists()) {
            Log.e("ERROR", "Folder does not exists");
            mydir.mkdir();

        } else {
            Log.e("ERROR", "Folder have exists in "+mydir.getPath());

        }

    }

    private String getCurrentTime() {
        return String.valueOf(System.currentTimeMillis());
    }

    private void startRecording(){


        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private MediaRecorder.OnErrorListener errorListener = new        MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Log.e(TAG,"Error: " + what + ", " + extra);
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Log.e(TAG,"Warning: " + what + ", " + extra);
        }
    };

    private void stopRecording(){

        try{
            if(null != recorder){
                Log.e(TAG,"stopRecording");

                recorder.stop();
                recorder.reset();
                recorder.release();



            }
        }catch(RuntimeException stopException){
            //handle cleanup here
            File cleanFile = new File(recFileName);
            cleanFile.delete();

        }
            recorder = null;

    }

    public void clickBackTest(View view) {
        finish();
    }

    private void showDialogScore(int score) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_custom_show_score, null);
        builder.setView(view);

        String strName = getIntent().getStringExtra("Name");
        String strSubject = getIntent().getStringExtra("Detail");

        final TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
        final TextView txt_score = (TextView) view.findViewById(R.id.txt_score);

        txt_name.setText(strName);
        txt_score.setText(String.valueOf(score));

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateandTime = sdf.format(new Date());

        myDBClass myDBClass = new myDBClass(getApplicationContext());
        myDBClass.insertHistory(strSubject, strName, currentDateandTime, String.valueOf(score) );

//        builder.setTitle("กรุณากรอกชื่อของคุณ");
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Check username password

                dialog.dismiss();


            }
        });


        builder.show();
    }


}