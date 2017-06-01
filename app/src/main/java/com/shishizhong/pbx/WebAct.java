package com.shishizhong.pbx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.zxing.Result;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class WebAct extends BaseActivity implements OnLongClickListener,OnClickListener{
    // Content View Elements

    private WebView mWebView;
    private RelativeLayout mLoading;
    private TextView mLoadFailed;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;
    private TextView mTv5;
    public static final String WEB_EXT_URL = "ext_url";
    public static final String WEB_EXT_TITLE = "ext_title";
    private String mUrl = "";
    private boolean isHome;
    private boolean isHide;
    private File file;
    private boolean isQR;//判断是否为二维码
    private ArrayAdapter<String> adapter;

    private Result result;//二维码解析结果
    private CustomDialog mCustomDialog;
    private long firstBack;
    boolean isBase64 = false;
	boolean isFist = false;

    // End Of Content View Elements

    private void bindViews() {

        mWebView = (WebView) findViewById(R.id.activty_wb_content);
        mLoading = (RelativeLayout) findViewById(R.id.loading);
        mLoadFailed = (TextView) findViewById(R.id.load_faild);
        mTv1 = (TextView) findViewById(R.id.tv1);
        mTv2 = (TextView) findViewById(R.id.tv2);
        mTv3 = (TextView) findViewById(R.id.tv3);
        mTv4 = (TextView) findViewById(R.id.tv4);
        mTv5 = (TextView) findViewById(R.id.tv5);
        mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        mTv3.setOnClickListener(this);
        mTv4.setOnClickListener(this);
        mTv5.setOnClickListener(this);
		isFist = true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//		mTitleView = null;
//		mWebView = null;
//		mLoadFailed = null;
//		mLoading = null;
        setContentView(R.layout.view_null);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_web);
		bindViews();
		initWebView();
	}

	private void initWebView() {
		    mUrl = this.getIntent().getStringExtra(WEB_EXT_URL);
	        String title = this.getIntent().getStringExtra(WEB_EXT_TITLE);
		    mUrl = "http://www.shishizhongaztz.com/";
		//    mUrl = "http://www.shishizhong888.com/";
//		    mUrl = "http://u10010.dded76.shishizhong888.com/";
	        mWebView.setInitialScale(50);
	        mWebView.setWebViewClient(new LCHWebViewClient());
	        mWebView.setWebChromeClient(new CusWebChromeClient());
	        mWebView.getSettings().setJavaScriptEnabled(true);
	        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

	        mWebView.getSettings().setDomStorageEnabled(true);
	        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
	        mWebView.getSettings().setAllowFileAccess(true);
	        mWebView.getSettings().setSupportZoom(true);
	        mWebView.getSettings().setBuiltInZoomControls(true);
	        mWebView.getSettings().setUseWideViewPort(true);
	        mWebView.loadDataWithBaseURL("about:blank", "<span style=\"color:#FF0000\">网页加载失败</span>", "text/html", "utf-8", null);

	        mWebView.getSettings().setLoadWithOverviewMode(true);
	        mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
	        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
	        mWebView.getSettings().setBlockNetworkImage(true);
	        mWebView.getSettings().setAppCacheEnabled(true);
	        mWebView.setOnLongClickListener(this);
	        isHome = true;
	        mWebView.loadUrl(mUrl);		
	}
	 private class LCHWebViewClient extends WebViewClient {

	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            if (null == url) {
	                return false;
	            }
	            if (isFist){
					mUrl = url;
					isFist =!isFist;
	            }
	            if (TextUtils.equals(url, mUrl))
				{   isHome = true;}
	            else
				{   isHome = false;}

	           // view.loadUrl(url);
				if (parseScheme(url)) {
					try {
						Uri uri = Uri.parse(url);
						Intent intent;
						intent = Intent.parseUri(url,
								Intent.URI_INTENT_SCHEME);
						intent.addCategory("android.intent.category.BROWSABLE");
						intent.setComponent(null);
						// intent.setSelector(null);
						startActivity(intent);

					} catch (Exception e) {

					}
				} else {
					view.loadUrl(url);
				}



				return true;
	        }

	        @Override
	        public void onPageFinished(WebView view, String url) {
	            super.onPageFinished(view, url);
	            mLoading.setVisibility(View.GONE);
	            mWebView.getSettings().setBlockNetworkImage(false);
	        }

	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	            super.onPageStarted(view, url, favicon);
	            mLoading.setVisibility(View.VISIBLE);
	            mLoadFailed.setVisibility(View.GONE);
	        }

	        @Override
	        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
	            mLoadFailed.setVisibility(View.VISIBLE);
	        }

	        @Override
	        public void onScaleChanged(WebView view, float oldScale, float newScale) {
	            super.onScaleChanged(view, oldScale, newScale);
	            mWebView.requestFocus();
	            mWebView.requestFocusFromTouch();
	        }

	    }

	    class CusWebChromeClient extends WebChromeClient {
	        @Override
	        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
	            setDlg(message, result);
	            result.cancel();
	            return true;
	        }


	        @Override
	        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
//	            setDlg(message,result);
	            return super.onJsConfirm(view, url, message, result);
	        }

	        public void setDlg(String msg, final JsResult result) {
	            final CusDlg cusDlg2 = new CusDlg(WebAct.this);
	            cusDlg2.setButtonText("确定");
	            cusDlg2.setTitle("提示");
	            cusDlg2.setMessage(msg);
	            cusDlg2.setOperationListener(new OnOperationListener() {
	                @Override
	                public void onLeftClick() {
//	                    result.cancel();
	                    cusDlg2.cancel();
	                }

	                @Override
	                public void onRightClick() {
//	                    result.cancel();
	                    cusDlg2.cancel();
	                }
	            });
	            cusDlg2.show();
	        }


			//扩展浏览器上传文件
			//3.0++版本
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
				openFileChooserImpl(uploadMsg);
			}

			//3.0--版本
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				openFileChooserImpl(uploadMsg);
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
				openFileChooserImpl(uploadMsg);
			}

			@Override
			public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
				onenFileChooseImpleForAndroid(filePathCallback);
				return true;
			}
	    }

	private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
		mUploadMessage = uploadMsg;
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("image/*");
		startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
	}

	public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
	public ValueCallback<Uri[]> mUploadMessageForAndroid5;
	private void onenFileChooseImpleForAndroid(ValueCallback<Uri[]> filePathCallback) {
		mUploadMessageForAndroid5 = filePathCallback;
		Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
		contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
		contentSelectionIntent.setType("image/*");

		Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
		chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
		chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

		startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
	}


		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			 final WebView.HitTestResult htr = mWebView.getHitTestResult();
		        if (htr.getType() == WebView.HitTestResult.IMAGE_TYPE) {
		            String extra = htr.getExtra();
		            if (extra.startsWith("data:image/png;base64,")) {
		                isBase64 = true;
		                String[] split = extra.split(",");
		                // 获取到图片地址后做相应的处理
		                MyAsyncTask mTask = new MyAsyncTask();
		                mTask.execute(split[1]);
		                showDialog();
		            } else {
		                isBase64 = false;
		                // 获取到图片地址后做相应的处理
		                MyAsyncTask mTask = new MyAsyncTask();
		                mTask.execute(htr.getExtra());
		                showDialog();
		            }
		        }
		        return false;
		}
		   public class MyAsyncTask extends AsyncTask<String, Void, String> {


		        @Override
		        protected void onPostExecute(String s) {
		            super.onPostExecute(s);

		            if (isQR) {
		                handler.sendEmptyMessage(0);
		            }


		        }

		        @Override
		        protected String doInBackground(String... params) {
		          /*  XgoLog.e("params:" + params[0]);
		           decodeImage(params[0]);
		            getBitmap(params[0]);*/
		            decodeImage(params[0]);
		            return null;
		        }
		    }
	private static final int WRITE_EXTERNAL_STORAGE_TASK_CODE = 1;
		   /**
		     * 判断是否为二维码
		     * param url 图片地址
		     * return
		     */
		    private boolean decodeImage(String sUrl) {
		        if (isBase64) {
		            Bitmap bitmap = PictureUtil.base64ToBitmap(sUrl);
		            result = DecodeImage.handleQRCodeFormBitmap(bitmap);
//					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//						//如果6.0以上版本
//					}else {
						saveMyBitmap(bitmap, "code");//先把bitmap生成jpg图片
//					}

		        } else {
					Log.e("getBitMap","getBitmap:" + getBitmap(sUrl));
					result = DecodeImage.handleQRCodeFormBitmap(getBitmap(sUrl));
		        }
		        if (result == null) {
		            isQR = false;
		        } else {
		            isQR = true;
		        }
		        return isQR;
		    }

		    /**
		     * 是二维码时，才添加为识别二维码
		     */
		    @SuppressLint("HandlerLeak")
		    private Handler handler = new Handler() {
		        public void handleMessage(Message msg) {
		            isQR  = false;//暂放
		            if (msg.what == 0) {
		                if (isQR) {
		                    adapter.add("识别图中二维码");
		                }
		                adapter.notifyDataSetChanged();
		            }
		        }

		        ;
		    };

		    /**
		     * 初始化数据
		     */
		    private void initAdapter() {
		        adapter = new ArrayAdapter<String>(this, R.layout.item_dialog);
		      //  adapter.add("发送给朋友");
		        adapter.add("保存到手机");
		      //  adapter.add("收藏");
		    }

		    /**
		     * 显示Dialog
		     * param v
		     */
		    private void showDialog() {
		        initAdapter();
		        mCustomDialog = new CustomDialog(this) {
		            @Override
		            public void initViews() {
		                // 初始CustomDialog化控件
		                ListView mListView = (ListView) findViewById(R.id.lv_dialog);
		                mListView.setAdapter(adapter);
		                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		                    @Override
		                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		                        // 点击事件
		                        switch (position) {
		                            case 0:
		                                saveImageToGallery(WebAct.this);
		                                closeDialog();
		                                break;
		                            case 1:
		                                goIntent();
		                                closeDialog();
		                                break;
		                        }

		                    }
		                });
		            }
		        };
		        mCustomDialog.show();
		    }
		 
		    /**
		     * 发送给好友
		     */
		    private void sendToFriends() {
		        Intent intent = new Intent(Intent.ACTION_SEND);
		        Uri imageUri = Uri.parse(file.getAbsolutePath());
		        intent.setType("image/*");
		        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(Intent.createChooser(intent, getTitle()));
		    }

		    /**
		     * 先保存到本地再广播到图库
		     */
		    public void saveImageToGallery(Context context) {

		        // 其次把文件插入到系统图库
		        try {
		            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), "code", null);
		            // 最后通知图库更新
		            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
		                    + file)));
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        }
		    }

		    public void goIntent() {
		      /*  try {
		            Uri uri = Uri.parse(result.toString());
		            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		            startActivity(intent);
		        }
		        catch ()*/
		        try {
		            goToWx(result.toString());
		        } catch (Exception e) {
		            Toast.makeText(this, "添加", Toast.LENGTH_SHORT).show();
		        }
		    }

		    public void goToWx(String url) {
		        // 如下方案可在非微信内部WebView的H5页面中调出微信支付
		        if (url.startsWith("weixin://wxpay/")) {
		            Intent intent = new Intent();
		            intent.setAction(Intent.ACTION_VIEW);
		            intent.setData(Uri.parse(url));
		            startActivity(intent);
		        } else if (parseScheme(url)) {
		            try {
		                Intent intent;
		                intent = Intent.parseUri(url,
		                        Intent.URI_INTENT_SCHEME);
		                intent.addCategory("android.intent.category.BROWSABLE");
		                intent.setComponent(null);
		                // intent.setSelector(null);
		                startActivity(intent);
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        } else {
		        }
		    }

		    public boolean parseScheme(String url) {
		        if (url.contains("platformapi/startapp")) {
		            return true;
		        } else if ((Build.VERSION.SDK_INT > 23)
		                && (url.contains("platformapi") && url.contains("startapp"))) {
		            return true;
		        } else {
		            return false;
		        }
		    }

		    public void showDLg() {

		        final CusDlg cusDlg = new CusDlg(this);

		        cusDlg.setButtonsText("复制", "取消");
		        cusDlg.setTitle("" + SPUtil.getInstant(this).get("cn.jpush.android.NOTIFICATION_CONTENT_TITLE", "提示"));
		        cusDlg.setMessage("" + SPUtil.getInstant(this).get("cn.jpush.android.ALERT", "有新消息"));
		        cusDlg.setOperationListener(new OnOperationListener() {
		            @Override
		            public void onLeftClick() {
		                //获取剪贴板管理器：
		                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		                // 创建普通字符型ClipData
		                ClipData mClipData = ClipData.newPlainText("Label", "" + SPUtil.getInstant(WebAct.this).get("cn.jpush.android.ALERT", "有新消息"));
		                // 将ClipData内容放到系统剪贴板里。
		                cm.setPrimaryClip(mClipData);
		                cusDlg.cancel();
		            }

		            @Override
		            public void onRightClick() {
		                cusDlg.cancel();
		            }
		        });
		        cusDlg.show();
		    }
		    public Bitmap getBitmap(String sUrl) {
		        try {
		            URL url = new URL(sUrl);
		            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		            conn.setConnectTimeout(5000);
		            conn.setRequestMethod("GET");
		            if (conn.getResponseCode() == 200) {
		                InputStream inputStream = conn.getInputStream();
		                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
						Log.e("bitmap","" + bitmap);
							saveMyBitmap(bitmap, "code");//先把bitmap生成jpg图片
		                return bitmap;
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return null;
		    }



	public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

	public void requestPermission(){
		//判断当前Activity是否已经获得了该权限
		if (ContextCompat.checkSelfPermission(this,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {

			//如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				Toast.makeText(this,"please give me the permission",Toast.LENGTH_SHORT).show();
			} else {
				//进行权限请求
				ActivityCompat.requestPermissions(this,
						new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
						EXTERNAL_STORAGE_REQ_CODE);
			}
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case EXTERNAL_STORAGE_REQ_CODE: {
				// 如果请求被拒绝，那么通常grantResults数组为空
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//申请成功，进行相应操作
					if (!file.exists()) {
						try {
							file.createNewFile();
							setFileOp();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					//申请失败，可以继续向用户解释。
					Toast.makeText(WebAct.this, "手机不让申请", Toast.LENGTH_SHORT).show();
				}
				return;
			}
		}
	}
	/**
		     * bitmap 保存为jpg 图片
		     *
		     * @param mBitmap 图片源
		     * @param bitName 图片名
		     */
	         Bitmap mBitmap = null;
		    public void saveMyBitmap(Bitmap mBitmap, String bitName) {


				this.mBitmap = mBitmap;
					file = new File(Environment.getExternalStorageDirectory() + "/" + bitName + ".jpg");
				if ( Build.VERSION.SDK_INT >= 21 ) {
					requestPermission();

				}else {
					setFileOp();
				}
		    }

	private void setFileOp() {
		FileOutputStream fOut = null;
		try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
		try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}


	@Override
		    public boolean onKeyDown(int keyCode, KeyEvent event) {
		        switch (keyCode) {
		            case KeyEvent.KEYCODE_BACK:
		                if (mWebView.canGoBack() && event.getRepeatCount() == 0 && !isHome) {
		                    mWebView.goBack();
		                    return true;
		                } else {
		                    //弹框是否退出
		                    final CusDlg cusDlg = new CusDlg(this);
		                    cusDlg.setButtonsText("取消", "确定");
		                    cusDlg.setTitle("");
		                    cusDlg.setMessage("是否确认退出");
		                    cusDlg.setOperationListener(new OnOperationListener() {
		                        @Override
		                        public void onLeftClick() {
		                            cusDlg.cancel();
		                        }

		                        @Override
		                        public void onRightClick() {
		                            finish();
		                        }
		                    });
		                    cusDlg.show();
		                }
		                break;
		        }

		        return super.onKeyDown(keyCode, event);
		    }

			@Override
			public void onClick(View v) {
				 switch (v.getId()) {
		            case R.id.load_faild:
		                mWebView.reload();
		            case R.id.tv1:
		                if (!TextUtils.isEmpty(mUrl) && !isHome) {
		                    mWebView.loadUrl(mUrl);
		                    isHome = true;
		                }
		                break;
		            case R.id.tv2:
		                if (mWebView.canGoBack())
		                    mWebView.goBack();
		                else Toast.makeText(this, "不能再后退", Toast.LENGTH_LONG).show();
		                break;
		            case R.id.tv3:
		                if (mWebView.canGoForward())
		                    mWebView.goForward();
		                else Toast.makeText(this, "不能再前进", Toast.LENGTH_LONG).show();
		                break;
		            case R.id.tv4:
		                mWebView.reload();
		                break;
		            case R.id.tv5:
		                showPopTop();
		                break;
		            default:
		                break;

		        }
				
			}
			private void showPopTop() {
		        View inflate = getLayoutInflater().inflate(R.layout.pop_layout2, null);

		        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)
		                .setView(inflate)
		                .create();
		        popWindow.showAsDropDown(mTv5, 0, -(mTv5.getHeight() + popWindow.getHeight()));
		        //popWindow.showAtLocation(mButton1, Gravity.NO_GRAVITY,0,0);
		        popupwindowOnClick(inflate, popWindow);
		    }
			  private void popupwindowOnClick(View inflate, CustomPopWindow popWindow) {
			        inflate.findViewById(R.id.tv1).setOnClickListener(new CusPopWindowOnClickListener(popWindow));
			        inflate.findViewById(R.id.tv2).setOnClickListener(new CusPopWindowOnClickListener(popWindow));
			        inflate.findViewById(R.id.tv3).setOnClickListener(new CusPopWindowOnClickListener(popWindow));
			    }
			  class CusPopWindowOnClickListener implements OnClickListener {

			        CustomPopWindow mPopWindow;

			        public CusPopWindowOnClickListener(CustomPopWindow popWindow) {
			            mPopWindow = popWindow;
			        }

			        @Override
			        public void onClick(View v) {
			            switch (v.getId()) {
			                case R.id.tv1:
			                	//访问接口
								share("http://90068.com/");
			                    break;
			                case R.id.tv2:
			                    showDLg();
			                    break;
			                case R.id.tv3:
			                    mPopWindow.dissmiss();
			                    Toast.makeText(WebAct.this, "缓存已清除完毕", Toast.LENGTH_LONG).show();;
			                    mWebView.loadUrl(mUrl);
			                    break;
			                default:
			                    break;
			            }
			        }

			    }
			   private void share(String msg) {

			        Intent intent = new Intent(Intent.ACTION_SEND);
			        intent.setType("image/*");
			        intent.putExtra(Intent.EXTRA_SUBJECT, "时时中彩票");
			        intent.putExtra(Intent.EXTRA_TEXT, "时时中彩票官方网址：" + msg);
			        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        startActivity(Intent.createChooser(intent, getTitle()));
			    }

	public static final int REQUEST_SELECT_FILE = 100;
	public final static int FILECHOOSER_RESULTCODE = 1;
	public ValueCallback<Uri[]> uploadMessage;
	public ValueCallback<Uri> mUploadMessage;



	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage) return;
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		} else if (requestCode == REQUEST_SELECT_FILE) {
			if (uploadMessage == null) return;
			uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
			uploadMessage = null;
		} else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5){
			if (null == mUploadMessageForAndroid5)
				return;
			Uri result = (intent == null || resultCode != RESULT_OK) ? null: intent.getData();
			if (result != null) {
				mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
			} else {
				mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
			}
			mUploadMessageForAndroid5 = null;
		}
	}



}
