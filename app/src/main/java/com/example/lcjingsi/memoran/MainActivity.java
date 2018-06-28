package com.example.lcjingsi.memoran;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.data;
public class MainActivity extends Activity implements OnScrollListener,
        OnItemClickListener, OnItemLongClickListener {
    public  int ages;
    private Context mContext;
    private ListView listview,listview2,listview3,listview4,listview5,listview6,listview7,listview1;
    private SimpleAdapter simp_adapter;
    private List<Map<String, Object>> dataList;
    private List<Map<String, Object>> dataList1;
    private List<Map<String, Object>> dataLists;
    private List<Map<String, Object>> dataList6;
    private List<Map<String, Object>> dataList2;
    private List<Map<String, Object>> dataList3;
    private List<Map<String, Object>> dataList4;
    private List<Map<String, Object>> dataList5;
    private List<Map<String, Object>> dataList7;
    private Button addNote;
    private TextView tv_content,abc;
    private NotesDB DB;
    private SQLiteDatabase dbread;
    private int years,months,days,delete;
    private LinearLayout  linear;
    private  String a;private int list;
    private  Builder builder;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initview();
        click_OnClicker();

        linear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        mContext = this;

        addNote.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noteEdit.ENTER_STATE = 0;
                Intent intent = new Intent(mContext,noteEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        DB = new NotesDB(this);
        dbread = DB.getReadableDatabase();
        dbread.execSQL("delete from note");
        try {
            RefreshNotesList();
        } catch (ParseException e) {
            e.printStackTrace();
        }}
    public void click_OnClicker()
    {
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        listview.setOnScrollListener(this);
        listview4.setOnItemClickListener(this);
        listview4.setOnItemLongClickListener(this);
        listview4.setOnScrollListener(this);
        listview2.setOnItemClickListener(this);
        listview2.setOnItemLongClickListener(this);
        listview2.setOnScrollListener(this);
        listview3.setOnItemClickListener(this);
        listview3.setOnItemLongClickListener(this);
        listview3.setOnScrollListener(this);
        listview4.setOnItemClickListener(this);
        listview4.setOnItemLongClickListener(this);
        listview4.setOnScrollListener(this);
        listview6.setOnItemClickListener(this);
        listview6.setOnItemLongClickListener(this);
        listview6.setOnScrollListener(this);
        listview5.setOnItemClickListener(this);
        listview5.setOnItemLongClickListener(this);
        listview5.setOnScrollListener(this);
        listview7.setOnItemClickListener(this);
        listview7.setOnItemLongClickListener(this);
        listview7.setOnScrollListener(this);


    }





    private void initview() {
        tv_content = (TextView) findViewById(R.id.tv_content);
        listview = (ListView) findViewById(R.id.listview);
        dataList = new ArrayList<Map<String, Object>>();
        dataLists = new ArrayList<Map<String, Object>>();
        dataList1= new ArrayList<Map<String, Object>>();
        dataList2 = new ArrayList<Map<String, Object>>();
        dataList3 = new ArrayList<Map<String, Object>>();
        dataList4 = new ArrayList<Map<String, Object>>();
        dataList5 = new ArrayList<Map<String, Object>>();
        dataList6 = new ArrayList<Map<String, Object>>();
        dataList7 = new ArrayList<Map<String, Object>>();
        addNote = (Button) findViewById(R.id.btn_editnote);
        linear=(LinearLayout)findViewById(R.id.linear) ;
        listview2 = (ListView) findViewById(R.id.listview2);
        listview3 = (ListView) findViewById(R.id.listview3);
        listview4 = (ListView) findViewById(R.id.listview4);
        listview5 = (ListView) findViewById(R.id.listview5);
        listview6 = (ListView) findViewById(R.id.listview6);
        listview7 = (ListView) findViewById(R.id.listview7);
        abc= (TextView) findViewById(R.id.abc);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getTime()
    {
        Calendar calendar=Calendar.getInstance();
        years=calendar.get(Calendar.YEAR);
        months=calendar.get(Calendar.MONTH);
        days=calendar.get(Calendar.DAY_OF_MONTH);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDateDialog()
    {   this. getTime();
        final  DatePickerDialog dialogs=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            }
        },years,months,days);
        dialogs.setButton(Dialog.BUTTON_POSITIVE, "确定", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatePicker picker=dialogs.getDatePicker();
                years=picker.getYear();
                months=picker.getMonth()+1;
                days=picker.getDayOfMonth();
                noteEdit.ENTER_STATE = 0;
                final  Intent intent=new Intent(mContext,noteEdit.class);
                Bundle bundle=new Bundle();
                bundle.putString("info","");
                bundle.putString("years", String.valueOf(years));
                bundle.putString("months", String.valueOf(months));
                bundle.putString("days", String.valueOf(days));
                intent.putExtras(bundle);
                startActivityForResult(intent,1);

            }

        });
        dialogs.show();
        return;


    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void RefreshNotesList() throws ParseException {
        refreshlist1();
        refreshlist2();
        refreshlist3();
        refreshlist4();
        refreshlist5();
        refreshlist6();
        refreshlist7();
        refreshlists();
        dataLists = getData();
        for (int i = 0; i < dataLists.size(); i++) {
            Map<String, Object> map = dataLists.get(i);
            Set entrySet = map.entrySet();
            Iterator<Map> it2 = entrySet.iterator();
            it2.next();
            Map.Entry me = (Map.Entry) it2.next();
            String b= (String) me.getKey();
            a = (String) me.getValue();
            if(getweek(a)=="1")
                dataList1.add(map);
            else if(getweek(a)=="2")
                dataList2.add(map);
            else if(getweek(a)=="3")
                dataList3.add(map);
            else if(getweek(a)=="4")
                dataList4.add(map);
            else if(getweek(a)=="5")
                dataList5.add(map);
            else if(getweek(a)=="6")
                dataList6.add(map);
            else if(getweek(a)=="7")
                dataList7.add(map);
        }
        simp_adapter = new SimpleAdapter(this, dataList1, R.layout.show_list,
                new String[]{"tv_content", "tv_date"}, new int[]{
                R.id.tv_content, R.id.tv_date});
        listview.setAdapter(simp_adapter);
        simp_adapter = new SimpleAdapter(this, dataList2, R.layout.show_list,
                new String[]{"tv_content", "tv_date"}, new int[]{
                R.id.tv_content, R.id.tv_date});
        listview2.setAdapter(simp_adapter);
        simp_adapter = new SimpleAdapter(this, dataList3, R.layout.show_list,
                new String[]{"tv_content", "tv_date"}, new int[]{
                R.id.tv_content, R.id.tv_date});
        listview3.setAdapter(simp_adapter);
        simp_adapter = new SimpleAdapter(this, dataList4, R.layout.show_list,
                new String[]{"tv_content", "tv_date"}, new int[]{
                R.id.tv_content, R.id.tv_date});
        listview4.setAdapter(simp_adapter);
        simp_adapter = new SimpleAdapter(this, dataList5, R.layout.show_list,
                new String[]{"tv_content", "tv_date"}, new int[]{
                R.id.tv_content, R.id.tv_date});
        listview5.setAdapter(simp_adapter);
        simp_adapter = new SimpleAdapter(this, dataList6, R.layout.show_list,
                new String[]{"tv_content", "tv_date"}, new int[]{
                R.id.tv_content, R.id.tv_date});
        listview6.setAdapter(simp_adapter);
        simp_adapter = new SimpleAdapter(this, dataList7, R.layout.show_list,
                new String[]{"tv_content", "tv_date"}, new int[]{
                R.id.tv_content, R.id.tv_date});
        listview7.setAdapter(simp_adapter);


    }


    private void refreshlist() {
        refreshlist1();
        refreshlist2();
        refreshlist3();
        refreshlist4();
        refreshlist5();
        refreshlist6();
        refreshlist7();
    }

    private void refreshlist7() {
        int size = dataList7.size();
        if (size > 0) {
            dataList7.removeAll(dataList7);
            simp_adapter.notifyDataSetChanged();

        }
    }

    private void refreshlist6() {
        int size = dataList6.size();
        if (size > 0) {
            dataList6.removeAll(dataList6);
            simp_adapter.notifyDataSetChanged();

        }
    }


    private void refreshlist5() {
        int size = dataList5.size();
        if (size > 0) {
            dataList5.removeAll(dataList5);
            simp_adapter.notifyDataSetChanged();

        }
    }

    private void refreshlist4() {
        int size = dataList4.size();
        if (size > 0) {
            dataList4.removeAll(dataList4);
            simp_adapter.notifyDataSetChanged();

        }
    }

    private void refreshlist3() {
        int size = dataList3.size();
        if (size > 0) {
            dataList3.removeAll(dataList3);
            simp_adapter.notifyDataSetChanged();

        }
    }

    private void refreshlist2() {int size = dataList2.size();
        if (size > 0) {
            dataList2.removeAll(dataList2);
            simp_adapter.notifyDataSetChanged();

        }
    }

    private void refreshlist1() {
        int size = dataList1.size();
        if (size > 0) {
            dataList1.removeAll(dataList1);
            simp_adapter.notifyDataSetChanged();

        }
    }
    private void refreshlists() {

        int size = dataLists.size();
        if (size > 0) {
            dataLists.removeAll(dataLists);
            simp_adapter.notifyDataSetChanged();

        }
    }





    @RequiresApi(api = Build.VERSION_CODES.N)//判断日期是星期几
    public String getweek(String time) throws ParseException {
        String week="";
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(format.parse(time));
        if(calendar.get(Calendar.DAY_OF_WEEK)==1)
        {
            week="7";
        }
        else if(calendar.get(Calendar.DAY_OF_WEEK)==2)
        {
            week="1";
        }else if(calendar.get(Calendar.DAY_OF_WEEK)==3)
        {
            week="2";
        }else if(calendar.get(Calendar.DAY_OF_WEEK)==4)
        {
            week="3";
        }else if(calendar.get(Calendar.DAY_OF_WEEK)==5)
        {
            week="4";
        }else if(calendar.get(Calendar.DAY_OF_WEEK)==6)
        {
            week="5";
        }else if(calendar.get(Calendar.DAY_OF_WEEK)==7)
        {
            week="6";
        }
        return week;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Map<String, Object>> getData() throws ParseException {
        Cursor cursor = dbread.query("note", null, "content!=\"\"", null, null,
                null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_content", name);
            map.put("tv_date", date);
            dataList.add(map);


        }
        cursor.close();
        return dataList;

    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }


    // 滑动listview监听事件
    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
        // TODO Auto-generated method stub
        switch (arg1) {
            case SCROLL_STATE_FLING:
                Log.i("main", "用户在手指离开屏幕之前，由于用力的滑了一下，视图能依靠惯性继续滑动");
            case SCROLL_STATE_IDLE:
                Log.i("main", "视图已经停止滑动");
            case SCROLL_STATE_TOUCH_SCROLL:
                Log.i("main", "手指没有离开屏幕，试图正在滑动");
        }
    }


    // 点击listview中某一项的监听事件


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    // 接受上一个页面返回的数据，并刷新页面
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            try {
                RefreshNotesList();
            } catch (ParseException e) {

            }
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        list=position;
        switch(parent.getId()) {
            case R.id.listview2:noteEdit.ENTER_STATE = 1;clicik_two();break;
            case R.id.listview3:noteEdit.ENTER_STATE = 1;clicik_three();break;
            case R.id.listview5:noteEdit.ENTER_STATE = 1;clicik_five();break;
            case R.id.listview6:noteEdit.ENTER_STATE = 1;clicik_six();break;
            case R.id.listview7:noteEdit.ENTER_STATE = 1;clicik_seven();break;
            case R.id.listview4:noteEdit.ENTER_STATE = 1;clicik_four();break;
            case R.id.listview: noteEdit.ENTER_STATE = 1;clicik_one();break;
        }
    }

    private void clicik_seven() {
        String content = listview7.getItemAtPosition(list) + "";
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));
        Log.d("CONTENT", content1);
        Cursor c = dbread.query("note", null,
                "content=" + "'" + content1 + "'", null, null, null, null);
        while (c.moveToNext()) {
            String No = c.getString(c.getColumnIndex("_id"));
            Log.d("TEXT", No);
            Intent myIntent = new Intent(MainActivity.this, noteEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("info", content1);
            noteEdit.id = Integer.parseInt(No);
            myIntent.putExtras(bundle);
            startActivityForResult(myIntent, 1);

        }
    }

    private void clicik_six() {
        String content = listview6.getItemAtPosition(list) + "";
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));
        Log.d("CONTENT", content1);
        Cursor c = dbread.query("note", null,
                "content=" + "'" + content1 + "'", null, null, null, null);
        while (c.moveToNext()) {
            String No = c.getString(c.getColumnIndex("_id"));
            Log.d("TEXT", No);
            Intent myIntent = new Intent(MainActivity.this, noteEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("info", content1);
            noteEdit.id = Integer.parseInt(No);
            myIntent.putExtras(bundle);
            startActivityForResult(myIntent, 1);

        }
    }

    private void clicik_five() {
        String content = listview5.getItemAtPosition(list) + "";
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));
        Log.d("CONTENT", content1);
        Cursor c = dbread.query("note", null,
                "content=" + "'" + content1 + "'", null, null, null, null);
        while (c.moveToNext()) {
            String No = c.getString(c.getColumnIndex("_id"));
            Log.d("TEXT", No);
            Intent myIntent = new Intent(MainActivity.this, noteEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("info", content1);
            noteEdit.id = Integer.parseInt(No);
            myIntent.putExtras(bundle);
            startActivityForResult(myIntent, 1);

        }
    }

    private void clicik_three() {
        String content = listview3.getItemAtPosition(list) + "";
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));
        Log.d("CONTENT", content1);
        Cursor c = dbread.query("note", null,
                "content=" + "'" + content1 + "'", null, null, null, null);
        while (c.moveToNext()) {
            String No = c.getString(c.getColumnIndex("_id"));
            Log.d("TEXT", No);
            Intent myIntent = new Intent(MainActivity.this, noteEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("info", content1);
            noteEdit.id = Integer.parseInt(No);
            myIntent.putExtras(bundle);
            startActivityForResult(myIntent, 1);

        }
    }

    private void clicik_four() {
        String content = listview4.getItemAtPosition(list) + "";
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));
        Log.d("CONTENT", content1);
        Cursor c = dbread.query("note", null,
                "content=" + "'" + content1 + "'", null, null, null, null);
        while (c.moveToNext()) {
            String No = c.getString(c.getColumnIndex("_id"));
            Log.d("TEXT", No);
            Intent myIntent = new Intent(MainActivity.this, noteEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("info", content1);
            noteEdit.id = Integer.parseInt(No);
            myIntent.putExtras(bundle);
            startActivityForResult(myIntent, 1);

        }
    }

    private void clicik_two() {
        String content = listview2.getItemAtPosition(list) + "";
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));
        Log.d("CONTENT", content1);
        Cursor c = dbread.query("note", null,
                "content=" + "'" + content1 + "'", null, null, null, null);
        while (c.moveToNext()) {
            String No = c.getString(c.getColumnIndex("_id"));
            Log.d("TEXT", No);
            Intent myIntent = new Intent(MainActivity.this, noteEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("info", content1);
            noteEdit.id = Integer.parseInt(No);
            myIntent.putExtras(bundle);
            startActivityForResult(myIntent, 1);

        }
    }


    private void clicik_one() {
        String content = listview.getItemAtPosition(list) + "";
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));
        Log.d("CONTENT", content1);
        Cursor c = dbread.query("note", null,
                "content=" + "'" + content1 + "'", null, null, null, null);
        while (c.moveToNext()) {
            String No = c.getString(c.getColumnIndex("_id"));
            Log.d("TEXT", No);
            Intent myIntent = new Intent(MainActivity.this, noteEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("info", content1);
            noteEdit.id = Integer.parseInt(No);
            myIntent.putExtras(bundle);
            startActivityForResult(myIntent, 1);

        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        delete=position;
        switch(parent.getId())
        {
            case R.id.listview:click_delete1();break;
            case R.id.listview2:click_delete2();break;
            case R.id.listview3:clicik_delete3();break;
            case R.id.listview4:clicik_delete4();break;
            case R.id.listview5:clicik_delete5();break;
            case R.id.listview6:clicik_delete6();break;
            case R.id.listview7:clicik_delete7();break;
        }

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
        return true;

    }

    private void clicik_delete7() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listview7.getItemAtPosition(delete) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));
                Cursor c = dbread.query("note", null, "content=" + "'"
                        + content1 + "'", null, null, null, null);
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex("_id"));
                    String sql_del = "update note set content='' where _id="
                            + id;
                    dbread.execSQL(sql_del);
                    try {
                        RefreshNotesList();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void clicik_delete6() {builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listview6.getItemAtPosition(delete) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));
                Cursor c = dbread.query("note", null, "content=" + "'"
                        + content1 + "'", null, null, null, null);
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex("_id"));
                    String sql_del = "update note set content='' where _id="
                            + id;
                    dbread.execSQL(sql_del);
                    try {
                        RefreshNotesList();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void clicik_delete5() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listview5.getItemAtPosition(delete) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));
                Cursor c = dbread.query("note", null, "content=" + "'"
                        + content1 + "'", null, null, null, null);
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex("_id"));
                    String sql_del = "update note set content='' where _id="
                            + id;
                    dbread.execSQL(sql_del);
                    try {
                        RefreshNotesList();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void clicik_delete4() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listview4.getItemAtPosition(delete) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));
                Cursor c = dbread.query("note", null, "content=" + "'"
                        + content1 + "'", null, null, null, null);
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex("_id"));
                    String sql_del = "update note set content='' where _id="
                            + id;
                    dbread.execSQL(sql_del);
                    try {
                        RefreshNotesList();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void clicik_delete3() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listview3.getItemAtPosition(delete) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));
                Cursor c = dbread.query("note", null, "content=" + "'"
                        + content1 + "'", null, null, null, null);
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex("_id"));
                    String sql_del = "update note set content='' where _id="
                            + id;
                    dbread.execSQL(sql_del);
                    try {
                        RefreshNotesList();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void click_delete2() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listview2.getItemAtPosition(delete) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));
                Cursor c = dbread.query("note", null, "content=" + "'"
                        + content1 + "'", null, null, null, null);
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex("_id"));
                    String sql_del = "update note set content='' where _id="
                            + id;
                    dbread.execSQL(sql_del);
                    try {
                        RefreshNotesList();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void click_delete1() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listview.getItemAtPosition(delete) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));
                Cursor c = dbread.query("note", null, "content=" + "'"
                        + content1 + "'", null, null, null, null);
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex("_id"));
                    String sql_del = "update note set content='' where _id="
                            + id;
                    dbread.execSQL(sql_del);
                    try {
                        RefreshNotesList();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
