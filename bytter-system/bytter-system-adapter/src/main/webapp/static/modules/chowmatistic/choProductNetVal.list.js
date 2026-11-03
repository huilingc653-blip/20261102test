var dict
bt.use(['form','datatable','selectType','dicts','rateType','corp','dateType','moneyType'],function () {
    var selectType=bt.selectType.init();
    dict = bt.dicts.init();
    var corp = bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();
    bt.rateType.init();
    var rateType = bt.rateType.getFun();
    table = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        colReorder:true,
        checkbox: false,
        searchForm:{
            formElem: "#searchForm",
            formFilter: "search"
        },
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        ajax: {
            'url': basePath+"/chowmatistic/choproductNetVal/datatable"
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
            {data:"financialTypeName",bSortable:true,className:'center'},
            {data:"bankName",bSortable:true,className:'center'},
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
            {data:"latestDate",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.date.format(data, "yyyy-MM-dd")
                }},
            {data:"latestNetVal",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this, true)">'
                        + data +'</a>';
                }
            },
            {data:"buyMoney",bSortable:true,className:'textRight',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.buyMoney);

                }
            },
            {data:"buyDate",bSortable:true,className:'center'},
            {data:"curName",bSortable:true,className:'center'},
            {data:"interestStartDate",bSortable:true,className:'center'},
            {data:"deadlineDate",bSortable:true,className:'center'},
            {data:"isMoveUp",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("sf_yn",full.isMoveUp);
                }
            },
            {data:"payWayName",bSortable:true,className:'center'},
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
            {data:"bizStatus",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("choProduct.bizStatus",data);
                }},
            {data:"null",bSortable:false,className:'center',render:function (data, type, full, meta) {
                var html = '';
                if(bt.isResource('cho:productNetVal:choProductNetVal:apply',full.corpId)){
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this, false)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                }
                    return html;
                }}
        ]
    })
});

function add() {
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath+'/chowmatistic/choproductreq/add');
        return;
    }
    parent.layer.open({
        type:2,
        area:['850px','530px'],
        maxmin:true,
        content:basePath+'/chowmatistic/choproductreq/add',
        end:function () {
            table && table.draw();
        }
    });
}

function update(e1, isView) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    var url = basePath + '/chowmatistic/choproductNetVal/update?reqId=' + data.id
    url = isView ? url + '&isView=1' : url
    var title = isView ? 'common.view.data' : 'common.update.data'
    if (data){
        if (bt.navtab){
            bt.navtab.addNavtab(bt.lang(title),url);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px','530px'],
            manmin: true,
            content: url,
            end:function () {
                table && table.draw();
            }
        })
    }
}

function del(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function () {
        $.post(basePath + "/chowmatistic/choproductreq/delete/" + data.id,function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg,{icon: result.success?1:2});
        },'JSON');
    })
}

function view(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/chowmatistic/choproductreq/view/" + data.id
    })
}

function batchSubmit(data) {
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids="";
    var rowData = data.splice(0,len);
    for(var i=0;i<rowData.length;i++){
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'),function(){
        $.post(basePath + "/chowmatistic/choproductreq/batchSubmit",{ids:ids}, function(result){
            table.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
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
    dict.get("productType").attr({'data-filterData':''})
    var url = basePath + "/chowmatistic/choproduct/json";
    bt.selectType.get("likes_productName").attr("data-url", url);
    bt.selectType.get("eqs_productCode").attr("data-url", url);
}