package com.liye.mycontacts.myContacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.liye.mycontacts.R;
import com.liye.mycontacts.adapter.DBManager;
import com.liye.mycontacts.adapter.IconPagerAdapter;
import com.liye.mycontacts.listener.MyOnclickListener;
import com.liye.mycontacts.menu.TelephoneActivity;
import com.liye.mycontacts.utils.CharacterParser;
import com.liye.mycontacts.utils.CommonUtil;
import com.liye.mycontacts.utils.ContactInfo;
import com.liye.mycontacts.utils.ContactsUtil;
import com.liye.mycontacts.utils.FestivalInfo;
import com.liye.onlineVoice.GlobalApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddPeopleActivity extends Activity implements OnClickListener {
	Button mBcancel, mSave;
	ContactsUtil mContactsUtil;
	EditText mEdtName;
	EditText mEdtPhone;
	EditText mEdtEmail;
	EditText mEdtAddress;
	ImageView mIgvIcon;
	EditText mEdtbirth;
	int birthmonth, birthday;
	boolean ishavebirth;
	private DBManager dm;
	//ViewPager mVpgIcon;
	List<ImageView> image = new ArrayList<ImageView>();
	int[] iconId = { R.drawable.t1, R.drawable.t2, R.drawable.t3,
			R.drawable.lianxiren, R.drawable.t4, R.drawable.t5, R.drawable.t6,
			R.drawable.t7, R.drawable.t8, R.drawable.t10, R.drawable.t11,
			R.drawable.touxiang };
	//ImageView[] mIcon = new ImageView[iconId.length];
	int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_people);
		initData();
	}

	public void initData() {
		mBcancel = (Button) this.findViewById(R.id.btn_cancel);
		mBcancel.setOnClickListener(this);
		mSave = (Button) this.findViewById(R.id.btn_save);
		mSave.setOnClickListener(this);
		mEdtName = (EditText) findViewById(R.id.edt_show_name4);
		mEdtPhone = (EditText) findViewById(R.id.edt_show_phone4);
		mEdtEmail = (EditText) findViewById(R.id.edt_show_email4);
		mEdtAddress = (EditText) findViewById(R.id.edt_show_address4);
		mIgvIcon = (ImageView) findViewById(R.id.img_show_photo4);
		mIgvIcon.setOnClickListener(this);
		mContactsUtil = new ContactsUtil(this);
		birthmonth = 0;
		birthday = 0;
		ishavebirth = false;
		dm = new DBManager(this);
		mEdtbirth = (EditText) findViewById(R.id.edt_show_birth);
		mEdtbirth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar c = Calendar. getInstance ();
				new DatePickerDialog(AddPeopleActivity.this, // 建对话（继承类）
						new DatePickerDialog.OnDateSetListener(){ // 继承监听器(接口)
							@Override
							public void onDateSet(DatePicker dp, int year,
												  int month, int dayOfMonth) {
								mEdtbirth.setText(year + "年" + (month + 1)
										+ "月" + dayOfMonth + "日");
								ishavebirth = true;
								birthmonth = month + 1;
								birthday = dayOfMonth;
							}
						}
						, c.get(Calendar. YEAR ) //设置初始日期
						, c.get(Calendar. MONTH )
						, c.get(Calendar. DAY_OF_MONTH )).show();
			}
		});
		// mVpgIcon = (ViewPager) findViewById(R.id.vpg_add_contact);
		// mVpgIcon = (ViewPager) findViewById(R.id.vpg_add_contact);

		for (int i = 0; i < iconId.length; i++) {

			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					iconId[i]);
			Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap, 100, 80,
					true);

			Bitmap circleBitmap = CommonUtil.createCircleImage(smallBitmap);

			ImageView igv = new ImageView(this);
			igv.setImageBitmap(circleBitmap);
			// igv.setImageResource(iconId[i]);
			image.add(igv);
			// mIcon[i] = igv;

		}

		////////////////begin
		Intent intent = getIntent();
		if(!intent.hasExtra("contactInfo")) return;
		String contactInfo = intent.getStringExtra("contactInfo");
		JSONObject jsonObject = null;
		try {
			Log.d("infooo",contactInfo);
			jsonObject = new JSONObject(contactInfo);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String invalid = "";
		String name = jsonObject.optString("name",invalid),phone = jsonObject.optString("phone",invalid),
				email = jsonObject.optString("email",invalid),address = jsonObject.optString("address",invalid);
		mEdtName.setText(name);
		mEdtPhone.setText(phone);
		mEdtEmail.setText(email);
		mEdtAddress.setText(address);
		////////////end
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_show_photo4:

				Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("请选择头像");
				View view = LayoutInflater.from(this).inflate(
						R.layout.item_viewpager, null);
				ViewPager mVpgIcon = (ViewPager) view
						.findViewById(R.id.vpg_add_viewpager);
				IconPagerAdapter adapter = new IconPagerAdapter(image, this);
				mVpgIcon.setAdapter(adapter);
				builder.setView(view);
				mVpgIcon.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						position = arg0;
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});

				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Bitmap bitmap = BitmapFactory.decodeResource(
										getResources(), iconId[position]);
								Bitmap smallBitmap = Bitmap.createScaledBitmap(
										bitmap, 100, 80, true);

								Bitmap circleBitmap = CommonUtil
										.createCircleImage(smallBitmap);
								mIgvIcon.setImageBitmap(circleBitmap);
							}
						});
				builder.show();

				break;
			case R.id.btn_cancel:
				finish();

				break;

			case R.id.btn_save:
				boolean flag = true;
				// 保存
				String ename = mEdtName.getText().toString();
				if (ename.length() == 0) {
					Toast.makeText(this, "名字不能为空", Toast.LENGTH_LONG).show();
					flag = false;
				}
				else {
					String myName = CharacterParser.getInstance().getSelling(mEdtName.getText().toString());
					String firstWord = myName.substring(0, 1).toString().toUpperCase();
					if (!firstWord.matches("[A-Z]")) {
						Toast.makeText(this, "请以字母开头", Toast.LENGTH_LONG).show();
						flag = false;
					}
				}

				String phoneNumber = mEdtPhone.getText().toString();
				for(int i = 0; i < phoneNumber.length(); i++) {
					if((phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9') && phoneNumber.charAt(i) != ' ') {
						flag = false;
						Toast.makeText(this, "号码不能含有非数字", Toast.LENGTH_LONG).show();
						break;
					}
				}
				if(flag) {
					addNewContact();

					//Intent intent = new Intent(AddPeopleActivity.this, MainActivity.class);
					Intent intent = new Intent();
					//startActivity(intent);
					/*intent.putExtra("name",mEdtName.getText().toString());
					intent.putExtra("phone",mEdtPhone.getText().toString());
					intent.putExtra("email",mEdtEmail.getText().toString());
					intent.putExtra("address",mEdtAddress.getText().toString());*/
					setResult(GlobalApplication.ADD_CONTACT_END, intent);
					finish();
				}
				break;
		}
	}

	private void addNewContact() {
		String name = mEdtName.getText().toString();
		String correctNmae = GlobalApplication.correctName(name);
		String phone = mEdtPhone.getText().toString();
		String email = mEdtEmail.getText().toString();
		String address = mEdtAddress.getText().toString();
		Drawable drawable = mIgvIcon.getDrawable();
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap bitmap = bd.getBitmap();
		mContactsUtil.insert(correctNmae, phone, email, address, bitmap);

		if(ishavebirth == true) {
			List<FestivalInfo> persons = new ArrayList<>();
			FestivalInfo p1 = new FestivalInfo(birthmonth,birthday,1,name);
			persons.add(p1);
			dm.add(persons);
		}
		//界面实时更新
		ContentResolver mContentResolver = mContactsUtil.getmContentResolver();
		Cursor contactsCursor = mContentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		for (int i = contactsCursor.getCount()-1; i < contactsCursor.getCount(); i++) {
			contactsCursor.moveToPosition(i);
			// 联系人的id
			int contactId = contactsCursor.getInt(contactsCursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor rawCuror = mContentResolver.query(
					ContactsContract.RawContacts.CONTENT_URI, null,
					ContactsContract.RawContacts.CONTACT_ID + "=?", new String[]{contactId
							+ ""}, null);

			for (int j = 0; j < rawCuror.getCount(); j++) {
				rawCuror.moveToPosition(j);
				int rawContactId = rawCuror.getInt(rawCuror
						.getColumnIndex(ContactsContract.RawContacts._ID));
				ContactInfo contact = new ContactInfo();
				contact.setContactId(contactId);
				contact.setRawContactId(rawContactId);
				// 获取联系人的头像
				mContactsUtil.getIconByContactId(contactId, contact);
				// 根据id获取联系人的名字
				mContactsUtil.getContactName(rawContactId, contact);
				// 获取手机号码
				mContactsUtil.getContactPhone(rawContactId, contact);
				// 获取邮箱
				mContactsUtil.getContactEamil(rawContactId, contact);
				// 获取地址
				mContactsUtil.getContactAddress(rawContactId, contact);
				//	Log.e(this + "", "contact=" + contact);
				// 将联系人添加到集合里
				GlobalApplication.addContacts(contact);
			}
			rawCuror.close();
		}
		contactsCursor.close();
	}

}
