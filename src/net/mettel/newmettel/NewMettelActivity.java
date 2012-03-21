package net.mettel.newmettel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.mettel.reference_class.EasySSLSocketFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewMettelActivity extends Activity {
	/** Called when the activity is first created. */
	private static final String MYTAG = "myTag";
	Button signButton;
//	String result = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView forgetPassword = (TextView) findViewById(R.id.forgot);
		String text = "<a href='https://www.onemettel.net'>Forget password?</a>";
		forgetPassword.setText(Html.fromHtml(text));
		forgetPassword.setMovementMethod(LinkMovementMethod.getInstance());

		Button signButton = (Button) findViewById(R.id.signButton);
		signButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
//				callWebService();
				Intent intent = new Intent(NewMettelActivity.this, ParseJSON.class);
				NewMettelActivity.this.startActivity(intent);
			}
		});
	}

	private void callWebService() {
		/**
		 * This piece of code is to handle both http and https connections
		 * Create new HttpClient with cm and params. It will accept connections
		 * to HTTPS hosts without verifying the SSL certificate:
		 * 
		 * To completely solve SSL problem, see
		 * http://stackoverflow.com/questions
		 * /2642777/trusting-all-certificates-using-httpclient-over-https the
		 * second answer!!!
		 */
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

		String hostURL = "http://max.onemettel.net/addons/autocomplete.asmx/HelloWorld";
		// String hostURL = "http://www.google.com";
		HttpClient client = new DefaultHttpClient(cm, params);
		// HttpClient client = new DefaultHttpClient();
		Log.i(MYTAG, "clint done");
		HttpPost request1 = new HttpPost(hostURL);
		Log.i(MYTAG, "request done");

		EditText usrname = (EditText) findViewById(R.id.inputusr);
		EditText psword = (EditText) findViewById(R.id.inputps);

		try {
			List<NameValuePair> query = new ArrayList<NameValuePair>();
			query.add(new BasicNameValuePair("username", usrname.getText()
					.toString()));
			query.add(new BasicNameValuePair("password", psword.getText()
					.toString()));
			Log.i(MYTAG, query.toString());
			request1.setEntity(new UrlEncodedFormEntity(query));
			Log.i(MYTAG, "set entity done");

			HttpResponse response1 = client.execute(request1);
			HttpEntity entity = response1.getEntity();
//			result = response1.getEntity().toString();
			InputStream resultStream = entity.getContent();
			Log.i(MYTAG, "excute done");

		} catch (Exception e) {
			Log.i(MYTAG, "POOF! Got Error...");
			e.printStackTrace();
		}
	}
	
	

}