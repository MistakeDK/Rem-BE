package com.datnguyen.rem.utils;

import com.datnguyen.rem.repository.specification.GenericSpecificationBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecificationUtils {
    public static void ConvertFormStringToSpecification(GenericSpecificationBuilder<?> builder, String... condition){
        for (String c:condition){
            Pattern pattern=Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
            Matcher matcher=pattern.matcher(c);
            if(matcher.find()){
                builder.with(matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5));
            }
        }
    }
}
