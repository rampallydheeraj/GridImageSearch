package com.example.gridimagesearch;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity{

	EditText etQuery;
	GridView gvResults;
	Button btnSearch,loadmore;
	int counter ;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		counter = 0;
		
		
		gvResults.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {

				Intent i = new Intent(getApplicationContext(),ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
			
		});
	}

	private void setupViews() {
		// TODO Auto-generated method stub
		etQuery = (EditText) findViewById(R.id.editText10);
		gvResults = (GridView) findViewById(R.id.gridView1);
		btnSearch = (Button) findViewById(R.id.Search);
		loadmore = (Button) findViewById(R.id.load);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public void onComposeAction(MenuItem mi) {
	     // handle click here
		String query = etQuery.getText().toString();
		Intent i = new Intent(this,Settings.class);
		i.putExtra("label", "Settings for "+query);
		startActivity(i);
	}
	
	public void onImageSearch(View v)
	{
		String query = etQuery.getText().toString();
		Toast.makeText(this, "Searching for "+query, Toast.LENGTH_SHORT).show();
		
		AsyncHttpClient client = new AsyncHttpClient();
		final String setSize = getIntent().getStringExtra("size");
		final String setColor = getIntent().getStringExtra("color");
		final String setType = getIntent().getStringExtra("type");
		final String setSite = getIntent().getStringExtra("site");
		
		client.get("https://ajax.googleapis.com/ajax/services/search/images?"+
				"rsz=8&start="+counter+"&v=1.0&q="+Uri.encode(query)+"&imgtype="+Uri.encode(setType)+
				"&imgsz="+Uri.encode(setSize)+"&imgcolor="+Uri.encode(setColor)+"&as_sitesearch="+Uri.encode(setSite), 
					new JsonHttpResponseHandler(){
			
					@Override
					public void onSuccess(JSONObject response)
					{
						JSONArray imageJsonResults = null;
						try
						{
							imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
							imageResults.clear();
							imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
							Log.d("DEBUG",imageResults.toString());
						}
						catch(JSONException e)
						{
							e.printStackTrace();
						}
					}
				});
		loadmore.setVisibility(View.VISIBLE);
		
		loadmore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Starting a new async task
				new loadMoreListView().execute();
			}
		});
		
	}
	
	private class loadMoreListView extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(
					SearchActivity.this);
			pDialog.setMessage("Please wait..");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected Void doInBackground(Void... unused) {
			runOnUiThread(new Runnable() {
				public void run() {
					// increment current page
					counter += 8;
					String query = etQuery.getText().toString();
					
					AsyncHttpClient client = new AsyncHttpClient();
					final String setSize = getIntent().getStringExtra("size");
					final String setColor = getIntent().getStringExtra("color");
					final String setType = getIntent().getStringExtra("type");
					final String setSite = getIntent().getStringExtra("site");
					client.get("https://ajax.googleapis.com/ajax/services/search/images?"+
							"rsz=8&start="+counter+"&v=1.0&q="+Uri.encode(query)+"&imgtype="+Uri.encode(setType)+
							"&imgsz="+Uri.encode(setSize)+"&imgcolor="+Uri.encode(setColor)+"&as_sitesearch="+Uri.encode(setSite), 
								new JsonHttpResponseHandler(){
						
								@Override
								public void onSuccess(JSONObject response)
								{
									JSONArray imageJsonResults = null;
									try
									{
										imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
										imageResults.clear();
										imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
										Log.d("DEBUG",imageResults.toString());
									}
									catch(JSONException e)
									{
										e.printStackTrace();
									}
								}
							});
					

				}
			});

			return (null);
		}
		
		
		protected void onPostExecute(Void unused) {
			// closing progress dialog
			pDialog.dismiss();
		}
	}

}
