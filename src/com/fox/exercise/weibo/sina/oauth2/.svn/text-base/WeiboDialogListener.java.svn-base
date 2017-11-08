package com.fox.exercise.weibo.sina.oauth2;

import android.os.Bundle;

public interface WeiboDialogListener {

    /**
     * Called when a dialog completes.
     * <p/>
     * Executed by the thread that initiated the dialog.
     *
     * @param values Key-value string pairs extracted from the response.
     */
    public void onComplete(Bundle values);

    /**
     * Called when a Weibo responds to a dialog with an error.
     * <p/>
     * Executed by the thread that initiated the dialog.
     */
    public void onWeiboException(WeiboException e);

    /**
     * Called when a dialog has an error.
     * <p/>
     * Executed by the thread that initiated the dialog.
     */
    public void onError(DialogError e);

    /**
     * Called when a dialog is canceled by the user.
     * <p/>
     * Executed by the thread that initiated the dialog.
     */
    public void onCancel();

}
