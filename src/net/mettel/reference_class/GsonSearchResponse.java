package net.mettel.reference_class;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GsonSearchResponse {
    
    @SerializedName("status")
    public String status;
    
    @SerializedName("success")
    public String success;
    
    public String data;
    
    
}