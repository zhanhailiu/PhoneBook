package com.liye.mycontacts.myContacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.liye.QrCode.zxing.profile.ShowQrCodeActivity;
import com.liye.mycontacts.R;
import com.liye.mycontacts.menu.TelephoneActivity;
import com.liye.mycontacts.utils.ContactInfo;
import com.liye.mycontacts.utils.ContactsUtil;

import java.util.List;

/**
 * Created by Mr.Zhao on 2018/7/8.
 */

public class XiangxiFirst extends android.support.v4.app.Fragment implements View.OnClickListener{
    Context mContext;
    TextView mPhone, mEmail, mAddress, mCallPhone;
    ContactsUtil mContactsUtil;
    ////////////////begin
    TextView mEditContact, mDeleteContact, mShowQrCode;
    //////////////end
    ContactInfo contactInfo;
    public XiangxiFirst() {
        // Required empty public constructor
    }

    public void SetContext(Context context) {
        // Required empty public constructor
        this.mContext = context;
    }

    public void setcon(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xiangxione, null);

        mPhone = (TextView) view.findViewById(R.id.txt_show_phone3);
        mPhone.setText(contactInfo.getPhone());
        mEmail = (TextView) view.findViewById(R.id.txt_show_email3);
        mEmail.setText(contactInfo.getEmail());
        mAddress = (TextView) view.findViewById(R.id.txt_show_address3);
        mAddress.setText(contactInfo.getAddress());

        mEditContact = (TextView) view.findViewById(R.id.edit_contact3);
        mEditContact.setOnClickListener(this);
        mDeleteContact = (TextView) view.findViewById(R.id.delete_contact3);
        mDeleteContact.setOnClickListener(this);

        ///////////////begin
        mShowQrCode = (TextView) view.findViewById(R.id.show_QR_code);
        mShowQrCode.setOnClickListener(this);
        /////////////end

        mCallPhone = (TextView) view.findViewById(R.id.callPhone);
        mCallPhone.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_contact3:
                Intent editContact = new Intent(mContext,
                        EditContactActivity.class);
                editContact.putExtra("contact", contactInfo);
                startActivity(editContact);
                break;
            case R.id.delete_contact3:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("删除");
                builder.setMessage("确定要删除联系人吗?");

                builder.setPositiveButton("删除",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mContactsUtil.delete(contactInfo.getContactId());
                                Intent delete = new Intent(mContext,
                                        TelephoneActivity.class);
                                startActivity(delete);
                            }
                        }

                ).show();

                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // dialog.dismiss();
                            }
                        }).show();

                break;
            case R.id.show_QR_code:

                Intent show_qr = new Intent(mContext,
                        ShowQrCodeActivity.class);
                show_qr.putExtra("contact", contactInfo);
                startActivity(show_qr);
                break;
//			case R.id.send_desk3:
//				// 发送到桌面
//				Intent send = new Intent(
//						"com.android.launcher.action.INSTALL_SHORTCUT");
//				// 快捷方式 图标 名字
//				// 图标
//				send.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, R.drawable.t4);
//				// 名字
//				send.putExtra(Intent.EXTRA_SHORTCUT_NAME, "");
//				Intent handle = XiangxiActivity.this.getPackageManager()
//						.getLaunchIntentForPackage(
//								XiangxiActivity.this.getPackageName());
//				// 点击快捷方式---->处理的事情
//				//Intent handle = new Intent(Intent.ACTION_CALL);
//				send.putExtra(Intent.EXTRA_SHORTCUT_INTENT, handle);
//
//				sendBroadcast(send);
//
//				Toast.makeText(getApplicationContext(), "桌面的快捷方式添加完成",
//						Toast.LENGTH_LONG).show();
//
//				break;
            case R.id.callPhone:
                // 打电话的意图
                Intent intent = new Intent();
                // Intent.ACTION_CALL打电话的动作
                intent.setAction(Intent.ACTION_CALL);
                // uri统一资源标示符
                intent.setData(Uri.parse("tel:" + contactInfo.getPhone()));
                // 开启一个新的界面
                startActivity(intent);
                break;

        }

    }
}