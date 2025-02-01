package org.test.vkapi.mapper;

public interface Mapper<S,T> {
    T map(S source);
}
