var billTable={};
bt.use(['form','datatable','btdate','moneynum','moneyType','corp','select','dateType','bank','dicts','btImport','element'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.moneyType.init();
    bt.dicts.init();
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
            'url': basePath+"/bill/blankvou/bvpayeechequecheck/datatable"
        },
        buttons: [
            {
                text:bt.lang('common.button.batchCheck'),
                hide:bt.isnotResource('bill:blankvou:bvPayeeChequeCheck:update'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchCheck(data);
                }
            },{
                text:bt.lang('common.button.batchBack'),
                hide:bt.isnotResource('bill:blankvou:bvPayeeChequeCheck:update'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchBack(data);
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
            {data:"payee",bSortable:true},
            {data:"payeeBankName",bSortable:true},
            {data:"payeeAcc",bSortable:true},
            {data:"payer",bSortable:true},
            {data:"payerBankName",bSortable:true},
            {data:"payerAcc",bSortable:true},
            {data:"uses",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    if(data.status==1){//复核中
                        if(bt.isResource('bill:blankvou:bvPayeeChequeCheck:update')){
                            html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="checkColumn(this)">'+bt.lang("common.button.pass")+'</a>';
                        }
                        if(bt.isResource('bill:blankvou:bvPayeeChequeCheck:update')){
                            html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="backColumn(this)">'+bt.lang("common.button.back")+'</a>';
                        }
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

//某一列通过
function checkColumn(el){
    var data = billTable.row(layui.jquery(el).data("row")).data()
    parent.layer.confirm(bt.lang('common.message.confirm.check'),function(){
        $.post(basePath + "/bill/blankvou/bvpayeechequecheck/check/"+data.id, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvPayeeCheque.check.failed"), {icon: result.success?1:2});
            }
        },'JSON');
    });

}


//某一列退回
function backColumn(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '249px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvpayeechequecheck/retreatPage/" + data.id,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}



//批量通过
function batchCheck(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
        if(bt.isnotResource('bill:blankvou:bvPayeeChequeCheck:update',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        if(rowData[i].status!=1){
            layer.alert(bt.lang('bill.blankvou.bvPayeeChequeCheck.update.check'), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchCheck'),function(){
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        $.post(basePath + "/bill/blankvou/bvpayeechequecheck/batchCheck",{"ids":ids}, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvPayeeChequeCheck.batchCheck.error"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}

//批量退回 
function batchBack(data){
    var len = data.length;
    var rmk =$("input[name='rmk']").val();

    if(rmk==''){
        layer.alert(bt.lang('common.message.repulseRequired'), {icon: 7});
        return;
    }
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
        if(bt.isnotResource('bill:blankvou:bvPayeeChequeCheck:update',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        if(rowData[i].status!=1){
            layer.alert(bt.lang('bill.blankvou.bvPayeeChequeCheck.update.check'), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchRepulse'),function(){
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        $.post(basePath + "/bill/blankvou/bvpayeechequecheck/bacthRetreat",{"ids":ids,"rmk":rmk}, function(result){
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
