
package org.commcare.mwellcare;

import android.app.Activity;
import android.os.Bundle;

public class PrintPDFActivity extends Activity {

    private final String TAG = this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        new CreatePDFAsync(this).execute();
        
    }
    
}
