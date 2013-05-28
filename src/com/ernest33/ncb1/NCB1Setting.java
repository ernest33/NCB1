package com.ernest33.ncb1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

class NCB1Setting extends Activity 
{
	//Variables
	private static final int	REQUEST_CODE	= 1;
	ImageView img;			// Target the Image View control
	SeekBar seekbar;		// Target the transparency seekbar
	private Bitmap bitmap;	// Contain the bitmap to use
	private Toast tt;		// Used to show small message text
	public static final String PREFS_NAME = "ernest33_ncb1_prefs";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ncb1_setting);
		img = (ImageView) findViewById(R.id.imageView1);
		seekbar = (SeekBar) findViewById(R.id.seekBar1);

		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		int trans = settings.getInt("last_alpha", 255);
		seekbar.setProgress(trans);
		
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
		{	
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) 
			{
				// TODO Auto-generated method stub
				// not used
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) 
			{
				// TODO Auto-generated method stub
				// not used
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
			{
				// Change image transparency using progress value
				if((progress > 1) && (progress < 255))
				{
					/*
					Drawable d = img.getDrawable();
			        d.setAlpha(progress);
			        ((ImageView) findViewById(R.id.imageView1)).setImageDrawable(d);
					*/
					createNewImage(progress);
					img.setImageBitmap(bitmap);
					
				}
			}
		});
		

		try
		{
			if(bitmap != null)
				bitmap.recycle();
			File dir = Environment.getExternalStorageDirectory();// + "/notifbg.png";
			File output = new File(dir, "notifbg.png");
			InputStream stream = getContentResolver().openInputStream(Uri.fromFile(output));
			bitmap = BitmapFactory.decodeStream(stream);
			stream.close();
	        img.setImageBitmap(bitmap);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		Button btsel = (Button)findViewById(R.id.button3);
		btsel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				select_image(arg0);
			}
		});
		Button btapply = (Button)findViewById(R.id.button2);
		btapply.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				try
				{
					apply_image(arg0);
				}
				catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertbox("FILENOTFOUND",e.getMessage());
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertbox("IOEXCEP",e.getMessage());
				}
			}
		});	
		//Button btrst = (Button)findViewById(R.id.button1);
		
	}

	@Override
    protected void onStop(){
       super.onStop();

      // We need an Editor object to make preference changes.
      // All objects are from android.context.Context
      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
      SharedPreferences.Editor editor = settings.edit();
      editor.putInt("last_alpha", seekbar.getProgress());

      // Commit the edits!
      editor.commit();
    }

	
	@SuppressWarnings("deprecation")
	protected void alertbox(String title, String mymessage)
	{
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setCancelable(false); // This blocks the 'BACK' button
		ad.setTitle(title);
		ad.setMessage(mymessage);
		ad.setButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		ad.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ncb1_setting, menu);
		return true;
	}

	public void select_image(View v)
	{
		Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("crop", true);
		intent.putExtra("scale", true);
		intent.putExtra("outputX", 720);
		intent.putExtra("outputY", 1280);
		intent.putExtra("aspectX", 9);
		intent.putExtra("aspectY", 16);
		File dir = Environment.getExternalStorageDirectory();
		File output = new File(dir, "notifbg.png");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
		{
			try
			{
				if(bitmap != null)
					bitmap.recycle();
				File dir = Environment.getExternalStorageDirectory();// + "/notifbg.png";
				File output = new File(dir, "notifbg.png");
				InputStream stream = getContentResolver().openInputStream(Uri.fromFile(output));
				bitmap = BitmapFactory.decodeStream(stream);
				stream.close();
	            img.setImageBitmap(bitmap);
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	public void toastMessage(String message)
	{
		tt = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        tt.show();
	}
	public void apply_image(View v) throws FileNotFoundException, IOException
	{
		/*img.setDrawingCacheEnabled(true);
		img.buildDrawingCache(true);
		File imageFile = new File(Environment.getExternalStorageDirectory(), "notifbg.png");
		if(imageFile.exists())
		{
			//alertbox("TEST", "B:EXISTE DEJA");
		}
		else
		{
			//alertbox("TEST", "B:NOUVEAU FICHIER");
		}
		FileOutputStream fileOutputStream = null;
		fileOutputStream = new FileOutputStream(imageFile);
		//alertbox("TEST", "C");
		// Sauve image
		img.getDrawingCache(false).compress(CompressFormat.PNG, 100, fileOutputStream);
		//alertbox("TEST", "D");
		// Fermeture
		fileOutputStream.flush();
		fileOutputStream.close();
		img.setDrawingCacheEnabled(false);
		//alertbox("TEST", "E");
		*/
		File imageFile = new File(Environment.getExternalStorageDirectory(), "notifbg.png");
		FileOutputStream fileOutputStream = null;
		fileOutputStream = new FileOutputStream(imageFile);
		bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
		fileOutputStream.flush();
		fileOutputStream.close();
	}
	
    /**
     * adjust alpha value of the image
     * @param alphaValue
     */
    private void createNewImage(int alphaValue) {
            int m_width = bitmap.getWidth();
            int m_height = bitmap.getHeight();

            // try to use Pixel Array now...
            int[] pixels = new int[m_width * m_height];
            bitmap.getPixels(pixels, 0, m_width, 0, 0, m_width, m_height);
            for (int y = 0; y < m_height; y++) {
                    for (int x = 0; x < m_width; x++) {
                            int index = y * m_width + x;
                            int a = (pixels[index] >> 24) & 0xff;
                            int r = (pixels[index] >> 16) & 0xff;
                            int g = (pixels[index] >> 8) & 0xff;
                            int b = pixels[index] & 0xff;
                            a = alphaValue;
                            pixels[index] = (a << 24) | (r << 16) | (g << 8) | b;
                    }
            }
            
            // myImage.setPixels(pixels, 0, m_width, 0, 0, m_width, m_height); seems like setPixel method have some problem
            Bitmap bm = Bitmap.createBitmap(pixels, 0, m_width, m_width, m_height, Bitmap.Config.ARGB_8888);
            pixels = null;
            bitmap = bm;
    }
	
}
