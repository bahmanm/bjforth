package bjforth.primitives.lib;

import bjforth.machine.MachineException;

import java.util.HashMap;

public class ClassCache {

    private static HashMap<String, Class<?>> cache = new HashMap<>();

    /**
     * Tries to load the class from cache if exists.
     *
     * <p>If the type name contains '.', tries to look for the type name verbatim.
     *
     * <p>Otherwise looks in 'java.lang', 'java.util' and 'java.io' in order.
     *
     * <p>Throws if the type cannot be found.
     *
     * @throws MachineException
     */
    public static Class<?> forName(String typeName) {
        if (typeName.contains(".")) {
            try {
                if (cache.containsKey(typeName)) {
                    return cache.get(typeName);
                } else {
                    var clazz = Class.forName(typeName);
                    cache.put(typeName, clazz);
                    return clazz;
                }
            } catch (ClassNotFoundException e) {
                throw new MachineException(e.getMessage());
            }
        } else {
            try {
                var type = "java.lang.%s".formatted(typeName);
                if (cache.containsKey(type)) {
                    return cache.get(type);
                } else {
                    var clazz = Class.forName(type);
                    cache.put(type, clazz);
                    return clazz;
                }
            } catch (ClassNotFoundException _e) {
                try {
                    var type = "java.util.%s".formatted(typeName);
                    if (cache.containsKey(type)) {
                        return cache.get(type);
                    } else {
                        var clazz = Class.forName(type);
                        cache.put(type, clazz);
                        return clazz;
                    }
                } catch (ClassNotFoundException _ex) {
                    try {
                        var type = "java.io.%s".formatted(typeName);
                        if (cache.containsKey(type)) {
                            return cache.get(type);
                        } else {
                            var clazz = Class.forName(type);
                            cache.put(type, clazz);
                            return clazz;
                        }
                    } catch (ClassNotFoundException e) {
                        throw new MachineException(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Tries to load the variadic class from cache if exists.
     *
     * <p>If the type name contains '.', tries to look for the type name verbatim.
     *
     * <p>Otherwise looks in 'java.lang', 'java.util' and 'java.io' in order.
     *
     * <p>Throws if the type cannot be found.
     *
     * @throws MachineException
     */    
    public static Class<?> forNameVararg(String typeName) {
        if (typeName.contains(".")) {
            try {
                var typeNameVararg = "[L%s;".formatted(typeName);
                if (cache.containsKey(typeNameVararg)) {
                    return cache.get(typeNameVararg);
                } else {
                    var clazz = Class.forName(typeNameVararg);
                    cache.put(typeNameVararg, clazz);
                    return clazz;
                }
            } catch (ClassNotFoundException e) {
                throw new MachineException(e.getMessage());
            }
        } else {
            try {
                var typeNameVararg = "[Ljava.lang.%s;".formatted(typeName);
                if (cache.containsKey(typeNameVararg)) {
                    return cache.get(typeNameVararg);
                } else {
                    var clazz = Class.forName(typeNameVararg);
                    cache.put(typeNameVararg, clazz);
                    return clazz;
                }
            } catch (ClassNotFoundException _e) {
                try {
                    var typeNameVararg = "[Ljava.util.%s;".formatted(typeName);
                    if (cache.containsKey(typeNameVararg)) {
                        return cache.get(typeNameVararg);
                    } else {
                        var clazz = Class.forName(typeNameVararg);
                        cache.put(typeNameVararg, clazz);
                        return clazz;
                    }
                } catch (ClassNotFoundException _ex) {
                    try {
                        var typeNameVararg = "[Ljava.io.%s;".formatted(typeName);
                        if (cache.containsKey(typeNameVararg)) {
                            return cache.get(typeNameVararg);
                        } else {
                            var clazz = Class.forName(typeNameVararg);
                            cache.put(typeNameVararg, clazz);
                            return clazz;
                        }
                    } catch (ClassNotFoundException e) {
                        throw new MachineException(e.getMessage());
                    }
                }
            }
        }
    }
}
