package com.bmts.heating.commons.utils.restful;

import lombok.Data;

@Data
public class PageBase {
    private int currentPage = 1;
    private int pageCount = 20;
}
