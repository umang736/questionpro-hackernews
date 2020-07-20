package com.interview.questionpro.hackernews.task;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.interview.questionpro.hackernews.constant.Constants;
import com.interview.questionpro.hackernews.dto.ItemDto;

public final class FetchItemTask implements Callable<Pair<Long, ItemDto>> {
	private final Long id;
	private final RestTemplate restTemplate;
	private static final String endPointUrl = new StringBuilder(Constants.HACKER_NEWS_APIS_PREFIX).append(Constants.HACKER_NEWS_ITEM_API_PATH).append(Constants.HACKER_NEWS_APIS_SUFFIX).toString();
	private static final Logger logger = LoggerFactory.getLogger(FetchItemTask.class);

	public FetchItemTask(Long id, RestTemplate restTemplate){
		this.id = id;
		this.restTemplate = restTemplate;
	}

	@Override
	public Pair<Long, ItemDto> call() throws Exception {
		ItemDto itemDto = null;
		try {
			itemDto = this.restTemplate.getForObject(endPointUrl, ItemDto.class, id);
		} catch (RestClientException ex) {
			logger.error("getItem API failed for id ="+id, ex);
		}
		return Pair.of(id, itemDto);
	}

}