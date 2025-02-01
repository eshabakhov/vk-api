package org.example.mapper;

public interface Mapper<S,T> {
    T map(S source);
}
