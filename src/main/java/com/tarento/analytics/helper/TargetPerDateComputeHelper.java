package com.tarento.analytics.helper;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tarento.analytics.dto.AggregateRequestDto;
import com.tarento.analytics.dto.Data;

/**
 * This implementation of Compute Helper is used to compute the difference of dates between the Request Date
 * The difference is then multiplied against the Per Day Unit of Target which has been obtained from Elastic Search
 * @author darshan
 *
 */
@Component
public class TargetPerDateComputeHelper implements ComputeHelper {
	public static final Logger logger = LoggerFactory.getLogger(TargetPerDateComputeHelper.class);
	
	@Override
	public List<Data> compute(AggregateRequestDto request, Map<String, List<Data>> dataMap) {
		return null;
	}
	@Override
	public Double compute(AggregateRequestDto request, double value) {
		return null;
	}
	

}
