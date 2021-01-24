package com.n26.rest;

import com.n26.model.Statistics;
import com.n26.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ajay Singh Pundir
 * REST endpoints for getting statistics.
 */
@Api(value = "Statistics API")
@RestController
public class StatisticsController {

	private final StatisticsService statisticsService;

	@Autowired
	public StatisticsController( StatisticsService statisticsService ) {
		this.statisticsService = statisticsService;
	}

	@ApiOperation(value = "Calculate the Statistics")
	@GetMapping("/statistics")
	@ResponseStatus(value = HttpStatus.OK)
	public Statistics statistics() {
		return statisticsService.getStatistics();
	}
}
