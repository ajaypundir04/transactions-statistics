package com.n26.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.n26.model.Statistics;

import java.io.IOException;

/**
 * @author Ajay Singh Pundir
 * Needed for serilaization of @{@link Statistics}
 */
public class StatisticsSerializer extends JsonSerializer<Statistics> {

    @Override
    public void serialize(Statistics statistics, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("sum", doubleToString(statistics.getSum()));
        jsonGenerator.writeStringField("avg", doubleToString(statistics.getAvg()));
        jsonGenerator.writeStringField("min", doubleToString(statistics.getMin()));
        jsonGenerator.writeStringField("max", doubleToString(statistics.getMax()));
        jsonGenerator.writeNumberField("count", statistics.getCount());
        jsonGenerator.writeEndObject();
    }

    private String doubleToString(double number) {
        double n = Math.round(number * 100) / 100.0;
        return String.format("%10.2f", n).trim();
    }
}
