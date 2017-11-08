package com.example.xinlv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.fox.exercise.newversion.entity.SleepEffect;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SleepEffectService {
	static Context context;
	static List<SleepEffect> list01;
	public SleepEffectService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	/**
	 * 保存到本地数据库
	 * @param SleepEffect
	 */

	public void saveObject(SleepEffect sleepEffect) {
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
			objectOutputStream.writeObject(sleepEffect);
			objectOutputStream.flush();
			byte data[] = arrayOutputStream.toByteArray();
			objectOutputStream.close();
			arrayOutputStream.close();
			Dbhelper dbhelper = Dbhelper.getInstens(context);
			SQLiteDatabase database = dbhelper.getWritableDatabase();
			database.execSQL("insert into classtable (classtabledata) values(?)", new Object[] { data });
			database.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 从本地数据库中获取SleepEffect对象
	 * @param SleepEffect
	 */
	public static List<SleepEffect> getObject() {
		SleepEffect sleepEffect = null;
		list01=new ArrayList<SleepEffect>();
		Dbhelper dbhelper = Dbhelper.getInstens(context);
		SQLiteDatabase database = dbhelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from classtable", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				byte data[] = cursor.getBlob(cursor.getColumnIndex("classtabledata"));
				ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
				try {
					ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
					sleepEffect = (SleepEffect) inputStream.readObject();
					list01.add(sleepEffect);
					inputStream.close();
					arrayInputStream.close();
					break;//这里为了测试就取一个数据
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list01;
	}
}
