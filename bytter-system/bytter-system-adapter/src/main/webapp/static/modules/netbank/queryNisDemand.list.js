var datatable;
var dateFun;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'selectType', 'element','selectTable', 'mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    //单位回调函数
    bt.corpCallback = function (id) {
        //重新加载收款账号
        bankAccInit.get("bankAcc").reload({ corpId: id });
    }
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();
    dateFun = bt.dateType.getFun();
    bt.dicts.init();
    bt.mulselect.init();
    //需求账号
    var bankAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",}
    ];
    var bankAccInit = bt.selectTable.init({
        elem: "#bankAccTable",
        url: basePath + "/netbank/nisdemand/getBankAccQuery",
        urldata: {resource:'netbank:nisDemand:query'},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#eqs_bankAcc",
        paging: true,
        columns: bankAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#eqs_bankAcc').val(data.bankAcc);
            } else {
                $('input.text').val('');
            }
        }
    });

    var superBankAccInit = bt.selectTable.init({
        elem: "#superBankAccTable",
        url: basePath + "/netbank/nisdemand/getBankAccQuery",
        urldata: {resource:'netbank:nisDemand:query'},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#eqs_superBankAcc",
        paging: true,
        columns: bankAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#eqs_superBankAcc').val(data.bankAcc);
            } else {
                $('input.text').val('');
            }
        }
    });

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[1,'desc']],
        checkbox: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/netbank/nisdemand/datatableQuery"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "billNo", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "accName", bSortable: false},
            {data: "bankName", bSortable: false},
            {data: "bal", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bal);
                }
            },
            {data: "availBal", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.availBal);
                }
            },
            {data: "demandType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisDemand.demandType",full.demandType);
                }
            },
            {data: "demandDate", bSortable: true},
            {data: "demandMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.demandMoney);
                }
            },
            {data: "demandAllocationMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.demandAllocationMoney);
                }
            },
            {data: "payBillNo", bSortable: true},
            {data: "payBillDate", bSortable: true},
            {data: "superCorpName", bSortable: false},
            {data: "superBankAcc", bSortable: true},
            {data: "superAccName", bSortable: false},
            {data: "superBankName", bSortable: false},
            {data: "status", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisDemand.status",full.status);
                }
            },
            {data: "failReason", bSortable: false,render: function (data, type, full, meta) {
                    var html = "";
                    if ("1" == full.status || "4" == full.status){
                        html = data;
                    }
                    return html;
                }
            },
            {data: "allocationNo", bSortable: false,
                render: function (data, type, full, meta) {
                    var html = "";
                    if(full.allocationNo != null) {
                        html += '<a href="#" onclick="view(\''+full.allocationId+'\')" >' + full.allocationNo + '</a>';
                    }
                    return html;
                }
            },
            {data: "allocationDate", bSortable: false},
            {data: "isPos", bSortable: false,render: function (data, type, full, meta) {
                    var html = "否";
                    if ("1" == data){
                        html = '<a href="#" onclick="viewPos(\''+full.id+'\')" >是</a>';
                    }
                    return html;
                }
            },
            {data: "posStatus", bSortable: false,render: function (data, type, full, meta) {
                    var html = "";
                    if ("1" == data){
                        html = "等待结果";
                    }
                    if ("2" == data){
                        html = "交易成功";
                    }
                    if ("3" == data){
                        html = "部分成功";
                    }
                    if ("4" == data){
                        html = "交易失败";
                    }
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '10',
                    dataName: 'demandMoney'
                }
            ]
        }
    });
});

function view(id) {
    if (bt.navtab) {
        var url = basePath + "/bdlink/bisexc/view/" + id;
        console.log(url);
        bt.navtab.addNavtab(bt.lang('common.view.data'), url);
        return;
    }
}

function viewPos(id){
    if (bt.navtab) {
        var url = basePath + "/allocation/nisallocationreq/demandList?erpId=" + id;
        bt.navtab.addNavtab(bt.lang('查询'), url);
        return;
    }
}









