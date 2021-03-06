package com.github.phantomthief.scope;

import static com.github.phantomthief.scope.Scope.getCurrentScope;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

/**
 * 强类型数据读写的封装
 *
 * @author w.vela
 */
public final class ScopeKey<T> {

    private final T defaultValue;
    private final Supplier<T> initializer;

    private ScopeKey(T defaultValue, Supplier<T> initializer) {
        this.defaultValue = defaultValue;
        this.initializer = initializer;
    }

    @Nonnull
    public static <T> ScopeKey<T> allocate() {
        return withDefaultValue0(null);
    }

    @Nonnull
    private static <T> ScopeKey<T> withDefaultValue0(T defaultValue) {
        return new ScopeKey<>(defaultValue, null);
    }

    @Nonnull
    public static ScopeKey<Boolean> withDefaultValue(boolean defaultValue) {
        return withDefaultValue0(defaultValue);
    }

    @Nonnull
    public static ScopeKey<Integer> withDefaultValue(int defaultValue) {
        return withDefaultValue0(defaultValue);
    }

    @Nonnull
    public static ScopeKey<Long> withDefaultValue(long defaultValue) {
        return withDefaultValue0(defaultValue);
    }

    @Nonnull
    public static ScopeKey<Double> withDefaultValue(double defaultValue) {
        return withDefaultValue0(defaultValue);
    }

    @Nonnull
    public static ScopeKey<String> withDefaultValue(String defaultValue) {
        return withDefaultValue0(defaultValue);
    }

    @Nonnull
    public static <T extends Enum<T>> ScopeKey<T> withDefaultValue(T defaultValue) {
        return withDefaultValue0(defaultValue);
    }

    /**
     * @param initializer 初始化ScopeKey（仅在Scope有效时）
     *
     * 等效代码:（调用 {@link #get} 时）
     * <pre> {@code
     * T obj = SCOPE_KEY.get();
     * if (obj == null) {
     *     obj = initializer.get();
     *     SCOPE_KEY.set(obj);
     * }
     * return obj;
     * }</pre>
     */
    @Nonnull
    public static <T> ScopeKey<T> withInitializer(Supplier<T> initializer) {
        return new ScopeKey<>(null, initializer);
    }

    public T get() {
        Scope currentScope = getCurrentScope();
        if (currentScope == null) {
            return defaultValue();
        }
        return currentScope.get(this);
    }

    Supplier<T> initializer() {
        return initializer;
    }

    T defaultValue() {
        return defaultValue;
    }

    /**
     * @return {@code true} if in a scope and set success.
     */
    public boolean set(T value) {
        Scope currentScope = getCurrentScope();
        if (currentScope != null) {
            currentScope.set(this, value);
            return true;
        } else {
            return false;
        }
    }
}