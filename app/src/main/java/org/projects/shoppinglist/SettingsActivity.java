package org.projects.shoppinglist;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Anders on 17-11-2017.
 */

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        //note that the ID named "content" is defined by Android -
        //it is NOT an identifier we define in .xml
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MyPreferences())
                .commit();
        //note - there is no setContentView and no xml layout
        //for this activity. Because that is defined 100 %
        //in the fragment (MyPreferencesFragment)
    }
}
