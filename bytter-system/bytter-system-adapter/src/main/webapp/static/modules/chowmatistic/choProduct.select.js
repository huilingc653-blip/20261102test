var corp,dict,selectType,dateType,moneyType
bt.use(['form','datatable','selectType','dicts','rateType','corp','dateType','moneyType','mulselect'],function () {
    selectType=bt.selectType.init();
    dict = bt.dicts.init();
    corp = bt.corp.init();
    dateType = bt.dateType.init();
    moneyType = bt.moneyType.init();
    var rateType = bt.rateType.getFun();
    var mulselect = bt.mulselect.init();
    //银行类别选择回调方法逻辑部分
    bt.callbackTypeName = function(data){
        var bankTypeIds = [];
        for(var i = 0; i < data.length; i++){
            bankTypeIds.push(data[i].id);
        }
        var mulselectBankName = mulselect.get("bankName");
        mulselectBankName.attr({ "data-url":basePath+"/chowmatistic/choproductreq/bankInfoJson?bankTypeIds="+bankTypeIds});
    };
    table = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        colReorder:true,
        checkbox: true,
        searchForm:{
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/chowmatistic/choproductreq/listValidSign"
        },
        buttons:[{
            text: bt.lang('common.button.select'),
            action: function (e, dt, node, config) {
                var data = dt.rows('.selected').data();
                batchSelectProduct(data);
            }
        },
            {
                extend: "colvis",
                text: bt.lang("common.button.theadHide"),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns:[
            {data:"financialTypeName",bSortable:true,className:'center'},
            {data:"productCode",bSortable:true,className:'center'},
            {data:"productName",bSortable:true,className:'center'},
            {data:"corpName",bSortable:true,className:'center'},
            {data:"bankTypeName",bSortable:true,className:'center'},
            {data:"bankName",bSortable:true,className:'center'},
            {data:"productType",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("cho.productType",full.productType);
                }
            },
            {data:"incomeClause",bSortable:false,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("choProduct.incomeClause",full.incomeClause);
                }
            },
            {data:"bankAcc",bSortable:false,className:'center'},
            {data:"buyMoney",bSortable:false,className:"center",
                render:function (data, type, full, meta) {
                    return bt.money.format(full.buyMoney);
                }
            },
            {data:"redeemedMoney",bSortable:false,className:'center',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.redeemedMoney);
                }
            },
            {data:"waitRedeemMoney",bSortable:false,className:'center',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.waitRedeemMoney);
                }
            },
            {data:"curName",bSortable:false,className:'center'},
            {data:"interestStartDate",bSortable:false,className:'center'},
            {data:'deadlineDate',bSortable:false,className:'center'},
            {data:'referIncomeRate',bSortable:false,className:'center',
                render:function (data, type, full, meta) {
                    if (full.endReferIncomeRate !== undefined && full.endReferIncomeRate !== null && full.endReferIncomeRate != '0') {
                        return rateType.format(full.referIncomeRate, 'percent', 4)
                            + '-' + rateType.format(full.endReferIncomeRate, 'percent', 4)
                    }
                    return rateType.format(full.referIncomeRate, 'percent', 4);
                }
            },
            {data:'isMoveUp',bSortable:false,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("sf_yn",full.isMoveUp);
                }
            }
        ]
    });

    table.on('click', '.selectProduct', function (event) {
        var currentParentIframe = top.$('.layui-tab-content > .layui-show').find('iframe')[0]['name'];
        var isRenewal = $('#isRenewal').val();
        var func = isRenewal != null && isRenewal !=='' ?
            top.frames[currentParentIframe].window.btSelectCallBack['bt-biz-choProductRenewal']['callback'] :
            top.frames[currentParentIframe].window.btSelectCallBack['bt-biz-choProduct']['callback'];
        if (func) {
            var data = table.row($(this).data('row')).data();
            data.isExternal=true;
            func(data);
        }
    });
});
function batchSelectProduct(data) {
    var currentParentIframe = top.$('.layui-tab-content > .layui-show').find('iframe')[0]['name'];
    var isRenewal = $('#isRenewal').val();
    var func = isRenewal != null && isRenewal !=='' ?
        top.frames[currentParentIframe].window.btSelectCallBack['bt-biz-choProductRenewal']['callback'] :
        top.frames[currentParentIframe].window.btSelectCallBack['bt-biz-choProduct']['callback'];
    //验证批量数据
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    //可批量选择的条件：同单位、同产品（产品代码）、同理财账号
    //单位
    var corpId="";
    //理财账号
    var bankAccId = "";
    //产品代码
    var productCode = "";
    var rowData = data.splice(0,len);
    for(var i=0;i<rowData.length;i++){
        if (corpId == "") {
            corpId += rowData[i].corpId;
        }
        if (bankAccId == "" && !(rowData[i].bankAccId === null)) {
            bankAccId += rowData[i].bankAccId;
        }
        if (productCode == "") {
            productCode += rowData[i].productCode;
        }
        if(i != 0) {
            if (corpId != rowData[i].corpId) {
                layer.alert(bt.lang('common.message.choProductRedeem.batch'), {icon: 7});
                return;
            }
            if (bankAccId != rowData[i].bankAccId && !(rowData[i].bankAccId === null)) {
                layer.alert(bt.lang('common.message.choProductRedeem.batch'), {icon: 7});
                return;
            }
            if (productCode != rowData[i].productCode) {
                layer.alert(bt.lang('common.message.choProductRedeem.batch'), {icon: 7});
                return;
            }
        }
    }
    if (func) {
        func(rowData);
    }
}
//银行类别选择回调方法
function callbackTypeName(data){
    bt.callbackTypeName(data)
}