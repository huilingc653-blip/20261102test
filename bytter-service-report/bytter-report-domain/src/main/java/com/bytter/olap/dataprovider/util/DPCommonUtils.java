package com.bytter.olap.dataprovider.util;

import com.bytter.olap.dataprovider.config.AggConfig;
import com.bytter.olap.dataprovider.config.DimensionConfig;
import com.bytter.olap.dataprovider.config.ValueConfig;
import com.bytter.olap.dataprovider.result.AggregateResult;
import com.bytter.olap.dataprovider.result.ColumnIndex;

import java.util.ArrayList;
import java.util.List;

//JAVA8
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//import java.util.stream.Stream;

/**
 * Created by zyong on 2017/9/28.
 */
public class DPCommonUtils {

//    /**
//     * 使用了JAVA8特性
//     * <p>
//     * （1）图表预览查询会调用这个方法
//     *
//     * @param config 配置
//     * @param list   数据列表
//     * @return
//     * @throws Exception
//     */
//    public static AggregateResult transform2AggResult(AggConfig config, List<String[]> list) throws Exception {
//        // recreate a dimension stream
//
//        //把config中的列维和行维都转化列表放到 List<ColumnIndex> dimensionList当中。
//        Stream<DimensionConfig> dimStream = Stream.concat(config.getColumns().stream(), config.getRows().stream());
//        List<ColumnIndex> dimensionList = dimStream.map(ColumnIndex::fromDimensionConfig).collect(Collectors.toList());
//        int dimSize = dimensionList.size();
//        dimensionList.addAll(config.getValues().stream().map(ColumnIndex::fromValueConfig).collect(Collectors.toList()));
//
//        IntStream.range(0, dimensionList.size()).forEach(j -> dimensionList.get(j).setIndex(j));
//        list.forEach(row -> {
//            IntStream.range(0, dimSize).forEach(i -> {
//                if (row[i] == null) row[i] = NULL_STRING;
//            });
//        });
//        String[][] result = list.toArray(new String[][]{});
//        AggregateResult aggregateResult = new AggregateResult(dimensionList, result);
//        return aggregateResult;
//        //return new AggregateResult(dimensionList, result);
//    }


    /**
     * 使用了JAVA8特性
     * <p>
     * （1）图标预览查询会调用这个方法
     *
     * @param config 配置
     * @param list   数据列表
     * @return
     * @throws Exception
     */
    public static AggregateResult transform2AggResult(AggConfig config, List<String[]> list) throws Exception {
        // recreate a dimension stream

        //去除JAVA8的特性
        //把config中的列维和行维都转化列表放到 List<ColumnIndex> dimensionList当中。
        List<DimensionConfig> dimensionConfigListTemp = new ArrayList<>();
        List<ColumnIndex> dimensionList = new ArrayList<>();
        dimensionConfigListTemp.addAll(config.getColumns());
        dimensionConfigListTemp.addAll(config.getRows());
        for (DimensionConfig dimensionConfig : dimensionConfigListTemp) {
            dimensionList.add(ColumnIndex.fromDimensionConfig(dimensionConfig));
        }

        List<ValueConfig> valueConfigList = config.getValues();
        for (ValueConfig valueConfig : valueConfigList) {
            dimensionList.add(ColumnIndex.fromValueConfig(valueConfig));
        }

        int dimSize = dimensionList.size();
        for (int j = 0; j < dimSize; j++) {
            dimensionList.get(j).setIndex(j);
        }

        // 对数据进行判空处理：如果数据为空，则填充NULL_STRING
        // 这里数据量很大时，很耗费时间
//        String[] row;
//        for (int i = 0, listSize = list.size(); i < listSize; i++) {
//            row = list.get(i);
//            for (int j = 0; j < dimSize; j++) {
//                if (row[j] == null) {
//                    row[j] = NULL_STRING;
//                }
//            }
//        }

        String[][] result = list.toArray(new String[][]{});
        AggregateResult aggregateResult = new AggregateResult(dimensionList, result);
        return aggregateResult;

    }

}
