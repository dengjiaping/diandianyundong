package cn.ingenic.indroidsync.contactsms.contacts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Data;
import android.util.Log;

import com.android.vcard.VCardEntryCommitter;
import com.android.vcard.VCardEntryConstructor;
import com.android.vcard.VCardEntryCounter;
import com.android.vcard.VCardInterpreter;
import com.android.vcard.VCardParser;
import com.android.vcard.VCardParser_V21;
import com.android.vcard.VCardParser_V30;
import com.android.vcard.VCardSourceDetector;
import com.android.vcard.exception.VCardException;
import com.android.vcard.exception.VCardNestedException;
import com.android.vcard.exception.VCardNotSupportedException;
import com.android.vcard.exception.VCardVersionException;

public class ParseOneContact {
	
	private String LOG_TAG="ParseOneContact";
	
	private String oneVCard;
	
	private Context mContext;
	
	private final static int VCARD_VERSION_V21 = 1;
    private final static int VCARD_VERSION_V30 = 2;
    private VCardParser mVCardParser;
	
	public ParseOneContact(Context context,String oneContactVCard){
		this.oneVCard=oneContactVCard;
		this.mContext=context;
	}
	
	public String start(){
		try {
			ImportRequest importRequest=constructImportRequest(oneVCard.getBytes());
			
			final VCardEntryConstructor constructor =
	            new VCardEntryConstructor(importRequest.estimatedVCardType, null, importRequest.estimatedCharset);
	    final VCardEntryCommitter committer = new VCardEntryCommitter(mContext.getContentResolver());
	    
	    constructor.addEntryHandler(committer);
	    boolean successful=readOneVCard(new ByteArrayInputStream(oneVCard.getBytes()),importRequest.estimatedVCardType,
	    		importRequest.estimatedCharset,constructor,importRequest.vcardVersion);
	   
	    if(successful){
	    	ArrayList<Uri> contactUriList=committer.getCreatedUris();
	    	final long rawContactId = ContentUris.parseId(contactUriList.get(0));
	    	String lookupKey=getLookupKey(mContext.getContentResolver(),ContentUris.withAppendedId(
                    RawContacts.CONTENT_URI, rawContactId));
	    	return lookupKey;
            
	    }else{
	    	Log.e(LOG_TAG,"read one contact failed.");
	    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String getLookupKey(ContentResolver resolver, Uri rawContactUri){
		 final Uri dataUri = Uri.withAppendedPath(rawContactUri, Data.CONTENT_DIRECTORY);
         final Cursor cursor = resolver.query(dataUri, new String[] {Contacts.LOOKUP_KEY}, null, null, null);

         String LookKey = null;
         try {
             if (cursor != null && cursor.moveToFirst()) {
                 final String lookupKey = cursor.getString(0);
                 if(lookupKey!=null){
                	 return lookupKey;
                 }
             }
         } finally {
             if (cursor != null) cursor.close();
         }
        return LookKey;
	}
	
	private ImportRequest constructImportRequest(final byte[] data)throws IOException, VCardException {
		VCardEntryCounter counter = null;
		VCardSourceDetector detector = null;
		int vcardVersion = VCARD_VERSION_V21;
		try {
		    boolean shouldUseV30 = false;
		    InputStream is;
		    if (data != null) {
		        is = new ByteArrayInputStream(data);
		    } else {
		        return null;
		    }
		    mVCardParser = new VCardParser_V21();
		    try {
		        counter = new VCardEntryCounter();
		        detector = new VCardSourceDetector();
		        mVCardParser.addInterpreter(counter);
		        mVCardParser.addInterpreter(detector);
		        mVCardParser.parse(is);
		    } catch (VCardVersionException e1) {
		        try {
		            is.close();
		        } catch (IOException e) {
		        }

		        shouldUseV30 = true;
		        if (data != null) {
		            is = new ByteArrayInputStream(data);
		        } else {
		        	return null;
		        }
		        mVCardParser = new VCardParser_V30();
		        try {
		            counter = new VCardEntryCounter();
		            detector = new VCardSourceDetector();
		            mVCardParser.addInterpreter(counter);
		            mVCardParser.addInterpreter(detector);
		            mVCardParser.parse(is);
		        } catch (VCardVersionException e2) {
		            throw new VCardException("vCard with unspported version.");
		        }
		    } finally {
		        if (is != null) {
		            try {
		                is.close();
		            } catch (IOException e) {
		            }
		        }
		    }

		    vcardVersion = shouldUseV30 ? VCARD_VERSION_V30 : VCARD_VERSION_V21;
		} catch (VCardNestedException e) {
		    Log.w(LOG_TAG, "Nested Exception is found (it may be false-positive).");
		    // Go through without throwing the Exception, as we may be able to detect the
		    // version before it
		}
		return new ImportRequest(data,
                detector.getEstimatedType(),
                detector.getEstimatedCharset(),
                vcardVersion, counter.getCount());
		
	}
	private boolean readOneVCard(InputStream is, int vcardType, String charset,
            final VCardInterpreter interpreter,
            final int version) {
        boolean successful = false;
            try {
               

                // We need synchronized block here,
                // since we need to handle mCanceled and mVCardParser at once.
                // In the worst case, a user may call cancel() just before creating
                // mVCardParser.
                synchronized (this) {
                	mVCardParser = (version == VCARD_VERSION_V30 ?new VCardParser_V30(vcardType) :
                        new VCardParser_V21(vcardType));
                
                }
                mVCardParser.parse(is, interpreter);

                successful = true;
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException was emitted: " + e.getMessage());
            } catch (VCardNestedException e) {
                // This exception should not be thrown here. We should instead handle it
                // in the preprocessing session in ImportVCardActivity, as we don't try
                // to detect the type of given vCard here.
                //
                // TODO: Handle this case appropriately, which should mean we have to have
                // code trying to auto-detect the type of given vCard twice (both in
                // ImportVCardActivity and ImportVCardService).
                Log.e(LOG_TAG, "Nested Exception is found.");
            } catch (VCardNotSupportedException e) {
                Log.e(LOG_TAG, e.toString());
            } catch (VCardVersionException e) {
                    Log.e(LOG_TAG, "Appropriate version for this vCard is not found.");
                
            } catch (VCardException e) {
                Log.e(LOG_TAG, e.toString());
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            }
       

        return successful;
    }

   private class ImportRequest{
	   
	   public final byte[] data;
	   
	   public final int  estimatedVCardType;
	   
	   public final String estimatedCharset;
	   
	   public final int vcardVersion;
	   
	   public final int entryCount;
	   
	   public ImportRequest(byte[] data, int estimatedType, String estimatedCharset,
	           int vcardVersion, int entryCount) {
	       this.data = data;
	       this.estimatedVCardType = estimatedType;
	       this.estimatedCharset = estimatedCharset;
	       this.vcardVersion = vcardVersion;
	       this.entryCount = entryCount;
	   }
   }
  

	
	

}
