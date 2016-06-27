package norapol.saowarak.narubeth.rmutr.broadcastertest.utility;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

import norapol.saowarak.narubeth.rmutr.broadcastertest.soundfile.SoundFile;

public class FrameGains extends AsyncTask<String, String, SoundFile> {

    private static final String TAG = "DownloadFile";
    private final String fileName;
    private SoundFile mSoundFile;
    private File mFile;
    private int[] frameGains;

    public FrameGains(String fileName) {
        this.fileName = fileName;

    }

    @Override
    protected void onPreExecute() {
        myLog("onPreExecute");

        mFile = new File(fileName);


    }
    @Override
    protected SoundFile doInBackground(String... v) {
        myLog(": doInBackground");


        try {
            mSoundFile = SoundFile.create(mFile.getAbsolutePath());

            if (mSoundFile == null) {
                String name = mFile.getName().toLowerCase();
                String[] components = name.split("\\.");
                String err;
                if (components.length < 2) {

                    Log.e(TAG, "No Extension error");
                } else {
                    Log.e(TAG, "Bad Extension error"+ " " +
                            components[components.length - 1]);
                }


            }
        } catch (final Exception e) {
            e.printStackTrace();

        }

        return mSoundFile;

    }



    @Override
    protected void onPostExecute(SoundFile mSoundFile) {
        myLog(" onPostExecute");

    }



    @Override
    protected void onCancelled() {
        myLog("onCancelled");
    }

    private void myLog(String m) {
        //Log.i("MyLog", m + ": "+Thread.currentThread().getName());
    }
}
