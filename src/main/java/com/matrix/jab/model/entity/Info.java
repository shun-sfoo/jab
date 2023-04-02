package com.matrix.jab.model.entity;


import com.matrix.jab.common.ColumnWidth;
import jakarta.persistence.*;
import lombok.Data;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@Entity
@Table(name = "info")
@Data
@ExcelIgnoreUnannotated
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExcelProperty(value = "商品名称")
    private String productName;
    @ExcelProperty(value = "商品规格")
    private String specification;

    @ExcelProperty(value = "包装单位")
    private String productUnit;

    @ExcelProperty(value = "生产厂家")
    private String provider;

    @ExcelProperty(value = "类别")
    private String className;

    @com.alibaba.excel.annotation.format.DateTimeFormat("yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "日期")
    private Date salesDate;

    @ExcelProperty(value = "单位名称")
    private String customer = " ";

    @ExcelProperty(value = "开票人")
    private String operator = " ";

    @ExcelProperty(value = "批号")
    private String batchNo;

    @com.alibaba.excel.annotation.format.DateTimeFormat("yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "有效期至")
    private Date availableDate;

    @ExcelProperty(value = "入库数量")
    private Integer input;

    @ExcelProperty(value = "出库数量")
    private Integer output;

    @ExcelProperty(value = "库存数量")
    private Integer num;

}
