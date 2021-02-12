package pisi.unitedmeows.meowlib.etc;

public enum MLibSettings {
    ASYNC_WAIT_DELAY("ASYNC_WAIT_NEXT", Long.class, 200l);

    private String name;
    private Class<?> type;
    private Object value;
    MLibSettings(String name, Class<?> type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
