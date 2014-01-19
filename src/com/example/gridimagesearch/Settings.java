package com.example.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Settings extends Activity {

	 private Spinner spinner1, spinner2, spinner3;
	 private Button btnSubmit;
	 EditText siteFilter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		TextView tvLabel = (TextView) findViewById(R.id.testID);
		tvLabel.setText(getIntent().getStringExtra("label"));
		addListenerOnButton();
	}

	private void addListenerOnButton() {
		
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner3 = (Spinner) findViewById(R.id.spinner3);
		siteFilter = (EditText) findViewById(R.id.editText10);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
	 
		btnSubmit.setOnClickListener(new OnClickListener() {
	 
		  @Override
		  public void onClick(View v) {
	 
		    /*Toast.makeText(Settings.this,
			"OnClickListener : " + 
	                "\nImage Size : "+ String.valueOf(spinner1.getSelectedItem()) + 
	                "\nColor Filter : "+ String.valueOf(spinner2.getSelectedItem()) +
	                "\nImage Type : "+ String.valueOf(spinner3.getSelectedItem()) +
	                "\nSite Filter : "+ siteFilter.getText().toString() ,
				Toast.LENGTH_SHORT).show();*/
		    Intent i = new Intent(getApplicationContext(),SearchActivity.class);
		    i.putExtra("size", String.valueOf(spinner1.getSelectedItem()));
		    i.putExtra("color", String.valueOf(spinner2.getSelectedItem()));
		    i.putExtra("type", String.valueOf(spinner3.getSelectedItem()));
		    i.putExtra("site", siteFilter.getText().toString());
		    startActivity(i);
		  }
	 
		});
	  }
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
