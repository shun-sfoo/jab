package com.matrix.jab.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "info")
@Data
@ExcelIgnoreUnannotated
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @com.alibaba.excel.annotation.format.DateTimeFormat("yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "记账日期")
    private Date salesDate;

    @ExcelProperty(value = "单据类型")
    private String doubtType;


    @ExcelProperty(value = "商品编码")
    private String productCode;

    @ExcelProperty(value = "商品名称")
    private String productName;

    @ExcelProperty(value = "商品规格")
    private String specification;

    @ExcelProperty(value = "单位")
    private String productUnit;

    @ExcelProperty(value = "生产企业")
    private String provider;

    @ExcelProperty(value = "批号")
    private String batchNo;

    @ExcelProperty(value = "批次号")
    private String batchCode;

    @com.alibaba.excel.annotation.format.DateTimeFormat("yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "有效期")
    private Date availableDate;


    @ExcelProperty(value = "数量")
    private Integer num;


    @ExcelProperty(value = "批销价")
    private BigDecimal price;


    @ExcelProperty(value = "客户编码")
    private String customerCode;


    @ExcelProperty(value = "客户名称")
    private String customer;

    @ExcelProperty(value = "供应商名称")
    private String supplier;


    @ExcelProperty(value = "开票员")
    private String operator;


    @ExcelProperty(value = "保管账")
    private String keeper;


    @ExcelProperty(value = "所属账号")
    private String account;
}
