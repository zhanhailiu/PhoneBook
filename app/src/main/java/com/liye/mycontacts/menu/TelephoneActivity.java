package com.liye.mycontacts.menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.liye.QrCode.zxing.activity.CaptureActivity;
import com.liye.mycontacts.R;
import com.liye.mycontacts.adapter.ContactAdapter;
import com.liye.mycontacts.adapter.ListViewAdapter;
import com.liye.mycontacts.listener.MListOnItemClickListener;
import com.liye.mycontacts.listener.MyAddTextChangedListener;
import com.liye.mycontacts.listener.MyOnItemClickListener;
import com.liye.mycontacts.listener.MyOnclickListener;
import com.liye.mycontacts.myContacts.AddPeopleActivity;
import com.liye.mycontacts.myContacts.SearchEditText;
import com.liye.mycontacts.myContacts.SideBar;
import com.liye.mycontacts.utils.CommonUtil;
import com.liye.mycontacts.utils.ContactInfo;
import com.liye.mycontacts.utils.ContactsUtil;
import com.liye.mycontacts.utils.PinyinComparator;
import com.liye.onlineVoice.GlobalApplication;
import com.liye.onlineVoice.OnlineVoiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

///////edit

public class TelephoneActivity  extends FragmentActivity {
    /*************zjt:sidebar*************/
    private SideBar sideBar;
    private TextView dialog;
    /*************zjt:sidebar*************/
    //侧滑布局对象，用于通过手指滑动将左侧的菜单布局进行显示或隐藏。
    private ImageView ImageView_menu;
    ProgressDialog mProgressDialog;// 圆形进度条的对话框
    public static final int LOAD_FINISH = 0X01;
    private List<Map<String, Object>> listitem;
    ListView mList;
    //侧滑中展示其它功能的
    ListView mListShow;
    List<ContactInfo> filterContacts;
    ImageView mHead;
    ImageView mExit;
    Button mAddText;
    Button mCallphone;

    /////////begin
    Switch onlineVoiceSwitch;
    /////////////end

    Button mScanQrCode;
    public static final int GET_CODE = 0;

    ContactInfo mContactInfo;
    List<ContactInfo> contacts;
    ContactsUtil mContactsUtil;
    public DrawerLayout mDrawerLayout;
    public ContactAdapter adapter;
    SearchEditText mSearchEditText;
    public String[] contentItems = {"Content Item a1", "Content Item 2", "Content Item 3",
            "Content Item 4"};
    String[] title = {"日历", "午后小憩", "听音乐", "心情记事本"};
    int imageId[] = {R.drawable.calendar2, R.drawable.image30, R.drawable.tocas, R.drawable.pink_design};
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == LOAD_FINISH) {
                // 按首字母排序
                Collections.sort(contacts, new PinyinComparator());
                adapter = new ContactAdapter(TelephoneActivity.this, contacts);
                mList.setAdapter(adapter);
                // 取消进度条
                mProgressDialog.dismiss();
            }
        }

        ;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone);


        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, "请稍等", "数据正在加载......");
        listitem = new ArrayList<Map<String, Object>>();
        mContactInfo = new ContactInfo();
        // 将上述资源转化为list集合
        for (int i = 0; i < title.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", imageId[i]);
            map.put("title", title[i]);
            listitem.add(map);
        }
        new Thread() {
            public void run() {
                mContactsUtil = new ContactsUtil(TelephoneActivity.this);
                contacts = mContactsUtil.select();
                handler.sendEmptyMessage(LOAD_FINISH);
            }
        }.start();
        //initView();
    }

    public void initView() {
        /*
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        mHead = (ImageView) findViewById(R.id.imagebtn);
        getCircleHead();
        mExit = (ImageView) findViewById(R.id.imagebtn1);
        mExit.setOnClickListener(new MyOnclickListener(this));
        mListShow = (ListView) findViewById(R.id.contentList);
        ListViewAdapter adapter1 = new ListViewAdapter(this, listitem);
        mListShow.setAdapter(adapter1);
        mListShow.setOnItemClickListener(new MyOnItemClickListener(this));

        ImageView_menu = (ImageView) findViewById(R.id.menu1);
        ImageView_menu.setOnClickListener(new MyOnclickListener(this));
        mList = (ListView) this.findViewById(R.id.lst_show_contact);
        mList.setOnItemClickListener(new MListOnItemClickListener(this));
        mAddText = (Button) this.findViewById(R.id.txt_add_contact);
        mAddText.setOnClickListener(new MyOnclickListener(this));
        mCallphone = (Button) this.findViewById(R.id.txt_call_phone);
        mCallphone.setOnClickListener(new MyOnclickListener(this));


        mScanQrCode = (Button) this.findViewById(R.id.txt_scan_QrCode);
        mScanQrCode.setOnClickListener(new MyOnclickListener(this));
        ///////zhl
        onlineVoiceSwitch = (Switch) this.findViewById(R.id.online_voice_switch);
        onlineVoiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){

                    final EditText et = new EditText(TelephoneActivity.this);
                    et.setInputType(InputType.TYPE_CLASS_PHONE);
                    et.setText(GlobalApplication.getLoginInfo());
                    new AlertDialog.Builder(TelephoneActivity.this).setTitle("请输入本机号码")
                            .setView(et)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String local_phone = et.getText().toString();
                                    if( !OnlineVoiceManager.getInstance().initialize(local_phone) ){
                                        Toast.makeText(TelephoneActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                                        onlineVoiceSwitch.setChecked(false);
                                    }else GlobalApplication.setLoginInfo(local_phone);

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onlineVoiceSwitch.setChecked(false);
                                 }
                            }).show();
                }else{
                    OnlineVoiceManager.getInstance().uninitialize();
                }
            }
        });
        //////////zhl

        mSearchEditText = (SearchEditText) this.findViewById(R.id.edt_search);
        // 添加一个文本改变的监听事件
        mSearchEditText.addTextChangedListener(new MyAddTextChangedListener(this));*/


        /***********************************zjt:sidebar*****************/
        sideBar = (SideBar) findViewById(R.id.mysidebar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getFirstPosition(s.charAt(0));
                if(position != -1) {
                    mList.setSelection(position);
                }

            }
        });
    }


    // 过滤联系人的信息
    public void filterContact(String input) {
        filterContacts = new ArrayList<ContactInfo>();
        // 如果输入是空的
        if (TextUtils.isEmpty(input)) {
            filterContacts = contacts;
            adapter.reflash(filterContacts);
        } else {
            // 过滤
            for (int i = 0; i < contacts.size(); i++) {
                // 姓名
                String name = contacts.get(i).getName();
                if (name.contains(input) || name.startsWith(input)) {
                    filterContacts.add(contacts.get(i));
                }
            }
            // 拿到过滤联系人的信息filterContacts
            // 刷新适配器
            adapter.reflash(filterContacts);
        }
    }

    //图片圆形化
    public void getCircleHead() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.t1);
        Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap, 100, 80, true);
        Bitmap circleBitmap = CommonUtil.createCircleImage(smallBitmap);
        mHead.setImageBitmap(circleBitmap);

    }

    //////////////begin
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == GET_CODE && resultCode == CaptureActivity.RESULT_CODE_QR_SCAN) {

            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来

            try {
                JSONObject jsonObject = new JSONObject(scanResult);
                String invalid = "";
                String name = jsonObject.optString("name",invalid),phone = jsonObject.optString("phone",invalid),
                        email = jsonObject.optString("email",invalid),address = jsonObject.optString("address",invalid);
                if(name.equals(invalid) || phone.equals(invalid)  ){
                    Toast.makeText(TelephoneActivity.this,"二维码名片格式错误",Toast.LENGTH_SHORT).show();

                    return ;
                }else{
                    Intent intent = new Intent(TelephoneActivity.this,
                            AddPeopleActivity.class);
                    Log.d("infooo1",scanResult);
                    intent.putExtra("contactInfo",scanResult);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(TelephoneActivity.this,"二维码名片格式错误",Toast.LENGTH_SHORT).show();
            }
        }
    }




}
