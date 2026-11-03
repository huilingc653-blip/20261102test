//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.bytter.core.common.beancopy;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Label;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
public abstract class BeanCopier {
    private static final BeanCopierKey KEY_FACTORY = (BeanCopierKey) KeyFactory.create(BeanCopierKey.class);
    private static final Type CONVERTER = TypeUtils.parseType("org.springframework.cglib.core.Converter");
    private static final Type BEAN_COPIER = TypeUtils.parseType(BeanCopier.class.getName());
    private static final Signature COPY;
    private static final Signature CONVERT;
    public BeanCopier() {
    }
    public static BeanCopier create(Class source, Class target, boolean useConverter) {
        Generator gen = new Generator();
        gen.setSource(source);
        gen.setTarget(target);
        gen.setUseConverter(useConverter);
        return gen.create();
    }
    public static BeanCopier create(Class source, Class target, boolean useConverter, boolean useNoneNull) {
        Generator gen = new Generator();
        gen.setSource(source);
        gen.setTarget(target);
        gen.setUseConverter(useConverter);
        gen.setUseNoneNull(useNoneNull);
        return gen.create();
    }
    public abstract void copy(Object var1, Object var2, Converter var3);
    static {
        COPY = new Signature("copy", Type.VOID_TYPE,
                new Type[] { Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER });
        CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");
    }
    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE = new Source(BeanCopier.class.getName());
        private Class source;
        private Class target;
        // 使用转换器
        private boolean useConverter;
        // 使用非空判断
        private boolean useNoneNull;
        public Generator() {
            super(SOURCE);
        }
        public void setSource(Class source) {
            if (!Modifier.isPublic(source.getModifiers())) {
                this.setNamePrefix(source.getName());
            }
            this.source = source;
        }
        public void setTarget(Class target) {
            if (!Modifier.isPublic(target.getModifiers())) {
                this.setNamePrefix(target.getName());
            }
            this.target = target;
        }
        public void setUseNoneNull(boolean useNoneNull) {
            this.useNoneNull = useNoneNull;
        }
        public void setUseConverter(boolean useConverter) {
            this.useConverter = useConverter;
        }
        protected ClassLoader getDefaultClassLoader() {
            return this.source.getClassLoader();
        }
        protected ProtectionDomain getProtectionDomain() {
            return ReflectUtils.getProtectionDomain(this.source);
        }
        public BeanCopier create() {
            Object key = KEY_FACTORY.newInstance(source.getName(), target.getName(), useConverter, this.useNoneNull);
            return (BeanCopier) super.create(key);
        }
        public void generateClass(ClassVisitor v) {
            Type sourceType = Type.getType(source);
            Type targetType = Type.getType(target);
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(Constants.V1_7, Constants.ACC_PUBLIC, getClassName(), BEAN_COPIER, null,
                    Constants.SOURCE_FILE);
            EmitUtils.null_constructor(ce);
            CodeEmitter e = ce.begin_method(Constants.ACC_PUBLIC, COPY, null);
            PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(source);
            PropertyDescriptor[] setters = ReflectUtils.getBeanSetters(target);
            Map<Object, Object> names = new HashMap<>();
            for (int i = 0; i < getters.length; ++i) {
                names.put(getters[i].getName(), getters[i]);
            }
            Local targetLocal = e.make_local();
            Local sourceLocal = e.make_local();
            if (this.useConverter) {
                e.load_arg(1);
                e.checkcast(targetType);
                e.store_local(targetLocal);
                e.load_arg(0);
                e.checkcast(sourceType);
                e.store_local(sourceLocal);
            } else {
                e.load_arg(1);
                e.checkcast(targetType);
                e.load_arg(0);
                e.checkcast(sourceType);
            }
            for (int i = 0; i < setters.length; ++i) {
                PropertyDescriptor setter = setters[i];
                PropertyDescriptor getter = (PropertyDescriptor) names.get(setter.getName());
                if (getter != null) {
                    Class<?> returnType = getter.getReadMethod().getReturnType();
                    // 是否为基本数据类型 true是,false否
                    boolean isPrimitive = returnType.isPrimitive();
                    MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
                    MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
                    Type setterType = write.getSignature().getArgumentTypes()[0];
                    if (this.useConverter) {
                        Label start = null;
                        if (this.useNoneNull && !isPrimitive) {
                            // 将sourceLocal压入栈顶，以便在if处能获取到source对象的变量
                            e.load_local(sourceLocal);
                            // 创建标签
                            start = e.make_label();
                            // 调用getter方法，获取值
                            e.invoke(read);
                            // 进行非空判断
                            e.ifnull(start);
                        }
                        e.load_local(targetLocal);
                        e.load_arg(2);
                        e.load_local(sourceLocal);
                        e.invoke(read);
                        e.box(read.getSignature().getReturnType());
                        EmitUtils.load_class(e, setterType);
                        e.push(write.getSignature().getName());
                        e.invoke_interface(BeanCopier.CONVERTER, BeanCopier.CONVERT);
                        e.unbox_or_zero(setterType);
                        e.invoke(write);
                        if (start != null) {
                            // 结束
                            e.mark(start);
                        }
                    } else if (compatible(getter, setter)) {
                        // 如果是非空复制
                        Label start = null;
                        if (this.useNoneNull && !isPrimitive) {
                            // 复制栈顶并进行压栈
                            e.dup();
                            // 创建标签
                            start = e.make_label();
                            // 调用getter方法，获取值
                            e.invoke(read);
                            // 进行非空判断
                            e.ifnull(start);
                        }
                        e.dup2();
                        e.invoke(read);
                        e.invoke(write);
                        if (start != null) {
                            // 结束
                            e.mark(start);
                        }
                    }
                }
            }
            e.return_value();
            e.end_method();
            ce.end_class();
        }
        private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter) {
            return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
        }
        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type);
        }
        protected Object nextInstance(Object instance) {
            return instance;
        }
    }
    interface BeanCopierKey {
        Object newInstance(String var1, String var2, boolean var3, boolean useNoneNull);
    }
}
