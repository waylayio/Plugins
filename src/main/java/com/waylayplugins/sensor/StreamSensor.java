package com.waylayplugins.sensor;

import com.ai.api.*;

import com.google.gson.JsonObject;
import com.waylayplugins.util.ImmutableLogMessage;
import com.waylayplugins.util.SensorResultBuilder;
import com.waylayplugins.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@PluginHeader(
        name = StreamSensor.NAME,
        version = "1.0.1",
        author = "Veselin",
        category = "Functions",
        iconURL = "https://raw.githubusercontent.com/waylayio/documentation/master/icons/engine_data_stream.png",
        documentationURL = "")

public class StreamSensor implements SensorPlugin {

    protected final Logger log = LoggerFactory.getLogger(StreamSensor.class);
    public static final String NAME = "inMemoryStreamSensor";
    private static final String PARAMETER = "parameter";
    private static final String THRESHOLD = "threshold";
    private final Map<String, Object> propertiesMap = new ConcurrentHashMap<>();;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public SensorResult execute(PluginExecutionRequest sessionContext) {
        log.info("execute " + NAME + ", sensor type:" + this.getClass().getName());
        List<LogMessage> logMessages = new ArrayList<>();
        Map <String, RuntimeDataValue>runtimeData = sessionContext.getNodeRuntimeData();
        Double thresholdValue, runtimeValue;
        try {
            if (runtimeData.get(getProperty(PARAMETER)) != null) {
                runtimeValue = runtimeData.get(getProperty(PARAMETER).toString()).getValue().getAsDouble();
                logMessages.add(new ImmutableLogMessage(ImmutableLogMessage.INFO, "Stream Variable <" + runtimeValue + ">"));
                if (runtimeData.get(getProperty(THRESHOLD)) != null) {
                    thresholdValue = runtimeData.get(getProperty(THRESHOLD).toString()).getValue().getAsDouble();
                    logMessages.add(new ImmutableLogMessage(ImmutableLogMessage.INFO, "Threshold found stream<" + runtimeValue + ">"));
                } else {
                    thresholdValue = Utils.getDouble(getProperty(THRESHOLD));
                    logMessages.add(new ImmutableLogMessage(ImmutableLogMessage.INFO, "Threshold in property<" + runtimeValue + ">"));
                }
                String state = "Equal";
                if (thresholdValue > runtimeValue)
                    state = "Below";
                else if (thresholdValue < runtimeValue)
                    state = "Above";
                logMessages.add(new ImmutableLogMessage(ImmutableLogMessage.INFO, "State<" + state + ">"));
                final JsonObject rawData = new JsonObject();
                rawData.addProperty(PARAMETER, runtimeValue);
                rawData.addProperty(THRESHOLD, thresholdValue);
                return SensorResultBuilder
                        .success()
                        .withObserverState(state)
                        .withRawData(rawData)
                        .withLog(logMessages)
                        .build();
            } else {
                return SensorResultBuilder.failure("Stream data not found: ").build();
            }
        } catch(Exception ex){
            return SensorResultBuilder.failure("Failure: " + ex.getMessage()).build();
        }
    }

    @Override
    public LinkedHashSet<String> getSupportedStates() {
        return new LinkedHashSet<>(Arrays.asList(new String[]{"Above", "Equal", "Below"}));
    }

    @Override
    public LinkedHashMap<String, PropertyType> getRequiredProperties() {
        LinkedHashMap<String, PropertyType> map = new LinkedHashMap<>();
        map.put(PARAMETER, new PropertyType(DataType.STRING, true, false));
        map.put(THRESHOLD, new PropertyType(DataType.DOUBLE, true, false));
        return map;
    }

    @Override
    public void setProperty(String key, Object value) {
        if(getRequiredProperties().keySet().contains(key)) {
            propertiesMap.put(key, value);
        } else {
            throw new RuntimeException("Property "+ key + " not in the required settings");
        }

    }

    @Override
    public Object getProperty(String string) {
        return propertiesMap.get(string);
    }

    @Override
    public LinkedHashMap<String, RawDataType> getProducedRawData() {
        LinkedHashMap<String, RawDataType> map = new LinkedHashMap<>();
        map.put(PARAMETER, new RawDataType(DataType.DOUBLE, "double", true, CollectedType.INSTANT));
        map.put(THRESHOLD, new RawDataType(DataType.DOUBLE, "double", true, CollectedType.INSTANT));
        return map;
    }
}


