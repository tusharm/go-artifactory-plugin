package com.tw.go.plugins.artifactory.utils;

import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.List;

public class Iterables {
    public static <S, T> Iterable<T> map(Iterable<S> sources, Function<S, T> mapFunction) {
        List<T> targets = new ArrayList();

        for (S source : sources) {
            targets.add(mapFunction.apply(source));
        }

        return targets;
    }
}
