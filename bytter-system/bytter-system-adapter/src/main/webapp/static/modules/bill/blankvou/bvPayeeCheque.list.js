var billTable={};
bt.use(['form','datatable','btdate','moneynum','corp','moneyType','select','dateType','bank','dicts','btImport','element'], function(){
    $ = bt.jquery,form = bt.form();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.bank.init({
        elem:'.bank'
    });
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
            'url': basePath+"/bill/blankvou/bvpayeecheque/datatable"
        },
        buttons: [
            {
                text:bt.lang('common.button.add'),
                hide:bt.isnotResource('bill:blankvou:bvPayeeCheque:save'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text: bt.lang('common.button.batchSubmit'),
                hide:bt.isnotResource('bill:blankvou:bvPayeeCheque:storage'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchStorage(data);
                }
            },{
                text:bt.lang('common.button.batchDelete'),
                hide:bt.isnotResource('bill:blankvou:bvPayeeCheque:delete'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            },{
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            }

        ]
        ,columns: [
            {data:"corpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
                }
            },
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
            {data:"statusName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    if(data.status==0 || data.status==-1 ){//入库状态
                        if(bt.isResource('bill:blankvou:bvPayeeCheque:update')){
                            html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
                        }
                        if(bt.isResource('bill:blankvou:bvPayeeCheque:delete')){
                            html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
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
    corpCallback=function (ids) {

    }
    bt.corp.init();
    //导入初始化
    importExcel = function () {
        up = bt.btImport.init({
            code: "b9f912f778dd11e8b443060400ef5315",
            titleRows: 0,
            headRows: 1,
            success: function (head,data,select) {
                $.ajax({
                    type:"POST",
                    url: basePath + "/bill/blankvou/bvpayeecheque/saveImport",
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(up.getSelectData()),
                    success:function(result){
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                        if(result.success){
                            bt.datatable.refreshCurrentPage();
                        }
                    }
                });
            }
        });
    };
});
//跳转至添加页面 
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/bill/blankvou/bvpayeecheque/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvpayeecheque/add",
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}


//批量提交
function batchStorage(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    var ids;
    // {isControl:是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递） ,params:[{name:检查名称,appNo:申请单据号,corpId:单位ID},{name:检查名称,appNo:申请单据号,corpId:单位ID}]}
    var bwDataParams=new Array();
    for(i=0;i<len;i++){
        if(bt.isnotResource('bill:blankvou:bvPayeeCheque:storage',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        if(rowData[i].status<-1 && rowData[i].status>0 ){
            layer.alert(bt.lang('bill.blankvou.bvpayeecheque.batchStorage.error'), {icon: 7});
            return;
        }
        if(ids){
            ids=ids+","+rowData[i].id;
        }else{
            ids=rowData[i].id;
        }
        bwDataParams.push({"name":rowData[i].payer,"appNo":rowData[i].origNo,"corpId":rowData[i].corpId})
    }
    //对黑白名单进行验证
    var reData=externalCorpCheckBatch({"isControl":"0","params":bwDataParams});
    if(reData.reCode=='3'){
        parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
        return;
    }else if(reData.reCode=='2'){
        parent.layer.confirm(reData.msg,function(){
            storageSubmit(ids);
        });
    }else{
        storageSubmit(ids);
    }
}

function storageSubmit(ids){
    parent.layer.confirm(bt.lang('common.message.confirm.batchStorage'),function(){
        $.post(basePath + "/bill/blankvou/bvpayeecheque/batchStorage/stotage",{"ids":ids}, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvpayeecheque.storage.repeat"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}

//查看详情 
function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['950px', '550px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvpayeecheque/view/" + data.id
    });
}

//编辑更新
function update(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/bill/blankvou/bvpayeecheque/update/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvpayeecheque/update/" + data.id,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

//删除某一列
function deleteColumn(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/blankvou/bvpayeecheque/delete/"+data.id, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvPayeeCheque.delete.failed"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}


//批量删除 
function deleteBatch(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
        if(bt.isnotResource('bill:blankvou:bvPayeeCheque:delete',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        if(rowData[i].status<-1 && rowData[i].status>0){
            layer.alert(bt.lang('bill.blankvou.bvPayeeCheque.delete.check'), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'),function(){
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        $.post(basePath + "/bill/blankvou/bvpayeecheque/batchDelete",{"ids":ids}, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvvouinput.delete.failed"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}

//重置
function resetUpdate(){
    init();
}



function  init(){
}
