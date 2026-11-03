var dicts, dateType, selectType, corp;
bt.use(['form', 'datatable', 'corp', 'btdate', 'date', 'moneyType', 'selectType', 'dateType', 'dicts'], function () {
    $ = bt.jquery, form = bt.form(), moneyFun = bt.moneyType.getFun();
    corp = bt.corp.init();
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    dicts = bt.dicts.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        // customDom: "Brtp",
        colReorder: true,
        checkbox: false,
        isInitData: false,
        // paging:false,
        ordering: false,
//        ignoreButtons:['colvis','print','output'],// 忽略的表单默认按钮
        buttonsLayout: false,
        pageSizeOptions: [100],
        ajax: {
            'url': basePath + "/finacial/check/fcIncomStatement/datatable"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            callback: function (data) { //点击查询按钮的回调方法,返回data对象，包含：elem:当前按钮jq对象；field:查询表单内容；form:表单jq对象
                var checkDate = data.field.checkDate;
                if (isNotNull(checkDate)) {
                    checkDate = checkDate.replace("-", bt.lang("table.fcDayClose.closeYear"));
                    $("#dateTime").html(checkDate + bt.lang("table.fbsBudgetPeriod.month"));
                }
            }
        },
        columns: [
            {
                data: "item", className: 'textLeft', bSortable: true,
                render: function (data, type, full, meta) {
                    var str = spaceToShowText(data);
                    if (str && str.indexOf('&ensp') !== -1){
                        var arr = str.split(';');
                        var count = 0;
                        for (var i = 0; i < arr.length; i++) {
                            if (arr[i] =='&ensp'){
                                count ++;
                            }
                        }
                        return '<div style="padding-left: '+count*6+'px">'+data+'</div>'
                    }
                    return str
                }
            },
            {data: "orderNo", className: 'center', bSortable: true},
            {
                data: "balance", className: 'textRight', bSortable: true,
                render: function (data, type, full, meta) {
                    $("#netName").html(full.netName);
                    $("#cur").html(full.curName);
                    return moneyFun.format(full.balance);
                }
            },
            {
                data: "yearTotal", className: 'textRight', bSortable: true,
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.yearTotal);
                }
            },
        ]


    });

    var spaceToShowText = function (str) {
        if (str) {
            // 空格转义
            // return str.replace(/ /g, "&nbsp;");
            return str.replace(/ /g, "&ensp;");
        }
        return "";
    }

    // 导出按钮
    form.on('submit(excel)', function (data) {
        var eqs_netCode = $("select[name=eqs_netCode]").val();
        var eqs_curCode = $("select[name=eqs_curCode]").val();
        var checkDate = $("input[name=checkDate]").val();
        var isConvert = dicts.get("isConvert").val();
        var preDate = dateType.get("preDate").val();
        var convertCurId = selectType.get("convertCurId").val();
        var rateType = selectType.get("rateType").val();
        var data = {
            "eqs_netCode": eqs_netCode,
            "eqs_curCode": eqs_curCode,
            "checkDate": checkDate,
            "isConvert": isConvert,
            "preDate": preDate,
            "convertCurId": convertCurId,
            "rateType": rateType
        };
        var url = basePath + "/finacial/check/fcIncomStatement/doExportInterestBill?impType=1";
        DownLoadFile({url: url, data: data, method: 'post'});
        return false;
    });

    var DownLoadFile = function (options) {
        var config = $.extend(true, {method: 'post'}, options);
        var $iframe = $('<iframe id="down-file-iframe" />');
        var $form = $('<form target="_self" method="' + config.method + '" />');
        $form.attr("action", config.url);
        for (var key in config.data) {
            $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
        }
        $iframe.append($form);
        $(document.body).append($iframe);
        $form[0].submit();
        $iframe.remove();
    }

    // 打印按钮点击事件
    $("#printBt").click(function () {
        printHorizontal();
    });

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
                var theadHtml = printElem.find('.dataTables_scrollHead table thead').html();//获取表头
                theadHtml = theadHtml.replace(new RegExp("textRight","gm"),"center");
                datatableHtml += theadHtml;

                datatableHtml += '</thead>';
                datatableHtml += '<tbody>';
                datatableHtml += printElem.find('.dataTables_scrollBody table tbody').html();//获取表体
                datatableHtml += '</tbody>';
                datatableHtml += '</table>';
                cloneElem.find('.dataTables_wrapper').html(datatableHtml);
                allhtml = cloneElem.html();//获取html
            }
            var html = '<head>' + style + '</head><body>' + allhtml + '</body>';
            LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Width:90%");
            // 表头
            LODOP.ADD_PRINT_TEXT('2mm', '50%', '300', 100, bt.lang("fcIncomStatement.list"));
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
            LODOP.ADD_PRINT_TEXT('13mm', '3%', '60%', 100, bt.lang("table.fcIncomStatement.corp") + $("#netName").html());   // 网点名称
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            //LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
            LODOP.ADD_PRINT_TEXT('13mm', '45%', '20%', 100, bt.lang("table.fcIncomStatement.state") + $("#dateTime").html());   // 日期
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            //LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
            LODOP.ADD_PRINT_TEXT('13mm', '75%', '20%', 100, bt.lang("table.fcIncomStatement.cur") + $("#cur").html());  // 币别
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            //LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 表格数据
            LODOP.ADD_PRINT_TABLE('19mm', '0mm', 'RightMargin:0mm', "BottomMargin:9mm", html);

            // 会计主管
            LODOP.ADD_PRINT_TEXT('97%', '3%', 300, 120, '会计主管:');
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 复核员
            LODOP.ADD_PRINT_TEXT('97%', '33%', 300, 120, '复核员:');
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 制表人
            LODOP.ADD_PRINT_TEXT('97%', '63%', 300, 120, '制表人:');
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

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

});

function isNotNull(data) {
    if (data == null || data == "" || data == undefined) {
        return false;
    } else {
        return true;
    }
}

/**
 * 是否折合本位币回调
 */
function isConvertCallBack() {
    var isConvert = dicts.get("isConvert").val();
    if ("0" == isConvert) {
        dateType.get("preDate").attr({"lay-verify": ""});
        $("#preDate").val("");
        $("#preDate").attr("disabled", "disabled");
        dateType.get("preDate").attr({"lay-verify": ""});
        selectType.get("convertCurId").attr({"data-value": "", "data-required": false, "data-disabled": true});
        selectType.get("rateType").attr({"data-value": "", "data-required": false, "data-disabled": true});
    } else {
        dateType.get("preDate").attr({"lay-verify": "required"});
        selectType.get("convertCurId").attr({"data-value": "", "data-required": true, "data-disabled": false});
        selectType.get("rateType").attr({"data-value": "", "data-required": true, "data-disabled": false});
        $("#preDate").removeAttr("disabled");
    }
    /**
     * 设置月份最后一天
     */
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
    var checkDate = dateType.get("checkDate").val();
    var isConvert = dicts.get("isConvert").val();
    if (isNotNull(checkDate) && "1" == isConvert) {
        var lastDay = getLastDay(Number(checkDate.split("-")[0]), Number(checkDate.split("-")[1]));
        dateType.get("preDate").val(checkDate + "-" + lastDay);
    }
    /**
     * 检查汇率转换
     */
    checkCurId();
}


/**
 * 获取所选月份最后一天
 */
function getLastDay(year, month) {
    var new_year = year; //取当前的年份
    var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）
    if (month > 12) {//如果当前大于12月，则年份转到下一年
        new_month -= 12; //月份减
        new_year++; //年份增
    }
    var new_date = new Date(new_year, new_month, 1); //取当年当月中的第一天
    return (new Date(new_date.getTime() - 1000 * 60 * 60 * 24)).getDate();//获取当月最后一天日期
}

/**
 * 汇率检查
 */
function checkCurId() {
    var isConvert = dicts.get("isConvert").val();
    if ("1" == isConvert) {
        var convertCurId = selectType.get("convertCurId").val();
        var rateType = selectType.get("rateType").val();
        var predate = dateType.get("preDate").val();
        var curId = selectType.get("curId").val();
        var netCode = corp.get("corpId").val();
        var yearMonth = dateType.get("checkDate").val();
        if (isNotEmpty(convertCurId) && isNotEmpty(rateType) &&
            isNotEmpty(predate) && isNotEmpty(netCode) && isNotEmpty(yearMonth)) {
            $.ajax({
                url: basePath + "/finacial/check/fcIncomStatement/checkCurRate",
                type: "post",
                dataType: "json",
                data: {
                    "convertCurId": convertCurId,
                    "rateType": rateType,
                    "preDate": predate,
                    "oriCurId": curId,
                    "netCode": netCode,
                    "yearMonth": yearMonth
                },
                // async: false,
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
function corpCallBack() {
    /**
     * 检查汇率转换
     */
    checkCurId();
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