package com.example.lcjingsi.memoran;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class editnote extends Activity implements ListView.OnItemClickListener{

    private View.OnClickListener btnclickhandler = new View.OnClickListener() {
        Intent intent;
        File file;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.addphoto:
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用系统照相机
                    file = new File(getMediaDir(),System.currentTimeMillis()+".jpg");
                    if (!file.exists()){//文件不存在 创建
                        try{
                            file.createNewFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    currentPath = file.getAbsolutePath();//取得当前路径
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//指定输出路径 创建文件到SD卡
                    startActivityForResult(intent,REQUEST_CODE_GET_PHOTO);
                    break;

                case R.id.addvideo:
                    intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    file = new File(getMediaDir(),System.currentTimeMillis()+".mp4");
                    if (!file.exists()){
                        try{
                            file.createNewFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    currentPath = file.getAbsolutePath();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent,REQUEST_CODE_GET_VIDEO);
                    break;

                case  R.id.btnsave:
                    //先保存记事，获取到记事ID 再去保存媒体
                    saveMedia(saveNote());
                    setResult(RESULT_OK);
                    finish();
                    break;

                case R.id.btncancel:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;

                default:
                    break;
            }

        }
    };
    //如果外界传入的值有noteid 默认修改note 没有noteid 默认新添加note
    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editnote);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        db = new noteDB(this);
        dbread = db.getReadableDatabase();
        dbwrite = db.getReadableDatabase();

        adapter = new MediaAdapter(this);
        lv = (ListView) findViewById(R.id.list1);
        lv.setAdapter(adapter);

//主键初始化
        etname = (EditText) findViewById(R.id.editTextName);
        etcontent = (EditText) findViewById(R.id.editTextContent);

        noteid = getIntent().getIntExtra(EXTRA_NOTE_ID,-1);

        if(noteid >-1){//修改日志
            //初始化
            //呈现文本标题  文本内容
            etname.setText(getIntent().getStringExtra(EXTRA_NOTE_NAME));
            etcontent.setText(getIntent().getStringExtra(EXTRA_NOTE_CONTENT));

            //查询相关媒体资源 呈现在列表中
            Cursor cursor = dbread.query(noteDB.TABLE_NAME_MEDIA,null,noteDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID +"=?",new String[]{noteid + " "},
                    null,null,null);
            while(cursor.moveToNext()){
                adapter.add(new MediaListCellDate(cursor.getString(cursor.getColumnIndex(noteDB.COLUMN_NAME_MEDIA_PATH)),
                        cursor.getInt(cursor.getColumnIndex(noteDB.COLUMN_NAME_ID))));
                adapter.notifyDataSetChanged();
            }
        }

        findViewById(R.id.btnsave).setOnClickListener(btnclickhandler);
        findViewById(R.id.btncancel).setOnClickListener(btnclickhandler);
        findViewById(R.id.addphoto).setOnClickListener(btnclickhandler);
        findViewById(R.id.addvideo).setOnClickListener(btnclickhandler);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        MediaListCellDate date = adapter.getItem(position);
        Intent intent;
        switch (date.type){
            case MediaType.photo:
                intent = new Intent(this,photoviewer.class);
                intent.putExtra(photoviewer.EXTRA_PATH,date.path);
                startActivity(intent);
                break;

            case MediaType.video:
                intent = new Intent(this,videoviewer.class);
                intent.putExtra(videoviewer.EXTRA_PATH,date.path);
                startActivity(intent);
                break;
        }
    }
    /*
        @Override
        protected void onListItemClick(ListView l, View v, int position, long id) {

            MediaListCellDate date = adapter.getItem(position);
            Intent intent;
            switch (date.type){
                case MediaType.photo:
                    intent = new Intent(this,photoviewer.class);
                    intent.putExtra(photoviewer.EXTRA_PATH,date.path);
                    startActivity(intent);
                    break;

                case MediaType.video:
                    intent = new Intent(this,videoviewer.class);
                    intent.putExtra(videoviewer.EXTRA_PATH,date.path);
                    startActivity(intent);
                    break;
            }
            super.onListItemClick(l, v, position, id);
        }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case  REQUEST_CODE_GET_PHOTO:
            case  REQUEST_CODE_GET_VIDEO:
                if (resultCode == RESULT_OK){
                    adapter.add(new MediaListCellDate(currentPath));
                    adapter.notifyDataSetChanged();
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public File getMediaDir(){//取得媒体路径
        File dir = new File(Environment.getExternalStorageDirectory(),"noteMedia");
        if (!dir.exists()){//目录不存在 创建
            dir.mkdirs();
        }
        return dir;
    }

    public void saveMedia(int noteid){//保存媒体
        MediaListCellDate date;
        ContentValues cv;
        for (int i=0;i<adapter.getCount();i++){
            date = adapter.getItem(i);
            if(date.id<=-1){//新的媒体 需要插入  >1不重复插入
                cv = new ContentValues();
                cv.put(noteDB.COLUMN_NAME_MEDIA_PATH,date.path);
                cv.put(noteDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID,noteid);
                dbwrite.insert(noteDB.TABLE_NAME_MEDIA,null,cv);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int saveNote(){//保存记事
        ContentValues cv = new ContentValues();
        cv.put(noteDB.COLUMN_NAME_NOTE_NAME,etname.getText().toString());
        cv.put(noteDB.COLUMN_NAME_NOTE_CONTENT,etcontent.getText().toString());
        cv.put(noteDB.COLUMN_NAME_NOTE_DATE,new SimpleDateFormat("YYYY-MM-DD HH:MM:SS").format(new Date()));
        //写入
        if(noteid>-1){//有内容
            dbwrite.update(noteDB.TABLE_NAME_NOTES,cv,noteDB.COLUMN_NAME_ID+ "=?",new String[]{noteid + " "});
            return  noteid;
        }else {
            return (int) dbwrite.insert(noteDB.TABLE_NAME_NOTES,null,cv);
        }
    }

    @Override
    protected void onDestroy() {
        dbread.close();
        dbwrite.close();
        super.onDestroy();
    }

    private int noteid = -1;
    private EditText etname,etcontent;
    private MediaAdapter adapter;
    private noteDB db;
    private SQLiteDatabase dbread,dbwrite;
    private String currentPath = null;//当前输出路径
    private ListView lv;

    public static final int REQUEST_CODE_GET_PHOTO = 1;
    public static final int REQUEST_CODE_GET_VIDEO = 2;

    public static final String EXTRA_NOTE_ID = "noteid";
    public static final String EXTRA_NOTE_NAME = "notename";
    public static final String EXTRA_NOTE_CONTENT = "notecontent";

    static class MediaAdapter extends BaseAdapter{

        public MediaAdapter(Context context){
            this.context = context;
        }

        public void add(MediaListCellDate date){
            list.add(date);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public MediaListCellDate getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if(view == null){
                view = LayoutInflater.from(context).inflate(R.layout.medialist,null);
            }
            //取得列表项数据
            MediaListCellDate date = getItem(position);

            ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            TextView tvpath = (TextView) view.findViewById(R.id.tvPath);

            ivIcon.setImageResource(date.iconID);
            tvpath.setText(date.path);
            return view;
        }

        private Context context;
        private List<MediaListCellDate>list = new ArrayList<MediaListCellDate>();
    }

    static class MediaListCellDate{

        public MediaListCellDate(String path){
            this.path = path;

            if(path.endsWith(".jpg")){
                iconID = R.drawable.iconphoto;
                type = MediaType.photo;
            }else if(path.endsWith(".mp4")){
                iconID = R.drawable.iconvideo;
                type = MediaType.video;
            }
        }

        public MediaListCellDate(String path,int id){
            this(path);
            this.id = id;
        }
        int type = 0;
        int id = -1;
        String path = "";
        int iconID = R.drawable.iconphoto;
    }
    static class  MediaType{
        static final  int photo = 1;
        static final  int video = 2;
    }
}
