package com.swjtu.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TransApi
 * @Description TODO
 */
public class TransApi {

    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private String appid;
    private String securityKey;

    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public String getTransResult(String query, String from, String to) {
        Map<String, String> params = buildParams(query, from, to);
        return HttpGet.get(TRANS_API_HOST, params);
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // random number
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // signature
        String src = appid + query + salt + securityKey; // Original text before encryption
        params.put("sign", MD5.md5(src));

        return params;
    }

}
