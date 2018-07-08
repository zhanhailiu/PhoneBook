package com.liye.mycontacts.myContacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.liye.QrCode.zxing.profile.ShowQrCodeActivity;
import com.liye.mycontacts.R;
import com.liye.mycontacts.menu.TelephoneActivity;
import com.liye.mycontacts.utils.CommonUtil;
import com.liye.mycontacts.utils.ContactInfo;
import com.liye.mycontacts.utils.ContactsUtil;

import java.util.ArrayList;
import java.util.List;

public class XiangxiActivity extends ActionBarActivity implements OnClickListener, PopupMenu.OnMenuItemClickListener{
	TextView mReturn;
	ImageView mIcon;
	private PopupMenu popupMenu;
	TextView mName;
	ContactsUtil mContactsUtil;
	////////////////begin
	//////////////end
	ContactInfo contactInfo;

	private TextView pe_item_one;
	private TextView pe_item_two;
	private ViewPager peViewPager;
	private XiangxiFirst oneFragment;
	private XiangxiSecond twoFragment;
	private List<Fragment> list;
	private TabFragmentPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiangxi);
		Intent intent = getIntent();
		contactInfo = intent.getParcelableExtra("contact");
//		Log.e(this + "",
//				contactInfo + " contactInfo=" + contactInfo.getAddress());
		initData();
		pe_item_one.setOnClickListener(this);
		pe_item_two.setOnClickListener(this);
		peViewPager.setOnPageChangeListener(new XiangxiActivity.MyPagerChangeListener());

		list = new ArrayList<>();
		oneFragment = new XiangxiFirst();
		twoFragment = new XiangxiSecond();
		oneFragment.SetContext(this);
		oneFragment.setcon(contactInfo);
		twoFragment.SetContext(this);
		list.add(oneFragment);
		list.add(twoFragment);
		adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
		peViewPager.setAdapter(adapter);
		peViewPager.setCurrentItem(0);  //初始化显示第一个页面
		pe_item_one.setBackgroundColor(Color.WHITE);
		pe_item_one.setTextColor(Color.rgb(128,128,128));
	}

	public void show(View v){
		//实例化一个弹出式菜单，传入上下文和控件
		popupMenu = new PopupMenu(this,v);
		//根据菜单填充器获得菜单的布局
		popupMenu.getMenuInflater().inflate(R.menu.menu_a,popupMenu.getMenu());
		//设置菜单的点击事件
		popupMenu.setOnMenuItemClickListener(this);
		//显示菜单
		popupMenu.show();
	}
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()){
			case R.id.menu1:
				Intent editContact = new Intent(XiangxiActivity.this,
						EditContactActivity.class);
				editContact.putExtra("contact", contactInfo);
				startActivity(editContact);
				break;
			case R.id.menu2:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("删除");
				builder.setMessage("确定要删除联系人吗?");

				builder.setPositiveButton("删除",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								mContactsUtil.delete(contactInfo.getContactId());
								Intent delete = new Intent(XiangxiActivity.this,
										TelephoneActivity.class);
								startActivity(delete);
								finish();
							}
						}

				).show();

				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								finish();
								// dialog.dismiss();
							}
						}).show();
				break;
			case R.id.menu3:
				Intent show_qr = new Intent(XiangxiActivity.this,
						ShowQrCodeActivity.class);
				show_qr.putExtra("contact", contactInfo);
				startActivity(show_qr);
				break;
			case R.id.menu4:
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
		return true;
	}

	public void initData() {

		mReturn = (TextView) this.findViewById(R.id.txt_return3);
		mReturn.setOnClickListener(this);
		mIcon = (ImageView) this.findViewById(R.id.img_show_photo3);
		Bitmap smallBitmap = Bitmap.createScaledBitmap(contactInfo.getIcon(), 100, 80, true);
		Bitmap circleBitmap = CommonUtil.createCircleImage(smallBitmap);
		mIcon.setImageBitmap(circleBitmap);

		pe_item_one = (TextView) findViewById(R.id.pe_item_one);
		pe_item_two = (TextView) findViewById(R.id.pe_item_two);
		peViewPager = (ViewPager) findViewById(R.id.peViewPager);

		mName = (TextView) this.findViewById(R.id.txt_show_name3);
		mName.setText(contactInfo.getName());
		mContactsUtil = new ContactsUtil(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.txt_return3:
				finish();
				break;
			case R.id.tv_item_one:
				peViewPager.setCurrentItem(0);
				pe_item_one.setBackgroundColor(Color.WHITE);
				pe_item_two.setBackgroundColor(Color.rgb(44,162,192));
				pe_item_one.setTextColor(Color.rgb(128,128,128));
				pe_item_two.setTextColor(Color.WHITE);
				break;
			case R.id.tv_item_two:
				peViewPager.setCurrentItem(1);
				pe_item_one.setBackgroundColor(Color.rgb(44,162,192));
				pe_item_two.setBackgroundColor(Color.WHITE);
				pe_item_two.setTextColor(Color.rgb(128,128,128));
				pe_item_one.setTextColor(Color.WHITE);
				break;
		}

	}

	public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
				case 0:
					pe_item_one.setBackgroundColor(Color.WHITE);
					pe_item_two.setBackgroundColor(Color.rgb(44,162,192));
					pe_item_one.setTextColor(Color.rgb(128,128,128));
					pe_item_two.setTextColor(Color.WHITE);
					break;
				case 1:
					pe_item_one.setBackgroundColor(Color.rgb(44,162,192));
					pe_item_two.setBackgroundColor(Color.WHITE);
					pe_item_two.setTextColor(Color.rgb(128,128,128));
					pe_item_one.setTextColor(Color.WHITE);
					break;
			}
		}
	}
}