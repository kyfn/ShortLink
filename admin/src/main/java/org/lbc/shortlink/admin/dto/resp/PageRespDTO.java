package org.lbc.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRespDTO<T> {

    private List<T> list;

    private Long total;

    private Long current;

    private Long size;

    private Long pages;
}

