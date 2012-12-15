package org.personalfinance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.personalfinance.database.DBManager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	SQLiteDatabase database;
	float moneySpent;
	float moneyEarned;
	ArrayList<Transaction> recentTransactionsArray;
	String dateFormat;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        updateDataDisplayed();
        
        // Create the actions for the buttons
        
    }
    
    public void updateDataDisplayed() {
    	int nTransaccionesMasRecientes;
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        recentTransactionsArray = new ArrayList<Transaction>();
        
    	// Abrimos la base de datos en modo escritura
        DBManager sqlH = new DBManager(this);
        database = sqlH.getWritableDatabase();
        
        //crearDatosDePrueba(database);
        
        // Fill general information views
        if(database != null){
        	TextView moneySpentTV = (TextView) findViewById(R.id.spentMoneyTV);
        	TextView moneyLeftTV = (TextView) findViewById(R.id.leftMoneyTV);
        	TextView moneyEarnedTV = (TextView) findViewById(R.id.earnedMoneyTV);
        	ProgressBar moneyProgressBar = (ProgressBar) findViewById(R.id.moneyProgressBar);
        	
        	// Sum all expenses
        	moneySpent = 0;
        	nTransaccionesMasRecientes = 0;
        	String[] campos = new String[] {"Gasto", "Fecha", "Nota"};
        	Cursor cursorOutcome = database.query("OUTCOME", campos, null, null, null, null, "Fecha DESC");
        	if (cursorOutcome.moveToFirst()) {
	             do {
	            	  moneySpent += Float.parseFloat(cursorOutcome.getString(0));
	            	  if(nTransaccionesMasRecientes < 3){
	            		  recentTransactionsArray.add(crearTransaccionMedianteCursor(cursorOutcome, true));
	            		  nTransaccionesMasRecientes++;
	            	  }
	               } while(cursorOutcome.moveToNext());
	        }
        	cursorOutcome.close();
        	
        	// Sum all incomes
        	moneyEarned = 0;
        	nTransaccionesMasRecientes = 0;
        	campos = new String[] {"Ganacia", "Fecha", "Nota"};
        	Cursor cursorIncome = database.query("INCOME", campos, null, null, null, null, "Fecha DESC");
        	if (cursorIncome.moveToFirst()) {
	             do {
	            	 moneyEarned += Float.parseFloat(cursorIncome.getString(0));
	            	 if(nTransaccionesMasRecientes < 3){
	            		 recentTransactionsArray.add(crearTransaccionMedianteCursor(cursorIncome, false));
	            		 nTransaccionesMasRecientes++;
	            	 }
	               } while(cursorIncome.moveToNext());
	        }
        	cursorIncome.close();
        	
        	// Fill balance information
        	moneySpentTV.setText(""+moneySpent);
        	moneyLeftTV.setText(""+(moneyEarned-moneySpent));
        	moneyEarnedTV.setText(""+moneyEarned);
        	moneyProgressBar.setMax((int) moneyEarned);
        	moneyProgressBar.setProgress((int) (moneyEarned-moneySpent));
        	
        	// Fill list of the 3 most recent transactions
        	Collections.sort(recentTransactionsArray);
        	ListView lastTransactionsLV = (ListView) findViewById(R.id.lastTransactionsLV);
        	lastTransactionsLV.setAdapter(new RecentTransactionsAdapter(this, 
        			android.R.layout.simple_list_item_1, R.id.lastTransactionsLV,
        			recentTransactionsArray));
        }
        database.close();
    } 
    

    // Convierte un cursor en un objeto Transaccion
    public Transaction crearTransaccionMedianteCursor(Cursor cursor, boolean isOutcome){
    	Transaction transaccion = null;
    	
    	SimpleDateFormat format = new SimpleDateFormat(dateFormat);
  	  	try{
  	  		Date date = format.parse(cursor.getString(1));
    		transaccion = new Transaction(Float.parseFloat(cursor.getString(0)), date, cursor.getString(2), isOutcome);
  	  	}
  	  	catch(ParseException e){
    		System.out.println("Couldn't parse date");
  	  	} catch (java.text.ParseException e) {
  		  System.out.println("Couldn't parse cursor");
  	  	}
  	  
  	  	return transaccion;
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    private class RecentTransactionsAdapter extends ArrayAdapter<Transaction>{

    	public RecentTransactionsAdapter(Context context, int resource,
    			int textViewResourceId, ArrayList<Transaction> recentTransactionsArray) {
    		super(context, resource, textViewResourceId, recentTransactionsArray);
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View row = inflater.inflate(R.layout.recent_transaction_lv, parent, false);
    		
    		if (position >= recentTransactionsArray.size())
    			return row;
    		
    		TextView transactionSignTV = (TextView) row.findViewById(R.id.transactionSignTV);
    		TextView transactionNameTV = (TextView) row.findViewById(R.id.transactionTV);
    		TextView transactionAmountTV = (TextView) row.findViewById(R.id.transactionAmountTV);
    		
    		String transactionSign = (recentTransactionsArray.get(position).isOutcome()) ? "-" : "+";
    		transactionSignTV.setText(transactionSign);
    		
    		String nota = recentTransactionsArray.get(position).getNota();
    		if (nota == null)
    			nota = "expense";
    		transactionNameTV.setText(nota);
    		
    		transactionAmountTV.setText(""+recentTransactionsArray.get(position).getCantidadDinero());
    		
    		return row;
    	}
    }
    
    
    void crearDatosDePrueba(SQLiteDatabase database){
    	
    	database.execSQL("INSERT INTO INCOME (Id, Nota, Fecha, Ganacia) " +
                "VALUES (1, 'Sueldo', '2012-12-06 05:40:00',2500)");
    	
    	
    	database.execSQL("INSERT INTO OUTCOME (Id, Nota, Fecha, Categoria, LocalizacionValida, Longitud, Latitud, Gasto) " +
    			"VALUES (2, 'Compras del super', '2012-12-07 05:40:00', 1, 0, 0, 0, 100)");
    	
    	database.execSQL("INSERT INTO OUTCOME (Id, Nota, Fecha, Categoria, LocalizacionValida, Longitud, Latitud, Gasto) " +
    			"VALUES (3, 'Ropa', '2012-12-08 05:40:00', 2, 0, 0, 0, 50)");
    	
    	database.execSQL("INSERT INTO OUTCOME (Id, Nota, Fecha, Categoria, LocalizacionValida, Longitud, Latitud, Gasto) " +
    			"VALUES (4, 'Bocadillo', '2012-12-09 05:40:00', 1, 0, 0, 0, 2.50)");
    	
    }
    
}
