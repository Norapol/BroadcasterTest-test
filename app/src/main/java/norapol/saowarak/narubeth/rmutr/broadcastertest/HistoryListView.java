package norapol.saowarak.narubeth.rmutr.broadcastertest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HistoryListView extends AppCompatActivity {

    private static final int CONTEXTMENU_OPTION1 = 100;
    private static final int CONTEXTMENU_OPTION2 = 200;
    //Explicit
    private ArrayList<Map<String, String>> titleStrings;
    private String[] detailStrings;
    private int iconAnInt;
    private int[] videoInts;
    private MyAdapter objMyAdapter;
    private myDBClass myDB;
    private ListView myListView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Log.e("oncreate","888");


        //ประกาศตัวแปร myDBClass
        myDB = new myDBClass(getApplicationContext());


        titleStrings = getListHistory("select subject_name, tester_name, date_test, score, id from history order by id desc");


        //Create ListView
        createListView();

    }   // Main Method

    private void createListView() {

        objMyAdapter = new MyAdapter(HistoryListView.this, titleStrings, iconAnInt);
        myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(objMyAdapter);
        registerForContextMenu(myListView);
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

//        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
//
//                System.out.println(objMyAdapter.getFileName(position));
//
//                File mydir = getApplicationContext().getDir(Utility.DIRECTORY_NAME, Context.MODE_PRIVATE); //Creating an internal dir;
//                final String fileName =  objMyAdapter.getFileName(position);
//
//                File videoFile = new File(mydir + "/" +fileName);
//
//                if (!videoFile.exists()) {
//                    Log.e("ERROR", fileName + "does not exists");
//
//                    new DownloadFile(HistoryListView.this, fileName) {
//                        @Override
//                        protected void onPostExecute(String respCode) {
//                            super.onPostExecute(respCode);
//
//                            if ("404".equals(respCode)) {
//
//                                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryListView.this);
//                                builder.setMessage(R.string.dialog_file_found)
//                                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                // FIRE ZE MISSILES!
//                                            }
//                                        }).show();
//
//                            } else {
//
//                                Toast.makeText(HistoryListView.this, "File has been downloaded. ", Toast.LENGTH_LONG).show();
//
//
//
//                                // gets the files in the directory
//                                File mydir = getApplicationContext().getDir(Utility.DIRECTORY_NAME, Context.MODE_PRIVATE); //Creating an internal dir;
//                                System.out.println(mydir.getPath());
//
//                                // lists all the files into an array
//                                File[] dirFiles = mydir.listFiles();
//
//                                if (dirFiles.length != 0) {
//                                    // loops through the array of files, outputing the name to console
//                                    for (File dirFile : dirFiles) {
//                                        String fileOutput = dirFile.toString();
//                                        System.out.println(fileOutput);
//                                    }
//                                }
//
//                                showVedio(fileName, position);
//
//
//                            }
//                        }
//                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//                } else {
//                    Log.e("ERROR", "Folder have exists file :  "+fileName);
//
//                    showVedio(fileName, position);
//
//                }
//
//
//
//
//            }   // event
//        });

    }   // createListView

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        //กำหนด Title
        menu.setHeaderTitle("เลือกการกระทำ");

        //เพิ่ม menu
        menu.add(Menu.NONE, CONTEXTMENU_OPTION1, 0, "ลบ");
        menu.add(Menu.NONE, CONTEXTMENU_OPTION2, 1, "ลบทั้งหมด");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        // Get extra info about list item that was long-pressed
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        // ตรวจสอบรายการเมนูที่เลือกมา
        switch (item.getItemId()) {

            case CONTEXTMENU_OPTION1:
                // ลบบางรายการ
                myDB.deleteSingleRow(objMyAdapter.getRowID(menuInfo.position));
                objMyAdapter.remove(menuInfo.position);
                objMyAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Option 1: ID "+menuInfo.id+", position "+menuInfo.position, Toast.LENGTH_SHORT).show();
                break;

            case CONTEXTMENU_OPTION2:
                // ลบทั้งหมด
                myDB.clearTableHistory();
                myListView.setAdapter(new MyAdapter(HistoryListView.this, new ArrayList<Map<String, String>>(), iconAnInt)); //reset value to listview
//                objMyAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Option 2: ID "+menuInfo.id+", position "+menuInfo.position, Toast.LENGTH_SHORT).show();
                break;

        }

        return true ;

    }

    private ArrayList<Map<String,String>> getListHistory(String strQuery) {
        myDBClass myDb = new myDBClass(getApplicationContext());

        List<myDBClass.sMembers> MebmerList = myDb.selectAllHistory(strQuery);
        ArrayList<Map<String,String>> product_name = new ArrayList<>(MebmerList.size());
        for (myDBClass.sMembers mem : MebmerList) {

            Map<String,String> map = new HashMap<String,String>();
            map.put("subject_name", mem.getSubjectName());
            map.put("tester_name", mem.getTesterName());
            map.put("date_test", mem.getDateTest());
            map.put("score", mem.getScore());
            map.put("id", mem.getRowID());
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

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View objView1 = objLayoutInflater.inflate(R.layout.custom_history_list, viewGroup, false);

            TextView txt_vedio_name = (TextView) objView1.findViewById(R.id.txt_vedio_name);
            txt_vedio_name.setText(listStrings.get(i).get("subject_name"));

            TextView txt_name = (TextView) objView1.findViewById(R.id.txt_name);
            txt_name.setText(listStrings.get(i).get("tester_name"));

            TextView txt_date = (TextView) objView1.findViewById(R.id.txt_date);
            txt_date.setText(listStrings.get(i).get("date_test"));

            TextView txt_score = (TextView) objView1.findViewById(R.id.txt_score);
            txt_score.setText(listStrings.get(i).get("score"));



            return objView1;
        }


        public String getRowID(int position) {
            return  listStrings.get(position).get("id");

        }

        public void remove(int position) {
            listStrings.remove(position);
        }

        public void removeAll() {

            listStrings.clear();

        }
    }

}   // Main Class