package org.personalfinance;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class leaderBoardActivity extends Activity {

	List<HighScore> scores;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_board);
		
		new LeaderBoardLoading().execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_score, menu);
		return true;
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
		
	}

	/**
	 * Get the content from the server
	 * @author bogdan
	 *
	 */
	private class LeaderBoardLoading extends
			AsyncTask<Void, Integer, List<HighScore>> {

		private String urlScores = "http://finance-api.herokuapp.com/persons/";

		@Override
		protected List<HighScore> doInBackground(Void... params) {

			List<HighScore> scoreList = new ArrayList<HighScore>();

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(urlScores);

			try {
				HttpResponse response = client.execute(request);
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

			if (result == null) {
				return;
			}

			leaderBoardActivity.this.scores = result;
			
			leaderBoardActivity.this.updateDisplay();
		}
	}
}
