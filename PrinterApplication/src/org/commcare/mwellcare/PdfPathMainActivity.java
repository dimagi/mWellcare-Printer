package org.commcare.mwellcare;

import org.commcare.mwellcare.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class PdfPathMainActivity extends Activity implements OnClickListener{
    private EditText mPDFConfigStatus;
    private Activity mActivity;
    private Button mBrowseBtn;
    private LinearLayout mMainLayout;
    public static final int PDF_PATH_REQUEST_CODE = 201;
    public static final String FILE_PATH = "FILE_PATH";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        initUI();
        
        mMainLayout.setVisibility(View.VISIBLE);
        String pdfPath = Util.getPdfNameFromSP(this);
        if(pdfPath !=null){
            mPDFConfigStatus.setText(pdfPath);
        }else{
            mPDFConfigStatus.setText(Environment.getExternalStorageDirectory()+"");
        }
        
    }
    /**
     * This method is to initatite UI for pdf configuration
     */
    private void initUI() {
        mMainLayout = (LinearLayout)findViewById(R.id.ll_main_layout);
        mPDFConfigStatus = (EditText)findViewById(R.id.et_pdf_conf_description);
        mPDFConfigStatus.setOnClickListener(this);
        mBrowseBtn = (Button)findViewById(R.id.btn_browse);
        mBrowseBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_browse){
            Intent intent = new Intent(mActivity, PdfPathConfigurationActivity.class);
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

}
