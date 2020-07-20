package com.interview.questionpro.hackernews.task;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.interview.questionpro.hackernews.constant.Constants;
import com.interview.questionpro.hackernews.dto.UserDto;

public final class FetchUserTask implements Callable<Pair<String, UserDto>> {
	private final String id;
	private final RestTemplate restTemplate;
	private static final String endPointUrl = new StringBuilder(Constants.HACKER_NEWS_APIS_PREFIX).append(Constants.HACKER_NEWS_USER_API_PATH).append(Constants.HACKER_NEWS_APIS_SUFFIX).toString();
	private static final Logger logger = LoggerFactory.getLogger(FetchUserTask.class);

	public FetchUserTask(String id, RestTemplate restTemplate){
		this.id = id;
		this.restTemplate = restTemplate;
	}

	@Override
	public Pair<String, UserDto> call() throws Exception {
		UserDto userDto = null;
		try {
			userDto = this.restTemplate.getForObject(endPointUrl, UserDto.class, id);
		} catch (RestClientException ex) {
			logger.error("getUser API failed for id ="+id, ex);
		}
		return Pair.of(id, userDto);
	}

}