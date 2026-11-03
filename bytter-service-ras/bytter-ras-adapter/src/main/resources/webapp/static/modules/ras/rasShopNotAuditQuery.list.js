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
        order: [[4,'desc']],
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ras/query/datatableNotAudit"
        }
        ,columns: [
            {data:"corpName",className:'center',bSortable:true},
            {data:"shopName",className:'center',bSortable:true},
            {data:"payTypeName",className:'center',bSortable:true},
            {data:"transDate",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.date.format(full.transDate, "yyyy-MM-dd");
                }
            },
            {data:"nueMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    var  html = '<a class="" href="#" data-row="' + meta.row + '" onclick="revenueView(this)">' + bt.money.format(full.nueMoney) + '</a>';
                    return html;
                }
            },
            {data:"vedMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="receivedView(this)">' + bt.money.format(full.vedMoney) + '</a>';
                    return html;
                }
            },
            {data:"statusName",className:'center',bSortable:true}
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
        var value = data.shopId+","+data.payType+","+bt.date.format(data.transDate,"yyyy-MM-dd")+","+data.status;
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('table.query.revenueDtl'),
                basePath + "/ras/query/differentRevenueDtlList/" + value + "/notAudit");
            return;
        }
        // parent.layer.open({
        //     type: 2,
        //     area: ['850px', '530px'],
        //     maxmin: true,
        //     content: basePath + "/ras/query/differentRevenueDtlList/" + value + "/notAudit",
        //     end:function(){
        //         datatable && datatable.draw();
        //     }
        // });
    }
}

//跳转实收明细页面
function receivedView(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        var value = data.shopId+","+data.payType+","+bt.date.format(data.transDate,"yyyy-MM-dd")+","+data.status;
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('table.query.receivedDtl'),
                basePath + "/ras/query/differentReceivedDtlList/" + value + "/notAudit");
            return;
        }
        // parent.layer.open({
        //     type: 2,
        //     area: ['850px', '530px'],
        //     maxmin: true,
        //     content: basePath + "/ras/query/differentRevenueDtlList/" + value + "/notAudit",
        //     end:function(){
        //         datatable && datatable.draw();
        //     }
        // });
    }
}