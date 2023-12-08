package com.example.databinding_explicitintents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.databinding_explicitintents.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(v->{
            try {
                double peso = Double.parseDouble(binding.userWeight.getText().toString());
                double altura = Double.parseDouble(binding.userHeight.getText().toString());
                //binding.textResult.setText(getString(R.string.result_message, calc_imc(peso, altura)));
                openResultActivity(peso, altura);
            }catch(NumberFormatException ex){
                Log.e("MainActivity",ex.toString());
                Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calc_imc(double peso, double altura){
        double altura_m = altura/100;
        return peso/(altura_m*altura_m);
    }

    private void openResultActivity(double peso, double altura) {
        double imc = calc_imc(peso, altura);
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(ResultActivity.WEIGHT_KEY,peso);
        intent.putExtra(ResultActivity.HEIGHT_KEY,altura);
        intent.putExtra(ResultActivity.IMC_KEY, imc);
        startActivity(intent);
    }
}