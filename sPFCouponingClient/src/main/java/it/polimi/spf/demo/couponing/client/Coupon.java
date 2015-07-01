package it.polimi.spf.demo.couponing.client;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import lombok.Getter;
import lombok.Setter;

public class Coupon {


	/*
	 * ----------------------------ATTENTION-----------------------------------------------
	 * When you modify this class on Client you must update also Coupon class on Provider,
	 * because GSon (JSON library) uses Coupon.java.
	 * ------------------------------------------------------------------------------------
	 */

	public interface Contract extends BaseColumns {
		String TABLE_NAME = "coupons";
		String COLUMN_TITLE = "coupon_title";
		String COLUMN_TEXT = "coupon_text";
		String COLUMN_PHOTO = "coupon_photo";
		String COLUMN_CATEGORY = "coupon_category";
	}
	
	public static Coupon fromCursor(Cursor cursor){
		Coupon coupon = new Coupon();
		coupon.id = cursor.getLong(cursor.getColumnIndex(Contract._ID));
		coupon.title = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_TITLE));
		coupon.text = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_TEXT));
		coupon.category = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_CATEGORY));
		coupon.photo = cursor.getBlob(cursor.getColumnIndex(Contract.COLUMN_PHOTO));
		return coupon;
	}
	
	@Getter @Setter private long id;
	@Getter @Setter private String title, text, category;
	@Getter @Setter private byte[] photo;
	
	public Coupon(){
		id = -1;
	}

	public ContentValues toContentValues(){
		ContentValues cv =new ContentValues();
		cv.put(Contract.COLUMN_TITLE, title);
		cv.put(Contract.COLUMN_TEXT, text);
		cv.put(Contract.COLUMN_CATEGORY, category);
		cv.put(Contract.COLUMN_PHOTO, photo);
		return cv;
	}
	
	@Override
	public String toString() {
		return String.format(
				"Coupon {id: %s, title: %s, text: %s, category: %s, photo: %s",
				id, title, text, category, photo == null ? "no" : "yes");
	}
}
