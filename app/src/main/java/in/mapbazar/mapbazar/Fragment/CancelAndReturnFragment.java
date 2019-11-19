package in.mapbazar.mapbazar.Fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.JsonObject;
import in.mapbazar.mapbazar.R;
import in.mapbazar.mapbazar.Utili.Common;
import in.mapbazar.mapbazar.connection.API;
import in.mapbazar.mapbazar.connection.RestAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelAndReturnFragment extends Fragment {

    private WebView webView;

    Dialog ProgressDialog;
    //Shared Preferences
    private SharedPreferences sPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);

        sPref = PreferenceManager.getDefaultSharedPreferences(Common.Activity);

        ProgressDialog = new Dialog(Common.Activity, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.progressbar);
        ProgressDialog.setCancelable(false);


        webView = (WebView) view.findViewById(R.id.webview);
        if (Common.isNetworkAvailable(getActivity())) {
            getCancelReturn();
        }
        return view;
    }

    private void getCancelReturn() {

        ProgressDialog.show();

        API api = RestAdapter.createAPI();
        Call<JsonObject> callbackCall = api.cancelAndReturn(sPref.getString("uid", ""));
        callbackCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (ProgressDialog != null && ProgressDialog.isShowing())
                    ProgressDialog.dismiss();
                JsonObject resp = response.body();

                if (resp != null) {
                    if (resp.has("cancel_and_return")) {
                        String cancel_and_return = resp.get("cancel_and_return").toString();
                        startWebView(cancel_and_return);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (ProgressDialog != null && ProgressDialog.isShowing())
                    ProgressDialog.dismiss();
                Log.e("onFailure", t.getMessage());

            }
        });


    }

    private void startWebView(final String data) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        webView.setWebViewClient(new WebViewClient() {

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadData(data, "text/html; charset=utf-8", "UTF-8");
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {

            }

            public void onPageFinished(WebView view, String url) {
                try {

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        //Load url in webview
        //webView.loadDataWithBaseURL("",data, "text/html; charset=utf-8", "UTF-8","");
        webView.loadData(data, "text/html", null);

    }


}
