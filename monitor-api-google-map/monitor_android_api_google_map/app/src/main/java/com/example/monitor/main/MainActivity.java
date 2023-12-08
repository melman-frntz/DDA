package com.example.monitor.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.monitor.Earthquake;
import com.example.monitor.databinding.ActivityMainBinding;
import com.example.monitor.detail.DetailActivity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainViewModel viewModel = new ViewModelProvider(this,
                new MainViewModelFactory(getApplication())).get(MainViewModel.class);

        binding.eqRecycler.setLayoutManager(new LinearLayoutManager(this));

        EqAdapter adapter = new EqAdapter();
        /*adapter.setOnItemClickListener(earthquake ->
                Toast.makeText(MainActivity.this,
                        earthquake.getPlace(),
                        Toast.LENGTH_SHORT).show());*/
        adapter.setOnItemClickListener(earthquake -> openDetailActivity(earthquake));
        binding.eqRecycler.setAdapter(adapter);

        viewModel.downloadEarthquakes();

        viewModel.getEqList().observe(this,eqList ->{
            adapter.submitList(eqList);
        });


    }

    void openDetailActivity(Earthquake terremoto){
        Log.d("SDI", "openDetailActivity: "+terremoto.getPlace()
                +"\nMagnitude: "+terremoto.getMagnitude()
                +"\nTime: "+terremoto.getTime());
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.TERREMOTO_KEY, terremoto);
        startActivity(intent);
    }
}