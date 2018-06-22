package com.eiga.an.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.eiga.an.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

// appid = wxac781390a3adb385

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private String TAG=getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_call_back);
        Log.e(TAG,"onCreate");
        if(WXPay.getInstance() != null) {
            Log.e(TAG,"WXPay.getInstance() != nul");
            WXPay.getInstance().getWXApi().handleIntent(getIntent(), this);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.e(TAG,"onNewIntent");
        if(WXPay.getInstance() != null) {
            WXPay.getInstance().getWXApi().handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.e(TAG,"onResp");
            if(WXPay.getInstance() != null) {
                if(baseResp.errStr != null) {
                    Log.e("wxpay", "errstr=" + baseResp.errStr);
                }
                Log.e(TAG,"baseResp.errCode="+baseResp.errCode);
                WXPay.getInstance().onResp(baseResp.errCode);
                finish();
            }
        }
    }
}
