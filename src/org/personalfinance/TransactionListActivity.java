package org.personalfinance;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.personalfinance.database.TransactionDAO;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TransactionListActivity extends Activity {
	
	private TransactionListAdapter adapter;
	
	private List<Transaction> transactions;
	
	private static String TransactionListKey = "TransactionList";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_list);
		
		if(savedInstanceState != null){
			
			this.transactions = savedInstanceState.getParcelableArrayList(TransactionListKey);
			
		}
		
		if(this.transactions == null) {
		
			new LoadTransactions().execute();
			
		} else {
			
			this.updateDisplay();
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {

		outState.putParcelableArrayList(TransactionListKey, (ArrayList<? extends Parcelable>) this.transactions);
		
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * ListView adapter for the transactions list
	 * 
	 * @author bogdan
	 * 
	 */
	private class TransactionListAdapter extends ArrayAdapter<Transaction> {
		
		private Integer[] imageIconDatabase = { R.drawable.clothes,
	        R.drawable.food, R.drawable.bar, R.drawable.general,
	        R.drawable.gift, R.drawable.hobbies, R.drawable.household,
	        R.drawable.car, R.drawable.personal, R.drawable.shopping, R.drawable.travel};

		private List<Transaction> data;
		private int layoutResourceId;
		private Context context;

		public TransactionListAdapter(Context context, int resource,
				List<Transaction> objects) {

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
				holder.categoryIcon = (ImageView) row
						.findViewById(R.id.TransListViewRow_CategoryImage);
				holder.txtQuantity = (TextView) row
						.findViewById(R.id.TransactionListView_Quantity);
				holder.txtDate = (TextView) row
						.findViewById(R.id.TransactionListView_Date);

				row.setTag(holder);
			} else {
				holder = (RowHolder) row.getTag();
			}

			Transaction trans = this.data.get(position);

			holder.categoryIcon.setImageResource(this.imageIconDatabase[trans.getCategory()]);
			
			holder.txtQuantity
					.setText(Float.toString(trans.getCantidadDinero()));

			String date = DateFormat.getDateInstance().format(trans.getFecha());
			holder.txtDate.setText(date);

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
		LinearLayout row;
		ImageView categoryIcon;
		TextView txtQuantity;
		TextView txtDate;
	}
	
	private class LoadTransactions extends AsyncTask<Void, Void, List<Transaction>>{
		
		private ProgressBar bar;
		private ListView transactionListView;
		
		@Override
		protected void onPreExecute() {

			this.transactionListView = (ListView) findViewById(R.id.TransactionListView);
			this.transactionListView.setVisibility(View.GONE);
			
			this.bar = (ProgressBar) findViewById(R.id.TransactionListView_progressBar);
			this.bar.setVisibility(View.VISIBLE);
			
			super.onPreExecute();
		}
		
		@Override
		protected List<Transaction> doInBackground(Void... param) {
			
			TransactionDAO DAO = new TransactionDAO(getApplicationContext());
			DAO.open();
			List<Transaction> transactions = DAO.getAllTransactions();
			DAO.close();
			
			return transactions;
			
		}
		
		@Override
		protected void onPostExecute(List<Transaction> result) {
			
			transactions = result;
			
			updateDisplay();
			
		}
		
		
	}
	
	private void updateDisplay(){
			
		ProgressBar bar = (ProgressBar) findViewById(R.id.TransactionListView_progressBar);
		bar.setVisibility(View.GONE);
		
		RelativeLayout relLayout = (RelativeLayout) findViewById(R.id.TransactionListView_relativeLayout);
		relLayout.setVisibility(View.GONE);
		
		ListView transactionListView = (ListView) findViewById(R.id.TransactionListView);
		transactionListView.setVisibility(View.VISIBLE);

		TransactionListActivity.this.adapter = new TransactionListAdapter(TransactionListActivity.this,
				R.layout.transaction_listview_row, this.transactions);

		transactionListView.setAdapter(TransactionListActivity.this.adapter);
	}
}
