package com.DW2.InnovaMedic.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {
    public Gson gson(){
        return new Gson();
    }
}
