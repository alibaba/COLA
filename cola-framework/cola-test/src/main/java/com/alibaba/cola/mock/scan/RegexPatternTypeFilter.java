package com.alibaba.cola.mock.scan;

import java.util.regex.Pattern;

import org.springframework.util.Assert;

/**
 * @author shawnzhan.zxy
 * @since 2018/09/24
 */
public class RegexPatternTypeFilter implements TypeFilter{
    private final Pattern pattern;
    private final String uid;

    public RegexPatternTypeFilter(Pattern pattern) {
        Assert.notNull(pattern, "Pattern must not be null");
        this.pattern = pattern;
        this.uid = pattern.toString();
    }
    public RegexPatternTypeFilter(String regex) {
        this(Pattern.compile(regex));
    }

    @Override
    public boolean match(Class clzz) {
        return match(clzz.getName());
    }

    public boolean match(String ori) {
        return this.pattern.matcher(ori).matches();
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        TypeFilter compareObj = (TypeFilter)o;
        if(getUid().equals(compareObj.getUid())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return getUid().hashCode();
    }
}
