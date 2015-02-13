package com.example.myapp;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by robinsuri on 2/13/15.
 */
public class TestConnectivity {

    private static final int CONNECT_TIMEOUT = 1;
    static String url = "www.google.com";
    static int socketPort = 80;

    public static void setUrl(String url) {
        TestConnectivity.url = url;
    }

    public static void setSocketPort(int socketPort) {
        TestConnectivity.socketPort = socketPort;
    }

  void call() throws IOException, InterruptedException {


        TestDNS();
        TestSocketConnection();
        TestHttpConnection();
        TestHttpRoundTrip();
        TestSocketRoundTripUsingHttp();
        TestSSLSocket();
        TestSSLRoundTrip();

    }


    private static void TestSocketRoundTripUsingHttp() throws IOException {
        long start = System.currentTimeMillis();

        Socket s = new Socket(InetAddress.getByName(url), socketPort);

        PrintWriter pw = new PrintWriter(s.getOutputStream());
        pw.println("GET / HTTP/1.1");
        pw.println("Host: " + url);
        pw.println("Accept: text/plain, text/html, text/*");
        pw.println("");
        pw.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String t;
        t=br.readLine();
        while((t = br.readLine()) != null) System.out.println(t);
        br.close();
        long end = System.currentTimeMillis();

        System.out.println("Time taken for TestSocketRoundTripUsingHttp() = "+(end-start));

    }

    private static void TestSSLRoundTrip() {

    }

    private static void TestSSLSocket() throws IOException {
        final SSLSocket sslSocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
        long start = System.currentTimeMillis();

        sslSocket.connect(new InetSocketAddress(url, socketPort));
        long end = System.currentTimeMillis();
        System.out.println("Time taken to connect to SSLSocket = "+(end-start));
        System.out.println("is sslSocket Connected = "+sslSocket.isConnected());
    }



    private static void TestHttpRoundTrip() throws IOException {
        long start = System.currentTimeMillis();
        String USER_AGENT = "Mozilla/5.0";

        URL obj = new URL("http://"+url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        inputLine = in.readLine();
        long end = System.currentTimeMillis();
        in.close();
        System.out.println("Time taken by TestHttpRoundTrip() = "+(end-start));

    }

    private static void TestDNS() throws UnknownHostException {
        long start = System.currentTimeMillis();

        InetAddress giriAddress = InetAddress.getByName("www.google.com");
        long end = System.currentTimeMillis();
        System.out.println("TimeTaken by TestDNS() = "+(end-start));

        System.out.println("host address = "+giriAddress.getHostAddress());
    }

    private static void TestSocketConnection() throws IOException {
        SocketAddress sockaddr = new InetSocketAddress("www.google.com", socketPort);
        Socket socket = new Socket();

        long start = System.currentTimeMillis();
        socket.connect(sockaddr, 60*1000);
        long end = System.currentTimeMillis();
        System.out.println("Time taken by TestSocketConnection() = "+(end-start));

        System.out.println("is socket connected = " + socket.isConnected());
        System.out.println("socket timeout = "+socket.getSoTimeout());
        socket.close();
    }

    private static void TestHttpConnection() throws IOException {
        HttpURLConnection conn = null;
        boolean success = true;
        try {
            conn = (HttpURLConnection) new URL("http://"+url).openConnection();
            conn.setRequestProperty("Connection", "close");
            conn.setConnectTimeout(60*1000); //
            Object obj = conn.getContent();

            long start = System.currentTimeMillis();
            conn.connect();
            long end = System.currentTimeMillis();
            System.out.println("Time taken by TestHttpConnection() = "+(end-start));

            System.out.println("read timeout = "+conn.getReadTimeout());
            System.out.println("connectTimeout = " + conn.getConnectTimeout());
        } catch (Exception e) {
            success = false;
        }
        System.out.println("success = " + success);
        conn.disconnect();
        if(conn==null)
            System.out.println("Connection is null");
        if (conn != null) {
            System.out.println("conn.getResponseCode() " + conn.getResponseCode());
        }
    }
}

