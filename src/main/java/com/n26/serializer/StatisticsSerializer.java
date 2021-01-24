package com.n26.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.n26.model.Statistics;

import java.io.IOException;

public class StatisticsSerializer extends JsonSerializer< Statistics > {

    @Override
    public void serialize( Statistics statistics, JsonGenerator jgen, SerializerProvider serializerProvider ) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField( "sum", doubleToString( statistics.getSum() ) );
        jgen.writeStringField( "avg", doubleToString( statistics.getAvg() ) );
        jgen.writeStringField( "min", doubleToString( statistics.getMin() ) );
        jgen.writeStringField( "max", doubleToString( statistics.getMax() ) );
        jgen.writeNumberField( "count", statistics.getCount() );
        jgen.writeEndObject();
    }

    private String doubleToString( double number ) {
        double n = Math.round( number * 100 ) / 100.0;
        return String.format( "%10.2f", n ).trim();
    }
}
