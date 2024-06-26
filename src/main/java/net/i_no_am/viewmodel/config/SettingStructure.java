package net.i_no_am.viewmodel.config;

public abstract class SettingStructure<T> {
    private final T value;

    public SettingStructure(T value) {
        this.value = value;
    }

    public T getVal() {
        return value;
    }
}

class FloatSettingStructure extends SettingStructure<Float> {
    public FloatSettingStructure(Float value) {
        super(value);
    }
}

class BooleanSetting extends SettingStructure<Boolean> {
    public BooleanSetting(Boolean value) {
        super(value);
    }
}

class IntegerSettingStructure extends SettingStructure<Integer> {
    public IntegerSettingStructure(Integer value) {
        super(value);
    }
}
