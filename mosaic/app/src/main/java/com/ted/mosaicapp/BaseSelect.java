package com.ted.mosaicapp;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import android.view.View;
import android.widget.Button;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

public abstract class BaseSelect extends Activity
{
	protected ImageView MDO;
protected abstract void ondo()
protected SeekBar sekbar;
protected Button tran,swit;
public void toa(String in){
	Toast.makeText(this,in,1).show();
}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.Base);
		sekbar=findViewById(R.id.BaseSeekBar1);
		Button bt=findViewById(R.id.BaseButton1);
		tran=findViewById(R.id.Basetran);
		swit=findViewById(R.id.Baseswitch);
		bt.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					MDO.setImageDrawable( null);
					//((BitmapDrawable) MDO.getDrawable()).getBitmap().recycle();
					System.gc();
					MDO.setImageBitmap(uri_to_bitmap(u));
					
				}
			});
			if(MDO instanceof Custom_ImageView){
				sekbar.setMax(36);
				sekbar.setProgress(4);
			}else{
		sekbar.setMax(24);
        sekbar.setProgress(4);
		}
		sekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

				@Override
				public void onProgressChanged(SeekBar p1, int p2, boolean p3)
				{
					if(MDO instanceof Blur_View){
						((Blur_View)MDO).Blur=p2+1;
					}else{
						int i2 = p2 + 4;
						Custom_ImageView ab=(Custom_ImageView) MDO;
						ab.grid_h = i2;
						ab.grid_w = i2;
						
					}
					
				}

				@Override
				public void onStartTrackingTouch(SeekBar p1)
				{
				}

				@Override
				public void onStopTrackingTouch(SeekBar p1)
				{
				}
			});
			load();
			if(MDO instanceof Blur_View){
				tran.setVisibility(View.GONE);
				swit.setVisibility(View.GONE);
			}
		swit.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
				  Custom_ImageView ab=(Custom_ImageView)MDO;
				  if(ab.action==0){
					  ab.action=1;
					  toa("清除模式");
				  }
				  else
			      {
					  toa("马赛克模式");
					  ab.action=0;
				  }
				}
			});
		tran.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (state == 0) {
					state=1;
						Custom_ImageView ab=(Custom_ImageView) MDO;
					ab.set_mode(state);
						p1.setBackgroundColor(-7829368);
						return;
					}
					state=0;
					//Do_Action.access$102(this.this$0, 0);
					Custom_ImageView ab=(Custom_ImageView) MDO;
					ab.set_mode(state);
					//Do_Action.access$000(this.this$0).set_mode(Do_Action.access$100(this.this$0));
					p1.setBackgroundColor(0);
					
				}
			});
	}
private int state = 0;
	@Override
	public void setContentView(int layoutResID)
	{
		ViewGroup var3 = (ViewGroup)LayoutInflater.from(this).inflate(layoutResID, null);
		ondo();
		LinearLayout.LayoutParams a=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		
		var3.addView(MDO,a);
		super.setContentView(var3);
	}
	public void save(View v){
		write_image_to_galley();
	}
	
	private void write_image_to_galley() {
        File file;
        Bitmap bitmap = ((BitmapDrawable) MDO.getDrawable()).getBitmap();
        File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Mosaic_folder");
        if (!file2.exists()) {
            file2.mkdir();
        }
        String str = "Image_" + 1 + ".png";
        int i = 1;
        boolean z = true;
        do {
            file = new File(file2, str);
            if (!file.exists()) {
                z = false;
                continue;
            } else {
                i++;
                str = "Image_" + i + ".png";
                continue;
            }
        } while (z);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tt", e.getMessage());
        }
      //  MediaScannerConnection.scanFile(this, new String[]{file.toString()}, (String[]) null, new 2(this));
        Toast.makeText(this, "保存成功", 0).show();
    }
	public void load() {
        if (Build.VERSION.SDK_INT < 23) {
            //init();
            select_image();
        } else if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 786);
        } else {
           // init();
            select_image();
        }
    }
	private void select_image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(intent, 1);
    }
	private Uri u;
	protected void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
           u = intent.getData();
            MDO.setImageBitmap(uri_to_bitmap(u));
        } else {
            finish();
        }
        super.onActivityResult(i, i2, intent);
    }
	private Bitmap uri_to_bitmap(Uri uri2) {
        Bitmap bitmap;
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int i = point.x;
        int i2 = point.y;
        ContentResolver contentResolver = getContentResolver();
        try {
            InputStream openInputStream = contentResolver.openInputStream(uri2);
            bitmap = BitmapFactory.decodeStream(openInputStream, (Rect) null, getBitmapOptions(3));
            try {
                if (i > bitmap.getWidth() && i2 > bitmap.getHeight()) {
                    bitmap.recycle();
                    System.gc();
                    try {
                        openInputStream.close();
                    } catch (Exception e) {
                       
                    }
                    return BitmapFactory.decodeStream(contentResolver.openInputStream(uri2), (Rect) null, getBitmapOptions(1));
                }
            } catch (FileNotFoundException e2) {
        
                return bitmap;
            }
        } catch (FileNotFoundException e3) {
           
            bitmap = null;
        
            return bitmap;
        }
        return bitmap;
    }
	private BitmapFactory.Options getBitmapOptions(int i) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = i;
        options.inMutable = true;
        return options;
    }
}
