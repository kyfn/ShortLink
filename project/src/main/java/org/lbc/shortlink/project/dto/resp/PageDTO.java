package org.lbc.shortlink.project.dto.resp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {

    private List<T> list;

    private Long total;

    private Long current;

    private Long size;

    private Long pages;

    public static <T> PageDTO<T> of(IPage<T> page) {
        return new PageDTO<>(
                page.getRecords(),
                page.getTotal(),
                page.getCurrent(),
                page.getSize(),
                page.getPages()
        );
    }

    public static <S, T> PageDTO<T> of(IPage<S> page, Function<S, T> converter) {
        return new PageDTO<>(
                page.getRecords().stream().map(converter).toList(),
                page.getTotal(),
                page.getCurrent(),
                page.getSize(),
                page.getPages()
        );
    }
}

