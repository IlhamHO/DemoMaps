package be.ehb.demomaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import be.ehb.demomaps.model.Hoofdstad;

public class DetailsHoofdstadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_hoofdstad);

        TextView tvstadsnaam = findViewById(R.id.tv_detail_stadnaam);
        Hoofdstad selectedCity = (Hoofdstad) getIntent().getSerializableExtra("stad");

        tvstadsnaam.setText(selectedCity.getCityName());

    }
}
