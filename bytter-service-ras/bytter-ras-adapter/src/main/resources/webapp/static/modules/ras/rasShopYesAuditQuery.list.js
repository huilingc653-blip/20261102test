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
        order: [[6,'desc']],
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ras/query/datatableYesAudit"
        }
        ,columns: [
            {data:"auditNo",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    if(full.nueOrVedMoney == 0&&(full.revenueSize == 0 || full.receivedSize==0)){
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data:"corpName",className:'center',bSortable:true},
            {data:"shopName",className:'center',bSortable:true},
            {data:"nueOrVedMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.nueOrVedMoney);
                }
            },
            {data:"payTypeName",className:'center',bSortable:true},
            {data:"auditDate",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.date.format(full.auditDate, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {data:"createName",className:'center',bSortable:true},
            {data:"auditTypeName",className:'center',bSortable:true},
            {data:null,className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="layui-btn" href="#" data-row="' + meta.row + '" onclick="viewReveAndRece(this)">' + bt.lang("table.query.viewReveAndRece") + '</a>';
                    return html;
                }
            }
        ]
    });
});
//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").init({"corpId":data.id});
}


//跳转实收和营收明细页面
function viewReveAndRece(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('table.query.viewReveAndRece'),
                basePath + "/ras/query/viewReveAndReceDtl/" + data.id + "/yesAudit");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/ras/query/viewReveAndReceDtl/" + data.id + "/yesAudit",
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}