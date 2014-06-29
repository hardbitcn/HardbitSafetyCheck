package com.hardbit.hbsc;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hardbit.hbsc.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;
/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private Button cancelScanButton;
	TextView title = null;
	TextView qr_scan_text = null;
	float rate = 0.0f;
	ImageView erweimakuang = null; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanqr);
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		Intent intent = this.getIntent();	
		int title1 = intent.getExtras().getInt("title",0);
		String info="";
		info=intent.getExtras().getString("info");
		title = (TextView)this.findViewById(R.id.title);	
		qr_scan_text = (TextView)this.findViewById(R.id.qr_scan_text);
		erweimakuang = (ImageView)this.findViewById(R.id.erweimakuang);		
		if (title1!=0)
			title.setText(getString(title1));
		qr_scan_text.setText(info);
		 DisplayMetrics displayMetrics = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         Constant.displayWidth = displayMetrics.widthPixels;
         Constant.displayHeight = displayMetrics.heightPixels;
         if(Constant.displayWidth / 480 > 1)
         {
        	  rate = (float)Constant.displayWidth/480;
        	 
         }else {
			rate = 1;
		 }
         LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.7f + 0.5f),
                 (int) (Constant.displayHeight * 0.062f + 0.5f));
         double jushang =  Constant.displayHeight * 0.03 * 2;
         double juzuo = Constant.displayWidth * 0.3 * 2;
         params0.setMargins((int)juzuo,  (int)jushang, 0, 0);
         title.setTextSize((int)(title.getTextSize()*rate));
         title.setLayoutParams(params0);
         
         LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.7f + 0.5f),
                 (int) (Constant.displayHeight * 0.07f + 0.5f));
         double jushang2 =  Constant.displayHeight * 0.020 * 2;
         double juzuo2 = Constant.displayWidth * 0.04 * 2;         
         params1.setMargins((int)juzuo2, (int)jushang2, 0, 0);
         qr_scan_text.setTextSize((int)(qr_scan_text.getTextSize()*rate));
         qr_scan_text.setLayoutParams(params1);
         
         LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth),
                 (int) (Constant.displayWidth));
         double jushang3 =  Constant.displayHeight * 0.005 * 2;    
           
         
         params2.setMargins(0, -(int)jushang3, 0, 0);
         
         erweimakuang.setLayoutParams(params2);
         LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
        		 (int) (Constant.displayWidth * 0.8f + 0.5f),
                 (int) (Constant.displayHeight * 0.1f + 0.5f));
         double juzuo3 = Constant.displayWidth * 0.06 * 2;
         params3.setMargins((int)juzuo3, 6, 0, 0);
         cancelScanButton.setTextSize((int)(cancelScanButton.getTextSize()*rate));       	         
         cancelScanButton.setLayoutParams(params3);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
		//quit the scan view
		cancelScanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		byte[] resultbytes=result.getRawBytes();
		String resultString = result.toString();//getText();		
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else {
//			System.out.println("Result:"+resultString);
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putByteArray("resultbytes", resultbytes);
			bundle.putString("result", resultString);
			resultIntent.putExtras(bundle);
			this.setResult(RESULT_OK, resultIntent);
		}
		CaptureActivity.this.finish();
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}