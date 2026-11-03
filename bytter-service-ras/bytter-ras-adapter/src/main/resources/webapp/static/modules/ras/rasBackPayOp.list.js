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
            'url': basePath+"/ras/backPay/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.batchSubmit'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        submitOrders(rowData);//批量提交
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            },{
                text: bt.lang('common.button.batchBack'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        backOrders(rowData);//批量打回
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            }
        ]
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
            {data:"statusName",className:'center',bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '';
                if(full.status==="00")
                	html += '<a class="" href="#" data-row="' + meta.row + '" onclick="submitOrder('+full.id+')">' + bt.lang("common.button.submit") + '</a><span class="text-explode">|</span>';
                if(full.status==="00"||full.status==="10")
               	 	html += '<a class="" href="#" data-row="' + meta.row + '" onclick="backOrder('+full.id+')">' + bt.lang("common.button.back") + '</a>';
                return html;
            }}
        ]
    });
});
//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").init({"corpId":data.id});
}

//提交按钮（批量）
function submitOrders(list){
    var ids = [];
    for(var i=0;i<list.length;i++){
        var dId = list[i].id;
        if(list[i].status!="00"){
            layer.msg(bt.lang('ras.message.pleaseselectvalid'));
            return;
        }
        if(dId){
            ids.push(dId);
        }
    }
    submitOrder(ids);
}
//提交按钮（单个）
function submitOrder(ids){
    var action = "/ras/backPay/submitOrder/" + ids;
    $.post(basePath + action, function (result) {
        parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
        if (result.success) {
            datatable.draw();
            if (!bt.navtab) {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
        }
    }, 'JSON');
}

//打回按钮（批量）
function backOrders(list){
    var ids = [];
    for(var i=0;i<list.length;i++){
        var dId = list[i].id;
        if(list[i].status!="00"&&list[i].status!="10"){
            layer.msg(bt.lang('ras.message.pleaseselectvalid'));
            return;
        }
        if(dId){
            ids.push(dId);
        }
    }
    backOrder(ids);
}
// 打回按钮（单个）
function backOrder(ids){
    var value;
    if(ids.length>1){//批量打回时，不查询原因
        value = "";
        ids = ids.join(',');
    }else{
        //同步查询处理失败的原因
        $.ajax({
            type:"POST",
            url:basePath+"/ras/backPay/checkReason/"+ids,
            async:false,
            dataType:'json',
            success:function(result){
                value = result.obj;
            }
        });
    }
    //打回框提交
    parent.layer.prompt({
        title: bt.lang('table.rasPosBinding.msg'),
        formType: 2,
        value:value,
        yes: function(index, layero){
            // 获取文本框输入的值
            var value = layero.find(".layui-layer-input").val();
            if (value!== "") {
                if(value.length>60){
                    parent.layer.alert(bt.lang('ras.message.msglength'), {icon: 0});
                    return;
                }
                $.post(basePath +"/ras/backPay/saveBackOrder",{"ids":ids,"msg":value},function(result){
                    if(result.success){
                        datatable.draw();
                        parent.layer?parent.layer.closeAll():layer.closeAll();
                    }
                    parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                },'JSON');
            } else {
                parent.layer.alert(bt.lang('ras.message.msgisnotnull'), {icon: 0});
                return;
            }
        },
        cancel: function(index, layero){
            bt.loading.hide(); //关闭加载中
        },
        end:function(){
            bt.loading.hide(); //关闭加载中
        }
    },function(pass){

    });
}
