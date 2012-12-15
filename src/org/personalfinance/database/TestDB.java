package org.personalfinance.database;

import java.util.Calendar;
import java.util.Date;

import org.personalfinance.Transaction;

public class TestDB {
	
	public static void insert10Transactions(){
		
		Transaction transaction;
		
		for(int i=0;i<10;i++){
			
			Calendar cal = Calendar.getInstance();
			Date nowDate = cal.getTime();
			
			transaction = new Transaction(i*10, nowDate , "Something random", false);
			
		}
	}

}
