package com.datnguyen.rem.utils;

import com.datnguyen.rem.repository.critea.SearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class SortUtils {

    public static Sort.Order createSortOrder(String sortBy){
        Sort.Order sorts=null;
        // price:asc|desc
        Pattern pattern=Pattern.compile("(\\w+?)(:)(.*)");
        Matcher matcher=pattern.matcher(sortBy);
        if(matcher.find()){
            //matcher.find is compulsory for access in matcher group because we need move cursor of matcher to find
            //string has been match with regex expression or not.
            // If don't have matcher it will throw IllegalArgumentException
            if(matcher.group(3).equalsIgnoreCase("asc")){
                sorts=(new Sort.Order(Sort.Direction.ASC,matcher.group(1)));
            }
            else {
                sorts=(new Sort.Order(Sort.Direction.DESC,matcher.group(1)));
            }
        }
        return sorts;
    }
    public  static String createSortOrderWithSql(String sortBy){
        StringBuilder adjustQuery=new StringBuilder();
        Pattern pattern=Pattern.compile("(\\w+?)(:)(.*)");
        Matcher matcher=pattern.matcher(sortBy);
        if(matcher.find()){
            //matcher.find is compulsory for access in matcher group because we need move cursor of matcher to find
            //string has been match with regex expression or not.
            // If don't have matcher it will throw IllegalArgumentException
            adjustQuery.append(String.format("order by p.%s %s",matcher.group(1),matcher.group(3)));
        }
        return adjustQuery.toString();
    }
    public static SearchCriteria createSearchForListCriteria(String search){
        // price:value
        Pattern pattern=Pattern.compile("(\\w+?)(:|>|<)(.*)");
        Matcher matcher=pattern.matcher(search);
        if(matcher.find()){
            return new SearchCriteria(matcher.group(1), matcher.group(2),matcher.group(3));
        }
        return null;
    }
}
