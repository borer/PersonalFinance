package org.personalfinance.database;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.personalfinance.Transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class used to perform CRUD operations on Transaction objects
 * 
 * @author bogdan
 * 
 */
public class TransactionDAO {

	private SQLiteDatabase database;
	private DBManager dbHelper;

	private String[] allColumnsOutcome = { DBManager.COLUMN_ID,
			DBManager.COLUMN_Nota, DBManager.COLUMN_Fecha,
			DBManager.COLUMN_Categoria, DBManager.COLUMN_LocalizacionValida,
			DBManager.COLUMN_Longitud, DBManager.COLUMN_Latitud,
			DBManager.COLUMN_Gasto };

	private String[] allColumnsIncome = { DBManager.COLUMN_ID,
			DBManager.COLUMN_Nota, DBManager.COLUMN_Fecha,
			DBManager.COLUMN_GANANCIA };

	public TransactionDAO(Context context) {
		dbHelper = new DBManager(context);
	}

	/**
	 * Opens database connection
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Close database connection
	 */
	public void close() {
		dbHelper.close();
	}
	
	
	/**
	 * Helper method
	 * @param trans
	 * @return
	 */
	public Transaction saveTransaction(Transaction trans){
		
		if(trans.isOutcome){
			
			return this.saveTransactionOutcome(trans);
			
		} else {
			
			return this.saveTransactionIncome(trans);
		}
		
	}
	

	/**
	 * Saves the income transaction to the database and returns the new saved
	 * transaction
	 * 
	 * @param trans
	 * @return
	 */
	public Transaction saveTransactionIncome(Transaction trans) {
		ContentValues values = new ContentValues();

		values.put(DBManager.COLUMN_GANANCIA, trans.getCantidadDinero());
		values.put(DBManager.COLUMN_Nota, trans.getNota());
		values.put(DBManager.COLUMN_Fecha,
				DateFormat.getDateInstance().format(trans.getFecha()));

		long insertId = database.insert(DBManager.TABLE_INCOME, null, values);

		Cursor cursor = database.query(DBManager.TABLE_INCOME,
				this.allColumnsIncome, DBManager.COLUMN_ID + " = " + insertId,
				null, null, null, null);

		cursor.moveToFirst();

		Transaction newTransaction = cursorToTransactionIncome(cursor);

		cursor.close();

		return newTransaction;
	}

	/**
	 * Saves the outcome transaction to the database and returns the new saved
	 * transaction
	 * 
	 * @param trans
	 * @return
	 */
	public Transaction saveTransactionOutcome(Transaction trans) {
		ContentValues values = new ContentValues();

		values.put(DBManager.COLUMN_Gasto, trans.getCantidadDinero());
		values.put(DBManager.COLUMN_Nota, trans.getNota());
		values.put(DBManager.COLUMN_Categoria, trans.getCategory());
		values.put(DBManager.COLUMN_Fecha,
				DateFormat.getDateInstance().format(trans.getFecha()));
		values.put(DBManager.COLUMN_LocalizacionValida,
				trans.getLocalizacionValida());
		values.put(DBManager.COLUMN_Longitud, trans.getLongitud());
		values.put(DBManager.COLUMN_Latitud, trans.getLatitud());

		long insertId = database.insert(DBManager.TABLE_OUTCOME, null, values);

		Cursor cursor = database.query(DBManager.TABLE_OUTCOME,
				this.allColumnsOutcome, DBManager.COLUMN_ID + " = " + insertId,
				null, null, null, null);

		cursor.moveToFirst();

		Transaction newTransaction = cursorToTransactionOutcome(cursor);

		cursor.close();

		return newTransaction;
	}

	/**
	 * Helper method
	 */
	public void deleteAllTransactions() {

		this.deleteAllTransactions(true);

	}

	/**
	 * Removes all the outcome transactions from the database
	 * 
	 * @param outcome
	 *            if true delete the outcome rows,else delete the incom rows
	 */
	public void deleteAllTransactions(boolean outcome) {

		if (outcome) {
			this.database.delete(DBManager.TABLE_OUTCOME, null, null);
		} else {
			this.database.delete(DBManager.TABLE_INCOME, null, null);
		}
	}

	
	/**
	 * Helper method
	 * @return
	 */
	public List<Transaction> getAllTransactions() {

		return this.getAllTransactions(true);

	}

	/**
	 * Return a list with all the transactions in the database
	 * 
	 * @return
	 */
	public List<Transaction> getAllTransactions(boolean outcome) {
		List<Transaction> transactions = new ArrayList<Transaction>();

		if (outcome) {

			Cursor cursor = database.query(DBManager.TABLE_OUTCOME,
					allColumnsOutcome, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Transaction trans = cursorToTransactionOutcome(cursor);
				transactions.add(trans);
				cursor.moveToNext();
			}

			// Make sure to close the cursor
			cursor.close();

		} else {

			Cursor cursor = database.query(DBManager.TABLE_INCOME,
					allColumnsIncome, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Transaction trans = cursorToTransactionIncome(cursor);
				transactions.add(trans);
				cursor.moveToNext();
			}

			// Make sure to close the cursor
			cursor.close();
		}

		return transactions;
	}

	/**
	 * Extracts the date from the row and make a new transation
	 * 
	 * @param cursor
	 * @return
	 */
	private Transaction cursorToTransactionIncome(Cursor cursor) {

		int id = cursor.getInt(0);
		String nota = cursor.getString(1);
		Date transactionDate = null;
		try {
			transactionDate = DateFormat.getDateInstance().parse(
					cursor.getString(2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		float dinero = cursor.getFloat(3);

		Transaction transaction = new Transaction();

		transaction.setId(id);
		transaction.setNota(nota);
		transaction.setFecha(transactionDate);
		transaction.setCantidadDinero(dinero);
		transaction.setOutcome(false);

		return transaction;
	}

	/**
	 * Extracts the date from the row and make a new transation
	 * 
	 * @param cursor
	 * @return
	 */
	private Transaction cursorToTransactionOutcome(Cursor cursor) {

		int id = cursor.getInt(0);
		String nota = cursor.getString(1);
		Date transactionDate = null;
		try {
			transactionDate = DateFormat.getDateInstance().parse(
					cursor.getString(2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int category = cursor.getInt(3);
		int localizacionValida = cursor.getInt(4);
		float longitud = cursor.getFloat(5);
		float latitud = cursor.getFloat(6);
		float dinero = cursor.getFloat(7);

		Transaction transaction = new Transaction();

		transaction.setId(id);
		transaction.setNota(nota);
		transaction.setFecha(transactionDate);
		transaction.setCategory(category);
		transaction.setLongitud(longitud);
		transaction.setLatitud(latitud);
		transaction.setCantidadDinero(dinero);
		transaction.setLocalizacionValida(localizacionValida);
		transaction.setOutcome(true);

		return transaction;
	}

}
