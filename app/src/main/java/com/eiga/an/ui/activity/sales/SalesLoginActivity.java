package com.eiga.an.ui.activity.sales;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eiga.an.R;
import com.eiga.an.base.BaseActivity;
import com.eiga.an.model.Constant;
import com.eiga.an.model.jsonModel.ApiGetBankInfoModel;
import com.eiga.an.model.salesModel.ApiSalesLoginModel;
import com.eiga.an.ui.activity.user.PhoneVerifyActivity;
import com.eiga.an.ui.activity.user.UserLoginActivity;
import com.eiga.an.utils.ActivityManager;
import com.eiga.an.utils.PhoneUtils;
import com.eiga.an.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/4/10.
 */

public class SalesLoginActivity extends BaseActivity {
    @BindView(R.id.sales_login_name)
    EditText salesLoginName;
    @BindView(R.id.sales_login_psw)
    EditText salesLoginPsw;
    @BindView(R.id.sales_login_layout)
    LinearLayout salesLayout;
    @BindView(R.id.sales_login_forgot)
    TextView salesForgot;
    @BindView(R.id.sales_login_go)
    TextView salesGo;
    private String TAG = getClass().getName();

    private String salesPhone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_login);
        ButterKnife.bind(this);
        salesPhone= (String) SharedPreferencesUtils.getShared(SalesLoginActivity.this,Constant.Sales_Login_Phone,"");

        findViews();
    }

    private void findViews() {
        salesLoginName.setText(salesPhone);
        salesLoginName.setSelection(salesPhone.length());
        addLayoutListener(salesLayout,salesForgot);
        //controlKeyboardLayout(salesLayout,salesForgot);
    }

    @OnClick({R.id.sales_login_finish, R.id.sales_login_go, R.id.sales_login_forgot})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.sales_login_finish:
                finish();
                break;
            case R.id.sales_login_go:

                if (PhoneUtils.isMobile(salesLoginName.getText().toString())&& !TextUtils.isEmpty(salesLoginPsw.getText().toString())) {

                    //在这里请求接口
                    httpRequestLogin();

                }else {
                    PhoneUtils.toast(SalesLoginActivity.this,"信息有误,请检查");
                }


                break;
            case R.id.sales_login_forgot:
                break;
        }
    }

    private void httpRequestLogin() {

        StringRequest mStringRequest = new StringRequest(Constant.Url_Sales_Login, RequestMethod.POST);
        mStringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);//设置缓存模式
        mStringRequest.add("CellPhone",salesLoginName.getText().toString());
        mStringRequest.add("Password",salesLoginPsw.getText().toString());
        StringRequest(101, mStringRequest, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                dismissLoading();
                if (what == 101) {
                    PhoneUtils.showLargeLog(response.get(),3900,TAG);
                    ApiSalesLoginModel model=null;
                    try {
                        model=new Gson().fromJson(response.get(),ApiSalesLoginModel.class);
                        if (model.Status==1){
                            //setHttpData(model.BankCard);

                            //SharedPreferencesUtils.putShared(SalesLoginActivity.this, Constant.IsFirstOpenApp,"1");//1 - sales
                            //保留DActivity,其余全部关闭
                            ActivityManager.getInstance().finishAllActivityByWhitelist(SalesLoginActivity.class);
                            SharedPreferencesUtils.putShared(SalesLoginActivity.this, Constant.Sales_Login_Phone, salesLoginName.getText().toString());
                            SharedPreferencesUtils.putShared(SalesLoginActivity.this, Constant.Sales_Login_Token, model.Token);
                            Intent intent=new Intent(SalesLoginActivity.this,SalesMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }catch (Exception e){
                        Log.e(TAG,"Exception="+e);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                dismissLoading();
                Log.e(TAG, "onFailed==" + response.get());
                PhoneUtils.toast(SalesLoginActivity.this,"网络请求失败,请检查网络后重试");
            }
        });
    }


    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //1、获取main在窗体的可视区域
                main.getWindowVisibleDisplayFrame(rect);
                //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                int screenHeight = main.getRootView().getHeight();//屏幕高度
                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
                if (mainInvisibleHeight > screenHeight / 5) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    // 4､获取Scroll的窗体坐标，算出main需要滚动的高度
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    //5､让界面整体上移键盘的高度
                    Log.e(TAG,"srollHeight="+srollHeight);
                    main.scrollTo(0, srollHeight>0?srollHeight:screenHeight / 5);
                } else {
                    //3、不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
                    main.scrollTo(0, 0);
                }
            }
        });
    }


    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 200) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }
}
