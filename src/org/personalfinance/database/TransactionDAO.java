package org.personalfinance.database;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
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

	private String[] allColumns = { DBManager.COLUMN_ID, DBManager.COLUMN_Nota,
			DBManager.COLUMN_Fecha, DBManager.COLUMN_Categoria,
			DBManager.COLUMN_LocalizacionValida, DBManager.COLUMN_Longitud,
			DBManager.COLUMN_Latitud, DBManager.COLUMN_Gasto };

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
	 * Saves the transaction to the database and returns the new saved
	 * transaction
	 * 
	 * @param trans
	 * @return
	 */
	public Transaction saveTransaction(Transaction trans) {
		ContentValues values = new ContentValues();

		values.put(DBManager.COLUMN_Gasto, trans.getCantidadDinero());
		values.put(DBManager.COLUMN_Nota, trans.getNota());
		values.put(DBManager.COLUMN_Categoria, trans.getCategory());
		values.put(DBManager.COLUMN_Fecha,
				DateFormat.getDateInstance().format(trans.getFecha()));
		values.put(DBManager.COLUMN_Longitud, trans.getLongitud());
		values.put(DBManager.COLUMN_Latitud, trans.getLatitud());

		long insertId = database.insert(DBManager.TABLE_OUTCOME, null, values);

		Cursor cursor = database.query(DBManager.TABLE_OUTCOME,
				this.allColumns, DBManager.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Transaction newTransaction = cursorToScore(cursor);
		cursor.close();
		return newTransaction;
	}

	/**
	 * Removes all the transactions from the database
	 */
	public void deleteAllTransactions() {

		this.database.delete(DBManager.TABLE_OUTCOME, null, null);
	}

	/**
	 * Return a list with all the transactions in the database
	 * @return
	 */
	public List<Transaction> getAllTransactions() {
		List<Transaction> transactions = new ArrayList<Transaction>();

		Cursor cursor = database.query(DBManager.TABLE_OUTCOME, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Transaction trans = cursorToScore(cursor);
			transactions.add(trans);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();

		return transactions;
	}

	/**
	 *  Extracts the date from the row and make a new transation
	 * @param cursor
	 * @return
	 */
	private Transaction cursorToScore(Cursor cursor) {

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
		transaction.setLongitud(latitud);
		transaction.setCantidadDinero(dinero);

		return transaction;
	}

}
