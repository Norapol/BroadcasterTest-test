package norapol.saowarak.narubeth.rmutr.broadcastertest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class myDBClass extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 4;

	// Database Name
	private static final String DATABASE_NAME = "broadCaster";
    private static final String TAG = "myDBClass" ;

    private SQLiteDatabase db = this.getWritableDatabase(); // Write Data

	public myDBClass(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e(TAG, "Create "+DATABASE_NAME+" Successfully.");

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

        Log.e(TAG, "Create database in version : " + DATABASE_VERSION);

        createTableTalkNameDynasty(db);
        createTableNewTestMale(db);
        createTableNewTestFemale(db);
        createTableHistory(db);


	}




    @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS talkname");
        db.execSQL("DROP TABLE IF EXISTS newtest_male");
        db.execSQL("DROP TABLE IF EXISTS newtest_female");
        db.execSQL("DROP TABLE IF EXISTS history");
        onCreate(db);
	}


	private void createTableTalkNameDynasty(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE `talkname` (\n" +
                "\t`name_dynasty`\tTEXT NOT NULL,\n" +
                "\t`filename`\tTEXT\n" +
                ");");

		Log.e("CREATE TABLE", "Create Table talkname Successfully.");

	}

    private void createTableNewTestMale(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE `newtest_male` (\n" +
                "\t`subject_name`\tTEXT NOT NULL,\n" +
                "\t`filename`\tTEXT\n" +
                ");");

        Log.e("CREATE TABLE", "Create Table newtest_male Successfully.");

    }

    private void createTableNewTestFemale(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE `newtest_female` (\n" +
                "\t`subject_name`\tTEXT NOT NULL,\n" +
                "\t`filename`\tTEXT\n" +
                ");");

        Log.e("CREATE TABLE", "Create Table newtest_female Successfully.");

    }

    private void createTableHistory(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE `history` (\n" +
                "\t`id` \tINTEGER PRIMARY KEY   AUTOINCREMENT,\n"+
                "\t`subject_name`\tTEXT NOT NULL,\n" +
                "\t`tester_name`\tTEXT NOT NULL,\n" +
                "\t`date_test`\tTEXT NOT NULL,\n" +
                "\t`score`\tTEXT\n" +
                ");");

        Log.e("CREATE TABLE", "Create Table history Successfully.");

    }

    public void insertNameDynasty(String nameDynasty, String fileName) {


        try {

            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put("name_dynasty", nameDynasty);
            Val.put("filename", fileName);
            db.insert("talkname", null, Val);
            db.close();

        } catch (Exception ignored) {
        }
    }

    public void insertNewTestMale(String subjectName, String fileName) {


        try {

            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put("subject_name", subjectName);
            Val.put("filename", fileName);
            db.insert("newtest_male", null, Val);
            db.close();

        } catch (Exception ignored) {
        }
    }

    public void insertNewTestFemale(String subjectName, String fileName) {


        try {

            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put("subject_name", subjectName);
            Val.put("filename", fileName);
            db.insert("newtest_female", null, Val);
            db.close();

        } catch (Exception ignored) {
        }
    }


    public void insertHistory(String subjectName, String testerName, String dateTest, String score) {


        Log.e("insertHistory", testerName);
        try {

            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put("subject_name", subjectName);
            Val.put("tester_name", testerName);
            Val.put("date_test", dateTest);
            Val.put("score", score);
            db.insert("history", null, Val);
            db.close();

        } catch (Exception ignored) {
        }
    }

    public List<sMembers> selectAllSubject(String strQuery) {
        try {
            List<sMembers> MemberList = new ArrayList<>();

            db = this.getReadableDatabase(); // Read Data

            Log.e("String Query : ", strQuery);
            Cursor cursor = db.rawQuery(strQuery, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        sMembers cMember = new sMembers();
                        cMember.setSubjectName(cursor.getString(0));
                        cMember.setFileName(cursor.getString(1));
                        MemberList.add(cMember);
                        Log.e("Result Query : ", cursor.getString(0));
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            return MemberList;

        } catch (Exception e) {
            return null;
        }
    }

    public List<sMembers> selectAllHistory(String strQuery) {
        try {
            List<sMembers> MemberList = new ArrayList<>();

            db = this.getReadableDatabase(); // Read Data

            Log.e("String Query : ", strQuery);
            Cursor cursor = db.rawQuery(strQuery, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        sMembers cMember = new sMembers();
                        cMember.setSubjectName(cursor.getString(0));
                        cMember.setTesterName(cursor.getString(1));
                        cMember.setDateTest(cursor.getString(2));
                        cMember.setScore(cursor.getString(3));
                        cMember.setRowID(cursor.getString(4));
                        MemberList.add(cMember);
                        Log.e("Result Query : ", cursor.getString(0));
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            return MemberList;

        } catch (Exception e) {
            return null;
        }
    }

    public void clearTable () {
        Log.e(TAG,"clearTable");
        db = this.getWritableDatabase();
        db.execSQL("delete from talkname");
        db.execSQL("delete from newtest_male");
        db.execSQL("delete from newtest_female");
        Log.e(TAG,"clearTable");
    }

    public void deleteSingleRow(String rowId)
    {
        db.delete("history", "id =" + rowId, null);
    }

    public void clearTableHistory() {
        db = this.getWritableDatabase();
        db.execSQL("delete from history");
        Log.e(TAG,"clearTable");
    }

    public class sMembers {

        private String _ProductName;
        private String _NameDynasty;
        private String _FileName;
        private String nameDynasty;
        private String fileName;
        private String subjectName;
        private String testerName;
        private String dateTest;
        private String score;
        private String rowID;

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }


        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setTesterName(String testerName) {
            this.testerName = testerName;
        }

        public String getTesterName() {
            return testerName;
        }

        public void setDateTest(String dateTest) {
            this.dateTest = dateTest;
        }

        public String getDateTest() {
            return dateTest;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getScore() {
            return score;
        }

        public void setRowID(String rowID) {
            this.rowID = rowID;
        }

        public String getRowID() {
            return rowID;
        }
    }
}
