package org.personalfinance;


import java.util.Calendar;


import org.personalfinance.database.DBManager;


import android.R.bool;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView.OnEditorActionListener;

public class Introducir_coste extends Activity {
    final String[] TipoGasto = new String[]{"Clothes","Food","Bar","General","Gift","Hobbies"
    		,"HouseHold","Car","Personal","Shopping","Travel"};

    String IdActual;
    int categoria;
    boolean geo=false;
    private TextView dateDisplay;
    private Button pickDate;
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 0;

	private LocationManager locManager;
	private LocationListener locListener;
    double Latitud,Longitud;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introducir_coste);
        
        Resources res = getResources();
        //Configurar Tabs
        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();
        
        TabHost.TabSpec spec=tabs.newTabSpec("Cost");
        spec.setContent(R.id.Cost);
        spec.setIndicator("Cost");
        tabs.addTab(spec);
        IdActual="Cost";

        spec=tabs.newTabSpec("Profit");
        spec.setContent(R.id.Profit);
        spec.setIndicator("Profit");
        tabs.addTab(spec);
        
        tabs.setCurrentTab(0);
        
        tabs.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				Log.i("", "Pulsada pesta�a: " + tabId);
				IdActual=tabId;
			}
		});
        
        
        // Configurar Spinner Gasto
        Spinner TypeSpinnerGasto = (Spinner)findViewById(R.id.SelectTipoGasto);
        TypeSpinnerGasto.setAdapter(new spinnerAdapter(Introducir_coste.this, R.layout.row, TipoGasto));
        TypeSpinnerGasto.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                    android.view.View v, int position, long id) {
                	Log.i("", "selecccionado"+ position);
                	categoria=position;
                      //  lblMensaje.setText("Seleccionado: " + datos[position]);
                }
         
                public void onNothingSelected(AdapterView<?> parent) {
                	Log.i("", "no sel" );
                }
        });
        
        //configurar text edid numbers, para que tengan solo 2 decimales
        EditText editcost = (EditText)findViewById(R.id.costEdit);
        editcost.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        EditText editprofit = (EditText)findViewById(R.id.costEdit);
        editprofit.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        
        //configurar checkbox geolocalizacion
        final CheckBox cb = (CheckBox)findViewById(R.id.geocheck);

        cb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
	        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		        	geo=true;
		        	comenzarLocalizacion();
		        }
		        else {
		        	geo=false;
		        	finalizarLocalizacion();
		        }
	        }
        });
        
        //Configurar Time Selector
        pickDate = (Button) findViewById(R.id.TimeButton);
        
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        
        int mes=month+1;
        String date=day+"/"+mes+"/"+year;
        pickDate.setText(date);
        pickDate.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                   showDialog(DATE_DIALOG_ID);

             }
        });

     
        //Configurar Boton1 Cancelar
        final Button btnBoton1 = (Button)findViewById(R.id.CancelarButton);
        
        btnBoton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                finish();
            }
        });
        
        //configurar Boton2 Guardar
        final Button btnBoton2 = (Button)findViewById(R.id.AceptarButton);
        
        btnBoton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
            	
            	SQLiteDatabase database;
                DBManager db = new DBManager(arg0.getContext());
                database = db.getWritableDatabase();
    	        //Guardamos en la base de datos
    	        //Si hemos abierto correctamente la base de datos
    	        if(database != null)
    	        {
					EditText txt1 = (EditText) findViewById(R.id.costDescripcionEdit);
					String nota = txt1.getText().toString();
					int mes=month+1;
            		String date=day+"/"+mes+"/"+year;
    	        	if(IdActual.equals("Cost")){
     
						EditText txt2 = (EditText) findViewById(R.id.costEdit);
						double cantidadDeDinero = new Double( txt2.getText().toString());

        		        //Insertamos los datos en la tabla Outcome
        		        database.execSQL("INSERT INTO TABLE_OUTCOME (COLUMN_ID, COLUMN_Nota, COLUMN_Fecha,COLUMN_Categoria,COLUMN_LocalizacionValida,COLUMN_Longitud,COLUMN_Latitud,COLUMN_Gasto) " 
        		        +"VALUES ( 0," + nota + "," + date + "," + categoria + "," + geo + "," + Longitud + ", " + Latitud + ", " + cantidadDeDinero +")");

 
    	        	}else{

						EditText txt2 = (EditText) findViewById(R.id.ProfitEdit);
						double cantidadDeDinero = new Double( txt2.getText().toString());

        		        //Insertamos los datos en la tabla Outcome
        		        database.execSQL("INSERT INTO TABLE_INCOME (COLUMN_ID, COLUMN_Nota, COLUMN_Fecha, COLUMN_GANANCIA) " 
        		        +"VALUES ( 0," + nota + "," + date + "," + cantidadDeDinero +")");


    	        	}
    		         
    	         
    	            //Cerramos la base de datos
    	            database.close();
    	        }
    	        
                finish();
            }
        });
    }
	
    private DatePickerDialog.OnDateSetListener mDateSetListener =new DatePickerDialog.OnDateSetListener() {       		 
	       public void onDateSet(DatePicker view, int yearOf, int monthOfYear, int dayOfMonth) {
	            year = yearOf;
	            month = monthOfYear;
	            day = dayOfMonth;
	            int mes=month+1;
	            String date=day+"/"+mes+"/"+year;
	            pickDate.setText(date);
	     }
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	         case DATE_DIALOG_ID:
	               return new DatePickerDialog(this, mDateSetListener, year, month, day);
	     }
	     return null;
	}

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_introducir_coste, menu);
        return true;
    }
    
    private void comenzarLocalizacion()
    {
    	//Obtenemos una referencia al LocationManager
    	locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    	//Obtenemos la �ltima posici�n conocida
    	Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	
    	//Mostramos la �ltima posici�n conocida
    	guardarPosicion(loc);
    	
    	//Nos registramos para recibir actualizaciones de la posici�n
    	locListener = new LocationListener() {
	    	public void onLocationChanged(Location location) {
	    		guardarPosicion(location);
	    	}
	    	public void onProviderDisabled(String provider){
	    		Log.i("", "Provider OFF");
	    	}
	    	public void onProviderEnabled(String provider){
	    		Log.i("", "Provider OFF");
	    	}
	    	public void onStatusChanged(String provider, int status, Bundle extras){
	    		Log.i("", "Provider Status: " + status);
	    	}
    	};
    	
    	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locListener);
    }
     
    private void guardarPosicion(Location loc) {
    	if(loc != null)
    	{
    		Latitud=(loc.getLatitude());
    		Longitud=(loc.getLongitude());
    		Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
    	}
    	else
    	{
    		Latitud=0;
    		Longitud=0;

    	}
    }
    private void finalizarLocalizacion()
    {
    	locManager.removeUpdates(locListener);
    }

}