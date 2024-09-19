package net.i_no_am.viewmodel.config.settings;

public class ConfigSettings<T> {
    private final T value;

    public ConfigSettings(Class<T> type, T value) {
        this.value = value;
    }

    public T getVal() {
        return value;
    }
}
