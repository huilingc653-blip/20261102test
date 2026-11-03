package com.bytter.core.common.beancopy;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Label;
import org.springframework.asm.Type;
import org.springframework.cglib.beans.FixedKeySet;
import org.springframework.cglib.core.*;
import org.springframework.util.StringUtils;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
class BeanMapEmitter extends ClassEmitter {
    private static final Type BEAN_MAP = TypeUtils.parseType(BeanMap.class.getName());
    private static final Type FIXED_KEY_SET = TypeUtils.parseType(FixedKeySet.class.getName());
    private static final Signature CSTRUCT_OBJECT = TypeUtils.parseConstructor("Object");
    private static final Signature CSTRUCT_STRING_ARRAY = TypeUtils.parseConstructor("String[]");
    private static final Signature BEAN_MAP_GET = TypeUtils.parseSignature("Object get(Object, Object)");
    private static final Signature BEAN_MAP_PUT = TypeUtils.parseSignature("Object put(Object, Object, Object)");
    private static final Signature KEY_SET = TypeUtils.parseSignature("java.util.Set keySet()");
    private static final Signature NEW_INSTANCE = new Signature("newInstance", BEAN_MAP,
            new Type[] { Constants.TYPE_OBJECT });
    private static final Signature GET_PROPERTY_TYPE = TypeUtils.parseSignature("Class getPropertyType(String)");
    private static final Signature CAST_DATA_TYPE = TypeUtils.parseSignature("Object cast(Object,Class)");
    private static final Signature TO_LOWER_CASE = TypeUtils.parseSignature("String toLowerCase()");
    private static final Type TYPE_BASIC_DATA_CAST = TypeUtils.parseType(BasicDataCast.class.getName());
    private final boolean ignoreKeyCase;
    public BeanMapEmitter(ClassVisitor v, String className, Class type, int require, boolean ignoreKeyCase) {
        super(v);
        this.ignoreKeyCase = ignoreKeyCase;
        begin_class(Constants.V1_7, Constants.ACC_PUBLIC, className, BEAN_MAP, null, Constants.SOURCE_FILE);
        EmitUtils.null_constructor(this);
        EmitUtils.factory_method(this, NEW_INSTANCE);
        generateConstructor();
        Map getters = makePropertyMap(ReflectUtils.getBeanGetters(type));
        Map setters = makePropertyMap(ReflectUtils.getBeanSetters(type));
        Map<Object, Object> allProps = new HashMap<>();
        allProps.putAll(getters);
        allProps.putAll(setters);
        if (require != 0) {
            for (Iterator it = allProps.keySet().iterator(); it.hasNext();) {
                String name = (String) it.next();
                if ((((require & BeanMap.REQUIRE_GETTER) != 0) && !getters.containsKey(name))
                        || (((require & BeanMap.REQUIRE_SETTER) != 0) && !setters.containsKey(name))) {
                    it.remove();
                    getters.remove(name);
                    setters.remove(name);
                }
            }
        }
        generateGet(type, getters);
        generatePut(type, setters);
        String[] allNames = getNames(allProps);
        generateKeySet(allNames);
        generateGetPropertyType(allProps, allNames);
        end_class();
    }
    private String lowerCaseName(String name) {
        return name.toLowerCase(Locale.US);
    }
    private String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(lowerCaseName(name.substring(0, 1)));
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = lowerCaseName(s);
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }
    private Map makePropertyMap(PropertyDescriptor[] props) {
        Map<Object, Object> names = new HashMap<>();
        for (PropertyDescriptor pd : props) {
            if (ignoreKeyCase) {
                names.put(lowerCaseName(pd.getName()), pd);
                String underscoredName = underscoreName(pd.getName());
                if (!lowerCaseName(pd.getName()).equals(underscoredName)) {
                    names.put(underscoredName, pd);
                }
            } else {
                names.put(pd.getName(), pd);
            }
        }
        return names;
    }
    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    private String[] getNames(Map propertyMap) {
        return (String[]) propertyMap.keySet().toArray(new String[propertyMap.size()]);
    }
    private void generateConstructor() {
        CodeEmitter e = begin_method(Constants.ACC_PUBLIC, CSTRUCT_OBJECT, null);
        e.load_this();
        e.load_arg(0);
        e.super_invoke_constructor(CSTRUCT_OBJECT);
        e.return_value();
        e.end_method();
    }
    private void generateGet(Class type, final Map getters) {
        final CodeEmitter e = begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null);
        e.load_arg(0);
        e.checkcast(Type.getType(type));
        e.load_arg(1);
        e.checkcast(Constants.TYPE_STRING);
        EmitUtils.string_switch(e, getNames(getters), Constants.SWITCH_STYLE_HASH, new ObjectSwitchCallback() {
            public void processCase(Object key, Label end) {
                PropertyDescriptor pd = (PropertyDescriptor) getters.get(key);
                MethodInfo method = ReflectUtils.getMethodInfo(pd.getReadMethod());
                e.invoke(method);
                e.box(method.getSignature().getReturnType());
                e.return_value();
            }
            public void processDefault() {
                e.aconst_null();
                e.return_value();
            }
        });
        e.end_method();
    }
    private void generatePut(Class type, final Map setters) {
        final CodeEmitter e = begin_method(Constants.ACC_PUBLIC, BEAN_MAP_PUT, null);
        e.load_arg(0);
        e.checkcast(Type.getType(type));
        e.load_arg(1);
        e.checkcast(Constants.TYPE_STRING);
        // 是否忽略大小写
        if (ignoreKeyCase) {
            e.invoke_virtual(Constants.TYPE_STRING, TO_LOWER_CASE);
        }
        EmitUtils.string_switch(e, getNames(setters), Constants.SWITCH_STYLE_HASH, new ObjectSwitchCallback() {
            public void processCase(Object key, Label end) {
                PropertyDescriptor pd = (PropertyDescriptor) setters.get(key);
                if (pd.getReadMethod() == null) {
                    e.aconst_null();
                } else {
                    MethodInfo read = ReflectUtils.getMethodInfo(pd.getReadMethod());
                    e.dup();
                    e.invoke(read);
                    e.box(read.getSignature().getReturnType());
                }
                e.swap(); // move old value behind bean
                e.load_arg(2);
                MethodInfo write = ReflectUtils.getMethodInfo(pd.getWriteMethod());
                Type setterType = write.getSignature().getArgumentTypes()[0];
                EmitUtils.load_class(e, setterType);
                e.invoke_static(TYPE_BASIC_DATA_CAST, CAST_DATA_TYPE);
                e.box(Constants.TYPE_OBJECT);
                e.unbox(write.getSignature().getArgumentTypes()[0]);
                e.invoke(write);
                e.return_value();
            }
            public void processDefault() {
                // fall-through
            }
        });
        e.aconst_null();
        e.return_value();
        e.end_method();
    }
    private void generateKeySet(String[] allNames) {
        // static initializer
        declare_field(Constants.ACC_STATIC | Constants.ACC_PRIVATE, "keys", FIXED_KEY_SET, null);
        CodeEmitter e = begin_static();
        e.new_instance(FIXED_KEY_SET);
        e.dup();
        EmitUtils.push_array(e, allNames);
        e.invoke_constructor(FIXED_KEY_SET, CSTRUCT_STRING_ARRAY);
        e.putfield("keys");
        e.return_value();
        e.end_method();
        // keySet
        e = begin_method(Constants.ACC_PUBLIC, KEY_SET, null);
        e.load_this();
        e.getfield("keys");
        e.return_value();
        e.end_method();
    }
    private void generateGetPropertyType(final Map allProps, String[] allNames) {
        final CodeEmitter e = begin_method(Constants.ACC_PUBLIC, GET_PROPERTY_TYPE, null);
        e.load_arg(0);
        EmitUtils.string_switch(e, allNames, Constants.SWITCH_STYLE_HASH, new ObjectSwitchCallback() {
            public void processCase(Object key, Label end) {
                PropertyDescriptor pd = (PropertyDescriptor) allProps.get(key);
                EmitUtils.load_class(e, Type.getType(pd.getPropertyType()));
                e.return_value();
            }
            public void processDefault() {
                e.aconst_null();
                e.return_value();
            }
        });
        e.end_method();
    }
}
