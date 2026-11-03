var datatable;
var dicts;
var rasShops;
var corps;
var moneyType,dateFmt;
bt.use(['form','datatable','moneyType','corp','dicts','rasShop','dateType'], function(){
    $ = bt.jquery;
    bt.form();
    corps = bt.corp.init();
    bt.dateType.init();
    moneyType = bt.moneyType.init();
    dicts = bt.dicts.init();
    rasShops=bt.rasShop.init();
    dateFmt=bt.date;
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[2,'asc'],[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        pageSizeOptions:[10,50,100,500,1000,2000,3000,5000],
        ajax: {
            'url': basePath+"/ras/audit/back/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('ras:backAudit:op'),
                text: bt.lang('rasAuditInfo.backAudit'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    submit(rowData);//批量提交
                }
            }
        ]
        ,columns: [
            {data:"auditNo",className:'center',bSortable:true},
            {data:"corpName",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
            }},
            {data:"shopName",className:'center',bSortable:true},
            {data:"payTypeName",className:'center',bSortable:true},
            {data:"revenueMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.revenueMoney);
                }
            },
            {data:"receivedMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.receivedMoney);
                }
            },
            {data:"lessMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.lessMoney);
                }
            },
            {data:"createDate",className:'center',bSortable:true},
            {data:"createName",className:'center',bSortable:true},
            {data:"rmk",className:'center',bSortable:true},
            {data:"statusName",className:'center',bSortable:true},
            {data:"typeName",className:'center',bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }}
        ]
    });
});
//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").init({"corpId":data.id});
}

//跳转数据明细页面
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();

    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,basePath + "/ras/query/viewReveAndReceDtl/" + data.id + "/yesAudit");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ras/query/viewReveAndReceDtl/" + data.id + "/yesAudit"
    });
}

//反稽核提交按钮
function submit(data){
    var len = data.length;
    //没选择数据时则根据查询条件自动反稽核
    if(!len){
        var corpId = corps.get("corpId").val();
        var payType = dicts.get('payType').val();
        if(payType==' '){
            payType="";
        }
        var shopId = rasShops.get("shopId").val();
        if(shopId==' '){
            shopId="";
        }
        var auditNo = $("#auditNo").val();
        var createName = $("#createName").val();
        var lessMoney = moneyType.get("lessMoney").val();
        var lessMoneyEnd = moneyType.get("endMoney").val();
        var createDate = $("#createDate").val();
        var createDateEnd = $("#led_createDate").val();

        parent.layer.confirm(bt.lang('ras.auditback.autoback'),function(){
            bt.loading.show();
            $.ajax({
                type:"POST",
                url: basePath+ "/ras/audit/back/autoBackAudit",
                dataType:"json",
                contentType:"application/json",
                data:JSON.stringify({'corpId':corpId,'shopId':shopId,'payType':payType,'auditNo':auditNo,'createName':createName,
                    'lessMoney':lessMoney,'lessMoneyEnd':lessMoneyEnd,'createDateStart':createDate,'createDateEnd':createDateEnd}),
                success:function(result){
                    parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                    if(result.success){
                        bt.datatable.refreshCurrentPage();
                    }
                    bt.loading.hide();
                }
            });
        });
    }else{
        var rowData = data.splice(0,len);
        var ids;
        for(i=0;i<len;i++){
            if(bt.isnotResource('ras:backAudit:op',rowData[i].corpId)){
                layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
                return;
            }
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        parent.layer.confirm(bt.lang('common.message.confirm.backaudit'),function(){
            bt.loading.show();
            $.post(basePath + "/ras/audit/back/backAudit",{"aids":ids}, function(result){
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                if(result.success){
                    bt.datatable.refreshCurrentPage();
                }
                bt.loading.hide();
            },'JSON');
        });
    }
}

