package com.example.lcjingsi.memoran;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class noteEdit extends Activity {
    private TextView tv_date;
    private EditText et_content;
    private Button btn_ok;
    private Button btn_cancel;
    private NotesDB DB;
    private SQLiteDatabase dbread;
    public static int ENTER_STATE = 0;
    public static String last_content;
    public static int id;
    public static String years,months,days;
    private Date date1;
    private Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.two_activity);
        btn1=(Button)findViewById(R.id.addphoto) ;
        btn2=(Button)findViewById(R.id.addvideo);
        btn1.setOnClickListener(new mybutton());
        btn2.setOnClickListener(new mybutton());

        et_content = (EditText) findViewById(R.id.et_content);
        // 设置软键盘自动弹出
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        DB = new NotesDB(this);
        dbread = DB.getReadableDatabase();
        Bundle myBundle = this.getIntent().getExtras();
        if((myBundle.getString("years"))==null) {
            tv_date = (TextView) findViewById(R.id.tv_date);
            date1 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = sdf.format(date1);
            tv_date.setText(dateString);
            last_content = myBundle.getString("info");
            et_content.setText(last_content);
        }
        else
        {
            tv_date = (TextView) findViewById(R.id.tv_date);
            years=myBundle.getString("years");
            int year=   Integer.parseInt(years);
            months=myBundle.getString("months");
            int month= Integer.parseInt(months);
            days=myBundle.getString("days");
            int day=Integer.parseInt(days);
            last_content = myBundle.getString("info");
            String dateString=years+"-"+months+"-"+days;
            tv_date.setText(dateString);
            date1=new Date(year-1900,month-1,day);
        }

        // 确认按钮的点击事件
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // 获取日志内容
                String content = et_content.getText().toString();
                Log.d("LOG1", content);
                // 获取写日志时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateNum = sdf.format(date1);
                String sql;
                String sql_count = "SELECT COUNT(*) FROM note";
                SQLiteStatement statement = dbread.compileStatement(sql_count);
                long count = statement.simpleQueryForLong();
                Log.d("COUNT", count + "");
                Log.d("ENTER_STATE", ENTER_STATE + "");
                // 添加一个新的日志
                if (ENTER_STATE == 0) {
                    if (!content.equals("")) {
                        sql = "insert into " + NotesDB.TABLE_NAME_NOTES
                                + " values(" + count + "," + "'" + content
                                + "'" + "," + "'" + dateNum + "')";
                        Log.d("LOG", sql);
                        dbread.execSQL(sql);
                    }
                }
                // 查看并修改一个已有的日志
                else {
                    Log.d("执行命令", "执行了该函数");
                    String updatesql = "update note set content='"
                            + content + "' where _id=" + id;
                    dbread.execSQL(updatesql);
                    // et_content.setText(last_content);
                }
                Intent data = new Intent();
                setResult(2, data);
                finish();
            }
        });
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
    }
    class mybutton implements OnClickListener
    {

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.addphoto:take_photo();break;
                case R.id.addvideo:
            }

        }
    }

    private void take_photo() {
        startActivity(new Intent(noteEdit.this,photo_activity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // if (requestCode == 3 && resultCode == 4) {
        // last_content=data.getStringExtra("data");
        // Log.d("LAST_STRAING", last_content+"gvg");
        // }
    }
}
