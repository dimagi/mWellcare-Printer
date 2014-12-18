package org.commcare.mwellcare.components;


import org.commcare.mwellcare.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.cutom_progress_bar);
		setCancelable(false);
	}
	public void setText(String text) {
	    ((TextView)findViewById(R.id.textView1)).setText(text);
        
    }
	public String getDialogText(){
	    return ((TextView)findViewById(R.id.textView1)).getText().toString();
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
