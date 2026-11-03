var rat = {"isInit": false, "table": "ratTable"};
var cmsProvideLoan = {"isInit": false, "table": "cmsProvideLoanTable"};
var cmsLoan = {"isInit": false, "table": "cmsLoanTable"};
var bill = {"isInit": false, "table": "billTable"};
var international = {"isInit": false, "table": "internationalTable"};
var inl = {"isInit": false, "table": "inlTable"};

bt.use(['element', 'form', 'layedit', 'dateType', 'dicts', 'datatable', 'moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    var element = layui.element();
    dataType = bt.dateType.init();
    dicts = bt.dicts.init();
    moneyFun = bt.moneyType.getFun();
    element.on('tab(Gua)', function (data) {
        var layId = this.getAttribute('lay-id');
        initTableSet(layId);
    });
    $("#syscode").val("rat");
    initTable("#ratTable", rat.columns, {
        outputTitle: bt.lang("table.guaContract.ratUseInfo"),
        outputFilename: bt.lang("table.guaContract.ratUseInfo")
    }, "/cms/rat/ratbkration/getGuaContract");
    rat.isInit = true;
});

/**
 * 初始化操作
 * @param layId
 * @param columns
 */
function initTable(id, columns, exportFile, url) {
    bt.datatable.initMultiTables({
        elem: id,
        colReorder: true,
        // order: [1, 'desc'],
        ajax: {
            'url': basePath + url
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            customizeFormSearchFunction: function () {
                initTableSet($('#tab_ul').find('.layui-this').attr('lay-id'));
            }
        },
        exportFile: exportFile,
        columns: columns
    });
}

/**
 * 查看
 * @param id
 */
function view(id) {
    var sysCode = $("#syscode").val();
    var url = basePath;
    switch (sysCode) {
        case "accebill":
            url += "/bill/accebill/abemitbill/view?viceId=" + id;
            break;
        case "provide":
            url += "/cms/provide/loaninfo/viewProvideLoanInfoById?id=" + id + "&show=1";
            break;
        case "loan":
            url += "/cms/loan/cmsloaninfo/view/" + id;
            break;
        case "lcs":
            url += "/international/credit/createCredit/viewCredit?id=" + id + "&showCloseBtn=1";
            break;
        case "inl":
            url += "/inl/info/view?id=" + id + "&flag=1";
            break;
        default:
            url += "/cms/rat/ratbkration/view/" + id;
    }

    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), url);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1120px', '800px'],
        maxmin: true,
        content: url
    });
}

/**
 * 变更修改
 * @param id
 */
function edit(id, corpId, curId, bilCode) {
    var sysCode = $("#syscode").val();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('rat.button.changeGua'), basePath + "/cms/guarantee/guaContractChange/changeGuaContract?id=" + id + "&sysCode=" + sysCode + "&corpId=" + corpId + "&curId=" + curId + "&billCode=" + bilCode);
        return;
    }
}

/**
 * 初始化设置
 * @param layId
 */
function initTableSet(layId) {
    $("#syscode").val(layId);
    var tableId = null;
    var columns = null;
    var obj = null;
    var file = {}
    var url = ''
    if ("rat" == layId) {
        tableId = "#" + rat.table;
        columns = rat.columns;
        obj = rat;
        file.outputTitle = bt.lang("table.guaContract.ratUseInfo");
        file.outputFilename = bt.lang("table.guaContract.ratUseInfo");
        url = "/cms/rat/ratbkration/getGuaContract"
    } else if ("provide" == layId) {
        tableId = "#" + cmsProvideLoan.table;
        columns = cmsProvideLoan.columns;
        obj = cmsProvideLoan;
        file.outputTitle = bt.lang("table.guaContract.CmsProvideLoanUseInfo");
        file.outputFilename = bt.lang("table.guaContract.CmsProvideLoanUseInfo");
        url = "/cms/provide/loaninfo/getGuaContract"
    } else if ("loan" == layId) {
        tableId = "#" + cmsLoan.table;
        columns = cmsLoan.columns;
        obj = cmsLoan;
        file.outputTitle = bt.lang("table.guaContract.CmsLoanUseInfo");
        file.outputFilename = bt.lang("table.guaContract.CmsLoanUseInfo");
        url = "/cms/loan/cmsloaninfo/getGuaContract"
    } else if ("accebill" == layId) {
        tableId = "#" + bill.table;
        columns = bill.columns;
        obj = bill;
        file.outputTitle = bt.lang("table.guaContract.billUseInfo");
        file.outputFilename = bt.lang("table.guaContract.billUseInfo");
        url = "/bill/accebill/abvoucherinfoVice/getGuaContract"
    } else if ("lcs" == layId) {
        tableId = "#" + international.table;
        columns = international.columns;
        obj = international;
        file.outputTitle = bt.lang("table.guaContract.lcUseInfo");
        file.outputFilename = bt.lang("table.guaContract.lcUseInfo");
        url = "/international/credit/supplementCredit/getGuaContract"
    } else if ("inl" == layId) {
        tableId = "#" + inl.table;
        columns = inl.columns;
        obj = inl;
        file.outputTitle = bt.lang("内部拆借使用担保情况");
        file.outputFilename = bt.lang("内部拆借使用担保情况");
        url = "/inl/info/getGuaContract"
    }
    if (!obj.isInit) {
        initTable(tableId, columns, file, url);
        obj.isInit = true;
    } else {
        $.fn.dataTable.Api(tableId).draw();
    }
}

rat.columns = [
    {data: "itemNo", className: 'center', bSortable: false},//授信编号
    {data: "ratContractNo", className: 'center', bSortable: true},//授信合同号
    {
        data: "ratMode", className: 'center', bSortable: false,//授信模式
        render: function (data, type, full, meta) {
            return bt.dict("RatBkration.RatMode", full.ratMode);
        }
    },
    {
        data: "money", className: 'textRight', bSortable: false,//授信额度
        render: function (data, type, full, meta) {
            var money = moneyFun.format(full.money);
            return money;
        }
    },
    {
        data: "beginDate", className: 'center', bSortable: false,//授信开始时间
        render: function (data, type, full, meta) {
            return bt.date.format(full.beginDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "endDate", className: 'center', bSortable: false,//授信结束时间
        render: function (data, type, full, meta) {
            return bt.date.format(full.endDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "useDate", className: 'center', bSortable: false,//使用时间
        render: function (data, type, full, meta) {
            return bt.date.format(full.useDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "status", bSortable: false, className: "center",//操作
        render: function (data, type, full, meta) {
            var explode = "<span class=\"text-explode\">|</span>"
            var id = full.id;
            var param = "";
            param += "'" + full.id + "'";
            param += ",'" + full.corpId + "'";
            param += ",'" + full.curId + "'";
            param += ",'" + full.itemNo + "'";
            var html = "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"edit(" + param + ")\">" + bt.lang("rat.button.changeGua") + "</a>";
            html += explode + "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"view('" + id + "')\">" + bt.lang("common.button.view") + "</a>";
            return html;
        }
    }
];

bill.columns = [
    {
        data: "appNo",//开票申请单号
        className: 'center',
        bSortable: false
    },
    {
        data: "billType",//票据类型
        className: 'center',
        bSortable: true,
        render: function (data, type, full, meta) {
            return bt.dict("AB_BILL_TYPE", full.billType);
        }
    },
    {
        data: "billFlag",//流通方式
        className: 'center',
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.dict("AB_BILL_FLAG", full.billFlag);
        }
    },
    {
        data: "origNo",//票据号码
        className: 'center',
        bSortable: false,

    },
    {
        data: "billMoney",
        className: 'textRight',
        bSortable: false,//票据金额
        render: function (data, type, full, meta) {
            var money = moneyFun.format(full.billMoney);
            return money;
        }
    },
    {
        data: "payer",
        className: 'center',
        bSortable: false//出票人
    },
    {
        data: "payee",
        className: 'center',
        bSortable: false,//收票人
    },
    {
        data: "acceptor",//承兑人
        bSortable: false,
        className: "center"
    },
    {
        data: "startDate",//出票日期
        bSortable: false,
        className: "center",
        render: function (data, type, full, meta) {
            return bt.date.format(full.startDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "endDate",//到期日期
        bSortable: false,
        className: "center",
        render: function (data, type, full, meta) {
            return bt.date.format(full.endDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "useDate",//使用日期
        bSortable: false,
        className: "center",
        render: function (data, type, full, meta) {
            return bt.date.format(full.useDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "status", bSortable: false, className: "center",//操作
        render: function (data, type, full, meta) {
            var explode = "<span class=\"text-explode\">|</span>"
            var id = full.id;
            var param = "";
            param += "'" + full.id + "'";
            param += ",'" + full.corpId + "'";
            param += ",'" + full.curId + "'";
            param += ",'" + full.billCode + "'";
            var html = "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"edit(" + param + ")\">" + bt.lang("rat.button.changeGua") + "</a>";
            html += explode + "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"view('" + id + "')\">" + bt.lang("common.button.view") + "</a>";
            return html;
        }
    }
];
cmsLoan.columns = [
    {
        data: "billCode",//贷款流水号
        className: 'center',
        bSortable: false
    },
    {
        data: "loanGenre",//贷款类型
        className: 'center',
        bSortable: true
    },
    {
        data: "contractCode",//贷款合同编号
        className: 'center',
        bSortable: false
    },
    {
        data: "curName",//币种
        className: 'center',
        bSortable: false
    },
    {
        data: "loanMoney",//贷款金额
        className: 'textRight',
        bSortable: false,
        render: function (data, type, full, meta) {
            return moneyFun.format(full.loanMoney);
        }
    },
    {
        data: "loanDate",//贷款日期
        className: 'center',
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.date.format(full.loanDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "repayDate",//还款日期
        className: 'center',
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.date.format(full.repayDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "useDate",//使用日期
        className: 'center',
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.date.format(full.useDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "status",
        bSortable: false,
        className: "center",//操作
        render: function (data, type, full, meta) {
            var explode = "<span class=\"text-explode\">|</span>"
            var id = full.id;
            var param = "";
            param += "'" + full.id + "'";
            param += ",'" + full.corpId + "'";
            param += ",'" + full.curId + "'";
            param += ",'" + full.billCode + "'";
            var html = "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"edit(" + param + ")\">" + bt.lang("rat.button.changeGua") + "</a>";
            html += explode + "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"view('" + id + "')\">" + bt.lang("common.button.view") + "</a>";
            return html;
        }
    }
];
cmsProvideLoan.columns = [
    {
        data: "billCode",//借款单号
        className: 'center',
        bSortable: false
    },
    {
        data: "bankName",//借款银行
        className: 'center',
        bSortable: true
    },
    {
        data: "contractNo",//借款合同号
        className: 'center',
        bSortable: false
    },
    {
        data: "curName",//币种
        className: 'center',
        bSortable: false
    },
    {
        data: "plMoney",//借款金额
        className: 'textRight',
        bSortable: false,
        render: function (data, type, full, meta) {
            return moneyFun.format(full.plMoney);
        }
    },
    {
        data: "repayDate",//还款日期
        className: 'center',
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.date.format(full.repayDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "useDate",//使用时间
        className: 'center',
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.date.format(full.useDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "status",//操作
        bSortable: false,
        className: "center",
        render: function (data, type, full, meta) {
            var explode = "<span class=\"text-explode\">|</span>"
            var id = full.id;
            var param = "";
            param += "'" + full.id + "'";
            param += ",'" + full.corpId + "'";
            param += ",'" + full.curId + "'";
            param += ",'" + full.billCode + "'";
            var html = "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"edit(" + param + ")\">" + bt.lang("rat.button.changeGua") + "</a>";
            html += explode + "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"view('" + id + "')\">" + bt.lang("common.button.view") + "</a>";
            return html;
        }
    }
];

international.columns = [
    {
        data: "applicantName",//开证人
        className: 'center',
        bSortable: false
    },
    {
        data: "beneficiary",//受益人
        className: 'center',
        bSortable: false
    },
    {
        data: "bankName",//开证银行
        className: 'center',
        bSortable: false
    },
    {
        data: "creditNo",//信用证号
        className: 'center',
        bSortable: false
    },
    {
        data: "lcInter",//信用证类型
        className: 'center',
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.dict("letter_of_credit_inter", full.lcInter);
        }
    },
    {
        data: "creditMoney",//信用证金额
        className: 'textRight',
        bSortable: false,
        render: function (data, type, full, meta) {
            return moneyFun.format(full.creditMoney);
        }
    }, {
        data: "contractNo",//合同编号
        className: 'center',
        bSortable: false
    },
    {
        data: "issuingDate",//开证日期
        className: 'center',
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.date.format(full.issuingDate, 'yyyy-MM-dd');
        }
    }, {
        data: "expiryDate",//信用证有效日期
        className: 'center',
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.date.format(full.expiryDate, 'yyyy-MM-dd');
        }
    }, {
        data: "useDate",//使用日期
        className: 'center',
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.date.format(full.useDate, 'yyyy-MM-dd');
        }
    },
    {
        data: "status",//操作
        bSortable: false,
        className: "center",
        render: function (data, type, full, meta) {
            var explode = "<span class=\"text-explode\">|</span>"
            var id = full.id;
            var param = "";
            param += "'" + full.id + "'";
            param += ",'" + full.corpId + "'";
            param += ",'" + full.curId + "'";
            param += ",'" + full.billCode + "'";
            var html = "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"edit(" + param + ")\">" + bt.lang("rat.button.changeGua") + "</a>";
            html += explode + "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"view('" + id + "')\">" + bt.lang("common.button.view") + "</a>";
            return html;
        }
    }
];


inl.columns = [
    {
        data: "billNo",//内部拆借编号
        className: 'center',
        bSortable: false
    },
    {
        data: "guaContract",//内部资金拆借合同号
        className: 'center',
        bSortable: true
    },
    {
        data: "curName",//拆借资金币种
        className: 'center',
        bSortable: false
    },
    {
        data: "money",//拆借金额
        className: 'textRight',
        bSortable: false,
        render: function (data, type, full, meta) {
            return moneyFun.format(data);
        }
    },
    {
        data: "startDate",//拆借日期
        className: 'center',
        bSortable: false
    },
    {
        data: "endDate",//到期日期
        className: 'center',
        bSortable: false
    }, {
        data: "useDate",//使用时间
        className: 'center',
        bSortable: false
    }, {
        data: "status",//操作
        bSortable: false,
        className: "center",
        render: function (data, type, full, meta) {
            var explode = "<span class=\"text-explode\">|</span>"
            var id = full.id;
            var param = "";
            param += "'" + full.id + "'";
            param += ",'" + full.borrowInCorp + "'";
            param += ",'" + full.curId + "'";
            param += ",'" + full.billNo + "'";
            var html = "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"edit(" + param + ")\">" + bt.lang("rat.button.changeGua") + "</a>";
            html += explode + "<a class=\"\" href=\"#\" data-row=\"" + meta.row + "\" onclick=\"view('" + id + "')\">" + bt.lang("common.button.view") + "</a>";
            return html;
        }
    }
];
