package norapol.saowarak.narubeth.rmutr.broadcastertest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import norapol.saowarak.narubeth.rmutr.broadcastertest.utility.DownloadFile;
import norapol.saowarak.narubeth.rmutr.broadcastertest.utility.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailListView extends AppCompatActivity {

    //Explicit
    private ArrayList<Map<String, String>> titleStrings;
    private int iconAnInt;
    private MyAdapter objMyAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        //ประกาศตัวแปร myDBClass
        myDBClass myDB = new myDBClass(getApplicationContext());

        //ตรวจสอบ เมนูจาก sourceVideo เพื่อดึงเตรียมรายการมาใส่ใน Listview
        String sourceVideo = getIntent().getStringExtra("sourceVideo");
        iconAnInt = getIntent().getIntExtra("Icon", R.drawable.nameread);

        switch (sourceVideo) {
            case "talkname": //เลือกเมนู ขานพระนาม

                titleStrings = getListNameDynasty("select name_dynasty as subject_name, filename from talkname");
                break;
            case "newtest_male": //เลือกเมนู ผู้ประกาศชาย

                titleStrings = getListNameDynasty("select subject_name, filename from newtest_male");
                break;
            case "newtest_female": //เลือกเมนู ผู้ประกาศหญฺง

                titleStrings = getListNameDynasty("select subject_name, filename from newtest_female");
                break;
            default:
                break;
        }   // switch


        //Create ListView
        createListView();

    }   // Main Method


    private void createListView() {

        objMyAdapter = new MyAdapter(DetailListView.this, titleStrings, iconAnInt);
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(objMyAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                System.out.println(objMyAdapter.getFileName(position));

                File mydir = getApplicationContext().getDir(Utility.DIRECTORY_NAME, Context.MODE_PRIVATE); //Creating an internal dir;
                final String fileName =  objMyAdapter.getFileName(position);

                File videoFile = new File(mydir + "/" +fileName);

                if (!videoFile.exists()) {
                    Log.e("ERROR", fileName + "does not exists");


                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailListView.this);
                    builder.setMessage(R.string.str_download)
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    downloadFile(fileName, position);
                                }
                            })
                            .setNegativeButton(R.string.CANCLE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();



                } else {
                    Log.e("ERROR", "Folder have exists file :  "+fileName);

                    showVedio(fileName, position);

                }




            }   // event
        });

    }   // createListView

    private void downloadFile(final String fileName, final int position) {
        new DownloadFile(DetailListView.this, fileName) {
            @Override
            protected void onPostExecute(String respCode) {
                super.onPostExecute(respCode);

                if ("404".equals(respCode)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailListView.this);
                    builder.setMessage(R.string.dialog_file_found)
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                }
                            }).show();

                } else {

                    Toast.makeText(DetailListView.this, "File has been downloaded. ", Toast.LENGTH_LONG).show();



                    // gets the files in the directory
                    File mydir = getApplicationContext().getDir(Utility.DIRECTORY_NAME, Context.MODE_PRIVATE); //Creating an internal dir;
                    System.out.println(mydir.getPath());

                    // lists all the files into an array
                    File[] dirFiles = mydir.listFiles();

                    if (dirFiles.length != 0) {
                        // loops through the array of files, outputing the name to console
                        for (File dirFile : dirFiles) {
                            String fileOutput = dirFile.toString();
                            System.out.println(fileOutput);
                        }
                    }

                    showVedio(fileName, position);


                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void showVedio(String fileName, int position) {

        //Intent to ShowVideoActivity
        Intent objIntent = new Intent(DetailListView.this, ShowVideoActivity.class);
        objIntent.putExtra("Title", objMyAdapter.getSubjectName(position));
        objIntent.putExtra("Detail", objMyAdapter.getSubjectName(position));
        objIntent.putExtra("Video", fileName);
        startActivity(objIntent);
    }

    private ArrayList<Map<String,String>> getListNameDynasty(String strQuery) {
        myDBClass myDb = new myDBClass(getApplicationContext());

        List<myDBClass.sMembers> MebmerList = myDb.selectAllSubject(strQuery);
        ArrayList<Map<String,String>> product_name = new ArrayList<>(MebmerList.size());
        for (myDBClass.sMembers mem : MebmerList) {

            Map<String,String> map = new HashMap<String,String>();
            map.put("subject_name", mem.getSubjectName());
            map.put("filename", mem.getFileName());
            product_name.add(map);
            Log.e("DATA : ", mem.getSubjectName());
        }

        return product_name;
    }



    public class MyAdapter extends BaseAdapter {

        //Explicit
        private Context objContext;
        private ArrayList<Map<String, String>> listStrings;
        private int iconAnInt;

        public MyAdapter(Context objContext, ArrayList<Map<String, String>> listStrings, int iconAnInt) {
            this.objContext = objContext;
            this.listStrings = listStrings;
            this.iconAnInt = iconAnInt;
        }   // Constructor

        @Override
        public int getCount() {
                if (listStrings == null) return 0;
            return listStrings.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public String getSubjectName(int position) {

            return  listStrings.get(position).get("subject_name");

        }

        public String getFileName(int position) {

            return  listStrings.get(position).get("filename");

        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View objView1 = objLayoutInflater.inflate(R.layout.custom_detail_list, viewGroup, false);

            TextView listTextView = (TextView) objView1.findViewById(R.id.txtList);
            listTextView.setText(listStrings.get(i).get("subject_name"));

            ImageView iconImageView = (ImageView) objView1.findViewById(R.id.imvIcon);
            iconImageView.setImageResource(iconAnInt);

            return objView1;
        }


    }

}   // Main Class