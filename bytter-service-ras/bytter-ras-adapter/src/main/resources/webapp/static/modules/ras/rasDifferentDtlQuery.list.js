var datatable,form;
bt.use(['form','datatable','corp','dicts','dateType','moneyType','rasShop','date','select'], function(){
    $ = bt.jquery;
    form = bt.form();
    bt.corp.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.rasShop.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        order: [[10,'desc']],
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ras/query/datatableDifferent"
        }
        ,columns: [
            {data:"auditNo",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    if(full.revenueSize == 0 || full.receivedSize==0){
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data:"corpName",className:'center',bSortable:true},
            {data:"shopName",className:'center',bSortable:true},
            {data:"payTypeName",className:'center',bSortable:true},
            {data:"revenueMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if(full.revenueMoney>0) html = '<a class="" href="#" data-row="' + meta.row + '" onclick="revenueView(this)">' + bt.money.format(full.revenueMoney) + '</a>';
                    else html= bt.money.format(full.revenueMoney);
                    return html;
                }
            },
            {data:"revenueSize",className:'center',bSortable:true},
            {data:"receivedMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    var html = "";
                    if(full.receivedMoney>0) html = '<a class="" href="#" data-row="' + meta.row + '" onclick="receivedView(this)">' + bt.money.format(full.receivedMoney) + '</a>';
                    else html= bt.money.format(full.receivedMoney);
                    return html;
                }
            },
            {data:"receivedSize",className:'center',bSortable:true},
            {data:"lessMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.lessMoney);
                }
            },
            {data:"createDate",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.date.format(full.createDate, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {data:"createName",className:'center',bSortable:true},
            {data:"rmk",className:'center',bSortable:true}
        ]
    });
});
//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").init({"corpId":data.id});
}

//跳转营收明细页面
function revenueView(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('table.query.revenueDtl'),
                basePath + "/ras/query/differentRevenueDtlList/" + data.id + "/different");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/ras/query/differentRevenueDtlList/" + data.id + "/different",
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

//跳转实收明细页面
function receivedView(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('table.query.receivedDtl'),
                basePath + "/ras/query/differentReceivedDtlList/" + data.id + "/different");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/ras/query/differentReceivedDtlList/" + data.id + "/different",
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}