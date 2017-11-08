package cn.ingenic.indroidsync.contactsms.contacts;

import cn.ingenic.indroidsync.Column;

public enum ContactColumn implements Column {

    phonekey(String.class), watchkey(String.class),onevcard(String.class),tag(String.class)
    ,address(String.class),delete(Integer.class);
	
	private final Class<?> mClass;
	ContactColumn(Class<?> c) {
		mClass = c;
	}
	
	@Override
	public Class<?> type() {
		return mClass;
	}

	@Override
	public String key() {
		return name();
	}

}
