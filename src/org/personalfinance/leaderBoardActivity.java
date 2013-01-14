package org.personalfinance;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.personalfinance.database.TransactionDAO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class leaderBoardActivity extends Activity {

	private List<HighScore> scores;
	
	private LeaderBoardAdapter adapter;
	
	private static String ScoreListKey = "scoreList";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_board);

		//Load the score from local if possible
		if (savedInstanceState != null) {
			this.scores = savedInstanceState.getParcelableArrayList(ScoreListKey);
		}
		
		//Download score from server
		if(this.scores == null){
			
			this.scores = new ArrayList<HighScore>();
			
			new LeaderBoardLoading("Bogdan").execute();
			
			ListView leadderBoardListView = (ListView) findViewById(R.id.LeaderBoard_listView);
			
			this.adapter = new LeaderBoardAdapter(this,
					R.layout.leader_board_listview_row,
					this.scores);
			
			leadderBoardListView.setAdapter(this.adapter);
			
		} else {
		//Score is already downloaded
			ProgressBar progressBar = (ProgressBar) findViewById(R.id.LeaderBoard_progressBar);
			
			progressBar.setVisibility(View.GONE);
			
			this.updateDisplay();
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {

		outState.putParcelableArrayList(ScoreListKey, (ArrayList<? extends Parcelable>) this.scores);
		
		super.onSaveInstanceState(outState);
	}
	
	private void updateDisplay(){
		
		for (int i = 0; i < this.scores.size(); i++) {
			HighScore score = this.scores.get(i);

			// Set the special names
			if (i == 0) {
				TextView name1TextView = (TextView) findViewById(R.id.LeaderBoard_Name1);

				name1TextView.setText(score.getName());
			} else if (i == 1) {

				TextView name2TextView = (TextView) findViewById(R.id.LeaderBoard_Name2);

				name2TextView.setText(score.getName());

			} else if (i == 2) {

				TextView name3TextView = (TextView) findViewById(R.id.LeaderBoard_Name3);

				name3TextView.setText(score.getName());
			} else {
				break;
			}
			
			
		}
		
		ListView leaderBoardListView = (ListView) findViewById(R.id.LeaderBoard_listView);
		
		this.adapter = new LeaderBoardAdapter(this,
				R.layout.leader_board_listview_row,
				this.scores);
		
		
		leaderBoardListView.setAdapter(adapter);
		
		//this.adapter.notifyDataSetChanged();
		
	}

	/**
	 * Get the content from the server
	 * @author bogdan
	 *
	 */
	private class LeaderBoardLoading extends
			AsyncTask<Void, Integer, List<HighScore>> {

		private String urlScoresGet = "http://finance-api.herokuapp.com/persons/";
		
		private String userName;
		private int score;
		
		private String urlScoresPost = "http://finance-api.herokuapp.com/person";
		
		public LeaderBoardLoading(String name){
			this.userName = name;
		}

		//Hide all the view before load
		@Override
		protected void onPreExecute() {
					
			View view1 = findViewById(R.id.View01);
			View view2 = findViewById(R.id.View02);
			View view3 = findViewById(R.id.View03);
			ListView listView = (ListView) findViewById(R.id.LeaderBoard_listView);
					
			view1.setVisibility(View.GONE);
			view2.setVisibility(View.GONE);
			view3.setVisibility(View.GONE);
			listView.setVisibility(View.GONE);
					
			super.onPreExecute();
		}
		
		@Override
		protected List<HighScore> doInBackground(Void... params) {
			
			//Get TotalScores from database
			
			TransactionDAO DAO = new TransactionDAO(getApplicationContext());
			DAO.open();
			List<Transaction> transactions = DAO.getAllTransactions();
			DAO.close();
			
			this.score =0;
			for(Transaction trans: transactions){
				this.score += trans.getCantidadDinero();
			}

			//Send the local scores
			
			HttpClient client = new DefaultHttpClient();
			HttpPost requestPost = new HttpPost(this.urlScoresPost);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("name", this.userName ));
			pairs.add(new BasicNameValuePair("score", Integer.toString(this.score) ));
			try {
				requestPost.setEntity(new UrlEncodedFormEntity(pairs));
				//Return the user id
				//HttpResponse response = client.execute(requestPost);
				client.execute(requestPost);
			}catch (IOException e) {
				e.printStackTrace();
			}
			
			//Get the scores
			
			List<HighScore> scoreList = new ArrayList<HighScore>();

			client = new DefaultHttpClient();
			HttpGet requestGet = new HttpGet(urlScoresGet);

			try {
				HttpResponse response = client.execute(requestGet);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				String responseString = Utils.convertStreamToString(is);

				if (responseString == "null") {
					return null;
				}

				//Log.d("APPPPPPPPP", responseString);
				
				Gson gson = new Gson();
				Type listOfTestObject = new TypeToken<List<HighScore>>() {}.getType();
				scoreList = gson.fromJson(responseString, listOfTestObject);

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return scoreList;
		}

		@Override
		protected void onPostExecute(List<HighScore> result) {

			//After retrieve the result make the views reappear again
			View view1 = findViewById(R.id.View01);
			View view2 = findViewById(R.id.View02);
			View view3 = findViewById(R.id.View03);
			ListView listView = (ListView) findViewById(R.id.LeaderBoard_listView);
			ProgressBar progressBar = (ProgressBar) findViewById(R.id.LeaderBoard_progressBar);
			
			progressBar.setVisibility(View.GONE);
			view1.setVisibility(View.VISIBLE);
			view2.setVisibility(View.VISIBLE);
			view3.setVisibility(View.VISIBLE);
			listView.setVisibility(View.VISIBLE);			
			
			if (result == null) {
				return;
			}

			leaderBoardActivity.this.scores = result;
			
			leaderBoardActivity.this.updateDisplay();
		}
	}
	
	
	/**
	 * Adapter for the leader board listview
	 * @author bogdan
	 *
	 */
	private class LeaderBoardAdapter extends ArrayAdapter<HighScore> {
		
		private List<HighScore> data;
		private int layoutResourceId;
		private Context context;

		public LeaderBoardAdapter(Context context, int resource,
				List<HighScore> objects) {

			super(context, resource, objects);

			this.data = objects;
			this.layoutResourceId = resource;
			this.context = context;
		}

		public int getCount() {
			return this.data.size();
		}
		

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			RowHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) this.context)
						.getLayoutInflater();
				row = inflater.inflate(this.layoutResourceId, parent, false);

				holder = new RowHolder();
				holder.txtName = (TextView) row
						.findViewById(R.id.LeaderBoard_Row_Name);
				holder.txtScore = (TextView) row
						.findViewById(R.id.LeaderBoard_Row_Score);

				row.setTag(holder);
			} else {
				holder = (RowHolder) row.getTag();
			}

			HighScore score = this.data.get(position);

			holder.txtName
					.setText(score.getName());
			
			holder.txtScore.setText( Integer.toString(score.getScore()) );

			return row;
		}
	}
	
	/**
	 * Helper class for the listView adapter
	 * 
	 * @author bogdan
	 * 
	 */
	static class RowHolder {
		RelativeLayout row;
		TextView txtName;
		TextView txtScore;
	}
}
