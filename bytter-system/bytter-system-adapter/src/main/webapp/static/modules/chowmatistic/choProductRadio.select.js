var corp,dict,selectType,dateType,moneyType
bt.use(['form','datatable','selectType','dicts','rateType','corp','dateType','moneyType'],function () {
    selectType=bt.selectType.init();
    dict = bt.dicts.init();
    corp = bt.corp.init();
    dateType = bt.dateType.init();
    moneyType = bt.moneyType.init();
    var rateType = bt.rateType.getFun();
    table = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        colReorder:true,
        searchForm:{
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/chowmatistic/choproductreq/listValidSign"
        },
        buttons:[
            {
                extend: "colvis",
                text: bt.lang("common.button.theadHide"),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns:[
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {

                    return '<a href="javascript:void(0);" class="selectProduct" data-row="' + meta.row + '">' + bt.lang("common.button.select") + '</a>';
                }
            },
            {data:"financialTypeName",bSortable:true,className:'center'},
            {data:"productCode",bSortable:true,className:'center'},
            {data:"productName",bSortable:true,className:'center'},
            {data:"corpName",bSortable:true,className:'center'},
            {data:"bankTypeName",bSortable:true,className:'center'},
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
                    return rateType.format(full.referIncomeRate, 'percent', 6);
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