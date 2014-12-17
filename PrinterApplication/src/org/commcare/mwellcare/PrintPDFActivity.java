
package org.commcare.mwellcare;
/**
 * This Activity checks whether the data recieved from commcare app or not
 * if yes, it starts creating PDF async 
 * if No, just terminates the printer app
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class PrintPDFActivity extends Activity {

    private final String TAG = this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            Toast.makeText(this, R.string.printer_app_didnot_recieve_date, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        new CreatePDFAsync(this).execute();
        
    }
    
}
