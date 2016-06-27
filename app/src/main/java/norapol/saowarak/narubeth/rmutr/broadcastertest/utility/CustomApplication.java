package norapol.saowarak.narubeth.rmutr.broadcastertest.utility;

import android.util.Log;

import norapol.saowarak.narubeth.rmutr.broadcastertest.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class CustomApplication extends android.app.Application {

    private static final String TAG = "Application";

    public CustomApplication() { }

  @Override
  public void onCreate() {
      super.onCreate();
      Log.e(TAG, "onCreate");

//
//      TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Srivichai.ttf"); // font from assets: "assets/fonts/Srivichai.ttf

      CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                      .setDefaultFontPath("fonts/Srivichai.ttf")
                      .setFontAttrId(R.attr.fontPath)
                      .build()
      );

  }
}