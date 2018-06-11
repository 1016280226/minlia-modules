package com.minlia.module.lbsyun.service;

import com.google.gson.Gson;
import com.minlia.module.lbsyun.body.request.GeoSearchBoundRequest;
import com.minlia.module.lbsyun.body.request.GeoSearchDetailRequest;
import com.minlia.module.lbsyun.body.request.GeoSearchNearbyRequest;
import com.minlia.module.lbsyun.body.request.GeoSearchRequest;
import com.minlia.module.lbsyun.body.response.GeoSearchResponse;
import com.minlia.modules.http.GetParamter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by garen on 2018/6/9.
 */
@Service
public class GeoSearchServiceImpl implements GeoSearchService {

    @Autowired
    private RestTemplate restTemplate;

    //周边检索
    private static String nearby_url = "http://api.map.baidu.com/geosearch/v3/nearby";

    //矩形检索
    private static String bound_url = "http://api.map.baidu.com/geosearch/v3/bound";

    //本地检索
    private static String local_url = "http://api.map.baidu.com/geosearch/v3/local";

    //详情检索
    private static String detail_url = "http://api.map.baidu.com/geosearch/v3/detail/";

    @Override
    public Object nearby(GeoSearchNearbyRequest request) {
        String url = GetParamter.getUrl1(nearby_url,request);
        String response = restTemplate.getForObject(url,String.class);
        return new Gson().fromJson(response, HashMap.class);
    }

    @Override
    public Object bound(GeoSearchBoundRequest request) {
        String url = GetParamter.getUrl1(bound_url,request);
        String response = restTemplate.getForObject(url,String.class);
        return new Gson().fromJson(response, HashMap.class);
    }

    @Override
    public Object local(GeoSearchRequest request) {
        String url = GetParamter.getUrl1(local_url,request);
        String response = restTemplate.getForObject(url,String.class);
        return new Gson().fromJson(response, GeoSearchResponse.class);
    }

    @Override
    public Object detail(GeoSearchDetailRequest request) {
        String url = GetParamter.getUrl1(detail_url+request.getUid(),request);
        String response = restTemplate.getForObject(url,String.class);
        return new Gson().fromJson(response, HashMap.class);
    }

}
