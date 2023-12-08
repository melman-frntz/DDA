package com.example.monitor.main;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.monitor.api.EqApiClient;
import com.example.monitor.Earthquake;
import com.example.monitor.api.RequestStatus;
import com.example.monitor.api.StatusWithDescription;
import com.example.monitor.database.EqDatabase;
import com.example.monitor.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    private final MainRepository repository;
    private final MutableLiveData<List<Earthquake>> eqList = new MutableLiveData<>();
    private MutableLiveData<StatusWithDescription> statusMutableLiveData = new MutableLiveData<>();

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainViewModel viewModel = new ViewModelProvider(this,
                new MainViewModelFactory(getApplication()))
                .get(MainViewModel.class);

        binding.eqRecycler.setLayoutManager(new LinearLayoutManager(this));

        EqAdapter adapter = new EqAdapter();
        adapter.setOnItemClickListener(earthquake ->
                Toast.makeText(MainActivity.this,
                        earthquake.getPlace(),
                        Toast.LENGTH_SHORT).show());
        binding.eqRecycler.setAdapter(adapter);

        viewModel.downloadEarthquakes();

        viewModel.getEqList().observe(this,eqList ->{
            adapter.submitList(eqList);
        });

    }

    public LiveData<StatusWithDescription> getStatusMutableLiveData(){
        return statusMutableLiveData;
    }
    public LiveData<List<Earthquake>> getEqList(){
        return eqList;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        EqDatabase database = EqDatabase.getDatabase(application);
        repository = new MainRepository(database);
    }

    public LiveData<List<Earthquake>> getEqList() {
        return repository.getEqList();
    }

    public void downloadEarthquakes() {
        statusMutableLiveData.setValue(new StatusWithDescription(RequestStatus.LOADING,""));
        repository.downloadAndSaveEarthquakes(new MainRepository.DownloadStatusListener() {
            @Override
            public void downloadSuccess() {
                statusMutableLiveData.setValue(new StatusWithDescription(RequestStatus.DONE,""));
            }

            @Override
            public void downloadError(String message) {
                statusMutableLiveData.setValue(new StatusWithDescription(RequestStatus.LOADING, message));
            }
        });
    }

    public void getEarthquakes(){
        EqApiClient.EqService service = EqApiClient.getInstance().getService();

        service.getEarthquakes().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                List<Earthquake> earthquakeList = parseEarthquakes(response.body());
                eqList.setValue(earthquakeList);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private List<Earthquake>parseEarthquakes(String responseString){
        ArrayList<Earthquake>eqList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(responseString);
            JSONArray featuresJSONArray = jsonResponse.getJSONArray("features");
            for(int i = 0; i < featuresJSONArray.length(); i++){
                JSONObject jsonFeature = featuresJSONArray.getJSONObject(i);
                String id = jsonFeature.getString("id");

                JSONObject jsonProperties = jsonFeature.getJSONObject("properties");
                double magnitude = jsonProperties.getDouble("mag");
                String place = jsonProperties.getString("place");
                long time = jsonProperties.getLong("time");

                JSONObject jsonGeometry = jsonFeature.getJSONObject("geometry");
                JSONArray coordinatesJSONArray = jsonGeometry.getJSONArray("coordinates");
                double longitude = coordinatesJSONArray.getDouble(0);
                double latitude = coordinatesJSONArray.getDouble(1);
                eqList.add(new
                        Earthquake(id, place, magnitude, time, latitude, longitude));
            }
        } catch (JSONException e) {
        }
        return eqList;
    }

}
