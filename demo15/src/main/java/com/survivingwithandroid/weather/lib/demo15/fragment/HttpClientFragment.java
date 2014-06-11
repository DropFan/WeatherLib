/**
 * ${copyright}.
 */
package com.survivingwithandroid.weather.lib.demo15.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.demo15.R;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.provider.forecastio.ForecastIOProviderType;


public class HttpClientFragment extends Fragment {

    private WeatherClient client;
    private TextView tempView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
        WeatherConfig config = new WeatherConfig();
        config.ApiKey = "your_api_key";
        try {
            client = builder.attach(getActivity())
                    .provider(new ForecastIOProviderType())
                    .httpClient(com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient.class)
                    .config(config)
                    .build();
        } catch (WeatherProviderInstantiationException wpie) {
            wpie.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_http_client, container, false);
        tempView = (TextView) v.findViewById(R.id.temp);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        client.getCurrentCondition("43.11#12.23", new WeatherClient.WeatherEventListener() {
            @Override
            public void onWeatherRetrieved(final CurrentWeather weather) {

                tempView.setText("Temp:" + weather.weather.temperature.getTemp());
            }

            @Override
            public void onWeatherError(WeatherLibException wle) {
                wle.printStackTrace();
            }

            @Override
            public void onConnectionError(Throwable t) {

            }
        });
    }
}
