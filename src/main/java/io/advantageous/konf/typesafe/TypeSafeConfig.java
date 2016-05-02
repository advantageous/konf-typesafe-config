package io.advantageous.konf.typesafe;
import com.typesafe.config.ConfigFactory;
import io.advantageous.boon.core.reflection.Mapper;
import io.advantageous.boon.core.reflection.MapperSimple;
import io.advantageous.config.Config;
import io.advantageous.config.ConfigMemorySize;
import io.advantageous.config.MemorySizeUnit;

import java.net.URI;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class TypeSafeConfig implements Config {

    private final com.typesafe.config.Config config;
    private final Mapper mapper = new MapperSimple();

    public static Config fromTypeSafeConfig(com.typesafe.config.Config config) {
        return new TypeSafeConfig(config);
    }

    public static Config typeSafeConfig() {
        return fromTypeSafeConfig(ConfigFactory.load());
    }

    public TypeSafeConfig(com.typesafe.config.Config config) {
        this.config = config;
    }

    @Override
    public ConfigMemorySize getMemorySize(String path) {
        final com.typesafe.config.ConfigMemorySize memorySize = config.getMemorySize(path);
        return new ConfigMemorySize(MemorySizeUnit.BYTES, memorySize.toBytes());
    }

    @Override
    public List<ConfigMemorySize> getMemorySizeList(String path) {
        return config.getMemorySizeList(path).stream().map(memSize ->
                new ConfigMemorySize(MemorySizeUnit.BYTES, memSize.toBytes())).collect(Collectors.toList());
    }

    @Override
    public URI getUri(String path) {
        return URI.create(config.getString(path));
    }

    @Override
    public List<URI> getUriList(String path) {
        return config.getStringList(path).stream().map(URI::create).collect(Collectors.toList());
    }

    @Override
    public String getString(String path) {
        return config.getString(path);
    }

    @Override
    public boolean hasPath(String path) {
        return config.hasPath(path);
    }

    @Override
    public int getInt(String path) {
        return config.getInt(path);
    }

    @Override
    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        return config.getBooleanList(path);
    }

    @Override
    public float getFloat(String path) {
        return (float) config.getDouble(path);
    }

    @Override
    public double getDouble(String path) {
        return config.getDouble(path);
    }

    @Override
    public long getLong(String path) {
        return config.getLong(path);
    }

    @Override
    public Duration getDuration(String path) {
        return config.getDuration(path);
    }

    @Override
    public List<Duration> getDurationList(String path) {
        return config.getDurationList(path);
    }

    @Override
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    @Override
    public List<Integer> getIntList(String path) {
        return config.getIntList(path);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return config.getDoubleList(path);
    }

    @Override
    public List<Float> getFloatList(String path) {
        return config.getDoubleList(path).stream().map(Double::floatValue).collect(Collectors.toList());
    }

    @Override
    public List<Long> getLongList(String path) {
        return config.getLongList(path);
    }

    @Override
    public Map<String, Object> getMap(String path) {
        return configToMap(config.getConfig(path));
    }

    @Override
    public Config getConfig(String path) {
        return new TypeSafeConfig(config.getConfig(path));
    }

    @Override
    public List<Config> getConfigList(String path) {
        return config.getConfigList(path).stream().map(TypeSafeConfig::new).collect(Collectors.toList());
    }

    @Override
    public <T> T get(String path, Class<T> type) {
        final Map<String, Object> map = getMap(path);
        return mapper.fromMap(map, type);
    }

    @Override
    public <T> List<T> getList(String path, Class<T> componentType) {
        return config.getConfigList(path).stream().map(this::configToMap)
                .map(map -> mapper.fromMap(map, componentType)).collect(Collectors.toList());
    }

    private Map<String, Object> configToMap(final com.typesafe.config.Config config) {
        return new AbstractMap<String, Object>() {
            @Override
            public Object get(Object key) {

                return config.getValue(key.toString()).unwrapped();
            }

            @Override
            public Set<Entry<String, Object>> entrySet() {

                return config.entrySet().stream().map(stringConfigValueEntry -> new Entry<String, Object>() {
                    @Override
                    public String getKey() {
                        return stringConfigValueEntry.getKey();
                    }

                    @Override
                    public Object getValue() {
                        return stringConfigValueEntry.getValue().unwrapped();
                    }

                    @Override
                    public Object setValue(Object value) {
                        return null;
                    }
                }).collect(Collectors.toSet());
            }

            @Override
            public boolean containsKey(Object key) {
                return config.hasPath(key.toString());
            }
        };
    }
}
