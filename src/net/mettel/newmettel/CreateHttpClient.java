package net.mettel.newmettel;

import java.io.InputStream;

import net.mettel.reference_class.EasySSLSocketFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.util.Log;

/**
 * This piece of code is to handle both http and https connections Create new
 * HttpClient with cm and params. It will accept connections to HTTPS hosts
 * without verifying the SSL certificate:
 * 
 * To completely solve SSL problem, see http://stackoverflow.com/questions
 * /2642777/trusting-all-certificates-using-httpclient-over-https the second
 * answer!!!
 */

public class CreateHttpClient {
	private static final String MYTAG = "myTag";
	private InputStream is;

	public CreateHttpClient(String url) {

		this.is = connect(url);
	}

	public InputStream getis() {
		return this.is;
	}

	private InputStream connect(String url) {

		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		// http scheme
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		// https scheme
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(),
				443));
		// Create and initialize HTTP parameters
		HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
				new ConnPerRouteBean(30));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		/**
		 * Create an HttpClient with the ThreadSafeClientConnManager.This
		 * connection manager must be used if more than one thread will be using
		 * the HttpClient.
		 */
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);

		HttpClient client = new DefaultHttpClient(cm, params);
		// HttpClient client = new DefaultHttpClient();
		Log.i(MYTAG, "clint done");
		HttpGet request = new HttpGet(url);
		Log.i(MYTAG, "request done");

		try {
			HttpResponse response = client.execute(request);
			Log.i(MYTAG, "execute done");
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				Log.i(MYTAG, is.toString());
				return is;
			}
		} catch (Exception e) {
			Log.i(MYTAG, "POOF! Got Error in HTTP response...");
			e.printStackTrace();
		}
		return null;
	}
}
