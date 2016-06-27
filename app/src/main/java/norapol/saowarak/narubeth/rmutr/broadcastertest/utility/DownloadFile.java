package norapol.saowarak.narubeth.rmutr.broadcastertest.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFile extends AsyncTask<String, String, String> {

    private static final String TAG = "DownloadFile";
    private final String fileName;
    private Context _context;
    private ProgressDialog mProgressDialog;

    public DownloadFile(Context context, String fileName) {
        this._context = context;
        this.fileName = fileName;

    }

    @Override
    protected void onPreExecute() {
        myLog("onPreExecute");


        mProgressDialog = new ProgressDialog(_context);
        mProgressDialog.setMessage("Downloading file..");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

    }
    @Override
    protected String doInBackground(String... v) {
        myLog(": doInBackground");


        try {

            //create url
            String from = Utility.SERVER_URL + fileName;

            Log.e(TAG, from);




            URL url = new URL(from);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            int lenghtOfFile = conexion.getContentLength(); // Size of file

            System.out.println(lenghtOfFile);

            if (lenghtOfFile > 0) {

                createFile(url, lenghtOfFile);


            } else {

                Log.e("ERROR", fileName + "does not have on server");

                return "404";
            }





        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("ERROR", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();

        }

        return null;
    }

    private void createFile(URL url, int lenghtOfFile) {

        try {
            InputStream input = new BufferedInputStream(url.openStream());

            File mydir = _context.getDir(Utility.DIRECTORY_NAME, Context.MODE_PRIVATE); //Creating an internal dir;

            String path = mydir.getPath();





            OutputStream output = new FileOutputStream(path+"/"+fileName); // save to parh sd card

            System.out.println(path+fileName);

            long total = 0;

            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress(""+(int)((total*100)/lenghtOfFile)); //update percent on process bar
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("ERROR", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    protected void onProgressUpdate(String... progress) {
        Log.d("ANDRO_ASYNC",progress[0]);
        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String respCode) {
        myLog(" onPostExecute");

        mProgressDialog.dismiss();


    }



    @Override
    protected void onCancelled() {
        myLog("onCancelled");
    }

    private void myLog(String m) {
        //Log.i("MyLog", m + ": "+Thread.currentThread().getName());
    }
}
