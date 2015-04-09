package com.appsol.apps.projectcommunicate.model;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract;

public class Contacts {
	private String name;
	private String phone;
	private String email;
	private String image;
	private boolean isRegistered;
	
	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}
	
	public String getImage() {
		return image;
	}
	public boolean isRegistered() {
		return isRegistered;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
	public String getPhone() {
		return phone;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Contacts() {
		// TODO Auto-generated constructor stub
	}

	public static void addContact(Context context,Contacts c)
	{
		ContentValues values= new ContentValues();
		values.put(ContactsContract.Data.RAW_CONTACT_ID, 199);
		values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
		values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, c.getName());
		values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, c.getPhone());
		ArrayList<ContentProviderOperation> ops= new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build()
				);
		ops.add( ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValueBackReferences(values).build());
				
		
		try
		{
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
