package com.eiga.an.service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.allenliu.versionchecklib.core.AVersionService;

public class LoadService extends AVersionService {
    public LoadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onResponses(AVersionService service, String response) {
        Log.e("LoadService", response);
        //可以在判断版本之后在设置是否强制更新或者VersionParams
        //eg

        showVersionDialog(versionParams.getDownloadUrl(), versionParams.getTitle(), versionParams.getUpdateMsg());
//        or
//        showVersionDialog("http://www.apk3.com/uploads/soft/guiguangbao/UCllq.apk", "检测到新版本", getString(R.string.updatecontent),bundle);

    }
}
