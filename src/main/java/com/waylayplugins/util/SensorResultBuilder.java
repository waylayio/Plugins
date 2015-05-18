package com.waylayplugins.util;

import com.ai.api.LogMessage;
import com.ai.api.SensorResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.*;


public class SensorResultBuilder {

    private static final Gson gson = new GsonBuilder().create();

    private SensorResultBuilder(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static SensorResultBuild success(){
        return new SensorResultBuildImpl(true);
    }

    public static SensorResultBuild failure(String message){
        return new SensorResultBuildImpl(false).withErrorMessage(message);
    }

    public static interface SensorResultBuild{
        SensorResultBuild withRawData(final String rawData);

        SensorResultBuild withRawData(final JsonObject rawData);

        SensorResultBuild withObserverState(final String observerState);

        SensorResultBuild addObserverState(final Map<String, Number> state);

        SensorResultBuild withLog(final List<LogMessage> log);

        SensorResult build();
    }

    private static class SensorResultBuildImpl implements SensorResultBuild{

        private final boolean success;
        private String errorMessage;
        private List<Map<String, Number>> observerStates = new ArrayList<>();
        private List<LogMessage> log = new ArrayList<>();
        private JsonObject rawData = null;

        private SensorResultBuildImpl(final boolean success){
            this.success = success;
        }

        public SensorResultBuild withErrorMessage(final String message){
            this.errorMessage = message;
            return this;
        }

        public SensorResultBuild withRawData(final String rawData){
            this.rawData = gson.fromJson(rawData, JsonObject.class);
            return this;
        }

        public SensorResultBuild withRawData(final JsonObject rawData){
            this.rawData = rawData;
            return this;
        }

        public SensorResultBuild withObserverState(final String observerState){
            if(observerState != null) {
                Map<String, Number> states = new HashMap<>();
                states.put(observerState, 1.00);
                this.observerStates = Arrays.asList(states);
            }
            return this;
        }

        @Override
        public SensorResultBuild withLog(List<LogMessage> log) {
            this.log = log;
            return this;
        }

        public SensorResultBuild addObserverState(Map<String, Number> state){
            if(!state.isEmpty()) {
                this.observerStates.add(Collections.unmodifiableMap(state));
            }
            return this;
        }

        public SensorResult build(){
            if(success){
                return ImmutableSensorResult.success(observerStates, rawData, log);
            }else{
                return ImmutableSensorResult.failed(errorMessage, log);
            }
        }
    }

}
