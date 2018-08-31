package com.redspark.nycl.persistence;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by simonpark on 03/10/2016.
 */
public class Util {

    public static  <T>  String toJsonString(T object) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.ANY);
        return mapper.writeValueAsString(object);
    }
}
