
package org.commcare.mwellcare;
/**
 * This Activity checks whether the data recieved from commcare app or not
 * if yes, it starts creating PDF async 
 * if No, just terminates the printer app
 */

import org.commcare.mwellcare.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PrintPDFActivity extends Activity implements OnClickListener{

    private final String TAG = this.getClass().getName();
    private TextView mPDFConfigStatus;
    private Activity mActivity;
    private Button mConfigPDFBtn;
    private LinearLayout mMainLayout;
    private CreatePDFAsync mPdfAsync;
    private static final String PRINTOUT_ACTION = "org.commcare.mwellcare.PRINTOUT_ACTION";
    public static final int PDF_PATH_REQUEST_CODE = 201;
    public static final String FILE_PATH = "FILE_PATH";
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        initUI();
        String action = getIntent().getAction();
        
        if(action.equals(PRINTOUT_ACTION)){
            //means come up from commcare odk
            mMainLayout.setVisibility(View.GONE);
            Bundle bundle = getIntent().getExtras();
            if(bundle == null){
                Toast.makeText(this, R.string.printer_app_didnot_recieve_date, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            
            mPdfAsync = new CreatePDFAsync(this);
            mPdfAsync.execute();
        }else if(action.equals(Intent.ACTION_MAIN)){
            //means application opened normally and needs to configure PDF name
            mMainLayout.setVisibility(View.VISIBLE);
            String pdfPath = Util.getPdfNameFromSP(this);
            if(pdfPath !=null){
                mPDFConfigStatus.setText(pdfPath);
            }
        }
    }
    /**
     * This method is to initatite UI for pdf configuration
     */
    private void initUI() {
        mMainLayout = (LinearLayout)findViewById(R.id.ll_main_layout);
        mPDFConfigStatus = (TextView)findViewById(R.id.tv_pdf_conf_description);
        mPDFConfigStatus.setOnClickListener(this);
//        mConfigPDFBtn = (Button)findViewById(R.id.btn_configure_pdf_name);
//        mConfigPDFBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_pdf_conf_description){
//            new PdfConfigurationDialog(PrintPDFActivity.this, mPDFConfigStatus).show();
            Intent intent = new Intent(PrintPDFActivity.this, PdfPathConfigurationActivity.class);
            startActivityForResult(intent, PDF_PATH_REQUEST_CODE);
        }
        
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PDF_PATH_REQUEST_CODE && data!=null){
            Util.savePdfNameInSP(mActivity, data.getStringExtra(FILE_PATH));
            mPDFConfigStatus.setText(data.getStringExtra(FILE_PATH));
            
        }
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
