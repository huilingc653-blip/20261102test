var corpInit;
var dateType;
var selectType;
var dicts;
var moneyType;
var moneyFun;
bt.use(['form', 'bttable', 'btdate', 'datatable', 'corp', 'moneyType', 'laypage', 'dateType', 'selectType', 'date', 'dicts', 'jquery', 'element', 'selectTable', 'page', 'printType'], function () {
    $ = bt.jquery, form = bt.form();
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    corpInit = bt.corp.init();
    bt.printType.init();
    bt.selectTable.init({
        elem: "#example",
        url: basePath + "/inneraccount/fcacc/getValidAccountIsHavDel",
        scrollX: "500px",
        scrollY: "200px",
        searchInput: "#account",
        columns: [
            {data: "blnctrAcc",},
            {data: "accName",}
        ],
        clickCallback: function (data) {
            //设置账户
            $("#account").val(data.blnctrAcc);
        }
    });
    var page = null;
    var table = null;
    var headOtherInfo = null;
    table = bt.bttable.init({
        elem: "#appTable",
        /* scrollY:"800px",*/
        url: basePath + "/finacial/report/fcbookdtlquery/loadData",
        urldata: getParam(),
        columns: [
            {data: "tallyDate", className: "center", bSortable: false},
            {
                data: "voucherNo", className: "center", bSortable: true, render: function (data, type, full, meta) {
                    return "<a href='javaScript:void(0)' onclick='view(\"" + data.id + "\")'>" + data.voucherNo + "</a> ";
                }
            },
            {data: "voucherName", className: "center", bSortable: true},
            {data: "summaryContent", className: "center", bSortable: true},
            {
                data: "debitMoney", className: "textRight", bSortable: false,
                render: function (data, type, full, meta) {
                    var money = moneyFun.format(data.debitMoney);
                    return money;
                }
            },
            {
                data: "creditMoney", className: "textRight", bSortable: false,
                render: function (data, type, full, meta) {
                    var money = moneyFun.format(data.creditMoney);
                    return money;
                }
            },
            {data: "balanceWay", className: "center", bSortable: true},
            {
                data: "balance", className: "textRight", bSortable: false,
                render: function (data, type, full, meta) {
                    var money = moneyFun.format(data.balance);
                    return money;
                }
            }
        ],
        tableInitCallback: function (data) {
            headOtherInfo(data);
        },
        tableDrawCallback: function (data) {
            if (!page && data) {
                var pages;
                if (data.length) {
                    pages = data[0].accCount
                } else {
                    pages = 1;
                }
                page = bt.page.init({
                    elem: '#page',//分页组件容器
                    pages: pages,
                    count: data.length, //表格总页码
                    currentPage: $("#currentPage").val(),//当前页码，默认值为 0,即第一页
                    len: data.length, //当前显示条数
                    lengthMenu: false,
                    custom: {
                        start: 1
                    },
                    jump: function (obj) { //翻页回调方法
                        var curr = obj.curr;
                        var len = obj.len;
                        $("#currentPage").val(curr);
                        if (table) {
                            table.draw({
                                url: basePath + "/finacial/report/fcbookdtlquery/loadData",
                                urldata: getParam() + "&querySign=1"
                            })
                        }
                    }
                })
            } else {
                var elem = $("#page").find('.wrapper');
                var pages;
                var currentPage = $("#currentPage").val();
                var start = 1;
                if (data.length) {
                    pages = data[0].accCount;
                } else {
                    pages = 1;
                    currentPage = 1;
                    start = 0;
                }
                elem.find('.total').text(pages);
                elem.find('.table-record .table-startRecord1').text(start);
                elem.find('.table-pageNumber').val(currentPage);//更新当前页码
                page.updateStyle(elem.find('.table-record '), currentPage, pages);//更新样式
                elem.find('.table-record .table-endRecord1').text(data.length);//更新结束数据条数
                elem.find('.table-record .table-totalRecord1').text(data.length);//更新总条数
            }
            headOtherInfo(data);
        }
    });

    //监听提交
    form.on('submit(search)', function (data) {

        if (table) {
            $("#currentPage").val(1);
            table.draw({
                url: basePath + "/finacial/report/fcbookdtlquery/loadData",
                urldata: getParam() + "&querySign=1"
            })
        }

    });

    form.on('submit(excel)', function (data) {
        var url = basePath + "/finacial/report/fcbookdtlquery/exportExcel";
        DownLoadFile({url: url, data: data.field, method: 'post'});
        return false;
    });

    headOtherInfo = function (data) {
        if (data) {
            var sumDebitMoney = 0;
            var sumCreatMoney = 0;
            layui.each(data, function (i, d) {
                sumDebitMoney += parseFloat(d.debitMoney);
                sumCreatMoney += parseFloat(d.creditMoney);
            })
            if (data.length) {
                $("#accountStr").html(data[0].account);
                $("#accName").html(data[0].accName);
                $("#curName").html(data[0].curName);
                $("#subjectName").html(data[0].subjectName);
                $("#beginningBalance").html(moneyFun.format(data[0].beginningBalance));
                $("#count").val(data[0].accCount);
                // 网点名称
                $("#netName").val(data[0].netName);
            } else {
                //清空账户信息
                $("#search tr").each(function () {
                    $(this).children('td').children('span').text("");
                });
                //清空合计
                // $("#sum").each(function () {
                //     $(this).children('td').eq(4).text('');
                //     $(this).children('td').eq(5).text('');
                // });
            }
            // $("#sum").find('td[name="sumDebitMoney"]').text(moneyFun.format(sumDebitMoney));
            // $("#sum").find('td[name="sumCreditMoney"]').text(moneyFun.format(sumCreatMoney));

            // 附加 合计节点
            $("#appTable tbody").append(createdSumNodeElement(moneyFun.format(sumDebitMoney), moneyFun.format(sumCreatMoney)));
        } else {
            $("#appTable").append(createEmptyDataNodeElement());
        }
    }

    /**
     * 创建汇总节点
     * @param sumDebitMoney
     * @param sumCreditMoney
     * @returns {jQuery|HTMLElement}
     */
    function createdSumNodeElement(sumDebitMoney, sumCreditMoney) {
        var html = '<tr>';
        html += '<td align="center">' + bt.lang('table.interet.sum') + '</td>';
        html += '<td></td>';
        html += '<td></td>';
        html += '<td></td>';
        html += '<td align="right">' + sumDebitMoney + '</td>';
        html += '<td align="right">' + sumCreditMoney + '</td>';
        html += '<td></td>';
        html += '<td></td>';
        html += '</tr>';
        return $(html);
    }

    function createEmptyDataNodeElement() {
        var html = '<tbody>';
        var html = '<tr class="odd" style="height: 27px;">';
        html += '<th colspan="8" align="center">' + bt.lang('common.msg.temporarily_no_data') + '</th>';
        html += '</tr>';
        html += '</tbody>';
        return $(html);
    }

    // 打印按钮点击事件
    $("#printBt").click(function () {
        printHorizontal();
    });
    // 批量打印按钮点击事件
    $("#printBatch").click(function () {
        parent.layer.open({
            type: 2,
            area: ['1120px', '700px'],
            maxmin: true,
            content: basePath + "/finacial/report/fcbookdtlquery/batchPrint?"+getParam()
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
                + ' table.layui-table{border:1px solid #000!important;font-family:"Microsoft YaHe";}'
                + ' table th {border:1px solid #000!important; height:20px;font-size: 12px!important;}'
                + ' table td {border:1px solid #000!important; font-size: 12px!important; line-height: 15px!important;min-height: 20px!important;}'
                + ' .center {text-align:center} .textLeft{text-align:left;} .left{text-align:left;}  .textRight{text-align:right;} .right{text-align:right;}'
                + ' </style>';

            var allhtml = document.getElementById(printAreaElem).innerHTML;
            var html = '<head>' + style + '</head><body>' + allhtml + '</body>';
            LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Width:90%");
            // 表头 取网点名称
            LODOP.ADD_PRINT_TEXT('2mm', '0', '500', 100, $('#netName').val());
            LODOP.SET_PRINT_STYLEA(1, "FontSize", 18);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('13mm', '0', '200', 100, "明细账");
            LODOP.SET_PRINT_STYLEA(2, "FontSize", 18);
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            // 文本框里 内容对于文本框居中
            LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
            // 打印项在纸张中水平居中
            LODOP.SET_PRINT_STYLEA(0, "Horient", 2);
            // 每一页都打印
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 表头信息
            // LODOP.ADD_PRINT_HTM('15mm', '0mm', '100%', 200, $("#headInfoContainerElement").html());
            // LODOP.SET_PRINT_STYLEA(0, "FontName", "微软雅黑");
            // LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('22mm', '10%', '300', 100, bt.lang("table.fcAccbookDtl.account") + '：' + $("#accountStr").html());   // 账号
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('22mm', '40%', '300', 100, bt.lang("table.fcAccbookDtl.accName") + '：' + $("#accName").html());   // 户名
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('22mm', '75%', '300', 100, bt.lang("table.fcBusinessRuleDefine.curCode") + '：' + $("#curName").html());   // 币别
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('28mm', '10%', '300', 100, bt.lang("table.fcAccbookDtl.subjectCode") + '：' + $("#subjectName").html());   // 科目
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.ADD_PRINT_TEXT('28mm', '75%', '300', 100, bt.lang("table.accountSummary.beginning_balance") + '：' + $("#beginningBalance").html());   // 期初余额
            LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            // 表格数据
            LODOP.ADD_PRINT_TABLE('34mm', '0mm', 'RightMargin:0mm', "BottomMargin:10mm", html);

            // 制表日期
            LODOP.ADD_PRINT_HTM('97%', '3%', 300, 120, "<font style='font-size:12px' format='0'><span>" + bt.lang('com.table.makeDate') + ':' + getNowFormatDate() + "</span></font>");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
            // 分页显示
            LODOP.ADD_PRINT_HTM('97%', '88%', 300, 100, "<font style='font-size:12px' format='0'><span tdata='pageNO'>第##页</span>/<span tdata='pageCount'>共##页</span></font>");
            LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

            LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "100%");
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

var getParam = function () {
    var param = "";
    param += "netId=" + corpInit.get('netId').val() +
        "&gedTallyDate=" + dateType.get('gedTallyDate').val() + "&ledTallyDate=" + dateType.get('ledTallyDate').val() +
        "&account=" + $("#account").val() + "&gesVoucherNo=" + $("#gesVoucherNo").val() +
        "&lesVoucherNo=" + $("#lesVoucherNo").val() + "&voucherState=" + dicts.get('voucherState').val() +
        "&curCode=" + selectType.get('curCode').val() +
        "&debitMoney=" + moneyType.get("debitMoney").val() +
        "&creditMoney=" + moneyType.get("creditMoney").val() + "&start=" + $("#currentPage").val() + "&subjectId=" + selectType.get('subjectId').val();
    return param;
}

function view(id) {
    parent.layer.open({
        type: 2,
        area: ['1120px', '700px'],
        maxmin: true,
        content: basePath + "/finacial/accbook/fcaccbookblned/view?id=" + id
    });
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