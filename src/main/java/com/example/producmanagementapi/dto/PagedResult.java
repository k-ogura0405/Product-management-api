package com.example.producmanagementapi.dto;

import lombok.Data;
import java.util.List;

/**
 * Generic pagination result wrapper.
 * @param <T> type of items in the page
 */
@Data
public class PagedResult<T> {
    /** List of items for the current page */
    private List<T> content;
    /** Total number of items matching the query */
    private long totalElements;
    /** Zero‑based page index */
    private int pageNumber;
    /** Size of each page */
    private int pageSize;
}
