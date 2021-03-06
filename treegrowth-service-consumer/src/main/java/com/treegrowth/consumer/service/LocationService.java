package com.treegrowth.consumer.service;


import com.treegrowth.model.bo.LocationAnalysisResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient("service-provider")
@RequestMapping("service/location")
public interface LocationService {

    @RequestMapping(method = GET)
    LocationAnalysisResponse analysisAddress(@RequestParam("ip") String ip);
}
