var billTable={};
bt.use(['form','datatable','btdate','moneynum','moneyType','corp','select','dateType','bank','dicts','btImport','element'], function(){
    $ = bt.jquery,form = bt.form();
    var moneyFun = bt.moneyType.getFun();
    bt.dateType.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.bank.init({
        elem:'.bank'
    });
    bt.corp.init();
    init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/blankvou/bvpayeechequemanage/datatable"
        },
        buttons: [
            {
                text:bt.lang('table.bvPayeeCheque.batchArrival'),
                hide:bt.isnotResource('bill:blankvou:bvPayeeChequeManage:update'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchArrival(data);
                }
            },
            {
                text:bt.lang('common.button.batchRetreat'),
                hide:bt.isnotResource('bill:blankvou:bvPayeeChequeManage:update'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchRetreat(data);
                }
            },{
                text:bt.lang('common.button.batchDefeat'),
                hide:bt.isnotResource('bill:blankvou:bvPayeeChequeManage:update'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchTovoid(data);
                }
            }
        ]
        ,columns: [
            {data:"origNo",bSortable:true},
            {data:"inputDate",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data:"startDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }},
            {data:"endDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }},
            {data:"curName",bSortable:true},
            {data:"curExrate",bSortable:true,className:"textRight",
                render: function ( data, type, full, meta ) {
                    if(data){
                        return moneyFun.formatConvert(data);
                    }
                    return '';
                }},
            {data:"nativeCreditMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.money.format(full.nativeCreditMoney);
                    }
                    return '';
                }},
            {data:"payee",bSortable:true},
            {data:"payeeBankName",bSortable:true},
            {data:"payeeAcc",bSortable:true},
            {data:"payer",bSortable:true},
            {data:"payerBankName",bSortable:true},
            {data:"payerAcc",bSortable:true},
            {data:"uses",bSortable:true},
            {data:"statusName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    if(data.status==2){//在库  入库中
                        // 托收申请
                        if(bt.isResource('bill:blankvou:bvPayeeChequeManage:update')){
                            html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="collection(this)">'+bt.lang("bill.collection.Application")+'</a>';
                        }
                    }
                    if(full.status == 5 || full.status == 2){
                        // 退票
                        if(bt.isResource('bill:blankvou:bvPayeeChequeManage:update')){
                            html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="retreatColumn(this)">'+bt.lang("common.button.retreat")+'</a>';
                        }
                        // 作废
                        if(bt.isResource('bill:blankvou:bvPayeeChequeManage:update')){
                            html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="tovoidColumn(this)">'+bt.lang("common.button.tovoid")+'</a>';
                        }
                    }
                    // 到账
                    if(bt.isResource('bill:blankvou:bvPayeeChequeManage:update') && full.status == '5'){
                        html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="arrivalAccount(this)">'+bt.lang("table.bvPayeeCheque.account")+'</a>';
                    }
                    return html;
                }}
        ],
        fixedColumns:{
            rightColumns:1
        }
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",//需显示日期的元素选择器
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
});


//查看某一列详情
function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['950px', '550px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvpayeecheque/view/" + data.id
    });
}

//某一列作废
function tovoidColumn(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.tovoid'),function(){
        $.post(basePath + "/bill/blankvou/bvpayeechequemanage/tovoid/"+data.id, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvPayeeChequeManage.tovoid.failed"), {icon: result.success?1:2});
            }
        },'JSON');
    });

}


//某一列退票
function retreatColumn(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.retreat'),function(){
        $.post(basePath + "/bill/blankvou/bvpayeechequemanage/retreat/"+data.id, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvPayeeCheque.retreat.failed"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}

// 托收申请
function collection(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.submit.abbillcollectionapply'),function(){
        $.post(basePath + "/bill/blankvou/bvpayeechequemanage/collection/"+data.id, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvPayeeCheque.retreat.failed"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}

// 到账
function arrivalAccount(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.submit.abbillcollectionaccount'),function(){
        $.post(basePath + "/bill/blankvou/bvpayeechequemanage/arrivalAccount/"+data.id, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvPayeeCheque.retreat.failed"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}

//批量到账
function batchArrival(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
        if(bt.isnotResource('bill:blankvou:bvPayeeChequeManage:update',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        if(rowData[i].status!=5){
            layer.alert(bt.lang('bill.blankvou.bvPayeeChequeManage.arrivalWarn'), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('table.bvPayeeCheque.batchArrival.confirm'),function(){
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        $.post(basePath + "/bill/blankvou/bvpayeechequemanage/batchArrivalAccount",{"ids":ids}, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvPayeeChequeCheck.batchCheck.error"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}


//批量退票
function batchRetreat(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
        if(bt.isnotResource('bill:blankvou:bvPayeeChequeManage:update',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        // if(rowData[i].status!=2){
        //     layer.alert(bt.lang('bill.blankvou.bvPayeeChequeManage.warn'), {icon: 7});
        //     return;
        // }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchRetreat'),function(){
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        $.post(basePath + "/bill/blankvou/bvpayeechequemanage/batchRetreat",{"ids":ids}, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvPayeeChequeCheck.batchCheck.error"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}

//批量作废
function batchTovoid(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
        if(bt.isnotResource('bill:blankvou:bvPayeeChequeManage:update',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        // if(rowData[i].status!=2){
        //     layer.alert(bt.lang('bill.blankvou.bvPayeeChequeManage.warn'), {icon: 7});
        //     return;
        // }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchTovoid'),function(){
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        $.post(basePath + "/bill/blankvou/bvpayeechequemanage/batchTovoid",{"ids":ids}, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvpayeechequecheck.bacthRetreat.failed"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}

//组织单位回调
function corpCallback(ids){

}


//重置
function resetUpdate(){
    init();
}



function  init(){

}
