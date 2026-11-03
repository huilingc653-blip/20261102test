var dicts, selectType, dateType, corp, form, corpName, printType;
bt.use(['jquery', 'datatable', 'form', 'dicts', 'selectType', 'dateType', 'corp', 'printType', 'date'], function () {
    $ = bt.jquery;
    form = bt.form();
    corp = bt.corp.init();
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();
    dateType = bt.dateType.init();
    printType = bt.printType.init();
    var now = new Date();
    var year = now.getFullYear();
    var month = now.getMonth() + 2;
    if (Number(month) > 9) {
        if (Number(month) > 12) {
            year = Number(year) + 1;
            month = "01";
        }
    } else {
        month = "0" + month;
    }
    dateType.get("convertDate").val(bt.date.format(bt.date.addDay(year + "-" + month + "-" + "01", -1)));
    var datatable = bt.datatable.init({
        elem: "#datatable",
        customDom: "Brtp",
        checkbox: false,
        ajax: {
            url: basePath + "/finacial/report/cashFlowReport/datatable",
            dataSrc: "data"
        },
        pageSizeOptions: [1000],
        isInitData: false,
        colReorder: true,
        ignoreButtons: ['colvis', 'print', 'output'],
        buttons: [
            {
                text: bt.lang("common.button.exports"),
                action: function (e, dt, node, config) {
                    exportExcel();
                }
            }, {
                text: bt.lang("common.button.print"),
                action: function (e, dt, node, config) {
                    printHorizontal();
                }
            }
        ],
        columns: [
            {
                data: "leftItemName",
                className: "center"
            },
            {
                data: "leftOrderNo",
                className: "center"
            }, {
                data: "leftMoney",
                className: "textRight",
                render: function (data, type, full, meta) {
                    $("#reportCur").html(full.curName);
                    if (isEmpty(full.leftItemName)) {
                        return "";
                    }
                    return bt.money.format(data);
                }
            }, {
                data: "rightItemName",
                className: "center"
            }, {
                data: "rightOrderNo",
                className: "center"
            }, {
                data: "rightMoney",
                className: "textRight",
                render: function (data, type, full, meta) {
                    if (isEmpty(full.rightItemName)) {
                        return "";
                    }
                    return bt.money.format(data);
                }
            }
        ],
        btInitComplete: function (setting, json) {
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            callback: function (data) {
                var startDate = dateType.get("startDate").val();
                var endDate = dateType.get("endDate").val();
                $("#reportDate").html(startDate + bt.lang("table.relationBank.dateRange") + endDate);
                $("#reportCorp").html(corpName);
            }
        }
    });
});



/**
 * 是否折合本位币回调
 */
function isConvertCallBack() {
    var isConvert = dicts.get("isConvert").val();
    if ("0" == isConvert) {
        selectType.get("convertCurId").attr({"data-value":"", "data-required": false, "data-disabled": true});
        selectType.get("rateType").attr({"data-value":"", "data-required": false, "data-disabled": true});
    } else {
        selectType.get("convertCurId").attr({"data-value":"", "data-required": true, "data-disabled": false});
        selectType.get("rateType").attr({"data-value":"", "data-required": true, "data-disabled": false});
    }
    checkDateCallBack();
    /**
     * 检查汇率转换
     */
    checkCurId();
    form.renderIcon();
}




/**
 * 设置所选月份最后一天为汇率日期
 */
function checkDateCallBack() {
    /**
     * 检查汇率转换
     */
    checkCurId();
}
/**
 * 表格查询表单的校验方法，用于表格翻页时，需要先校验表单，通过了才可以去翻页
 */

function datableQueryFormVerify() {
    if(!form){
        return true
    }
    return form.verifyFun($('#searchForm'))

}

/**
 * 汇率检查
 */
function checkCurId() {
    var isConvert = dicts.get("isConvert").val();
    if ("1" == isConvert) {
        var convertCurId = selectType.get("convertCurId").val();
        var rateType = selectType.get("rateType").val();
        var convertDate = dateType.get("convertDate").val();
        var curId = selectType.get("curId").val();
        var netCode = corp.get("netCode").val();
        if (isNotEmpty(convertCurId) && isNotEmpty(rateType) &&
            isNotEmpty(convertDate) && isNotEmpty(netCode)) {
            $.ajax({
                url: basePath + "/finacial/check/fcIncomStatement/checkCurRate",
                type:"post",
                dataType:"json",
                data: {
                    "convertCurId": convertCurId,
                    "rateType": rateType,
                    "preDate": convertDate,
                    "oriCurId": curId,
                    "netCode": netCode
                },
                success: function (data) {
                    if (!data.success) {
                        selectType.get("convertCurId").val("");
                        layer.alert(data.msg, {icon: 2});
                    }
                }
            });
        }
    }
}

function isEmpty(value) {
    if (value == null || value == "" || value == undefined || value == " ") {
        return true;
    } else {
        return false;
    }
}

function isNotEmpty(value) {
    return !isEmpty(value);
}

/**
 * 单位回调
 */
function corpCallBack(ele,name,data,pq) {
    corpName = name;
    /**
     * 检查汇率转换
     */
    checkCurId();

    /**
     *月结日期检查
     */
    checkMonthCloseDate();
}

/**
 * 转换币种回调
 */
function convertCurIdCallBack() {
    /**
     * 检查汇率转换
     */
    checkCurId();
}

/**
 * 汇率类型回调
 */
function rateTypeCallBack() {
    /**
     * 检查汇率转换
     */
    checkCurId();
}

/**
 * 汇率日期回调
 */
function preDateCallBack() {
    /**
     * 检查汇率转换
     */
    checkCurId();
}

/**
 * exce导出
 */
function exportExcel() {
    var netCode = corp.get("netCode").val();
    var curId = selectType.get("curId").val();
    var startDate = dateType.get("startDate").val();
    var endDate = dateType.get("endDate").val();
    var isConvert = dicts.get("isConvert").val();
    var convertCurId = selectType.get("convertCurId").val();
    var rateType = selectType.get("rateType").val();
    var convertDate = dateType.get("convertDate").val();
    if (isEmpty(netCode)) {
        layer.alert(bt.lang("bill.accebill.abCorpLimitA.net"), {icon: 2});
        return false;
    }

    if (isEmpty(startDate) || isEmpty(endDate)) {
        layer.alert(bt.lang("finacial.report.select.startDateAndEndDate"), {icon: 2});
        return false;
    }

    var data = {
        "netCode": netCode,
        "curId": curId,
        "startDate": startDate,
        "endDate": endDate,
        "isConvert": isConvert,
        "convertCurId": convertCurId,
        "rateType": rateType,
        "convertDate": convertDate
    };
    var url = basePath + "/finacial/report/cashFlowReport/exportExcel";
    DownLoadFile({url: url, data: data, method: 'post'});
}

function DownLoadFile(options) {
    var config = $.extend(true, {method: 'post'}, options);
    var iframe = $('<iframe id="down-file-iframe" />');
    var form = $('<form target="down-file-iframe" method="' + config.method + '" />');
    form.attr("action", config.url);
    for (var key in config.data) {
        form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    iframe.append(form);
    $(document.body).append(iframe);
    form[0].submit();
    iframe.remove();
}

/**
 * 打印函数 - 横向打印
 */
function printHorizontal() {
    var printAreaElem = 'print';
    var printObj = bt.lodop.init();
    setTimeout(function () {
        LODOP = printObj.lodop;//获取lodop打印对象
        if (!LODOP) {
            return;
        }

        var style = '<link rel="stylesheet" href="' + basePath + '/static/plugins/layui/css/layui.css" media="all" />';
        style += '<link rel="stylesheet" href="' + basePath + '/static/css/common.css" />';
        style += '<style>'
            + ' table.layui-table{border:1px solid #000!important;font-family: 宋体;}'
            + ' table th {border:1px solid #000!important; height:20px;font-size: 12px!important;}'
            + ' table td {border:1px solid #000!important; font-size: 12px!important; line-height: 15px!important;min-height: 20px!important;}'
            + ' .center {text-align:center} .textLeft{text-align:left;} .left{text-align:left;}  .textRight{text-align:right;} .right{text-align:right;}'
            + ' </style>';

        var printElem = $('#' + printAreaElem);
        var allhtml = '';
        if (printElem.find('.dataTables_wrapper').length) { //当打印区域有datatable时
            var datatableHtml = '';
            var cloneElem = printElem.clone();
            datatableHtml += '<table class="layui-table"> ';
            datatableHtml += '<thead>';
            datatableHtml += printElem.find('.dataTables_scrollHead table thead').html();//获取表头
            datatableHtml += '</thead>';
            datatableHtml += '<tbody>';
            datatableHtml += printElem.find('.dataTables_scrollBody table tbody').html();//获取表体
            datatableHtml += '</tbody>';
            datatableHtml += '</table>';
            cloneElem.find('.dataTables_wrapper').html(datatableHtml);
            allhtml = cloneElem.html();//获取html
        } else {
            //如果有冻结树表格时，要引入树表格样式
            if (printElem.find('.l-grid').length) {
                style += '<style>table th,table td {height: 28px!important;padding-top: 0!important;padding-bottom: 0!important;}</style>'
                if (window.printType && window.printType['printHtml']) {
                    var cloneHtml = $("#" + printAreaElem).clone();
                    cloneHtml.find('.l-panel').before($('<table class="layui-table"> ' + window.printType['printHtml'] + '</table>'))
                    cloneHtml.find('.l-panel').remove();
                    allhtml = cloneHtml.html();
                }
            } else {
                allhtml = document.getElementById(printAreaElem).innerHTML;
            }
        }
        //  var allhtml = document.getElementById(printAreaElem).innerHTML;
        var html = '<head>' + style + '</head><body>' + allhtml + '</body>';
        LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Width:90%");
        // 表头 资产负债表
        LODOP.ADD_PRINT_TEXT('2mm', '0', '300', 100, bt.lang("finacial.report.cashFlowItem"));
        LODOP.SET_PRINT_STYLEA(1, "FontSize", 18);
        LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
        LODOP.SET_PRINT_STYLEA(1, "Bold", 1);

        // 文本框里 内容对于文本框居中
        LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
        // 打印项在纸张中水平居中
        LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
        // 每一页都打印
        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

        // 表头信息
        LODOP.ADD_PRINT_TEXT('11mm', '3%', '60%', 100, bt.lang("table.fcIncomStatement.corp") + $("#reportCorp").html());   // 网点名称
        LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
        //LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
        LODOP.ADD_PRINT_TEXT('11mm', '45%', '20%', 100, bt.lang("table.fcAccuBalance.date") + $("#reportDate").html());   // 日期
        LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
        //LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
        LODOP.ADD_PRINT_TEXT('11mm', '75%', '20%', 100, bt.lang("table.ratBkrationRelief.curCode") + $("#reportCur").html());  // 币别
        LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
        //LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

        // 表格数据
        LODOP.ADD_PRINT_TABLE('16mm', '0mm', 'RightMargin:0mm', "BottomMargin:9mm", html);

        // 分页显示
        LODOP.ADD_PRINT_HTM('97%', '88%', 300, 100, "<font style='font-size:12px;font-family: Microsoft YaHei;' format='0'><span tdata='pageNO'>第##页</span>/<span tdata='pageCount'>共##页</span></font>");
        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

        LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT","100%");
        // 2---横向打印，固定纸张；
        LODOP.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
        // 横向时的正向显示
        LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED", 1);
        // 设置可以重新选择纸张大小
        LODOP.SET_PRINT_MODE("RESELECT_PAGESIZE", 1);
        // 设置可以重新打印方向
        LODOP.SET_PRINT_MODE("RESELECT_ORIENT", 1);

        LODOP.PREVIEW();
    }, 800);
}

/**
 * 获取月结日期
 */
function checkMonthCloseDate(){
    var netCode = corp.get("netCode").val();
    var endDate = dateType.get("endDate").val();
    if (isNotEmpty(netCode) && isNotEmpty(endDate)) {
        $.ajax({
            url: basePath + "/finacial/report/cashFlowReport/getMonthCloseDate",
            type:"post",
            dataType:"json",
            data: {
                "netId": netCode
            },
            success: function (data) {
                if (data.success) {
                    var closeMonthDate = data.obj;
                    if (bt.date.compare(endDate,closeMonthDate) > 0) {
                        layer.alert(bt.lang("final.report.closeMonth.noSearch"), {icon: 2});
                        dateType.get("endDate").val("");
                    }
                } else {
                    layer.alert(data.msg, {icon: 2});
                }
            }
        });
    }
}

/**
 * 结束日期回调
 */
function endDateCallBack() {
    /**
     * 检查月结日期
     */
    checkMonthCloseDate()
}
