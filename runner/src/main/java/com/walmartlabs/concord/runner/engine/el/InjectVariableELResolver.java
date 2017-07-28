package com.walmartlabs.concord.runner.engine.el;

import com.walmartlabs.concord.common.InjectVariable;

import javax.el.BeanELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.MethodNotFoundException;
import java.beans.FeatureDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InjectVariableELResolver extends ELResolver {

    private final BeanELResolver delegate = new BeanELResolver();

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        return null;
    }

    @Override
    public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] paramValues) {
        if (base == null || method == null) {
            return null;
        }

        List<Method> methods = findMethodWithInjections(base.getClass(), method.toString());

        if (paramTypes == null) {
            paramTypes = getTypesFromValues(paramValues);
        }

        for(Method m : methods) {
            Class<?>[] newParamTypes = processParamTypes(m.getParameters(), paramTypes);
            if(newParamTypes == null) {
                continue;
            }
            Object[] newParams = processParams(context, m.getParameters(), paramValues);
            if(newParams == null) {
                continue;
            }

            try {
                return delegate.invoke(context, base, method, newParamTypes, newParams);
            } catch (MethodNotFoundException e) {
                // ignore
            }
        }

        return null;
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return Object.class;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return true;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return Object.class;
    }

    private static Class<?>[] getTypesFromValues(Object[] values) {
        if (values == null) {
            return null;
        }

        Class<?> result[] = new Class<?>[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                result[i] = null;
            } else {
                result[i] = values[i].getClass();
            }
        }
        return result;
    }

    private static Class<?>[] processParamTypes(Parameter[] methodParams, Class<?>[] originalParamTypes) {
        Class<?>[] result = new Class[methodParams.length];
        int originalParamTypesLength = originalParamTypes != null ? originalParamTypes.length : 0;
        int paramIndex = 0;
        for(int i = 0; i < methodParams.length; i++) {
            Parameter mp = methodParams[i];
            if(hasInjectedVariable(mp)) {
                result[i] = mp.getType();
            } else {
                if(paramIndex >= originalParamTypesLength) {
                    return null;
                }
                result[i] = originalParamTypes[paramIndex];
                paramIndex++;
            }
        }

        if(paramIndex < originalParamTypesLength) {
            return null;
        }

        return result;
    }

    private static Object[] processParams(ELContext context, Parameter[] methodParams, Object[] originalParams) {
        Object[] result = new Object[methodParams.length];
        int paramIndex = 0;
        int originalParamsLength = originalParams != null ? originalParams.length : 0;
        for(int i = 0; i < methodParams.length; i++) {
            Parameter mp = methodParams[i];
            if(hasInjectedVariable(mp)) {
                result[i] = ResolverUtils.getVariable(context, getInjectedVariableName(mp));
            } else {
                if(paramIndex >= originalParamsLength) {
                    return null;
                }
                result[i] = originalParams[paramIndex];
                paramIndex++;
            }
        }

        if(paramIndex < originalParamsLength) {
            return null;
        }

        return result;
    }

    private static String getInjectedVariableName(Parameter p) {
        return p.getAnnotation(InjectVariable.class).value();
    }

    private static List<Method> findMethodWithInjections(Class<?> type, String name) {
        List<Method> candidates = Arrays.stream(type.getMethods())
                .filter(m -> m.getName().equals(name))
                .collect(Collectors.toList());

        return candidates.stream()
                .map(m -> findMethodWithInjections(type, m))
                .filter(Objects::nonNull)
                .sorted((o1, o2) -> {
                        int result = -Integer.compare(getInjectedParamCount(o1), getInjectedParamCount(o2));
                        if(result == 0) {
                            return -Integer.compare(o1.getParameterCount(), o2.getParameterCount());
                        }
                        return result;
                })
                .collect(Collectors.toList());
    }

    private static Method findMethodWithInjections(Class<?> type, Method m) {
        if(getInjectedParamCount(m) > 0) {
            return m;
        }

        Class<?>[] inf = type.getInterfaces();
        Method mp;
        for (int i = 0; i < inf.length; i++) {
            try {
                mp = inf[i].getMethod(m.getName(), m.getParameterTypes());
                mp = findMethodWithInjections(mp.getDeclaringClass(), mp);
                if (mp != null) {
                    return mp;
                }
            } catch (NoSuchMethodException e) {
                // Ignore
            }
        }
        Class<?> sup = type.getSuperclass();
        if (sup != null) {
            try {
                mp = sup.getMethod(m.getName(), m.getParameterTypes());
                mp = findMethodWithInjections(mp.getDeclaringClass(), mp);
                if (mp != null) {
                    return mp;
                }
            } catch (NoSuchMethodException e) {
                // Ignore
            }
        }
        return null;
    }

    private static int getInjectedParamCount(Method m) {
        Parameter[] params = m.getParameters();
        if(params == null) {
            return 0;
        }

        int count = 0;
        for(Parameter p : params) {
            if(hasInjectedVariable(p)) {
                count++;
            }
        }
        return count;
    }

    private static boolean hasInjectedVariable(AnnotatedElement element) {
        return element.isAnnotationPresent(InjectVariable.class);
    }
}
