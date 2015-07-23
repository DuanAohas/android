package com.aohas.library.koushikdutta.async.http.socketio;

import org.json.JSONObject;

public interface JSONCallback {
    public void onJSON(JSONObject json, Acknowledge acknowledge);
}
    
