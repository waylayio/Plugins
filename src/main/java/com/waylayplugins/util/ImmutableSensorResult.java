package com.waylayplugins.util;

import com.ai.api.LogMessage;
import com.ai.api.SensorResult;
import com.google.gson.JsonObject;

import java.util.*;

public class ImmutableSensorResult implements SensorResult {

    private final boolean success;
    private final Optional<String> errorMessage;
    private final List<Map<String, Number>> states;
    private final JsonObject rawData;
    private final List<LogMessage> log;

    public static ImmutableSensorResult success(String state, JsonObject rawData, List<LogMessage> log){
        Map<String, Number> states = new HashMap<>();
        if(state != null) {
            states.put(state, 1.00);
        }
        return success(Collections.unmodifiableList(Collections.singletonList(states)), rawData, log);
    }

    public static ImmutableSensorResult success(List<Map<String, Number>> states, JsonObject rawData, List<LogMessage> log){
        return new ImmutableSensorResult(
                true,
                Optional.empty(),
                Collections.unmodifiableList(Objects.requireNonNull(states)),
                rawData,
                Collections.unmodifiableList(Objects.requireNonNull(log)));
    }

    public static ImmutableSensorResult success(String state){
        return success(state, new JsonObject(), Collections.emptyList());
    }

    public static ImmutableSensorResult success(String state, JsonObject rawData){
        return success(state, rawData, Collections.emptyList());
    }

    public static ImmutableSensorResult success(List<Map<String, Number>> states, JsonObject rawData){
        return success(states, rawData, Collections.emptyList());
    }

    public static ImmutableSensorResult failed(String errorMessage, List<LogMessage> log){
        return new ImmutableSensorResult(
                false,
                Optional.of(errorMessage),
                Collections.emptyList(),
                new JsonObject(),
                Collections.unmodifiableList(Objects.requireNonNull(log)));
    }

    public static ImmutableSensorResult failed(String errorMessage){
        return failed(errorMessage, Collections.emptyList());
    }

    private ImmutableSensorResult(boolean success, Optional<String> errorMessage, List<Map<String, Number>> states, JsonObject rawData, List<LogMessage> log) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.states = states;
        this.rawData = rawData;
        this.log = log;
    }

    @Override
    public Optional<String> errorMessage() {
        return errorMessage;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public List<Map<String, Number>> getObserverStates() {
        return states;
    }

    @Override
    public JsonObject getRawData() {
        return rawData;
    }

    @Override
    public List<LogMessage> getLog() {
        return log;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " -> " + errorMessage.orElse(states.toString());
    }
}
