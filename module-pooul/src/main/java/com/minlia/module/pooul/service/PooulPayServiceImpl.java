package com.minlia.module.pooul.service;

import com.auth0.jwt.interfaces.Claim;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.minlia.cloud.body.StatefulBody;
import com.minlia.cloud.body.impl.FailureResponseBody;
import com.minlia.cloud.body.impl.SuccessResponseBody;
import com.minlia.cloud.code.ApiCode;
import com.minlia.cloud.utils.ApiPreconditions;
import com.minlia.module.pooul.body.common.PooulData;
import com.minlia.module.pooul.body.jsminipg.PooulWechatJsminipgRequestBody;
import com.minlia.module.pooul.config.PooulProperties;
import com.minlia.module.pooul.contract.PooulContracts;
import com.minlia.module.pooul.util.PooulToken;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by garen on 2018/7/18.
 */
@Service
public class PooulPayServiceImpl implements PooulPayService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PooulProperties pooulProperties;

    @Override
    public StatefulBody wechatJsminipg(PooulWechatJsminipgRequestBody requestBody) {
        Map map = new BeanMap(requestBody);
        String token = PooulToken.create(map);
        String url = String.format(pooulProperties.getUrlV2Pay(), pooulProperties.getMerchantId());

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.TEXT_PLAIN);
//        headers.add("authorization","ebf960086a4aa3c0b835c94144841ac5f7afd22d");
//        HttpEntity<String> entity = new HttpEntity<String>(token, headers);
//        ResponseEntity<String> response1 = restTemplate.postForEntity(pooulProperties.getUrlV2Pay(),entity,String.class);

        HttpResponse<String> response = null;
        try {
            response = Unirest.post(url).body(token).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            ApiPreconditions.is(true, ApiCode.BASED_ON, "Pooul创建订单失败");
        }

        //如果"{"表明创建订单失败，返回错误信息
        if (response.getBody().startsWith(PooulContracts.RIGHT_PARENTHESIS)) {
            JSONObject jsonObject = JSONObject.fromObject(response.getBody());
            return FailureResponseBody.builder().code((Integer) jsonObject.get(PooulContracts.CODE)).message((String) jsonObject.get(PooulContracts.MSG)).build();
        }

        //获取返回token
        Map<String, Claim> claims = PooulToken.getClaims(response.getBody());
        if (!claims.get(PooulContracts.CODE).asInt().equals(0)) {
            ApiPreconditions.is(true, ApiCode.BASED_ON, claims.get(PooulContracts.MSG).asString());
        }
        PooulData pooulData = claims.get(PooulContracts.DATA).as(PooulData.class);
        return SuccessResponseBody.builder().payload(pooulData).build();
    }

}