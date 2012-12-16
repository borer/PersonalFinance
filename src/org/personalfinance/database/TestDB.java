package org.personalfinance.database;

import java.util.Calendar;
import java.util.Date;

import org.personalfinance.Transaction;

import android.content.Context;

public class TestDB {
	
	public static void insertTransactions(Context context,int n){
		
		TransactionDAO DAO =new  TransactionDAO(context);
		
		DAO.open();
		
		Transaction transaction;
		
		for(int i=0 ; i < n ; i++){
			
			Calendar cal = Calendar.getInstance();
			Date nowDate = cal.getTime();
			
			transaction = new Transaction(0, 1, i*10, nowDate, "Somo note", 1, 1.4f, 1.5f, true);
			
			DAO.saveTransaction(transaction);
			
		}
		
		DAO.close();
	}

}
