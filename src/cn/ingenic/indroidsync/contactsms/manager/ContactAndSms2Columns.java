package cn.ingenic.indroidsync.contactsms.manager;

public class ContactAndSms2Columns {
	
    public static final String SYNC_CONNECTIVITY_TRUE_ACTION="sync_connectivity_true_action";
	
	public static final String SYNC_CONNECTIVITY_FALSE_ACTION="sync_connectivity_false_action";
	
	public static final String MODE_CHANGED_ACTION="mode_changed_action";
	
	public static final String SAME_PHONE_ACTION="same_phone_action";
	
	public static final String DIFF_PHONE_ACTION="delete_phone_action";
	
	public static final String MANAGER_SEND_ADDRESS="manager_send_address";
	
	public static final String DIFF_PHONE_SEND_ADDRESS="diff_phone_send_address";
	
	public static final String insert_watch_tag="insert_watch_tag";
	
	public static final String delete_watch_tag="delete_watch_tag";
		
	public static final String update_watch_tag="update_watch_tag";
	
	public static final int FOR_RESPONSE = 5;
	
	public static final String INIT_MESSAGE="init_message";
	
	public class ContactColumn{
		public static final String WATCH_TAG_INSERT_ALL="to_watch_insert_all";
		
		public static final String SEND_WATCH_NEED_DATAS="send_watch_need_contacts";
	    
		public static final String WATCH_TAG_INSERT="to_watch_insert";
		public static final String WATCH_TAG_DELETE="to_watch_delete";
		public static final String WATCH_TAG_UPDATE="to_watch_update";
		
		public static final String SEND_ADDRESS="send_address";
		
	    public static final String PHONE_TAG="to_Phone";
	    
	    public static final String SEND_WATCHKEY_MESSAGE="send_watch_key_message";
	    
	    public static final int FOR_SEND_WATCHKEY_MESSAGE=11;
	    
	    public static final String SEND_PHONEKEY_ONLY="send_phone_key_only_message";
	    
	    public static final String SYNC_CONTACTS_MESSAGE="sync_contacts_message";
	    
	    public static final String CONTACT_SYNC_ACTION="for_contacts_sync_action";
		
		public static final String PHONE_DELETE="to_phone_delete";
		
		public static final String PHONE_HAVE_INSERT_WATCH_LOOKUPKEY="phone_have_insert_watch_lookupkey";
		
		public static final String SAME_PHONE="same_phone";
		
		public static final String SAME_PHONE_DATAS="same_phone_datas";
		
		public static final String DIFF_PHONE="delete_phone";
		
		public static final String SAVE_POWER_MESSAGE="save_power_message";
		
		public static final String START_INSERT_ALL_MESSAGE="start_insert_all_message";
		
		public static final String STOP_INSERT_ALL_MESSAGE="stop_insert_all_message";
		
		public static final String START_INSERT_CHANGE_MESSAGE="start_insert_change_message";
		
		public static final String STOP_INSERT_CHANGE_MESSAGE="stop_insert_change_message";
		
		public static final String CONTACT_SYNC_NO_DATAS_CHANGED_MESSAGE="contact_sync_no_datas_changed_message";
		
		public static final String CONTACT_WANT_DATAS="contact_want_datas";
		
		public static final String PHONE_HAVE_SEND_CONTACTS="phone_have_send_contacts";
		
		public static final String SAVE_POWER_TO_RIGHT_AND_DATAS_CHANGED="save_power_to_right_now_datas_changed";
		
		public static final String SAVE_POWER_TO_RIGHT_WANT_NEW_DATAS_ACTION="save_power_to_right_now_want_new_datas_action";
			
		public static final String CONTACT_DELAY_TIME_MESSAGE="contact_delay_time_message";
			
		public static final int FOR_WATCH_INSERT = 0;
		
		public static final int FOR_WATCH_UPDATE = 1;
		
		public static final int FOR_WATCH_DELETE = 2;
		
		public static final int FOR_PHONE_DELETE = 3;
		
		public static final int FOR_PHONE_TAG = 4;
		
		public static final int FOR_RESPONSE = 5;
		public static final int FOR_WATCH_INSERT_ALL = 6;
		public static final int FOR_SEND_ADDRESS=7;
		
		public static final int FOR_SAME_PHONE_DATAS=8;
		
		public static final int For_SAME_PHONE=9;
		
		public static final int FOR_SAVE_POWER_MESSAGE=13;
		public static final int FOR_CONTACT_WANT_DATAS=14;
		public static final int FOR_PHONE_HAVE_SEND_CONTACTS=15;
		public static final int FOR_SAVE_POWER_TO_RIGHT_AND_DATAS_CHANGED=16;
		public static final int FOR_SYNC_CONTACTS_MESSAGE=17;
		
		public static final String ToContactReceiverAction="com.android.contact.TO_CONTACT_RECEIVER";
		
		
		public static final String WATCH_SEND_INIT_CONTACT_MESSAGE="watch_send_init_contact_message";
		
		
		public static final String SAVE_POWER_MESSAGE_ACTION="save_power_message_action";
		
		public static final String CONTACT_WANT_UPDATE_DATAS_ACTION="contact_want_update_datas_action";
		
		public static final String UPDATE_ACTION="update_action";
		
		public static final String PHONE_HAVE_SEND_CONTACTS_ACTION="phone_have_send_contacts_action";
		
		public static final String CATCH_ALL_COTNACTS_DATAS_ACTION="catch_all_contacts_action";
		
		public static final String SAVE_POWER_TO_RIGHT_AND_DATAS_CHANGED_ACTION="save_power_to_right_now_datas_changed_action";
	}
	
	public class SmsColumn{
		
		 public static final String SEND_NEW_SMS="send_new_sms";
		 
		 public static final String SEND_ADDRESS_SMS2="sms2_send_address_tag";
		 
		 public static final String CATCH_ALL_SMS_ACTION="catch_all_sms_action";
			
		 public static final int FOR_SEND_ADDRESS_SMS2=102;
		 
		 public static final String SMS_SAME_PHONE_TAG="sms_same_PHONE_tag";
			
		 public static final String SMS_DIFF_PHONE_TAG="sms_difff_phone_tag";
		 
		 public static final String SMS_INSERT_TAG="sms_insert_tag";
		 
		 public static final String SMS_SEND_THREAD_DATAS="sms_send_thread_datas";
		 
		 public static final int FOR_SMS_INSERT_TAG=103;
		 
		 public static final String SMS_UPDATE_TAG="sms_update_tag";
		 
		 public static final int FOR_SMS_UPDATE_TAG=104;
		 
		 public static final String SMS_DELETE_TAG="sms_delete_tag";
		 
		 public static final int FOR_SMS_DELETE_TAG=105;
		 
		 public static final String SMS_INSERT_ALL_TAG="sms_insert_all_tag";
		 
		 public static final String SMS_CHECK_MESSAGE="sms_check_message";
		 
		 public static final String SMS_WANT_SYNC_DATAS_MESSAGE="sms_want_sync_datas_message";
		 
		 public static final int FOR_SMS_WANT_SYNC_DATAS_MESSAGE=112;
		
		 
		 public static final String SMS_SYNC_NO_DATAS_CHANGED_MESSAGE="sms_sync_no_datas_changed_message";
		 
		 public static final String SMS_SYNC_ACTION="sms_sync_action";
		 
		 public static final int FOR_SMS_INSERT_ALL_TAG=106;
		 
		 public static final String RESPONSE_PHONE="response_phone"; 
		 
		 public static final int FOR_RESPONSE_PHONE=107;
		 
		 public static final String WATCH_DELETE_SMS_TAG="watch_delete_sms_action";
		 
		 public static final int FOR_WATCH_DELETE_SMS_TAG=108;
		 
		 public static final String SMSAPP_WANT_GET_NEW_DATAS_TAG="sms_want_new_datas";
		 
		 public static final int FOR_SMSAPP_WANT_GET_NEW_DATAS_TAG=109;
		 
		 public static final String SEND_READ="send_read";
		 
		 public static final int FOR_SEND_READ=110;
		 
		 public static final String UPDATE_RESPONSE_TAG="update_response_tag";
		 
		 public static final int FOR_UPDATE_RESPONSE_TAG=111;
		 
		 public static final String START_INSERT_ALL_SMS_MESSAGE="start_insert_all_sms_message";
		 
		 public static final String STOP_INSERT_ALL_SMS_MESSAGE="stop_insert_all_sms_message";
		 
		 public static final String START_GET_SMS_CHANGE_MESSAGE="start_get_sms_change_message";
		 
		 public static final String STOP_GET_SMS_CHANGE_MESSAGE="stop_get_sms_change_message";
		
		 public static final String WATCH_SEND_INIT_MESSAGE="watch_send_init_message";
		
		public static final String SMS_SAVE_POWER_CHANGED_MESSAGE="sms_save_power_changed_message";
		
		public static final String SAVE_POWER_WATCH_WANT_CHANGED_DATAS="sms_save_power_watch_want_change_datas";
		
		public static final String SMS_PHONE_HAVE_SEND_CHANGED_DATAS="sms_phone_have_send_changed_datas";
		
		public static final int FOR_SMS_PHONE_HAVE_SEND_CHANGED_DATAS=113;
		
		public static final int FOR_SMS_SAVE_POWER_MESSAGE= 100;
		
		public static final String SMS_DELAY_TIME_MESSAGE="sms_delay_time_message";
		 
		//action	
		 public static final String SMS_SAME_PHONE_ACTION="sms_same_phone_action";
			
		 public static final String SMS_DIFF_PHONE_ACTION="sms_diff_phone_action";
		 
		 public static final String SMS_SAVE_POWER_CHANGED_MESSAGE_ACTION="sms_save_power_changed_message_action";
			
		 public static final String SMS_WANT_GET_NEW_DATAS_ACTION="sms_want_get_new_datas_action";
			
		 public static final String SMS_WANT_DATAS_ACTION="sms_want_datas_action";
			
		 public static final String SMS_PHONE_HAVE_SEND_CHANGED_DATAS_ACTION="sms_phone_have_send_changed_datas_action";
			
		 public static final String SMS_SAVE_POWER_TO_RIGHT_AND_DATAS_CHANGED_ACTION="sms_save_power_to_right_now_datas_changed_action";
			
		 public static final String SMS_SHOW_PROCESSBAR_ACTION="sms_show_processbar_action";
		 
		 public static final String SMS_SHOW_UPDATE_THREAD_ACTION="sms_show_update_thread_action";
		 
		 public static final String SMS_HAVE_DELETE_ACTION="sms_have_delete_action";
		 
		 public static final String WATCH_RESPONSE_SMS_ACTION="watch_response_sms_action";
		 
		 public static final String SMS_SEND_READ_ACTION="sms_send_read_action";
	}

}
