package org.commcare.mwellcare;

import java.io.File;
import java.util.ArrayList;

import org.commcare.mwellcare.projectconfigs.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * This class is used to configure the PDF path by browsing the 
 * SD card locations
 * @author rreddy.avula
 *
 */

public class PdfPathConfigurationActivity extends Activity implements OnClickListener, OnItemClickListener{
    private final String TAG = this.getClass().getName();
    private ListView mPdfPathLv;
    private Button mDoneBtn;
    private TextView mPdfPathToShowTv;
    private ArrayList<ConfigurationBean> mDirectoryList;
    private Activity mActivity;
    private String mFilePath = Environment.getExternalStorageDirectory()+"";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfpath_configuration);
        initUI();
        bindData();
    }

    private void bindData() {
        mPdfPathLv.setAdapter(new PdFConfigureAdapter());
    }

    private void initUI() {
        mActivity = this;
        mPdfPathLv = (ListView)findViewById(R.id.lv_pdf_path);
        mPdfPathLv.setOnItemClickListener(this);
        
        mDoneBtn = (Button)findViewById(R.id.btn_done);
        mDoneBtn.setOnClickListener(this);
        
        mPdfPathToShowTv = (TextView)findViewById(R.id.tv_pdfPath_show);
        mPdfPathToShowTv.setText(mFilePath);
        
        mDirectoryList = new ArrayList<ConfigurationBean>();
        exploreInnerDirectories(mFilePath);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_done){
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            intent.putExtra(PrintPDFActivity.FILE_PATH, mFilePath);
            finish();
            
        }
    }
    public static class ViewHolder{
        
        public TextView pdfPath;
 
    }
    /**
     * PDF configuration adapter to set for listview
     * @author rreddy.avula
     *
     */
    private class PdFConfigureAdapter extends BaseAdapter {
        private LayoutInflater inflator;
        public PdFConfigureAdapter() {
            inflator = mActivity.getLayoutInflater();
        }
        @Override
        public int getCount() {
            return mDirectoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDirectoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView =  inflator.inflate(R.layout.row_pdf_path,parent, false);
                
                holder = new ViewHolder();
                holder.pdfPath = (TextView) convertView.findViewById(R.id.tv_pdf_path);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder)convertView.getTag();
            }
            ConfigurationBean bean = mDirectoryList.get(position);
            if(Constants.LOG)Log.d(TAG,"Fie pdfpathtv::"+holder.pdfPath+"::"+bean+"::pos"+position);
            holder.pdfPath.setText(bean.getFileName());
            
            return convertView;
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        ConfigurationBean bean = mDirectoryList.get(position);
        mFilePath = bean.getFilePath();
        mPdfPathToShowTv.setText(mFilePath);
        exploreInnerDirectories(bean.getFilePath());
    }

    /**
     * This will Fetch out the current directory subfolders
     * @param currentDirectory
     */
    private void exploreInnerDirectories(String currentDirectory) {
        mDirectoryList = new ArrayList<ConfigurationBean>();
        File f = new File(currentDirectory);
        File[] files = f.listFiles();
        for(int i=0; i < files.length; i++){
            File file = files[i];

            if(!file.isHidden() && file.canRead()){
                if(Constants.LOG)Log.d(TAG,"FielPath"+file.getPath());
                if(file.isDirectory()){
                    ConfigurationBean bean = new ConfigurationBean();
                    bean.setFilePath(file.getPath());
                    bean.setFileName(file.getName());
                    mDirectoryList.add(bean);
                    
                    if(Constants.LOG)Log.d(TAG,"Fie Direcotry"+file.getName());
                }else{
                    if(Constants.LOG)Log.d(TAG,"Fie Direcotry"+file.getName());
                }
            } 
        }
        mPdfPathLv.setAdapter(new PdFConfigureAdapter());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mFilePath !=null && !mFilePath.equals(Environment.getExternalStorageDirectory()+"")){
                mFilePath =  mFilePath.substring(0, mFilePath.lastIndexOf("/"));
                mPdfPathToShowTv.setText(mFilePath);
                exploreInnerDirectories(mFilePath);
            }else{
                finish();
            }
        }
        return true;
    }

}
