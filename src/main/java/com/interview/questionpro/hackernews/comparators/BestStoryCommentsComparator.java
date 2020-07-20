package com.interview.questionpro.hackernews.comparators;

import java.util.Comparator;

import com.interview.questionpro.hackernews.dto.ItemDto;

public class BestStoryCommentsComparator implements Comparator<ItemDto>{

	@Override
	public int compare(ItemDto o1, ItemDto o2) {
		return o2.getKids().size() - o1.getKids().size(); 
	}

}
