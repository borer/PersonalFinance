package org.personalfinance;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TransactionListActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_transaction_list, menu);
        return true;
    }
}
