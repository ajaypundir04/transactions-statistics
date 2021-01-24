package com.n26.model;

import com.google.code.beanmatchers.BeanMatchers;
import org.apache.commons.lang3.RandomUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class ModelTest {

    @Test
    public void testModel() {
        Assert.assertThat(Statistics.class,
                Matchers.allOf(
                        BeanMatchers.hasValidBeanConstructor(),
                        BeanMatchers.hasValidBeanEquals(),
                        BeanMatchers.hasValidGettersAndSetters(),
                        BeanMatchers.hasValidBeanHashCode(),
                        BeanMatchers.hasValidBeanToString())
        );
        BeanMatchers.registerValueGenerator(() -> LocalDateTime.now().minusDays(RandomUtils.nextInt()), LocalDateTime.class);
        Assert.assertThat(Transaction.class,
                Matchers.allOf(
                        BeanMatchers.hasValidBeanConstructor(),
                        BeanMatchers.hasValidBeanEquals(),
                        BeanMatchers.hasValidGettersAndSetters(),
                        BeanMatchers.hasValidBeanHashCode(),
                        BeanMatchers.hasValidBeanToString())
        );
    }

}
