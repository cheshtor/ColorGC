package io.buyan.colorgc.client.service;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 模块管理器
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/20
 */
@Slf4j
public enum ModuleManager {

    INSTANCE;

    private Map<Class<?>, Module> modules = Collections.emptyMap();

    public void load() {
        modules = loadModules();

        prepare();
        boot();
        onComplete();
    }

    /**
     * 获取指定类型的模块
     * @param moduleClass 指定模块的类型
     * @param <T> 模块泛型
     * @return 指定类的模块
     */
    public <T extends Module> T findModule(Class<T> moduleClass) {
        return (T) modules.get(moduleClass);
    }

    private void prepare() {
        modules.values().stream().sorted(Comparator.comparingInt(Module::priority)).forEach(module -> {
            try {
                module.prepare();
                log.info("Module {} prepare successed.", module.getClass().getName());
            } catch (Throwable e) {
                log.error("Module {} try to prepare failed.", module.getClass().getName(), e);
            }
        });
    }

    private void boot() {
        modules.values().stream().sorted(Comparator.comparingInt(Module::priority)).forEach(module -> {
            try {
                module.boot();
                log.info("Module {} boot successed.", module.getClass().getName());
            } catch (Throwable e) {
                log.error("Module {} try to boot failed.", module.getClass().getName(), e);
            }
        });
    }

    private void onComplete() {
        modules.values().stream().sorted(Comparator.comparingInt(Module::priority)).forEach(module -> {
            try {
                module.onComplete();
                log.info("Module {} onComplete successed.", module.getClass().getName());
            } catch (Throwable e) {
                log.error("Module {} try to onComplete failed.", module.getClass().getName(), e);
            }
        });
    }

    private void shutdown() {
        modules.values().stream().sorted(Comparator.comparingInt(Module::priority)).forEach(module -> {
            try {
                module.shutdown();
                log.info("Module {} shutdown successed.", module.getClass().getName());
            } catch (Throwable e) {
                log.error("Module {} try to shutdown failed.", module.getClass().getName(), e);
            }
        });
    }

    private Map<Class<?>, Module> loadModules() {
        Map<Class<?>, Module> moduleCache = new HashMap<>();
        ServiceLoader<Module> loadedModules = ServiceLoader.load(Module.class, ModuleManager.class.getClassLoader());
        Iterator<Module> iterator = loadedModules.iterator();
        while (iterator.hasNext()) {
            Module module = iterator.next();
            moduleCache.put(module.getClass(), module);
        }
        return moduleCache;
    }

}
