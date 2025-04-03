package net.i_no_am.viewmodel.config.settings;

public class ConfigSettings<T> {
    private final T value;

    public ConfigSettings(T value) {
        this.value = value;
    }

    public T getVal() {
        return value;
    }
}
