var rat = {"isInit": false, "table": "ratTable"};
var cmsProvideLoan = {"isInit": false, "table": "cmsProvideLoanTable"};
var cmsLoan = {"isInit": false, "table": "cmsLoanTable"};
var bill = {"isInit": false, "table": "billTable"};
var international = {"isInit": false, "table": "internationalTable"};
var inl = {"isInit": false, "table": "inlTable"};
var cmsLease = {"isInit": false, "table": "cmsLeaseTable"};
var cmsBond = {"isInit": false, "table": "cmsBondTable"};
var cmsSecurity = {"isInit": false, "table": "cmsSecurityTable"};
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
    initTable("#ratTable", rat.columns, "/cms/rat/ratbkration/getGuaContract");
    rat.isInit = true;
});

/**
 * 初始化操作
 * @param layId
 * @param columns
 */
function initTable(id, columns,url) {
    bt.datatable.initMultiTables({
        elem: id,
        colReorder: true,
        order: [1, 'desc'],
        ajax: {
            'url': basePath + url,
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            customizeFormSearchFunction: function () {
                initTableSet($('#tab_ul').find('.layui-this').attr('lay-id'));
            }
        },
        columns: columns
    });
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
    var url;
    if ("rat" == layId) {
        tableId = rat.table;
        columns = rat.columns;
        obj = rat;
        url = "/cms/rat/ratbkration/getGuaContract"
    } else if ("provide" == layId) {
        tableId = cmsProvideLoan.table;
        columns = cmsProvideLoan.columns;
        obj = cmsProvideLoan;
        url = "/cms/provide/loaninfo/getGuaContract"
    } else if ("loan" == layId) {
        tableId = cmsLoan.table;
        columns = cmsLoan.columns;
        obj = cmsLoan;
        url = "/cms/loan/cmsloaninfo/getGuaContract"
    } else if ("accebill" == layId) {
        tableId = bill.table;
        columns = bill.columns;
        obj = bill;
        url = "/bill/accebill/abvoucherinfoVice/getGuaContract"
    } else if ("lcs" == layId) {
        tableId = international.table;
        columns = international.columns;
        obj = international;
        url = "/international/credit/supplementCredit/getGuaContract"
    } else if ("inl" == layId) {
        tableId = inl.table;
        columns = inl.columns;
        obj = inl;
        url = "/inl/info/getGuaContract"
    } else if ("lease" == layId) {
        tableId = cmsLease.table;
        columns = cmsLease.columns;
        obj = cmsLease;
        url = "/cms/lease/financing/info/getGuaContract"
    } else if ("cmsBond" == layId) {
        tableId = cmsBond.table;
        columns = cmsBond.columns;
        url = "/cms/guarantee/guaContractOccup/getUseContractList";
        obj = cmsBond;
    }else if ("security" == layId){
        tableId = cmsSecurity.table;
        columns = cmsSecurity.columns;
        obj = cmsSecurity;
        url = "/cms/guarantee/guaContractOccup/getUseContractList";
    }else {
        tableId = rat.table;
        columns = rat.columns;
        obj = rat;
        url = "/cms/rat/ratbkration/getGuaContract"
    }
    if (!obj.isInit) {
        initTable("#" + tableId, columns,url);
        obj.isInit = true;
    } else {
        $.fn.dataTable.Api("#" + tableId).draw();
    }
}

rat.columns = [
    {data: "itemNo", className: 'center', bSortable: false},//授信编号
    {data: "ratContractNo", className: 'center', bSortable: false},//授信合同号
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
        data: "status", bSortable: false, className: "center",//状态
        render: function (data, type, full, meta) {
            return bt.dict("Contract.UseStatus", full.status);
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
        bSortable: false,
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
        data: "status", bSortable: false, className: "center",//状态
        render: function (data, type, full, meta) {
            return bt.dict("Contract.UseStatus", full.status);
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
        bSortable: false
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
        className: "center",//状态
        render: function (data, type, full, meta) {
            return bt.dict("Contract.UseStatus", full.status);
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
        bSortable: false
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
        data: "status",//状态
        bSortable: false,
        className: "center",
        render: function (data, type, full, meta) {
            return bt.dict("Contract.UseStatus", full.status);
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
        data: "status",//状态
        bSortable: false,
        className: "center",
        render: function (data, type, full, meta) {
            return bt.dict("Contract.UseStatus", full.status);
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
        bSortable: false
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
            return bt.dict("Contract.UseStatus", full.status);
        }
    }
];

cmsLease.columns = [
    {
        data: "leaseNo",//租赁单号
        className: 'center',
        bSortable: false
    },
    {
        data: "leaseCode",//租赁合同号
        className: 'center',
        bSortable: false
    },
    {
        data: "oriCurName",//币种
        className: 'center',
        bSortable: false
    },
    {
        data: "oriMoney",//金额
        className: "textRight",
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.money.format(full.oriMoney);
        }
    },
    {
        data: "beginDate",//起租日
        className: 'center',
        bSortable: false
    },
    {
        data: "endDate",//到期日
        className: 'center',
        bSortable: false
    }, {
        data: "useDate",//使用时间
        className: 'center',
        bSortable: false
    },
    {
        data: "status",//操作
        bSortable: false,
        className: "center",
        render: function (data, type, full, meta) {
            return bt.dict("Contract.UseStatus", full.status);
        }
    }
];

cmsBond.columns = [
    {
        data: "bondIssueNum",//债券发行编号
        className: 'center',
        bSortable: false
    },
    {
        data: "oriCurName",//币种
        className: 'center',
        bSortable: false
    },
    {
        data: "oriMoney",//金额
        className: "textRight",
        bSortable: false,
        render: function (data, type, full, meta) {
            return bt.money.format(full.oriMoney);
        }
    },
    {
        data: "issueStartDate",//发行开始日
        className: 'center',
        bSortable: false
    },
    {
        data: "issueEndDate",//发行结束日
        className: 'center',
        bSortable: false
    }, {
        data: "useDate",//使用时间
        className: 'center',
        bSortable: false
    },
    {
        data: "status",//操作
        bSortable: false,
        className: "center",
        render: function (data, type, full, meta) {
            return bt.dict("Contract.UseStatus", full.status);
        }
    }
];

cmsSecurity.columns = [
    {data: "securityIssueCode", className: "center", bSortable: false},
    {data: "underwriter", className: "center", bSortable: false},
    {data: "batchNo", className: "center", bSortable: false},
    {data: "levelName", className: "center", bSortable: false},
    {data: "issueMoney", className: "textRight", render: function (data, type, full, meta) {
            return bt.money.format(full.issueMoney);
        }
    },
    {
        data: "issueDate", className: "center", render: function (data, type, full, meta) {
            return bt.date.format(full.issueDate, 'yyyy-mm-dd');
        }
    },
    {
        data: "expectEndDate", className: "center", render: function (data, type, full, meta) {
            if(full.expectEndDate){
                return bt.date.format(full.expectEndDate, "yyyy-MM-dd");
            }
            return "";
        }
    },
    {data: "useDate", className: 'center', bSortable: false},
    {data: "status", bSortable: false, className: "center", render: function (data, type, full, meta) {
            return bt.dict("Contract.UseStatus", full.status);
        }
    }
];
