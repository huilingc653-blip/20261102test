var selectType;
bt.use(['jquery', 'form', 'moneyType', 'selectType', 'printType', 'selectTree'], function () {
    bt.printType.init();

    //点击操作按钮事件
    $('#headerRight').on('click','button',function (e) {
        var targetEle = $('#'+$(this).data('link'));
        var type = $(this).data('type');
        var left = $('.table-header-container').width() - ($(this).offset().left + targetEle.outerWidth()+ 5);
        if (left < 0){
            if (type ==='print'){
                targetEle.css('left','-'+left+'px');
            }
        }
        targetEle.toggleClass('layui-hide');
        targetEle.siblings("div").addClass('layui-hide');
        return false;
    })
    //点击关闭下拉框
    $(document).on('click', function () {
        $('.operate-container').addClass('layui-hide');
    });

    // 科目加载前函数
    beforeInitSubject = function (nodes) {
        var newNodes = [];
        for (k in nodes) {
            var node = nodes[k];
            node.subjectNameText = "(" + node.subjectCode + ") " + node.subjectName;
            newNodes.push(nodes[k]);
        }
        return newNodes;
    }

    // 科目回调函数
    onChangeSubject = function (data) {
        if (data && data.id) {
            search(data);
        } else {
            noData();
        }
    };

    selectType = bt.selectType.init();
    moneyFun = bt.moneyType.getFun();

    init();

    /**
     * 初始化数据
     */
    function init() {
        if ($("#subjectId").val()) {
            search();
        } else {
            noData();
        }
    }

    /**
     * 搜索功能
     */
    function search(data) {
        $("#detailTable").html("");
        var param = {
            "netId": $("#netId").val(),
            "netName": $("#netName").text(),
            "curId": $("#curId").val(),
            "curName": $("#curName").text(),
            "subjectId": data ? data.id : $("#subjectId").val(),
            "balanceWay": data ? data.balanceway : $("#balanceWay").val(),
            "beginDate": $("#beginDate").val(),
            "endDate": $("#endDate").val(),
        };
        $.ajax({
            url: basePath + "/bi/financial/getGeneralLedgerDetailList/",
            method: 'POST',
            data: param,
            async: true,
            success: function (result) {
                //var data = JSON.parse(result);
                if (result && result.length) {
                    setDetail(result);
                } else {
                    noData();
                }
            }
        });
    }

    /**
     * 暂无数据
     */
    function noData() {
        $("#detailTable").html("");
        var html = " <tr class=\"odd\"><td colspan=\"6\" class='no-data'>" + bt.lang('common.msg.temporarily_no_data') + "</td></tr>";
        $("#detailTable").append(html);
    }

    /**
     * 设置明细数据
     */
    function setDetail(data) {
        // 科目余额方向
        var balanceWay;
        if ($("#balanceWay").val()) {
            balanceWay = $("#balanceWay").val();
        } else {
            var subjectId = selectType.get("detailSubjectId").getData();
            balanceWay = subjectId.obj.balanceWay
        }
        var detailHtml = getDetailHtml(balanceWay, data);
        $("#detailTable").append(detailHtml);
    }

    function getDetailHtml(balanceWay, data) {
        var html = "";
        for (var index in data) {
            var row = data[index];
            // 行数据类型 0-月份合计，1-本年累计，2-上年转入，3-本年年度累计数据
            if (row.rowDataType === 1) {
                html += "<tr>";
                html += "   <td class='center width80' ></td>";
                html += "   <td class='center width80' >" + bt.lang('bi.financial.generalLedger.yearTotal') + "</td>";
                html += "   <td class='textRight width150'>" + moneyFun.format(row.debitMoney) + "</td>";
                html += "   <td class='textRight width150'>" + moneyFun.format(row.creditMoney) + "</td>";
                html += "   <td class='center width50' ></td>";
                html += "   <td class='textRight width150'>" + moneyFun.format(row.balance) + "</td>";
                html += "</tr>";
            } else if (row.rowDataType === 2) {
                html += "<tr>";
                html += "   <td class='center width80'  ></td>";
                html += "   <td class='center width80'  >" + bt.lang('bi.financial.generalLedger.lastYear') + "</td>";
                html += "   <td class='textRight width150' ></td>";
                html += "   <td class='textRight width150' ></td>";
                html += "   <td class='center width50'  >" + bt.dict("Admin.BalanceWay", balanceWay) + "</td>";
                html += "   <td class='textRight width150' >" + moneyFun.format(row.balance) + "</td>";
                html += "</tr>";
            } else if (row.rowDataType === 3) {
                html += "<tr>";
                html += "   <td class='center width80' ></td>";
                html += "   <td class='center width80' >" + bt.lang('bi.financial.generalLedger.yearTotal') + "</td>";
                html += "   <td class='textRight width150' >" + moneyFun.format(row.debitMoney) + "</td>";
                html += "   <td class='textRight width150' >" + moneyFun.format(row.creditMoney) + "</td>";
                html += "   <td class='center width50'  ></td>";
                html += "   <td class='textRight width150' ></td>";
                html += "</tr>";
            } else {
                html += "<tr>";
                html += "   <td class='center width80' >" + row.ledgerDate + "</td>";
                html += "   <td class='center width80' >" + row.month + bt.lang('bi.financial.generalLedger.monthTotal') + " </td>";
                html += "   <td class='textRight width150' >" + moneyFun.format(row.debitMoney) + "</td>";
                html += "   <td class='textRight width150' >" + moneyFun.format(row.creditMoney) + "</td>";
                html += "   <td class='center width50'  >" + bt.dict("Admin.BalanceWay", balanceWay) + "</td>";
                html += "   <td class='textRight width150' >" + moneyFun.format(row.balance) + "</td>";
                html += "</tr>";
            }
        }
        return html;
    }

    getSubjectData = function () {
        var data;
        if ($("#showDetailSubject").val() == 1) {
            // 显示明细科目
            $.ajax({
                url: basePath + "/finacial/paramconfig/fcsubject/jsonSubjectDate",
                method: 'GET',
                async: false,
                success: function (result) {
                    data = result;
                }
            });

        } else {
            // 仅显示一级科目
            $.ajax({
                url: basePath + "/finacial/paramconfig/fcsubject/findSubjectByFatherId",
                method: 'GET',
                async: false,
                success: function (result) {
                    data = result;
                }
            });
        }
        return data;
    }

    getAllDetailHtml = function (LODOP) {
        var param = {
            "netId": $("#netId").val(),
            "netName": $("#netName").text(),
            "curId": $("#curId").val(),
            "curName": $("#curName").text(),
            "beginDate": $("#beginDate").val(),
            "endDate": $("#endDate").val(),
        };
        var style = '<link rel="stylesheet" href="' + basePath + '/static/plugins/layui/css/layui.css" media="all" />';
        style += '<link rel="stylesheet" href="' + basePath + '/static/css/common.css" />';
        // padding 上 右 下 左
        style += '<style>'
            + ' table {border:1px solid #000!important;font-family: 宋体;}'
            + ' table th {  border:1px solid #000!important; height:30px;font-size: 12px!important;padding: 6px 10px 6px 18px;}'
            + ' table td {  border:1px solid #000!important; font-size: 12px!important;line-height: 24px!important;padding: 5px 10px 5px 8px;min-height: 20px!important;}'
            + ' .width80 {width: 80px;} .width50 {width: 50px;}  .width150 {width: 150px;}'
            + ' .center {text-align:center}  .textRight{text-align:right;}'
            + ' </style>';


        // 获取科目信息
        var data = getSubjectData();
        if (data && data.length) {
            var pageIndex = 1;
            for (var index in data) {
                var subject = data[index];
                param["subjectId"] = subject.id;
                param["balanceWay"] = subject.balanceWay;
                // 循环打印所有科目页
                $.ajax({
                    url: basePath + "/bi/financial/getGeneralLedgerDetailList/",
                    method: 'POST',
                    data: param,
                    async: false,
                    success: function (result) {
                        //var data = JSON.parse(result);
                        if (result && result.length) {
                            // 组织当前科目页面数据
                            var html = "<table>" + $("#detailThead").html();
                            var detailHtml = getDetailHtml(subject.balanceWay, result);
                            html += detailHtml;
                            html += "</table>";

                            // 获取科目名称在哪几个打印页显示
                            var num = Math.ceil((result.length - 2) / 2 / 12);
                            var tempPageIndex = pageIndex;
                            for (var j = 2; j <= num; j++) {
                                pageIndex++;
                                tempPageIndex += "," + pageIndex;
                            }
                            // 科目名称
                            LODOP.ADD_PRINT_TEXT('21mm', '5mm', '40%', 100, $("#headerLeft").text() + subject.subjectName);
                            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
                            LODOP.SET_PRINT_STYLEA(0, "Alignment", 1);
                            LODOP.SET_PRINT_STYLEA(0, "PageIndex", tempPageIndex);

                            // 表格数据
                            var printHtml = '<head>' + style + '</head><body>' + html + '</body>';
                            LODOP.ADD_PRINT_TABLE('26mm', '0mm', 'RightMargin:3mm', "BottomMargin:9mm", printHtml);

                            // 强制分页
                            LODOP.NEWPAGEA();
                            pageIndex++;
                        }
                    }
                });
            }
        }
    }

    doPrintCurrent = function () {
        var subjectData = selectType.get("detailSubjectId").getData();
        if (!subjectData.obj) {
            return;
        }
        var printObj = bt.lodop.init();
        bt.loading.show();
        setTimeout(function () {
            LODOP = printObj.lodop;//获取lodop打印对象
            if (!LODOP) {
                return;
            }
            var style = '<link rel="stylesheet" href="' + basePath + '/static/plugins/layui/css/layui.css" media="all" />';
            style += '<link rel="stylesheet" href="' + basePath + '/static/css/common.css" />';
            // padding 上 右 下 左
            style += '<style>'
                + ' table {border:1px solid #000!important;font-family: 宋体;}'
                + ' table th {  border:1px solid #000!important; height:30px;font-size: 12px!important;padding: 6px 10px 6px 18px;}'
                + ' table td {  border:1px solid #000!important; font-size: 12px!important;line-height: 24px!important;padding: 5px 10px 5px 8px;min-height: 20px!important;}'
                + ' .width80 {width: 80px;} .width50 {width: 50px;}  .width150 {width: 150px;}'
                + ' .center {text-align:center}  .textRight{text-align:right;}'
                + ' </style>';

            LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Width:90%");
            // 表头 取网点名称
            LODOP.ADD_PRINT_TEXT('2mm', '50%', '100%', 100, $('#netName').html());
            LODOP.SET_PRINT_STYLEA(1, "FontSize", 18);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(1, "Bold", 1);
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('12mm', '50%', '100%', 100, bt.lang("bi.financial.generalLedger.generalLedger"));
            LODOP.SET_PRINT_STYLEA(2, "FontSize", 18);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(2, "Bold", 1);
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('21mm', '60%', '40%', 100, $("#headerCenter").text());  // 制表日期、币种
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 循环打印所有科目页
            $.ajax({
                url: basePath + "/bi/financial/getGeneralLedgerDetailList/",
                method: 'POST',
                data: {
                    "netId": $("#netId").val(),
                    "netName": $("#netName").text(),
                    "curId": $("#curId").val(),
                    "curName": $("#curName").text(),
                    "beginDate": $("#beginDate").val(),
                    "endDate": $("#endDate").val(),
                    "subjectId": subjectData.obj.id,
                    "subjectCode": subjectData.obj.subjectCode,
                    "subjectName": subjectData.obj.subjectName,
                    "balanceWay": subjectData.obj.balanceWay,
                },
                async: false,
                success: function (result) {
                    //var data = JSON.parse(result);
                    if (result && result.length) {
                        // 组织当前科目页面数据
                        var html = "<table>" + $("#detailThead").html();
                        var detailHtml = getDetailHtml(subjectData.obj.balanceWay, result);
                        html += detailHtml;
                        html += "</table>";

                        // 科目名称
                        LODOP.ADD_PRINT_TEXT('21mm', '5mm', '40%', 100, $("#headerLeft").text() + subjectData.obj.subjectName);
                        LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
                        LODOP.SET_PRINT_STYLEA(0, "Alignment", 1);
                        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

                        // 表格数据
                        var printHtml = '<head>' + style + '</head><body>' + html + '</body>';
                        LODOP.ADD_PRINT_TABLE('26mm', '0mm', 'RightMargin:3mm', "BottomMargin:9mm", printHtml);

                    }
                }
            });

            // 分页显示
            var pageHtml = "<font style='font-size:12px' format='0'>" +
                "<span tdata='pageNO'>" + bt.lang("common.table.the") + "##" + bt.lang("common.table.page") + "</span>" +
                "/<span tdata='pageCount'>" + bt.lang("common.table.total") + "##" + bt.lang("common.table.page") + "</span></font>";
            LODOP.ADD_PRINT_HTM('97%', '88%', 300, 100, pageHtml);
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.PREVIEW();
            bt.loading.hide();
        }, 800);
    }


    doPrintAll = function () {
        var printObj = bt.lodop.init();
        bt.loading.show();
        setTimeout(function () {
            LODOP = printObj.lodop;//获取lodop打印对象
            if (!LODOP) {
                return;
            }
            LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Width:90%");
            // 表头 取网点名称
            LODOP.ADD_PRINT_TEXT('2mm', '50%', '100%', 100, $('#netName').html());
            LODOP.SET_PRINT_STYLEA(1, "FontSize", 18);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(1, "Bold", 1);
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // LODOP.ADD_PRINT_HTM('8mm', '50%', "<font style='font-size:15px'><span>" + bt.lang("table.accountStatement.tableTitle") + "</span></font>");
            LODOP.ADD_PRINT_TEXT('12mm', '50%', '100%', 100, bt.lang("bi.financial.generalLedger.generalLedger"));
            LODOP.SET_PRINT_STYLEA(2, "FontSize", 18);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(2, "Bold", 1);
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 制表日期、币种
            LODOP.ADD_PRINT_TEXT('21mm', '60%', '40%', 100, $("#headerCenter").text());
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            getAllDetailHtml(LODOP);

            // 分页显示
            // 分页显示
            var pageHtml = "<font style='font-size:12px' format='0'>" +
                "<span tdata='pageNO'>" + bt.lang("common.table.the") + "##" + bt.lang("common.table.page") + "</span>" +
                "/<span tdata='pageCount'>" + bt.lang("common.table.total") + "##" + bt.lang("common.table.page") + "</span></font>";
            LODOP.ADD_PRINT_HTM('95%', '88%', 300, 100, pageHtml);
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.PREVIEW();
            bt.loading.hide();
        }, 2000);
    }
});

function doExportCurrentPage() {
    var subject = selectType.get("detailSubjectId").getData();
    if (!subject.obj) {
        return;
    }
    var param = {
        "netId": $("#netId").val(),
        "netName": $("#netName").text(),
        "curId": $("#curId").val(),
        "curName": $("#curName").text(),
        "subjectId": subject.obj.id,
        "subjectCode": subject.obj.subjectCode,
        "subjectName": subject.obj.subjectName,
        "balanceWay": subject.obj.balanceWay,
        "balanceWayName": bt.dict("Admin.BalanceWay", subject.obj.balanceWay),
        "beginDate": $("#beginDate").val(),
        "endDate": $("#endDate").val()
    };
    var url = basePath + "/bi/financial/exportCurrentPage/";
    openExportExcel(url, param);
}

function doExportAll() {
    var subject = selectType.get("detailSubjectId").getData();
    if (!subject.obj) {
        return;
    }
    var param = {
        "netId": $("#netId").val(),
        "netName": $("#netName").text(),
        "curId": $("#curId").val(),
        "curName": $("#curName").text(),
        "beginDate": $("#beginDate").val(),
        "endDate": $("#endDate").val()
    };
    // 获取科目信息
    var subjectParam = getSubjectData();
    var url = basePath + "/bi/financial/exportAll/";
    openExportExcel(url, param, subjectParam);
}

function openExportExcel(exportUrl, param, subjectParam) {
    var form = $("<form></form>").attr("style", "display:none").attr("action", exportUrl).attr("method", "post");
    try {
        form.append($("<input></input>").attr("type", "hidden").attr("name", "param").attr("value", encodeURIComponent(JSON.stringify(param))));
        if (subjectParam) {
            form.append($("<input></input>").attr("type", "hidden").attr("name", "subjectParam").attr("value", encodeURIComponent(JSON.stringify(subjectParam))));
        }
        form.appendTo('body');
        form.submit();
    } catch (e) {
        console.log(e);
    } finally {
        form.remove();
    }
}