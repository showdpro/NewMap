package in.mapbazar.mapbazar.callback;

import android.app.Dialog;

public interface ProductSizeCallbackMessage {
    void onSuccess(Dialog dialog,String sizeid,String colorcode);
    void onCancel(Dialog dialog);
}
