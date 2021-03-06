package com.eiga.an.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {
	private static final String TAG = "Utils";

	/**
	 * 获取整型的ip地址(0表示设备未联网)
	 *
	 * @param context 上下文
	 * @return 整型的ip地址
	 */
	public static int getIpAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return wifiInfo.getIpAddress();
	}

	/**
	 * 获取设置udid
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		String id;
		//android.telephony.TelephonyManager
		TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (mTelephony.getDeviceId() != null) {
			id = mTelephony.getDeviceId();
		} else {
			//android.provider.Settings;
			id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		}
		return id;
	}



	/**
	 * 将整型的ip地址转换为点分十进制格式("192.168.21.30")
	 *
	 * @param ip int型的ip地址
	 * @return 点分十进制格式的ip
	 */
	public static String getDecimalIpAddress(int ip) {
		// 分别取出每个字节的值
		return ((ip) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
				+ (ip >> 24 & 0xFF);
	}

	/**
	 * 获取ip的网络号。如:"192.168.21.30",若子网掩码为"255.255.255.0",则前三位"192.168.21"为ip的网络号,
	 * 最后一位"30"为ip的主机号
	 *
	 * @param ip int型的ip地址
	 * @return ip的网络号
	 */
	public static String getIpNetAddress(int ip) {
		return ((ip) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF);
	}


	public static int char2int(CharSequence charSequence){
		return Integer.valueOf(charSequence.toString());
	}

	/**
	 * 获取ip的主机号。如:"192.168.21.30",若子网掩码为"255.255.255.0",则前三位"192.168.21"为ip的网络号,
	 * 最后一位"30"为ip的主机号
	 *
	 * @param ip int型的ip地址
	 * @return ip的主机号
	 */
	public static int getIpHostAddress(int ip) {
		// 右移24位后获取到的一个字节
		return (ip >> 24) & 0xFF;
	}





	public static boolean checkBankCard(String bankCard) {
		if(bankCard.length() < 15 || bankCard.length() > 19) {
			return false;
		}
		char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
		if(bit == 'N'){
			return false;
		}
		return bankCard.charAt(bankCard.length() - 1) == bit;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * @param nonCheckCodeBankCard
	 * @return
	 */
	public static char getBankCardCheckCode(String nonCheckCodeBankCard){
		if(nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
				|| !nonCheckCodeBankCard.matches("\\d+")) {
			//如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeBankCard.trim().toCharArray();
		int luhmSum = 0;
		for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if(j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
	}



	/**
	 * 将图片转换成Base64编码的字符串
	 * @param path
	 * @return base64编码的字符串
	 */
	public static String imageToBase64(String path){
		if(TextUtils.isEmpty(path)){
			return null;
		}
		InputStream is = null;
		byte[] data = null;
		String result = null;
		try{
			is = new FileInputStream(path);
			//创建一个字符流大小的数组。
			data = new byte[is.available()];
			//写入数组
			is.read(data);
			//用默认的编码格式进行编码
			result = Base64.encodeToString(data,Base64.DEFAULT);
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			if(null !=is){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}



	public static String Bitmap2StrByBase64(Bitmap bit){
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
		byte[] bytes=bos.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	/**
	 *base64编码字符集转化成图片文件。
	 * @param base64Str
	 * @param path 文件存储路径
	 * @return 是否成功
	 */
	public static boolean base64ToFile(String base64Str,String path){
		byte[] data = Base64.decode(base64Str,Base64.DEFAULT);
		for (int i = 0; i < data.length; i++) {
			if(data[i] < 0){
				//调整异常数据
				data[i] += 256;
			}
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			os.write(data);
			os.flush();
			os.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}catch (IOException e){
			e.printStackTrace();
			return false;
		}

	}



	public static void e(String tag,String msg)
	{
		Log.e(tag, msg);
	}
	/**
	 * 分段打印出较长log文本
	 * @param logContent  打印文本
	 * @param showLength  规定每段显示的长度（AndroidStudio控制台打印log的最大信息量大小为4k）
	 * @param tag         打印log的标记
	 */
	public static void showLargeLog(String logContent, int showLength, String tag){
		if(logContent.length() > showLength){
			String show = logContent.substring(0, showLength);
			e(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
			if((logContent.length() - showLength) > showLength){
				String partLog = logContent.substring(showLength,logContent.length());
				showLargeLog(partLog, showLength, tag);
			}else{
				String printLog = logContent.substring(showLength, logContent.length());
				e(tag, printLog);
			}

		}else{
			e(tag, logContent);
		}
	}


	/**
	 * @throws
	 * @MethodName:closeInputMethod
	 * @Description:关闭系统软键盘
	 */

	static public void closeInputMethod(Context context, View view) {

		try {
			//获取输入法的服务
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			//判断是否在激活状态
			if (imm.isActive()) {
				//隐藏输入法!!,
				imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}

		} catch (Exception e) {
		} finally {
		}

	}

	/**
	 * @throws
	 * @MethodName:openInputMethod
	 * @Description:打开系统软键盘
	 */

	static public void openInputMethod(final EditText editText) {

		Timer timer = new Timer();

		timer.schedule(new TimerTask() {

			public void run() {

				InputMethodManager inputManager = (InputMethodManager) editText

						.getContext().getSystemService(

								Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(editText, 0);

			}

		}, 200);

	}


	/**
	 * 验证IP是否合法
	 *
	 * @param ipAddress ip地址
	 * @return ip合法返回true，否则返回false
	 */
	public static boolean verifyIP(String ipAddress) {
		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		if (ipAddress.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件或目录
	 *
	 * @param file 文件或目录
	 * @return 是否删除成功
	 */
	public static boolean deleteFile(File file) {
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			for (File childFile : childFiles) {
				deleteFile(childFile);
			}
		}
		return file.delete();
	}

	/**
	 * 获取应用版本号
	 *
	 * @param context 上下文
	 * @return 应用版本号
	 */
	public static String getVersionCode(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = null;
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		if (context==null){
			return "";
		}
		try {
			packageManager=context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packInfo.versionName;
			//return packInfo.versionName;
		} catch (NameNotFoundException e) {
		}
		return "";
	}

	/**
	 * 获取设备的MAC地址
	 *
	 * @param context 上下文
	 * @return 设备的MAC地址
	 */
	public static String getMacAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo != null) {
			return wifiInfo.getMacAddress();
		}
		return null;
	}

	/*
	 * 设置对话框大小
	 */
	public static void setDialogSize(Activity activity, Dialog dialog) {
		WindowManager m = activity.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
		p.height = (int) d.getHeight(); // 高度设置为屏幕的高度
		p.width = (int) d.getWidth(); // 宽度设置为屏幕的宽度
		dialog.getWindow().setAttributes(p);
	}

	/**
	 * Toast
	 *
	 * @param msg
	 * 提示内容
	 */
	private static Toast mToast = null;

	public static void toast(Context context, String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}

		mToast.show();
	}

	/**
	 * Toast
	 *
	 * @param resId 字串资源id
	 */
	public static void toast(Context context, int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(resId);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}

		mToast.show();
	}

	/**
	 * 获取缓存目录
	 *
	 * @return 缓存目录
	 */
//	public static String getCacheDir(Context context) {
//		Log.i(TAG, "CacheDir = " + context.getExternalCacheDir());
//		return context.getExternalCacheDir() + Constant.CACHE_DIR;
//	}

	/**
	 * 获取屏幕的宽度
	 */
	public static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param pxValue （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * @param url 指定的任意字符串
	 * @return 指定字符串通过md5运算并转化为字符串，如果url为null或长度为0，则返回null
	 */
	public static String md5(String url) {
		String result = null;
		if (url != null && url.length() > 0) {
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(url.getBytes());
				byte[] tar = md5.digest();
				StringBuilder sb = new StringBuilder("");
				for (byte b : tar) {
					int h = (b >> 4) & 15;
					int l = b & 15;
					// 因为4位二进制数最大为1111，即15
					sb.append(Integer.toHexString(h)).append(
							Integer.toHexString(l));
				}
				result = sb.toString();

			} catch (NoSuchAlgorithmException e) {
				result = String.valueOf(url.hashCode());
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 在卸载应用后，会自动删除该文件夹
	 * 优先使用内存卡
	 *
	 * @param context
	 * @param uniqueName 保存文件夹名称
	 * @return
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else { // 内存卡不存在,获取应用地址
			cachePath = context.getCacheDir().getPath();
		}
		File cacheFile = new File(cachePath + File.separator + uniqueName);
		// 如果文件夹不存在，则创建
		if (!cacheFile.exists()) {
			cacheFile.mkdirs();
		}
		return cacheFile;
	}

	/**
	 * 检验是否为手机号
	 *
	 * @param phoneNum
	 * @return
	 */
	public static boolean isMobile(String phoneNum) {
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
		Matcher matcher = pattern.matcher(phoneNum);
		return matcher.matches();
	}


	/**
	 * 判断字符串是否是数字
	 */
	public static boolean isNumber(String value) {
		return isInteger(value) || isDouble(value);
	}
	/**
	 * 判断字符串是否是整数
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是浮点数
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains("."))
				return true;
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}


	/**
	 * 判断网络是否连接
	 *
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivity) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断网络是否有网络连接
	 *
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				mNetworkInfo.isAvailable();
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}


	/**
	 * 判断是否是wifi连接
	 */

	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity) {
//		Intent intent = new Intent("/");
//		ComponentName cm = new ComponentName("com.android.settings",
//				"com.android.settings.WirelessSettings");
//		intent.setComponent(cm);
//		intent.setAction("android.intent.action.VIEW");
//		activity.startActivityForResult(intent, 0);

		Intent intent =  new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
		activity.startActivity(intent);
	}

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static boolean isGpsOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        //boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        }
        return false;
    }


    public static double get2Decimal(double d){
		BigDecimal b   =   new   BigDecimal(d);
		double  f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
		return  f1;
	};

}
