package com.n26.rest;

import com.n26.model.Statistics;
import com.n26.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

	private final StatisticsService statisticsService;

	public StatisticsController( StatisticsService statisticsService ) {
		this.statisticsService = statisticsService;
	}

	@GetMapping("/statistics")
	public Statistics statistics() {
		return statisticsService.getStatistics();
	}
}
