package testbw.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SetupStaticDBServiceAsync {

	void setupStaticDB(String url, AsyncCallback<String> callback);

}
