package org.commcare.mwellcare;

import org.commcare.mwellcare.util.Util;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PdfConfigurationDialog extends Dialog implements android.view.View.OnClickListener{
    private  EditText mPdfNameEt ;
    private Button mSaveBtn;
    private Button mCancelBtn;
    private Context mContext;
    private TextView mPDFPathTv;

    public PdfConfigurationDialog(Context context, TextView pdfPathTV) {
        super(context);
        setContentView(R.layout.dialog_configure_pdf);
        mContext = context;
        mPDFPathTv = pdfPathTV;
        initUI();
    }
    /**
     * Initializes the UI elements
     */

    private void initUI() {
        mPdfNameEt = (EditText)findViewById(R.id.et_pdf_name);
        mPdfNameEt.setFilters(new InputFilter[] { filter });
        
        mSaveBtn = (Button)findViewById(R.id.btn_save);
        mSaveBtn.setOnClickListener(this);
        
        mCancelBtn = (Button)findViewById(R.id.btn_cancel);
        mCancelBtn.setOnClickListener(this);
    }
    /**
     * Pdf name filter, this accepts only letters/digists, _,/
     */
    private InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                Spanned dest, int dstart, int dend) {
            
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i)) ) {
                    return "";
                }
            }
            return null;
        }
    };
    

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_save){
            String pdfName = mPdfNameEt.getText().toString().trim();
            if(!pdfName.equals("")){
                Util.savePdfNameInSP(mContext, "/"+pdfName+".pdf");
                    mPDFPathTv.setText(mContext.getResources().getString(R.string.pdf_configuration_path_is)+"/MwellCare"+"/"+pdfName+".pdf"+
                            ". "+mContext.getResources().getString(R.string.if_you_want_change_pdf));
                            
                    
               
            }else{
                Util.showAlert(mContext, mContext.getResources().getString(R.string.please_enter_pdf_name));
            }
            PdfConfigurationDialog.this.dismiss();
        }else if(v.getId() == R.id.btn_cancel){
            PdfConfigurationDialog.this.dismiss();
        }
    }

}
