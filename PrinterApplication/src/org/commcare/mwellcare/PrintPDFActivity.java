
package org.commcare.mwellcare;
/**
 * This Activity checks whether the data recieved from commcare app or not
 * if yes, it starts creating PDF async 
 * if No, just terminates the printer app
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class PrintPDFActivity extends Activity{

    private final String TAG = this.getClass().getName();
    private CreatePDFAsync mPdfAsync;
    private Activity mActvity;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActvity = this;
        
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            Toast.makeText(this, R.string.printer_app_didnot_recieve_date, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mPdfAsync = new CreatePDFAsync(this);
        mPdfAsync.execute();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mPdfAsync!=null && mPdfAsync.getmPdfAdapter()!=null){
                mPdfAsync.getmPdfAdapter().cancelTimer();
                
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                intent.putExtra(PdfPrintDocumentAdapter.ODK_INTENT_DATA, false);
                finish();
            }
           
        }
        return super.onKeyDown(keyCode, event);
    }
    
    
}
