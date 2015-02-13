package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Tuple<String,Integer> t = new Tuple<String, Integer>("www.google.com",80);
        List<Tuple<String,Integer>> urlPorts = null;
        urlPorts.add(t);

        try {
            fireConnectionTests(urlPorts);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void fireConnectionTests(List<Tuple<String,Integer>> urlPorts) throws InterruptedException {

        TestConnectivity[] testConnectivities = new TestConnectivity[ urlPorts.size()];
        int i=0;
        for(Tuple<String,Integer> t: urlPorts)
        {
            int port = (int)t.y;
            String url = t.x;
            testConnectivities[i].setUrl(url);
            testConnectivities[i].setSocketPort(port);

            i++;
        }
        ExecutorService service = Executors.newCachedThreadPool();
        List list1 = Arrays.asList(testConnectivities);
        service.invokeAll(list1);

    }
}
