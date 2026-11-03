var dateType, selectType;
bt.use(['form', 'corp', 'dicts', 'selectType', 'selectTable', 'selectTableType', 'printType', 'dateType'], function () {
    var $ = bt.jquery, form = bt.form();

    dateType = bt.dateType.init();

    selectNetTable = function (data, elem) {
        var corpCode = corp.get("corpId").val();
        var netCode = corp.get("netId").val();
        selectTable.get("selectTable").setUrl({netId: netCode, corpId: corpCode});
    }

    //initChangeByType();
    var corp = bt.corp.init();
    selectType = bt.selectType.init();
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
    // bt.printType.init();
    var selectTable = bt.selectTableType.init({
        elem: "#blnctrAcc",
        url: basePath + "/finacial/report/BalanceQuery/queryListAccount",
        columns: [
            {"data": "blnctrAcc", title: bt.lang("table.fcLessPayBill.account")},
            {"data": "accName", title: bt.lang("table.accountDTO.accName")},
        ],
        clickCallback: function (data, $tr, $elem, full) {
            if (data) {
                $('#blnctrAcc').val(data.blnctrAcc);
            }
        },
    });

    form.on('submit(search)', function (data) {
        // $("#pageNum").val("1");

        var action = basePath + "/finacial/report/subjectaccbalancequery/getList";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    });

    $("#reset").click(function () {
        reset();
    })

    form.on('submit(excel)', function (data) {
        var url = basePath + "/finacial/report/subjectaccbalancequery/excel";
        DownLoadFile({url: url, data: data.field, method: 'get'});
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
            LODOP.ADD_PRINT_TEXT('2mm', '0', '794', 100, $('#netName').val());
            LODOP.SET_PRINT_STYLEA(0, "FontSize", 20);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            // 文本框里 内容对于文本框居中
           LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('13mm', '0', '200', 100, "科目账户余额表");
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "FontSize", 16);
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 表头信息   币别    日期
            LODOP.ADD_PRINT_TEXT('20mm', '25%', 150, 100, bt.lang('table.fbsBudgetDetail.curId') + '：' + getCurName(selectType.get("curId").val()));
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            var showDateStr = dateType.get("gedCloseDate").val() + " - " + dateType.get("ledCloseDate").val();
            LODOP.ADD_PRINT_TEXT('20mm', '60%', 300, 100, bt.lang('table.fcAccuBalance.date') + '：' + showDateStr);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 表格数据
            LODOP.ADD_PRINT_TABLE('26mm', '0mm', 'RightMargin:0mm', "BottomMargin:10mm", html);

            // 制表日期
            LODOP.ADD_PRINT_HTM('97%', '3%', 300, 120, "<font style='font-size:12px' format='0'><span>" + bt.lang('com.table.makeDate') + ':' + getNowFormatDate() + "</span></font>");
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

    var getCurName = function (curId) {
        var curName = "";
        var curIdElement = selectType.get("curId");
        if (curIdElement) {
            if (curIdElement.currentObject) {
                var dataList = curIdElement.currentObject.obj;
                if (dataList) {
                    for (var i = 0; i < dataList.length; i++) {
                        if (curId == dataList[i].id) {
                            curName = dataList[i].curName;
                            break;
                        }
                    }
                }
            }
        }
        return curName;
    }

})


function reset() {
    bt.corp.init().get("netId").val("");
    bt.corp.init().get("corpId").val("");
    $("#account").val("");
    bt.dicts.init().get("accState").val("");
    bt.dicts.init().get("accType").val("");
    bt.selectType.init().get("subjectId").val("");
    bt.dicts.init().get("sumType").val("");
    bt.selectType.init().get("curId").val("");
    bt.dateType.init().get("gedCloseDate").val("");
    bt.dateType.init().get("ledCloseDate").val("");
}

function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

/*function changeByType(data) {
	if(data.value != "4"){
		$("#blnctrAcc").attr("disabled","true");
		$("#blnctrAcc").val("");
	}else {
		$("#blnctrAcc").removeAttr("disabled");
	}

}*/

/*
function initChangeByType() {
	var type = bt.dicts.init().get("sumType").val();
	if(type != "4"){
		$("#blnctrAcc").attr("disabled","true");
		$("#blnctrAcc").val("");
	}else {
		$("#blnctrAcc").removeAttr("disabled");
	}

}*/
