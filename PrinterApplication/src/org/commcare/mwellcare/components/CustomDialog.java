package org.commcare.mwellcare.components;


import org.commcare.mwellcare.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;


public class CustomDialog extends Dialog {
    private TextView mDialogTex;

	public CustomDialog(Context context) {
		super(context);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.cutom_progress_bar);
		mDialogTex = (TextView)findViewById(R.id.dialog_text);
		setCancelable(false);
	}
	public void setText(String text) {
	    if(mDialogTex!=null)
	        mDialogTex.setText(text);
        
    }
	public String getDialogText(){
	    return mDialogTex.getText().toString();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		CustomDialog.this.dismiss();
		if( event.getKeyCode() == KeyEvent.KEYCODE_BACK )
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
