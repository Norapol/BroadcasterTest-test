package norapol.saowarak.narubeth.rmutr.broadcastertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    } //Main Method

//    public void clickOKname(View view) {
//
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String strName = editText.getText().toString().trim();
//
//        //Check Space
//        if (strName.equals("")) {
//            //Have Space
//            Toast.makeText(this, "กรุณากรอกชื่อของคุณ", Toast.LENGTH_SHORT).show();
//        } else {
//
//            //No Space
//            String strTitle = getIntent().getStringExtra("Title");
//            String strDetail = getIntent().getStringExtra("Detail");
//
//            Intent intent = new Intent(NameActivity.this, TestActivity.class);
//            intent.putExtra("Title", strTitle);
//            intent.putExtra("Detail", strDetail);
//            intent.putExtra("Name", strName);
//            startActivity(intent);
//
//        } //if
//
//
//    } //ClickOK

    public void clickCancelName(View view) {
        finish();
    }

} //Main Class
