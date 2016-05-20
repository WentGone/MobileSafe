package com.went_gone.mobilesafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.went_gone.mobilesafe.adapter.CommonAdapter;
import com.went_gone.mobilesafe.adapter.ViewHolder;
import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.entity.ContactsEntity;
import com.went_gone.mobilesafe.utils.ToastUtil;


public class ContactsListActivity extends BaseActivity {
	private static final String TAG = "Contacts";
	private ListView mLV_Contacts;
	private List<ContactsEntity> mContactList;
	private ContactAdapter contactAdapter;
	private Intent intent;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			contactAdapter = new ContactAdapter(ContactsListActivity.this,mContactList, R.layout.item_listview_contact);
			mLV_Contacts.setAdapter(contactAdapter);
		};
	};
	
	@Override
	protected void initViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_contacts);
		mLV_Contacts = (ListView) findViewById(R.id.Contact_ListView);
		mLV_Contacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ContactsEntity entity = mContactList.get(position);
				String phoneNumber = entity.getPhoneNumber().get("data1");
				intent.putExtra("phoneNumber", phoneNumber);
				setResult(RESULT_OK, intent);
				ContactsListActivity.this.finish();
			}
		});
	}

	@Override
	protected void initVariables() {
		intent = getIntent();
		mContactList = new ArrayList<ContactsEntity>();
		
		//读取联系人很有可能是一个耗时操作，将其放在一个子线程中处理
		new Thread(){
			public void run() {
				//获取内容解析器的对象
				ContentResolver contentResolver = getContentResolver();
//				ithema(contentResolver);
				initContacts(contentResolver);
				//发送一个空的消息，告知主线程可以使用子线程初始化后的数据
				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}
	/**
	 * 通过查询系统的联系人的表获得姓名+电话号码   初始化ContactList
	 * @param contentResolver
	 */
	private void initContacts(ContentResolver contentResolver) {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		Cursor cursor = contentResolver.query(uri,new String[]{
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER},
				null, null,null);
		//查找联系人的姓名+电话
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String names[] = cursor.getColumnNames();
			ContactsEntity entity = new ContactsEntity();
			for (String name : names) {
				String value = cursor.getString(cursor.getColumnIndex(name));
				HashMap<String,String> hashMap = new HashMap<String, String>();
				hashMap.put(name, value);
				if (name.equals("display_name")) {
					entity.setName(hashMap);
				}else {
					entity.setPhoneNumber(hashMap);
				}
			}
			mContactList.add(entity);
			cursor.moveToNext();
		}
	}
	private void ithema(ContentResolver contentResolver) {
		//读取联系人权限
		//查询方法   uri，要查询的字段,查询条件"a=?",查询条件的值,排序
		Cursor cursor = contentResolver.query(
				Uri.parse("content://com.android.contacts/raw_contacts"),
				new String[]{"contact_id"},
				null, null,null);
		//循环游标，直到没有数据位置
		while(cursor.moveToNext()){
			String id = cursor.getString(0);
			//根据用户唯一性id值，查询data表和mimetypes表生成的视图，获取data以及mimetype字段
			Cursor indexCursor = contentResolver.query(
					Uri.parse("content://com.android.contacts/data"), 
					new String[]{"data1","mimetype"},"raw_contact_id=?",new String[]{id}, null);
			//循环每一个联系人的电话号码以及姓名，数据类型
			while(indexCursor.moveToNext()){
				Log.e(TAG, indexCursor.getString(0));
//				Log.e(TAG, indexCursor.getString(1));
//				indexCursor.getString(1);
			}
			indexCursor.close();
		}
		cursor.close();
	}
	
	/**
	 * 联系人的适配器
	 * @author Went_Gone
	 *
	 */
	class ContactAdapter extends CommonAdapter<ContactsEntity>{

		public ContactAdapter(Context context, List<ContactsEntity> mDatas,
				int layoutId) {
			super(context, mDatas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, ContactsEntity t) {
			holder.setTextViewText(R.id.Item_LV_Contact_Name,t.getName().get("display_name"))
					.setTextViewText(R.id.Item_LV_Contact_phoneNumber, t.getPhoneNumber().get("data1"));
		}
		
	}
}
