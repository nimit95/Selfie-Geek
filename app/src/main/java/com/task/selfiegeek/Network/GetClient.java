package com.task.selfiegeek.Network;

import android.content.Context;

import com.kinvey.android.Client;
import com.task.selfiegeek.utils.Constants;

/**
 * Created by Nimit Agg on 21-01-2017.
 */

public class GetClient {
    private Client myClient;
    String key= Constants.key;
    String secret=Constants.secretKey;
    public Client getClient(){
        return this.myClient;
    }
    public GetClient(Context context){
        myClient = new Client.Builder(key, secret,context).build();
    }
}
