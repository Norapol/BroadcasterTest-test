package norapol.saowarak.narubeth.rmutr.broadcastertest.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {


    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "broadcastertest";
    public static final String KEY_FirstName = "KEY_FirstName";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void logoutUser() {

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }


    public void createUserInformation(String firstname) {

        editor.putString(KEY_FirstName, firstname);
        editor.commit();
    }

    public String getUserInformation() {

        return pref.getString(KEY_FirstName, "");
    }
}

