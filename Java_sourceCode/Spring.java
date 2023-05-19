public void refresh() throws BeansException, IllegalStateException {
    synchronized(this.startupShutdownMonitor) {
        StartupStep contextRefresh = this.applicationStartup.start("spring.context.refresh");

        // Prepare this context for refreshing.
		//	准备工作包括设置启动时间，是否激活标识位，初始化属性源(property source)配置
        this.prepareRefresh();

        //返回一个factory 为什么需要返回一个工厂，因为要对工厂进行初始化
        ConfigurableListableBeanFactory beanFactory = this.obtainFreshBeanFactory();
        //准备工厂
        this.prepareBeanFactory(beanFactory);

        try {
            //这个方法在当前版本的spring是没用任何代码的，可能spring期待在后面的版本中去扩展吧
            this.postProcessBeanFactory(beanFactory);
            StartupStep beanPostProcess = this.applicationStartup.start("spring.context.beans.post-process");

            //在spring的环境中去执行已经被注册的 factory processors，设置执行自定义的ProcessBeanFactory 和spring内部自己定义的
            //1. 会执行 BeanFactoryPostProcessors 后置处理器及其子接口 BeanDefinitionRegistryPostProcessor，执行顺序：
            //    ① 先是执行 BeanDefinitionRegistryPostProcessor 接口的 postProcessBeanDefinitionRegistry() 方法 => 【BeanDefinition】
            //       该步骤会扫描到指定包下面的标有注解的类，然后将其变成BeanDefinition对象，然后放到一个Spring中的Map中，用于下面创建Spring bean的时候使用这个BeanDefinition。
            //       其中registerBeanPostProcessors方法根据实现了PriorityOrdered、Ordered接口，排序后注册所有的BeanPostProcessor后置处理器，主要用于Spring Bean创建时，执行这些后置处理器的方法，这也是Spring中提供的扩展点，让我们能够插手Spring bean创建过程。
            //
            //    ② 然后执行 BeanFactoryPostProcessor 接口的 postProcessBeanFactory() 方法  => 【BeanFactory】
            this.invokeBeanFactoryPostProcessors(beanFactory);
            //注册beanPostProcessor
            this.registerBeanPostProcessors(beanFactory);
            beanPostProcess.end();
            //初始化应用事件广播器
            this.initMessageSource();
            this.initApplicationEventMulticaster();
            this.onRefresh();
            this.registerListeners();
            //完成非懒加载的Spring bean的创建的工作
            this.finishBeanFactoryInitialization(beanFactory);
            this.finishRefresh();
        } catch (BeansException var10) {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Exception encountered during context initialization - cancelling refresh attempt: " + var10);
            }

            this.destroyBeans();
            this.cancelRefresh(var10);
            throw var10;
        } finally {
            this.resetCommonCaches();
            contextRefresh.end();
        }

    }
}

protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, this.getBeanFactoryPostProcessors());
    if (!NativeDetector.inNativeImage() && beanFactory.getTempClassLoader() == null && beanFactory.containsBean("loadTimeWeaver")) {
        beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
        beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
    }

}