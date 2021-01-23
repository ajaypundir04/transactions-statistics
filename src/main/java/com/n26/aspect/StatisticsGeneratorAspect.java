package com.n26.aspect;

import com.n26.model.Transaction;
import com.n26.service.StatisticsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class StatisticsGeneratorAspect {

	@Autowired private StatisticsService statisticsService;
	
	@Around("classAnnotated()")
	public Object keepStatistics(ProceedingJoinPoint pjp) throws Throwable{
		
		Object result = pjp.proceed();
		String methodName = pjp.getSignature().getName();
		
		if ("createTransaction".equals(methodName)) {
			statisticsService.register((Transaction) pjp.getArgs()[0]);
		} else if ("removeTransactions".equals(methodName)) {
			statisticsService.clearStatistics();
		}
		
		return result;
		
	}
	
	@Pointcut("within(@com.n26.aspect.StatisticsGenerator *)")
	public void classAnnotated() {}
	
}
