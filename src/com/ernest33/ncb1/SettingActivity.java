package com.ernest33.ncb1;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends FragmentActivity implements ActionBar.OnNavigationListener
{

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String	STATE_SELECTED_NAVIGATION_ITEM	= "selected_navigation_item";
	private static final int	REQUEST_CODE	= 1;
	ImageView img;
	private Bitmap bitmap;
	private Toast tt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
		new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1,
			android.R.id.text1, new String[]
			{ getString(R.string.title_section1), getString(R.string.title_section2),
			getString(R.string.title_section3), }), this);
		actionBar.hide();
		img = (ImageView) findViewById(R.id.imageView1);
		/*try
		{
			iwantroot();
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
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
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM))
		{
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id)
	{
		// When the given dropdown item is selected, show its contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		return true;
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment
	{
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String	ARG_SECTION_NUMBER	= "1";

		public DummySectionFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_setting_dummy, container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	public void selectimage(View v)
	{
		Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("crop", true);
		intent.putExtra("scale", true);
		intent.putExtra("outputX", 720);
		intent.putExtra("outputY", 1280);
		intent.putExtra("aspectX", 9);
		intent.putExtra("aspectY", 16);
		//File dir = Environment.getExternalStorageDirectory();// + "/notifbg.png";
		File tmp = Environment.getDataDirectory();
		File output = new File(tmp, "notifbg.png");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
	}
	
	public void save_image()
	{
		File tmp = Environment.getDataDirectory();
		File input = new File(tmp, "notifbg.png");
		File dir = Environment.getExternalStorageDirectory();// + "/notifbg.png"
		//File output = new File(dir, "notifbg.png");
		boolean success = input.renameTo(dir);
		if(success)
		{
			toastMessage("Restart to apply your new background :-)");
		}
		else
		{
			toastMessage("Unable to apply the new background...ooops");
		}
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
				/*File dir = Environment.getExternalStorageDirectory();// + "/notifbg.png";
				File output = new File(dir, "notifbg.png");*/
				File tmp = Environment.getDataDirectory();
				File output = new File(tmp, "notifbg.png");
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
	
	public void msg(){
		toastMessage("COUCOU");
	}
	
	public void useimage()
	{
		Process p = null;
		try
		{
			p = Runtime.getRuntime().exec("kill com.android.systemui");
			p.waitFor();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void iwantroot() throws Exception
	{
	    Process p;  
	    try {  
	       // Preform su to get root privledges  
	       p = Runtime.getRuntime().exec("su");   
	      
	       // Attempt to write a file to a root-only  
	       DataOutputStream os = new DataOutputStream(p.getOutputStream());  
	       os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n");  
	      
	       // Close the terminal  
	       os.writeBytes("exit\n");  
	       os.flush();  
	       try {  
	          p.waitFor();  
	               if (p.exitValue() != 255) {  
	                  // TODO Code to run on success  
	                  toastMessage("root");  
	               }  
	               else {  
	                   // TODO Code to run on unsuccessful  
	                   toastMessage("not root");  
	               }  
	       } catch (InterruptedException e) {  
	          // TODO Code to run in interrupted exception  
	           toastMessage("not root");  
	       }  
	    } catch (IOException e) {  
	       // TODO Code to run in input/output exception  
	        toastMessage("not root");  
	    }  
	}
}
