package com.newbilius.simpleweatherwidget.Options;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.newbilius.simpleweatherwidget.*;
import com.newbilius.simpleweatherwidget.YandexParsingParts.City;

import java.util.List;

public class OptionsActivity extends Activity {

    private EditText cityEditText;
    private Button saveButton;
    private ListView cityListView;
    private CityListAdapter cityDataAdapter;
    private OptionsActivity Context;

    private int widgetID;
    private Intent resultValue;
    private City SelectedItem;
    private TextView textViewLoadingInProgress;
    private View linearLayoutOptionsBlock;
    private RadioButton radioButtonDays;
    private RadioButton radioButtonTime;
    private ConfigKeeper configKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.activity_options);

        textViewLoadingInProgress = (TextView) findViewById(R.id.textViewLoadingInProgress);
        linearLayoutOptionsBlock = findViewById(R.id.linearLayoutOptionsBlock);

        textViewLoadingInProgress.setVisibility(View.VISIBLE);
        linearLayoutOptionsBlock.setVisibility(View.GONE);

        cityEditText = (EditText) findViewById(R.id.cityEditText);
        radioButtonDays = (RadioButton) findViewById(R.id.radioButtonDays);
        radioButtonTime = (RadioButton) findViewById(R.id.radioButtonTime);
        saveButton = (Button) findViewById(R.id.saveButton);
        cityListView = (ListView) findViewById(R.id.cityListView);
        Context = this;

        saveButton.setEnabled(false);

        configKeeper = new ConfigKeeper(this);

        new GetCityAsyncTask(new GetCityAsyncTask.IGetCityAsyncResult() {
            @Override
            public void GetData(List<City> cites) {
                if (cites == null) {
                    ShowError();
                    return;
                }
                cityDataAdapter = new CityListAdapter(Context, cites);
                cityListView.setAdapter(cityDataAdapter);

                textViewLoadingInProgress.setVisibility(View.GONE);
                linearLayoutOptionsBlock.setVisibility(View.VISIBLE);

                if (configKeeper.haveCityId(widgetID)) {
                    cityEditText.setText(configKeeper.getCityName(widgetID));

                    SelectedItem = new City();
                    SelectedItem.Name = configKeeper.getCityName(widgetID);
                    SelectedItem.Id = configKeeper.getCityId(widgetID);
                    radioButtonDays.setChecked(configKeeper.getIs4DaysMode(widgetID));
                    radioButtonTime.setChecked(!configKeeper.getIs4DaysMode(widgetID));
                    saveButton.setEnabled(true);
                }
            }
        }).execute();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WidgetConfigItem widgetConfigItem = new WidgetConfigItem();
                widgetConfigItem.CityId = SelectedItem.Id;
                widgetConfigItem.CityName = SelectedItem.Name;
                widgetConfigItem.WidgetId = widgetID;
                widgetConfigItem.Is4DaysMode = radioButtonDays.isChecked();
                configKeeper.Save(widgetConfigItem);
                setResult(RESULT_OK, resultValue);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
                WeatherWidget.UpdateWidget(getBaseContext(), appWidgetManager, widgetID);

                finish();
            }
        });

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectedItem = cityDataAdapter.getItem(position);
                saveButton.setEnabled(true);
                cityEditText.setText(SelectedItem.Name);
            }
        });

        cityEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (cityEditText != null && cityDataAdapter != null) {
                    Editable text = cityEditText.getText();
                    if (text == null)
                        cityDataAdapter.Filter("");
                    else
                        cityDataAdapter.Filter(cityEditText.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ShowError() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Ошибка загрузки списка городов");
        alertDialog.setMessage("Проверьте интернет-соединение или попробуйте позже.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
    }
}