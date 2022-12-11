package com.project.doctorhub.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.toIntExact;

@Component
public class ListUtil {


    public <T> Page<T> getPage(Pageable pageable, List<T> list) {
        int total = list.size();
        int start = toIntExact(pageable.getOffset());
        int end = Math.min((start + pageable.getPageSize()), total);

        if (pageable.getOffset() >= total)
            return new PageImpl<>(new ArrayList<>(), pageable, total);

        return new PageImpl<>(list.subList(start, end), pageable, total);
    }

    public <T> Set<T> combine(Set<T>... sets) {
        return Stream.of(sets)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

}
