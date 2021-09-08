package com.javamentor.qa.platform.models.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> implements Serializable {
    private int currentPageNumber;
    private int totalPageCount;
    private int totalResultCount;
    private List<T> items;
    private int itemsOnPage;
}
