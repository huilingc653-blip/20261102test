var datatable;
bt.use(['form','datatable','corp','dicts','rasShop','dateType'], function(){
    $ = bt.jquery;
    bt.form();
    bt.corp.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.rasShop.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ras/backPay/datatableQuery"
        },
        buttons: []
        ,columns: [
            {data:"shopName",className:'center',bSortable:true},
            {data:"corpName",className:'center',bSortable:true},
            {data:"backNo",className:'center',bSortable:true},
            {data:"backDate",className:'center',bSortable:true},
            {data:"backMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.backMoney);
                }
            },
            {data:"orderNo",className:'center',bSortable:true},
            {data:"orderDate",className:'center',bSortable:true},
            {data:"orderMoney",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.orderMoney);
                }
            },
            {data:"recMoney",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.recMoney);
                }
            },
            {data:"payTypeName",className:'center',bSortable:true},
            {data:"custName",className:'center',bSortable:true},
            {data:"custAcc",className:'center',bSortable:true},
            {data:"rmk",className:'center',bSortable:true},
            {data:"sendSignName",className:'center',bSortable:true},
            {data:"returnMsg",className:'center',bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '';
                if(full.sendSign==="00"&&full.status==="11"){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="reSendOrder(this)">' + bt.lang("table.rasBackQuery.reSend") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="backOrderQuery(this)">' + bt.lang("common.button.back") + '</a>';
                }
                return html;
            }}
        ]
    });
});
//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").init({"corpId":data.id});
}
//重发按钮
function reSendOrder(el){
    var data = datatable.row(layui.jquery(el).data("row")).data();
    var action = "/ras/backPay/reSendOrder/" + data.id;
    parent.layer.confirm(bt.lang('ras.message.confirm',[data.payTypeName]),function() {
        $.post(basePath + action, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                datatable.draw();
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
    });
}

// 打回按钮
function backOrderQuery(el){
    var data = datatable.row(layui.jquery(el).data("row")).data();
    var action = "/ras/backPay/backOrderQuery/" + data.id;
    parent.layer.confirm(bt.lang('ras.message.back',[data.payTypeName]),function() {
        $.post(basePath + action, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                datatable.draw();
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
    });
}
