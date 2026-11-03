package com.bytter.core.common.datatable;

import com.bytter.core.utils.EmptyUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel("列表查询条件")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class V1DataTablesInput implements Serializable {
    @ApiModelProperty("当前页码")
    private Integer pageNumber = 1;
    @ApiModelProperty("每页数据量")
    private Integer pageSize = 10;
    @ApiModelProperty("排序规则描述")
    private List<OrderColumn> orderColumns;
    @ApiModelProperty("查询条件")
    private   Map<String, Object>  params=new HashMap<>();



    /**
     * @return
     */
    public DataTablesInput convertAndInitColumns() {
        DataTablesInput input = new DataTablesInput();
        input.setFormdata(this.params);
        setStart(input, this.getPageNumber(), this.getPageSize());
        if (!EmptyUtils.isEmpty(orderColumns)) {
            for (OrderColumn oc : orderColumns) {
                SqlOrderValidator.validateColumn(oc.getField());
                SqlOrderValidator.validateDirection(oc.getOrder());
                input.addColumn(oc.getField(), false, true, null);
                input.addOrder(oc.getField(), oc.getOrder().equalsIgnoreCase("asc"));
            }
        }
        return input;
    }


    private void setStart(DataTablesInput input, Integer pageNumber, Integer pageSize) {
        input.setLength(pageSize);
        if (pageNumber <= 1) {
            input.setStart(0);
        } else {
            input.setStart((pageNumber - 1) * pageSize);
        }
    }

}
