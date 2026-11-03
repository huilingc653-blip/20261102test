var selectType, page, dateType, corp;
bt.use(['form', 'corp', 'dicts', 'selectType', 'selectTable', 'selectTableType', 'printType', 'dateType', 'bttable', 'page', 'date'], function () {
    var $ = bt.jquery, form = bt.form();
    dateType = bt.dateType.init();

    selectNetTable = function (data, elem) {
        var corpId = corp.get("corpId").val();
        selectTable.get("selectTable").setUrl({corpId: corpId});
    }
    corp = bt.corp.init();
    selectType = bt.selectType.init();
    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    page = bt.page.init({
        elem: '#page',//分页组件容器
        pages: $("#total").val(),
        count: $("#total").val(), //表格总页码
        currentPage: $("#currentPage").val(),//当前页码，默认值为 0,即第一页
        len: $("#total").val(), //当前显示条数
        lengthMenu: false,
        custom: {
            start: 1
        },
        jump: function (obj, first) { //翻页回调方法
            var curr = obj.curr;
            var len = obj.len;
            $("#currentPage").val(curr);
            if (!first) {
                location.href = basePath + "/finacial/report/accountPrint/list?" + getParam();
            }
        }
    })

    var getParam = function () {
        var param = "";
        var summaryContent = $("#summaryContent").val();
        if (bt.empty(summaryContent)) {
            summaryContent = "";
        }
        param += "corpId=" + corp.get('corpId').val() + "&account=" + $("#account").val() +
            "&voucherType=" + selectType.get('voucherType').val() + "&curId=" + selectType.get('curId').val() +
            "&startDate=" + dateType.get('startDate').val() + "&endDate=" + dateType.get('endDate').val() +
            "&summaryContent=" + summaryContent + "&searchFlag=1" +
            "&start=" + $("#currentPage").val();
        return param;
    }

    bt.beforeInitMulselect = function (data) {
        var filterValue = ['K', 'I', 'Q', 'C', 'H', 'T', 'W', 'B', 'G', 'E', 'F', 'U', 'R', 'Z'];
        var data = data;
        for (var i = 0, length = filterValue.length; i < length; i++) {
            for (var j = 0, len = data.length; i < length; j++) {
                if (data[j].value == filterValue[i]) {
                    data.splice(j, 1);
                    break;
                }
            }
        }
        return data
    }

    bt.dicts.init();
    bt.printType.init();
    var selectTable = bt.selectTableType.init({
        elem: "#account",
        url: basePath + "/finacial/report/accountPrint/queryListAccount",
        columns: [
            {"data": "blnctrAcc", title: bt.lang("table.fcLessPayBill.account")},
            {"data": "accName", title: bt.lang("table.accountDTO.accName")},
        ],
        clickCallback: function (data, $tr, $elem, full) {
            if (data) {
                $('#account').val(data.blnctrAcc);
                selectType.get('curId').attr({"data-disabled": true, "data-defaultvalue": data.curId})
            } else {
                selectType.get('curId').attr("data-disabled", false)
            }
        },
    });

    $("#account").blur(function () {
        selectType.get('curId').attr("data-disabled", false);
    })

    form.on('submit(search)', function (data) {
        $("#searchFlag").val("1");
        var action = basePath + "/finacial/report/accountPrint/list";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    });

    $("#reset").click(function () {
        reset();
    })

    form.on('submit(excel)', function (data) {
        var url = basePath + "/finacial/report/accountPrint/excel";
        DownLoadFile({url: url, data: data.field, method: 'post'});
        return false;
    });


    var DownLoadFile = function (options) {
        var config = $.extend(true, {method: 'get'}, options);
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
    $("#printBatch").click(function () {
        parent.layer.open({
            type: 2,
            area: ['1120px', '700px'],
            maxmin: true,
            content: basePath + "/finacial/report/accountPrint/batchPrint?"+getParam()
        });
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
                + ' table.layui-table{border:1px solid #000!important;}'
                + ' table th {border:1px solid #000!important; height:20px;font-size: 12px!important;font-weight: 400!important;}'
                + ' table td {border:1px solid #000!important; font-size: 12px!important; line-height: 15px!important;min-height: 20px!important;}'
                + ' .center {text-align:center} .textLeft{text-align:left;} .left{text-align:left;}  .textRight{text-align:right;} .right{text-align:right;}table.layui-table *{font-family: "宋体"}'
                + ' </style>';

            var allhtml = document.getElementById(printAreaElem).innerHTML;
            var html = '<head>' + style + '</head><body>' + allhtml + '</body>';
            LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Width:90%");
            // 表头 取网点名称
            LODOP.ADD_PRINT_TEXT('2mm', '50%', '794', 100, $('#netName').val());
            LODOP.SET_PRINT_STYLEA(0, "FontSize", 20);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // LODOP.ADD_PRINT_HTM('8mm', '50%', "<font style='font-size:15px'><span>" + bt.lang("table.accountStatement.tableTitle") + "</span></font>");
            LODOP.ADD_PRINT_TEXT('15mm', '50%', '200', 100, bt.lang("table.accountStatement.tableTitle"));
            LODOP.SET_PRINT_STYLEA(0, "FontSize", 16);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 表头信息
            LODOP.ADD_PRINT_TEXT('25mm', '0%', '25%', 100, $("#accountShowContainerElement").html());   // 账号
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
            LODOP.ADD_PRINT_TEXT('25mm', '25%', '25%', 100, $("#accNameShowContainerElement").html());   // 户名
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
            LODOP.ADD_PRINT_TEXT('25mm', '50%', '25%', 100, $("#yesterBalanceShowContainerElement").html());  // 期初余额
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
            LODOP.ADD_PRINT_TEXT('25mm', '75%', '25%', 100, $("#curNameShowContainerElement").html());      // 币别
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 表格数据
            LODOP.ADD_PRINT_TABLE('30mm', '0mm', 'RightMargin:0mm', "BottomMargin:9mm", html);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");

            // 制表日期
            LODOP.ADD_PRINT_HTM('97%', '3%', 300, 120, "<font style='font-size:12px' format='0'><span>" + bt.lang('com.table.makeDate') + ':' + $("#curDate").val() + "</span></font>");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
            // 分页显示
            LODOP.ADD_PRINT_HTM('97%', '88%', 300, 100, "<font style='font-size:12px' format='0'><span tdata='pageNO'>第##页</span>/<span tdata='pageCount'>共##页</span></font>");
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
})


function reset() {
    bt.corp.init().get("corpId").val("");
    $("#account").val("");
    bt.selectType.init().get("voucherType").val("");
    bt.selectType.init().get("curId").val("");
    var nowdate = bt.date.format(new Date(), 'yyyy-MM-dd');
    bt.dateType.init().get("startDate").val($("#initStartDate").val());
    bt.dateType.init().get("endDate").val(nowdate);
    $("#summaryContent").val("");
}
