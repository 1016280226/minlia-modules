package com.minlia.module.map.place.service;

import com.minlia.module.map.place.body.request.GadPlaceAroundRequestBody;
import com.minlia.module.map.place.body.response.GadPlaceAroundResponseBody;

/**
 * Created by garen on 2018/4/25.
 */
public interface GadPlaceService {

    /**
     * 周边搜索
     * @param body
     * @return
     */
    GadPlaceAroundResponseBody around(GadPlaceAroundRequestBody body);

}
