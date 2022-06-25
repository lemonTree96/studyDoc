/**
 * @Title Java 注解的底层实现 - Class类中的部分源码
 */

public final class Class<T> implements java.io.Serializable, GenericDeclaration, Type, AnnotatedElement {

    /**
     * ---------------------------------------------------------------------------------------
     * annotation data that might get invalidated when JVM TI RedefineClasses() is called
     * 当前类的注解缓存数据类，用于记录当前类的注解信息，以及当前类继承的注解信息
     * 1. declaredAnnotations -> 当前类的注解信息
     * 2. annotations -> 当前类继承的注解信息
     */
    private static class AnnotationData {
        final Map<Class<? extends Annotation>, Annotation> annotations;
        final Map<Class<? extends Annotation>, Annotation> declaredAnnotations;

        // Value of classRedefinedCount when we created this AnnotationData instance
        final int redefinedCount;

        AnnotationData(Map<Class<? extends Annotation>, Annotation> annotations,
                       Map<Class<? extends Annotation>, Annotation> declaredAnnotations,
                       int redefinedCount) {
            this.annotations = annotations;
            this.declaredAnnotations = declaredAnnotations;
            this.redefinedCount = redefinedCount;
        }
    }

    /**
     * ---------------------------------------------------------------------------------------
     * (1) => 通过Class类获取注解的入口函数
     * 1. 调用annotationData()方法创建Class.AnnotationData类实例，Class.AnnotationData是一个注解缓存类，用于缓存该类的注解信息。
     * 2. 由于一个类（或方法、注解）可以有多种注解修饰，所以这里使用了一个Map存储注解类和注解对象的映射，即annotations，
     * 类型为Map<Class<? extends Annotation>, Annotation>，这里的K是注解类，V是匿名的注解代理对象。
     *
     * @throws NullPointerException {@inheritDoc}
     * @since 1.5
     */
    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        Objects.requireNonNull(annotationClass);
        //获取当前类映射的注解信息
        return (A) annotationData().annotations.get(annotationClass);
    }

    /**
     * ----------------------------------------------------------------------------------------
     * （2）=> 向 AnnotationData 中获取注解缓存，若不存在，则添加注解信息AnnotationData缓存
     */
    @SuppressWarnings("UnusedDeclaration")
    private volatile transient AnnotationData annotationData;  //transient关键字表示对应的变量不会被序列化 

    private AnnotationData annotationData() {
        while (true) { // retry loop
            AnnotationData annotationData = this.annotationData;
            int classRedefinedCount = this.classRedefinedCount;
            // 1. 判断注解缓存是否失效，没有失效，则直接返回
            if (annotationData != null &&
                    annotationData.redefinedCount == classRedefinedCount) {
                return annotationData;
            }
            // null or stale annotationData -> optimistically create new instance
            // 2. 如果注解缓存为空，则创建新的缓存
            AnnotationData newAnnotationData = createAnnotationData(classRedefinedCount);
            // try to install it
            // 3. 利用CAS算法更新注解缓存
            if (Atomic.casAnnotationData(this, annotationData, newAnnotationData)) {
                // successfully installed new AnnotationData
                return newAnnotationData;
            }
        }
    }

    /**
     * ----------------------------------------------------------------------------------------
     * （3）=> 创建注解，并添加到AnnotationData缓存信息中
     * （其本质是通过.class的注解字节码文件来获取注解的信息，并加入到缓存信息中）
     */
    private AnnotationData createAnnotationData(int classRedefinedCount) {
        // 1. 解析修饰当前类的注解信息，会返回一个LinkedHashMap (Key = 注解类型，Value = 注解)
        //  -> getRawAnnotations()  内容按一定格式组织的字节数组（俗称字节码）
        //  -> getConstantPool()    表示的是Classfile标准中的常量池 （字节码中的常量池）
        Map<Class<? extends Annotation>, Annotation> declaredAnnotations = AnnotationParser.parseAnnotations(getRawAnnotations(), getConstantPool(), this);

        // 2. 获取当前类继承父类的注解
        Class<?> superClass = getSuperclass();
        Map<Class<? extends Annotation>, Annotation> annotations = null;
        if (superClass != null) {
            // 3. 迭代搜索父类的注解信息，并加入到注解Map中
            Map<Class<? extends Annotation>, Annotation> superAnnotations = superClass.annotationData().annotations;
            for (Map.Entry<Class<? extends Annotation>, Annotation> e : superAnnotations.entrySet()) {
                Class<? extends Annotation> annotationClass = e.getKey();
                if (AnnotationType.getInstance(annotationClass).isInherited()) {
                    if (annotations == null) { // lazy construction
                        annotations = new LinkedHashMap<>((Math.max(
                                declaredAnnotations.size(),
                                Math.min(12, declaredAnnotations.size() + superAnnotations.size())
                        ) * 4 + 2) / 3
                        );
                    }
                    annotations.put(annotationClass, e.getValue());
                }
            }
        }
        // 4. 如果没有继承父类注解，则将当前类的注解信息赋值到annotations
        if (annotations == null) {
            annotations = declaredAnnotations;
        } else {
            annotations.putAll(declaredAnnotations);
        }
        // 5. 创建注解信息缓存类实例 - Class.AnnotationData
        return new AnnotationData(annotations, declaredAnnotations, classRedefinedCount);
    }