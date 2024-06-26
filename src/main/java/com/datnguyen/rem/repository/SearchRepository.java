package com.datnguyen.rem.repository;

import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.dto.response.ProductResponse;
import com.datnguyen.rem.entity.Category;
import com.datnguyen.rem.entity.Product;
import com.datnguyen.rem.mapper.ProductMapper;
import com.datnguyen.rem.repository.critea.ProductSearchCriteriaConsumer;
import com.datnguyen.rem.repository.critea.SearchCriteria;
import com.datnguyen.rem.utils.SortUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Repository
public class SearchRepository {
    ProductMapper productMapper;
    @PersistenceContext
    EntityManager entityManager;

    //method use customize query for pagination and search in one field
    public PageResponse<?> getListProductByCustomizeQuery(int pageNo,int pageSize,String search,String sortBy){
        if(pageNo>0){
           pageNo=pageNo-1;
        }
        StringBuilder sqlQuery=new StringBuilder("select p from Product p where 1=1 ");
        if(StringUtils.hasLength(search)){
            sqlQuery.append("and lower(p.name) like lower(?1)");
        }
        if(StringUtils.hasLength(sortBy)){
            sqlQuery.append(SortUtils.createSortOrderWithSql(sortBy));
        }
        Query selectQuery=entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(pageNo*pageSize); //Offset in query
        selectQuery.setMaxResults(pageSize);
        if(StringUtils.hasLength(search)){
            selectQuery.setParameter(1,String.format("%%%s%%",search));
        }
        var product= selectQuery.getResultList();

        StringBuilder sqlCountQuery=new StringBuilder("select count(p) from Product p where 1=1 ");
        if(StringUtils.hasLength(search)){
            sqlCountQuery.append("and lower(p.name) like lower(?1)");
        }
        if(StringUtils.hasLength(sortBy)){
            sqlCountQuery.append(SortUtils.createSortOrderWithSql(sortBy));
        }
        Query selectCountQuery=entityManager.createQuery(sqlCountQuery.toString());
        if(StringUtils.hasLength(search)){
            selectCountQuery.setParameter(1,String.format("%%%s%%",search));
        }
        Long totalElement=(Long) selectCountQuery.getSingleResult(); //Total item
        Page<?> page=new PageImpl<Object>(product, PageRequest.of(pageNo,pageSize),totalElement);
        return  PageResponse.builder()
                .pageNo(pageNo+1)
                .pageSize(pageSize)
                .totalItem(totalElement)
                .totalPage(page.getTotalPages())
                .items(page.stream().map(o -> productMapper.toProductResponse((Product) o)).toList())
                .build();
    }
    //method use to query with criteria
    public PageResponse<?> advancedSearchProductWithCriteria(int pageNo,int pageSize,String sortBy,String category,String... search){
        if(pageNo>0){
            pageNo=pageNo-1;
        }
        List<SearchCriteria> criteriaList=new ArrayList<>();
        if(search!=null){
            for(String s:search){
                criteriaList.add(SortUtils.createSearchForListCriteria(s));
            }
        }
        List<Product> products=getProduct(pageNo,pageSize,criteriaList,sortBy,category);
        Long totalElement=getTotalElement(criteriaList,category);
        return PageResponse.builder()
                .pageNo(pageNo+1) //offset
                .pageSize(pageSize)
                .totalItem(totalElement.intValue())
                .totalPage((int) Math.ceil((double) totalElement.intValue() /pageSize))//total element
                .items(products.stream().map(productMapper::toProductResponse).toList())
                .build();
    }

    private List<Product> getProduct(int pageNo, int pageSize, List<SearchCriteria> criteriaList
            , String sortBy,String category) {
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query=criteriaBuilder.createQuery(Product.class);
        Root<Product> root=query.from(Product.class);
        Predicate predicate=criteriaBuilder.conjunction();
        ProductSearchCriteriaConsumer queryConsumer=new ProductSearchCriteriaConsumer(criteriaBuilder,root,predicate);
        if(StringUtils.hasLength(category)){
            Join<Product, Category> productCategoryJoin=root.join("category");
            Predicate productPredicate=criteriaBuilder.like(productCategoryJoin.get("name"),"%"+category+"%");
            criteriaList.forEach(queryConsumer);
            predicate=queryConsumer.getPredicate();
            query.where(predicate,productPredicate);
        }else {
            criteriaList.forEach(queryConsumer);
            predicate=queryConsumer.getPredicate();
            query.where(predicate);
        }

        //sort
        if(StringUtils.hasLength(sortBy)){
            Pattern pattern=Pattern.compile("(\\w+?)(:)(asc|desc)");
            Matcher matcher=pattern.matcher(sortBy);
            if(matcher.find()){
                String columnName=matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    query.orderBy(criteriaBuilder.asc(root.get(columnName)));
                }
                else {
                    query.orderBy(criteriaBuilder.desc(root.get(columnName)));
                }
            }
        }
        return entityManager.createQuery(query).
                setFirstResult(pageNo*pageSize).setMaxResults(pageSize).getResultList();
    }
    private Long getTotalElement(List<SearchCriteria> criteriaList,String category){
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query=criteriaBuilder.createQuery(Long.class);
        Root<Product> root=query.from(Product.class);
        Predicate predicate=criteriaBuilder.conjunction();
        ProductSearchCriteriaConsumer searchConsumer=new ProductSearchCriteriaConsumer(criteriaBuilder,root,predicate);

        if(StringUtils.hasLength(category)){
            Join<Product, Category> productCategoryJoin=root.join("category");
            Predicate productPredicate=criteriaBuilder.like(productCategoryJoin.get("name"),"%"+category+"%");
            criteriaList.forEach(searchConsumer);
            predicate=searchConsumer.getPredicate();
            query.select(criteriaBuilder.count(root));
            query.where(predicate,productPredicate);
        }else {
            criteriaList.forEach(searchConsumer);
            predicate=searchConsumer.getPredicate();
            query.select(criteriaBuilder.count(root));
            query.where(predicate);
        }
        return entityManager.createQuery(query).getSingleResult();
    }
}
