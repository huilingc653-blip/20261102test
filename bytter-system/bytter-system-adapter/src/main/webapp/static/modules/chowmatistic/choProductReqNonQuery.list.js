var dict
bt.use(['form','datatable','selectType','dicts','rateType','corp','mulselect','dateType','moneyType'],function () {
    var selectType=bt.selectType.init();
    dict = bt.dicts.init();
    var corp = bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.mulselect.init();
    var rateType = bt.rateType.getFun();
    table = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        order: [[0,'asc'],[3,'asc'],[13,'asc']],
        colReorder:true,
        searchForm:{
            formElem: "#searchForm",
            formFilter: "search"
        },
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        ajax: {
            'url': basePath+"/chowmatistic/choproductreqquery/datatable"
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
            {data:"productCode",bSortable:true,className:'center'},
            {data:"productName",bSortable:true,className:'center'},
            {data:"productType",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("cho.productType",full.productType);
                }
            },
            {data:"incomeClause",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("choProduct.incomeClause",full.incomeClause);
                }
            },
            {data:"incomeType",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    var rst = bt.dict("financial.type",data);
                    return rst === '' ? data : rst;
                }
            },
            {data:"bankAcc",bSortable:true,className:'center'},
            {data:"holdMoney",bSortable:true,className:'textRight',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.holdMoney);
                }
            },
            {data:"curName",bSortable:true,className:'center'},
            {data:"redeemDate",bSortable:true,className:'center'},
            {data:"redeemedPrincipal",className:'textRight',
                render:function (data, type, full, meta) {
                    return bt.money.format(data);
                }
            },
            {data:"redeemedMoney",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.redeemedMoney);
                }
            },
            {data:"waitRedeemMoney",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    if (data) {
                        return bt.money.format(data);
                    }else {
                        return '-';
                    }
                }
            },
            {data:"redeemedPortion",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.redeemedPortion);
                }
            },
            {data:"waitRedeemPortion",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.waitRedeemPortion);
                }
            },
            {data:"holdIncomeRate",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    if (data) {
                        return rateType.format(data, 'percent', 6);
                    }else {
                        return '-';
                    }
                }
            },
            {data:"actualIncome",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    if (data) {
                        return bt.money.format(data);
                    }else {
                        return '-';
                    }
                }
            },
            {data:"annualizedIncomeRate",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    if (data) {
                        return rateType.format(data, 'percent', 6);
                    }else {
                        return '-';
                    }
                }
            },
            {data:"annualizedIncome",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    if (data) {
                        return bt.money.format(data);
                    }else {
                        return '-';
                    }
                }
            },
            {data:"buyDate",bSortable:true,className:'center'},
            {data:"interestStartDate",bSortable:true,className:'center'},
            {data:"deadlineDate",bSortable:true,className:'center'},
            {data:"dayLimit",bSortable:false,className:'center',
                render:function (data, type, full, meta) {
                    return data ? data + '天' : ''
                }
            },
            {data:"subFeeMoney",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    if (full.subFeeMoney) {
                        return bt.money.format(full.subFeeMoney);
                    }else{
                        return '-';
                    }
                }
            },
            {data:"redeemChargeMoney",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    if (full.redeemChargeMoney) {
                        return bt.money.format(full.redeemChargeMoney);
                    }else {
                        return '-';
                    }
                }
            },
            {data:"chargeMoney",bSortable:false,className:'textRight',
                render:function (data, type, full, meta) {
                    if (full.chargeMoney) {
                        return bt.money.format(full.chargeMoney);
                    }else{
                        return '-';
                    }
                }
            },
            {data:"payWayName",bSortable:true,className:'center'},
            {data:"contractNo",bSortable:true,className:'center'},
            {data:"startContractRate",bSortable:true,className:'textRight',
                render:function (data, type, full, meta) {
                    if (full.endContractRate !== undefined && full.endContractRate !== null) {
                        return rateType.format(full.startContractRate, 'percent', 6)
                            + '~' + rateType.format(full.endContractRate, 'percent', 6)
                    }
                    return rateType.format(full.startContractRate, 'percent', 6);
                }
            },
            {data:"isMoveUp",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("sf_yn",full.isMoveUp);
                }
            },
            {data:"capitalSource",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("choProduct.capitalSource",full.capitalSource);
                }
            },
            {data:"status",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    if (full.status == 110){
                        return bt.dict("choProduct.query.status",full.status);
                    } else{
                        return bt.dict("choProduct.bizStatus",full.bizStatus);
                    }
                }},
            {data:"null",bSortable:false,className:'center',render:function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
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
                    dataName:'holdMoney'
                },
                {
                    colNum:'10',
                    dataName:'redeemedPrincipal'
                },
                {
                    colNum:'11',
                    dataName:'redeemedMoney'
                },
                {
                    colNum:'12',
                    dataName:'waitRedeemMoney'
                },
                {
                    colNum:'15',
                    dataName:'actualIncome'
                },
                {
                    colNum:'20',
                    dataName:'incomeMoney'
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

// 理财类型回调函数
function financialTypeCallback(data) {
    var filterData
    if (data.value === '1') {
        filterData = '3,4,5,6'
    } else if (data.value === '0') {
        filterData = '1,2'
    } else if (data.value === '2') {
        filterData = '2,3,4,5,6'
    }
    dict.get("productType").attr({'data-filterData': filterData})
    var url = basePath + "/chowmatistic/choproduct/json?financialType=" + data.value;
    bt.selectType.get("eqs_productCode").attr("data-url", url);
    bt.selectType.get("likes_productName").attr("data-url", url);
    bt.dicts.render()
}

function resetProductType() {
    // dict.get("productType").attr({'data-filterData':''})
    var url = basePath + "/chowmatistic/choproduct/json";
    bt.selectType.get("likes_productName").attr("data-url", url);
    bt.selectType.get("eqs_productCode").attr("data-url", url);
}