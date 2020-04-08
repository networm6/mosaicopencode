package com.ted.mosaicapp;

import android.app.*;
import android.os.*;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
	public void first(View v){
		startActivity(new Intent(this,Do_Action.class));
	}
	public void two(View v){
		startActivity(new Intent(this,WhiteActivity.class));
	}
	public void about(View v){
		Toast.makeText(this,"搬运自Play,魔改作者,酷安:有头发的琦玉",10).show();
	}
}
