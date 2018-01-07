package com.alibaba.sofa.boot;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import com.alibaba.sofa.logger.Logger;
import com.alibaba.sofa.logger.LoggerFactory;

import lombok.Getter;
import lombok.Setter;


/**
 * @author zhenxin.yzx ( Rocky )
 * @since 15/7/25
 */
public class ClassPathScanHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathScanHandler.class);
    
    /**
     * class file extension name.
     */
    private static final String CLASS_EXTENSION_NAME = ".class";
    /**
     * 是否排除内部类 true->是 false->否.
     */
    @Getter
    @Setter
    private boolean excludeInner = true;
    /**
     * 过滤规则适用情况 true—>搜索符合规则的 false->排除符合规则的.
     */
    @Getter
    @Setter
    private boolean checkInOrEx = true;
    /**
     * 过滤规则列表 如果是null或者空，即全部符合不过滤.
     */
    @Getter
    @Setter
    private List<String> classFilters = null;
    /**
     * the reflections.
     */
    @Getter
    @Setter
    private Reflections reflections = null;

    /**
     * the classes and the packages to be scanned.
     *
     * @param packages packages.
     */
    public ClassPathScanHandler(String... packages) {
        this.reflections = new Reflections(new ConfigurationBuilder().
                forPackages(packages).
                addScanners(new TypeAnnotationsScanner(), new SubTypesScanner())//.addUrls(urlList)
        );
    }

    /**
     * excludeInner:是否排除内部类 true->是 false->否<br>.
     * checkInOrEx：过滤规则适用情况 true—>搜索符合规则的 false->排除符合规则的<br>
     * classFilters：自定义过滤规则，如果是null或者空，即全部符合不过滤
     *
     * @param excludeInner whether exclude the inner class.
     * @param checkInOrEx  whether exclude the rule checking.
     * @param classFilters the customized the classes to be filtered.
     */
    public ClassPathScanHandler(Boolean excludeInner, Boolean checkInOrEx,
                                List<String> classFilters) {
        this.excludeInner = excludeInner;
        this.checkInOrEx = checkInOrEx;
        this.classFilters = classFilters;

    }

    /**
     * get all the classes with annotation.
     *
     * @param annotation     the specific annotation.
     * @param honorInherited honorInherited
     * @return the set of the classes.
     */
    public Set<Class<?>> getAllClassesWithAnnotation(Class<? extends Annotation> annotation, boolean honorInherited) {
        return reflections.getTypesAnnotatedWith(annotation, honorInherited);
    }

    /**
     * get all the sub classes with the specific parent class.
     *
     * @param parent the parent class.
     * @param <T>    sub class's type.
     * @return the set of the sub classes found.
     */
    public <T> Set<Class<? extends T>> getAllSubClassesByParent(Class<T> parent) {
        return reflections.getSubTypesOf(parent);
    }

    /**
     * scan the package.
     *
     * @param basePackage the basic class package's string.
     * @param recursive   whether to search recursive.
     * @return Set of the found classes.
     */
    public Set<Class<?>> getPackageAllClasses(String basePackage, boolean recursive) {
        if (basePackage == null)
            return new HashSet<>();
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        String packageName = basePackage;
        if (packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.lastIndexOf('.'));
        }
        String package2Path = packageName.replace('.', '/');

        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(package2Path);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    LOGGER.debug("扫描file类型的class文件....");
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    doScanPackageClassesByFile(classes, packageName, filePath, recursive);
                } else if ("jar".equals(protocol)) {
                    LOGGER.debug("扫描jar文件中的类....");
                    doScanPackageClassesByJar(packageName, url, recursive, classes);
                }
            }
        } catch (IOException e) {
            LOGGER.error("IOException error:");
        }

        TreeSet<Class<?>> sortedClasses = new TreeSet<>(new ClassNameComparator());
        sortedClasses.addAll(classes);
        return sortedClasses;
    }

    /**
     * 以jar的方式扫描包下的所有Class文件<br>.
     *
     * @param basePackage eg：michael.utils.
     * @param url         the url.
     * @param recursive   whether to search recursive.
     * @param classes     set of the found classes.
     */
    private void doScanPackageClassesByJar(String basePackage, URL url, final boolean recursive, Set<Class<?>> classes) {
        String package2Path = basePackage.replace('.', '/');
        JarFile jar;
        try {
            jar = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.startsWith(package2Path) || entry.isDirectory()) {
                    continue;
                }
                // 判断是否递归搜索子包
                if (!recursive && name.lastIndexOf('/') != package2Path.length()) {
                    continue;
                }
                // 判断是否过滤 inner class
                if (this.excludeInner && name.indexOf('$') != -1) {
                    LOGGER.debug("exclude inner class with name:" + name);
                    continue;
                }
                String classSimpleName = name.substring(name.lastIndexOf('/') + 1);
                // 判定是否符合过滤条件
                if (this.filterClassName(classSimpleName)) {
                    String className = name.replace('/', '.');
                    className = className.substring(0, className.length() - 6);
                    try {
                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
                    } catch (ClassNotFoundException e) {
                        LOGGER.error("Class.forName error:URL is ===>" + url.getPath());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("IOException error:URL is ===>" + url.getPath());
        } catch (Throwable e) {
            LOGGER.error("ScanPackageClassesByJar error:URL is ===>" + url.getPath());
        }
    }

    /**
     * 以文件的方式扫描包下的所有Class文件.
     *
     * @param packageName the package name for scanning.
     * @param packagePath the package path for scanning.
     * @param recursive   whether to search recursive.
     * @param classes     set of the found classes.
     */
    private void doScanPackageClassesByFile(
            Set<Class<?>> classes, String packageName, String packagePath, final boolean recursive) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return filterClassFileByCustomization(pathname, recursive);
            }
        });

        if (null == files || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                doScanPackageClassesByFile(classes, packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - CLASS_EXTENSION_NAME.length());
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    LOGGER.error("IOException error:");
                }
            }
        }
    }

    /**
     * filter the class file from the customized rules.
     *
     * @param file      the class file to be filtered.
     * @param recursive whether search recursive.
     * @return true: match,  false: not match.
     */
    private boolean filterClassFileByCustomization(@Nonnull File file, boolean recursive) {
        if (file.isDirectory()) {
            return recursive;
        }
        String filename = file.getName();
        if (excludeInner && filename.indexOf('$') != -1) {
            LOGGER.debug("exclude inner class with name:" + filename);
            return false;
        }
        return filterClassName(filename);
    }

    /**
     * 根据过滤规则判断类名.
     *
     * @param className the class name.
     * @return whether to be filtered.
     */
    private boolean filterClassName(String className) {
        if (!className.endsWith(CLASS_EXTENSION_NAME)) {
            return false;
        }
        if (null == this.classFilters || this.classFilters.isEmpty()) {
            return true;
        }
        String tmpName = className.substring(0, className.length() - 6);
        boolean flag = false;
        for (String str : classFilters) {
            flag = matchInnerClassname(tmpName, str);
            if (flag) break;
        }
        return (checkInOrEx && flag) || (!checkInOrEx && !flag);
    }

    /**
     * check the className whether match the inner class's rule.
     *
     * @param className    the inner class name.
     * @param filterString the filter string.
     * @return true or false.
     */
    private boolean matchInnerClassname(String className, String filterString) {
        String reg = "^" + filterString.replace("*", ".*") + "$";
        Pattern p = Pattern.compile(reg);
        return p.matcher(className).find();
    }
}
