package net.mettel.newmettel;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import net.mettel.reference_class.GsonResult;
import net.mettel.reference_class.GsonSearchResponse;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class ParseJSON extends Activity {
	private static final String MYTAG = "myTag";
	private JSONObject jsonObjRecv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page2);

		Button parseButton = (Button) findViewById(R.id.parseJSON);
		parseButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				CreateHttpClient newClient = new CreateHttpClient(
						"http://172.16.18.80/api/user/roles?access_token");
				InputStream source = newClient.getis();

				Gson gson = new Gson();

				Reader reader = new InputStreamReader(source);

				GsonSearchResponse response = gson.fromJson(reader,
						GsonSearchResponse.class);

				Toast.makeText(ParseJSON.this, response.status,
						Toast.LENGTH_SHORT).show();

				// List<GsonResult> results = response.results;
				//
				// for (GsonResult result : results) {
				// Toast.makeText(ParseJSON.this, result.fromUser,
				// Toast.LENGTH_SHORT).show();
				// }
				((TextView) findViewById(R.id.status)).setText(response.status);
				((TextView) findViewById(R.id.success))
						.setText(response.success);
				((TextView) findViewById(R.id.data)).setText(response.data);
				// try {
				// resultIN.close();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
			}
		});
	}

//	private void showJSON1(InputStream source) {
//
//		Gson gson = new Gson();
//
//		Reader reader = new InputStreamReader(source);
//
//		GsonSearchResponse response = gson.fromJson(reader,
//				GsonSearchResponse.class);
//
//		Toast.makeText(this, response.query, Toast.LENGTH_SHORT).show();
//
//		List<GsonResult> results = response.results;
//
//		for (GsonResult result : results) {
//			Toast.makeText(this, result.fromUser, Toast.LENGTH_SHORT).show();
//		}
//	}
}
