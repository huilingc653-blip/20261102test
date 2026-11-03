bt.use(['form','datatable','selectType','dicts','rateType','corp','dateType','mulselect'],function () {
    var selectType=bt.selectType.init();
    var dict = bt.dicts.init();
    var corp = bt.corp.init();
    bt.dateType.init();
    bt.mulselect.init();
    var rateType = bt.rateType.getFun();
    table = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        colReorder:true,
        searchForm:{
            formElem: "#searchForm",
            formFilter: "search"
        },
        fixedColumns: {
            rightColumns:2,//代表冻结右边一列
        },
        ajax: {
            'url': basePath+"/chowmatistic/choproductreqquery/uncollectedDatatable"
        },
        buttons:[
            {
                extend: "colvis",
                text: bt.lang("common.button.theadHide"),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns:[
            {data:"corpName",bSortable:true,className:'center'},
            {data:"bankTypeName",bSortable:true,className:'center'},
            {data:"bankName",bSortable:true,className:'center'},
            {data:"financialTypeName",bSortable:true,className:'center'},
            {data:"productType",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("cho.productType",full.productType);
                }
            },
            {data:"productCode",bSortable:true,className:'center'},
            {data:"productName",bSortable:true,className:'center'},
            {data:"bankAcc",bSortable:true,className:'center'},
            {data:"buyMoney",bSortable:true,className:'textRight',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.buyMoney);
                }
            },
            {data:"curName",bSortable:true,className:'center'},
            {data:"dayLimit",bSortable:true,className:'center'},
            {data:"buyDate",bSortable:true,className:'center'},
            {data:"interestStartDate",bSortable:true,className:'center'},
            {data:"deadlineDate",bSortable:true,className:'center'},
            {data:"calEndDate",bSortable:false,className:'center',
                render:function (data, type, full, meta) {
                    return bt.date.format(full.calEndDate, 'yyyy-MM-dd');;
                }
            },
            {data:"calDay",bSortable:false,className:'center'},
            {data:"referIncomeRate",bSortable:true,className:'textRight',
                render:function (data, type, full, meta) {
                    if (full.endReferIncomeRate !== undefined && full.endReferIncomeRate !== null) {
                        return rateType.format(full.referIncomeRate, 'percent', 6)
                            + '-' + rateType.format(full.endReferIncomeRate, 'percent', 6)
                    }
                    return rateType.format(full.referIncomeRate, 'percent', 6);
                }
            },
            {data:"referIncomeMoney",bSortable:true,className:'textRight',
                render:function (data, type, full, meta) {
                    if (full.endReferIncomeMoney !== undefined && full.endReferIncomeMoney !== null) {
                        return bt.money.format(full.referIncomeMoney)
                            + '-' + bt.money.format(full.endReferIncomeMoney)
                    }
                    return bt.money.format(full.referIncomeMoney);
                }
            },
            {data:"receivableMoney",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.receivableMoney);
                }
            },
            {data:"actualMoney",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.actualMoney);
                }
            },
            {data:"uncollectedMoney",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.uncollectedMoney);
                }
            },
            {data:"subFeeMoney",bSortable:true,className:'textRight',
                render:function (data, type, full, meta) {
                    if (full.subFeeMoney) {
                        return bt.money.format(full.subFeeMoney);
                    }else{
                        return '-';
                    }
                }
            },
            {data:"redeemChargeMoney",bSortable:true,className:'textRight',
                render:function (data, type, full, meta) {
                    if (full.redeemChargeMoney) {
                        return bt.money.format(full.redeemChargeMoney);
                    }else {
                        return '-';
                    }
                }
            },
            {data:"chargeMoney",bSortable:true,className:'textRight',
                render:function (data, type, full, meta) {
                    if (full.chargeMoney) {
                        return bt.money.format(full.chargeMoney);
                    }else{
                        return '-';
                    }
                }
            },
            {data:"contractNo",bSortable:true,className:'center'},
            {data:"startContractRate",bSortable:true,className:'textRight',
                render:function (data, type, full, meta) {
                    if (full.endContractRate !== undefined && full.endContractRate !== null) {
                        return rateType.format(full.startContractRate, 'percent', 6)
                            + '-' + rateType.format(full.endContractRate, 'percent', 6)
                    }
                    return rateType.format(full.startContractRate, 'percent', 6);
                }
            },
            {data:"incomeClause",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("choProduct.incomeClause",full.incomeClause);
                }
            },
            {data:"redeemDate",bSortable:true,className:'center'},
            {
                data: "null", bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: false,
            groupColumn:'curName',
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum:'8',
                    dataName:'buyMoney'
                },
                {
                    colNum:'18',
                    dataName:'receivableMoney'
                },
                {
                    colNum:'19',
                    dataName:'actualMoney'
                },
                {
                    colNum:'20',
                    dataName:'uncollectedMoney'
                }
            ]
        }
    })
});

function view(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/chowmatistic/choproductreqquery/view/" + data.id
    })
}