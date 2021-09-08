package com.javamentor.qa.platform.service.impl.dto;


import com.javamentor.qa.platform.dao.abstracts.dto.PaginationDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PaginationService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public abstract class PaginationServiceAbstract<T> implements PaginationService<T> {

    private final Map<String, PaginationDao<T>> paginationDaoMap;

    public PaginationServiceAbstract(Map<String, PaginationDao<T>> paginationDaoMap) {
        this.paginationDaoMap = paginationDaoMap;
    }


    @Override
    public PageDto<T> getPageDto(Map<String, Object> parameters) {

        String keyPagination = (String) parameters.get("workPagination");
        PaginationDao<T> paginationDao = paginationDaoMap.get(keyPagination);

        PageDto<T> pageDto = new PageDto<>();

        // общее количества данных в БД
        int allItems = paginationDao.getCountOfAllItems(parameters);
        pageDto.setTotalResultCount(allItems);

        // данные из БД
        pageDto.setItems(paginationDao.getItems(parameters));

        // количество данных на странице
        pageDto.setItemsOnPage((int) parameters.get("itemsOnPage"));

        // номер страницы
        pageDto.setCurrentPageNumber((int) parameters.get("currentPage"));

        // количество страниц
        int totalPage = (allItems % (int) parameters.get("itemsOnPage") == 0 ?
                allItems / (int) parameters.get("itemsOnPage") :
                allItems / (int) parameters.get("itemsOnPage") + 1);
        pageDto.setTotalPageCount(totalPage);

        return pageDto;
    }
}
