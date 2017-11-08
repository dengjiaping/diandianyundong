package cn.ingenic.indroidsync.contactsms.manager;

import cn.ingenic.indroidsync.Column;

public enum ManagerColumns implements Column{

	tag(String.class),address(String.class),contactsize(Integer.class),smssize(Integer.class);
	
	private final Class<?> mClass;
	ManagerColumns(Class<?> c) {
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
