package norapol.saowarak.narubeth.rmutr.broadcastertest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import norapol.saowarak.narubeth.rmutr.broadcastertest.utility.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_RECORD_AUDIO = 101;
    private static final int REQUEST_CODE_WRITE_STORAGE = 102;
    //Explicit
    private ImageView talkNameImageView, newTestMaleImageView, newTestFemaleImageView;

//    private int[] myVideo = {R.raw.talkname1, R.raw.talkname2, R.raw.talkname3,
//            R.raw.talkname4, R.raw.talkname5, R.raw.talkname6, R.raw.talkname7,
//            R.raw.talkname8, R.raw.talkname9, R.raw.talkname10, R.raw.talkname11,
//            R.raw.talkname12, R.raw.talkname13, R.raw.talkname14, R.raw.talkname15,};

//    private int[] myVideo1 = {R.raw.testfemale1,R.raw.testfemale2};
//
//    private int[] myVideo2 = {R.raw.testmale1,R.raw.testmale2};

    private myDBClass myDB;
    private ImageView historyImageView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reQuestPermissionOnSafe();

        //Bind Widget
        bindWidget();

        //Image Controller
        imageController();

        //Create private directory
        createDirectory();

        //create DB
        myDB = new myDBClass(this);
        myDB.getWritableDatabase(); // First method
        myDB.clearTable();

        insertListNameDynasty(); // insert list for show in takename
        insertListNewTestMale(); // insert list for show in news male
        insertListNewTestFemale(); // insert list for show in news female

    }   // Main Method

    private void reQuestPermissionOnSafe() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasRecordAudioPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (hasRecordAudioPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_RECORD_AUDIO);
                return;
            }

            int hasWriteStoragePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE_STORAGE);
                return;
            }
        }
    }

    private void createDirectory() {

        File mydir = getApplicationContext().getDir(Utility.DIRECTORY_NAME, Context.MODE_PRIVATE); //Creating an internal dir;

        if (!mydir.exists()) {
            Log.e("ERROR", "Folder does not exists");
            mydir.mkdir();

        } else {
            Log.e("ERROR", "Folder have exists in "+mydir.getPath());

        }

    }

    private void insertListNameDynasty() {


        myDB.insertNameDynasty("พระบาทสมเด็จพระเจ้าอยู่หัว", "talkname1.mp4");
        myDB.insertNameDynasty("สมเด็จพระนางเจ้าฯพระบรมราชินีนาถ", "talkname2.mp4");
        myDB.insertNameDynasty("สมเด็จพระบรมโอรสาธิราชฯสยามมกุฎราชกุมาร", "talkname3.mp4");
        myDB.insertNameDynasty("สมเด็จพระเทพรัตนราชสุดาฯสยามบรมราชกุมารี", "talkname4.mp4");
        myDB.insertNameDynasty("สมเด็จพระเจ้าลูกเธอ เจ้าฟ้าจุฬาภรณ์วลัยลักษณ์อัครราชกุมารี", "talkname5.mp4");
        myDB.insertNameDynasty("สมเด็จพระเจ้าภคินีเธอ เจ้าฟ้าเพชรรัตนราชสุดา สิริโสภาพัณณวดี", "talkname6.mp4");
        myDB.insertNameDynasty("สมเด็จพระเจ้าพี่นางเธอ เจ้าฟ้ากัลยาณิวัฒนา กรมหลวงนราธิวาสราชนครินทร์", "talkname7.mp4");
        myDB.insertNameDynasty("สมเด็จพระศรีนคริทราบรมราชชนี", "talkname8.mp4");
        myDB.insertNameDynasty("พระเจ้าวรวงศ์เธอ พระองค์เจ้าโสมสวลี พระวรราชาทินัดดามาตุ", "talkname9.mp4");
        myDB.insertNameDynasty("พระเจ้าหลานเธอ พระองค์เจ้าสิริภาจุฑาภรณ์", "talkname10.mp4");
        myDB.insertNameDynasty("พระเจ้าหลานเธอ พระองค์เจ้าอทิตยาทร กิติคุณ", "talkname11.mp4");
        myDB.insertNameDynasty("พระเจ้าหลานเธอ พระองค์เจ้าพัชรกิติยาภา", "talkname12.mp4");
        myDB.insertNameDynasty("พระเจ้าหลานเธอ พระองค์เจ้าสิริวัณณวรีนารีรัตน์", "talkname13.mp4");
        myDB.insertNameDynasty("พระเจ้าหลานเธอ พระองค์เจ้าทีปังกรรัศมีโชติ", "talkname14.mp4");
        myDB.insertNameDynasty("ทูลกระหม่อมหญิงอุบลรัตนราชกัญญา สิริวัฒนาพรรณวดี", "talkname15.mp4");



    }

    private void insertListNewTestMale() {

        myDB.insertNewTestMale("แบบทดสอบสำหรับผู้ชายชุดที่ 1", "testmale1.mp4");
        myDB.insertNewTestMale("แบบทดสอบสำหรับผู้ชายชุดที่ 2", "testmale2.mp4");
        myDB.insertNewTestMale("แบบทดสอบสำหรับผู้ชายชุดที่ 3", "testmale3.mp4");
        myDB.insertNewTestMale("แบบทดสอบสำหรับผู้ชายชุดที่ 4", "testmale4.mp4");
        myDB.insertNewTestMale("แบบทดสอบสำหรับผู้ชายชุดที่ 5", "testmale5.mp4");


    }

    private void insertListNewTestFemale() {

        myDB.insertNewTestFemale("แบบทดสอบสำหรับผู้หญิงชุดที่ 1", "testfemale1.mp4");
        myDB.insertNewTestFemale("แบบทดสอบสำหรับผู้หญิงชุดที่ 2", "testfemale2.mp4");
        myDB.insertNewTestFemale("แบบทดสอบสำหรับผู้หญิงชุดที่ 3", "testfemale3.mp4");
        myDB.insertNewTestFemale("แบบทดสอบสำหรับผู้หญิงชุดที่ 4", "testfemale4.mp4");
        myDB.insertNewTestFemale("แบบทดสอบสำหรับผู้หญิงชุดที่ 5", "testfemale5.mp4");


    }

    private void imageController() {
        talkNameImageView.setOnClickListener(this);
        newTestMaleImageView.setOnClickListener(this);
        newTestFemaleImageView.setOnClickListener(this);
        historyImageView.setOnClickListener(this);
    }

    private void bindWidget() {
        talkNameImageView = (ImageView) findViewById(R.id.imageView2);
        newTestMaleImageView = (ImageView) findViewById(R.id.imageView3);
        newTestFemaleImageView = (ImageView) findViewById(R.id.imageView4);
        historyImageView = (ImageView) findViewById(R.id.img_history);
    }

    @Override
    public void onClick(View view) {

        String sourceVideo = "";
        Class<?> toClass = null;
        int intIcon = R.drawable.nameread;

        switch (view.getId()) {
            case R.id.imageView2:
                intIcon = R.drawable.nameread;
                sourceVideo = "talkname";  //ส่งเงื่อนไขในการอ่านไฟล์จาก SQLite
               toClass = DetailListView.class;
                break;
            case R.id.imageView3:
                intIcon = R.drawable.testboy;
                sourceVideo = "newtest_male"; //ส่งเงื่อนไขในการอ่านไฟล์จาก SQLite
                toClass = DetailListView.class;

                break;
            case R.id.imageView4:
                intIcon = R.drawable.gtest;
                sourceVideo = "newtest_female"; //ส่งเงื่อนไขในการอ่านไฟล์จาก SQLite
                toClass = DetailListView.class;

                break;
            case R.id.img_history:
                //Intent to History
                toClass = HistoryListView.class;

                break;
            default:
                intIcon = R.drawable.nameread;
                sourceVideo = "talkname";  //ส่งเงื่อนไขในการอ่านไฟล์จาก SQLite
                break;
        }   // switch

        //Intent to ListView
        Intent objIntent = new Intent(MainActivity.this, toClass);
        objIntent.putExtra("Icon", intIcon);
        objIntent.putExtra("sourceVideo", sourceVideo);
        startActivity(objIntent);

    }   // onClick

}   // Main Class