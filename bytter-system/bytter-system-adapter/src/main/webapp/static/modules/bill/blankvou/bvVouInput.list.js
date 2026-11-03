var billTable={};
bt.use(['form','datatable','btdate','corp','select','dateType','bank','btImport'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.bank.init({
        elem:'.bank'
    });
    //加载凭证种类下拉列表
    bt.select.init({
        elem:'.selectVouTid',
        name:"eqs_vouTid",
        valueField:"id",
        textField:"name",
        placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
        search: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvvouchertype/queryVoucherTypeValidByInbox"
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
            'url': basePath+"/bill/blankvou/bvvouinput/datatable"
        },
        buttons: [
            {
                text:bt.lang('common.button.add'),
                hide:bt.isnotResource('bill:blankvou:bvVouInput:save'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text:bt.lang('common.button.batchDelete'),
                hide:bt.isnotResource('bill:blankvou:bvVouInput:delete'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            },{
                text: bt.lang('common.button.batchStorage'),
                hide:bt.isnotResource('bill:blankvou:bvVouInput:storage'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchStorage(data);
                }
            },{
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"voucherTypeName",bSortable:true},
            {data:"bankName",bSortable:true},
            {data:"bankAcc",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"corpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data:"ruleNo",bSortable:true},
            {data:"beginNo",bSortable:true},
            {data:"endNo",bSortable:true},
            {data:"numSize",bSortable:true},
            {data:"storageBoxName",bSortable:true},
            {data:"inputDate",bSortable:true},
            {data:"statusName",bSortable:true},
            {data:"rmk",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    if(data.status==0){//入库状态
                        if(bt.isResource('bill:blankvou:bvVouInput:storage')){
                            html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="storage(this)">'+bt.lang("common.button.storage")+'</a>';
                        }
                        if(bt.isResource('bill:blankvou:bvVouInput:update')){
                            html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
                        }
                        if(bt.isResource('bill:blankvou:bvVouInput:delete')){
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
        corpCallbackFun(ids);
    }
    bt.corp.init();
    //导入初始化
    importExcel = function () {
        up = bt.btImport.init({
            code: "210000005c5dcd90015c5dcdda790L01",
            titleRows: 0,
            headRows: 1,
            success: function (head,data,select) {
                $.ajax({
                    type:"POST",
                    url: basePath + "/bill/blankvou/bvvouinput/saveImport",
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
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/bill/blankvou/bvvouinput/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouinput/add",
        end:function(){
//            billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}
function storage(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.storage'),function(){
        $.post(basePath + "/bill/blankvou/bvvouinput/storageOperat/" + data.id, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvvouinput.storage.repeat"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}
function batchStorage(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
        if(bt.isnotResource('bill:blankvou:bvVouInput:storage',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        if(rowData[i].status!=0){
            layer.alert(bt.lang('bill.blankvou.bvvouinput.storage.check'), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchStorage'),function(){
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        $.post(basePath + "/bill/blankvou/bvvouinput/batchStorage",{"ids":ids}, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvvouinput.storage.repeat"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}
function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouinput/view/" + data.id
    });
}
function update(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/bill/blankvou/bvvouinput/update/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouinput/update/" + data.id,
        end:function(){
//            billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}
function deleteColumn(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/blankvou/bvvouinput/delete/" + data.id, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvvouinput.delete.failed"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}
function deleteBatch(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
        if(bt.isnotResource('bill:blankvou:bvVouInput:delete',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        if(rowData[i].status!=0){
            layer.alert(bt.lang('bill.blankvou.bvvouinput.delete.check'), {icon: 7});
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
        $.post(basePath + "/bill/blankvou/bvvouinput/batchDelete",{"ids":ids}, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvvouinput.delete.failed"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}
//单位回调获取银行账户、银行类别、库箱
function corpCallbackFun(ids){
    var $ = bt.jquery,form = bt.form();
    if(ids){
        //库箱
        bt.select.init({
            elem:'.selectSboxId',
            name:"eqs_sboxId",
            valueField:"id",
            textField:"name",
            placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
            search: true,
            disabled: false,
            url: basePath + "/bill/blankvou/bvstoragebox/jsonListByCorpIds/" + ids
        });
        //银行类别
        bt.select.init({
            elem:'.selectBankId',
            name:"eqs_bankName",
            valueField: "bankName",
            textField: "bankName",
            placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
            search: true,
            disabled: false,
            url: basePath + "/bill/blankvou/bvvouinput/getBankAccListByCorpIds/" + ids
        });
    }
}

//重置
function resetUpdate(){
    init();
}

//所属银行、所属库箱加载
function init(){
    bt.select.init({
        elem:'.selectSboxId',
        name:"eqs_sboxId",
        valueField:"id",
        textField:"name",
        placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
        search: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvstoragebox/selectData"
    });
    //下拉框--加载所属银行
    bt.select.init({
        elem:'.selectBankId',
        name:"eqs_bankName",
        valueField: "bankName",
        textField: "bankName",
        placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
        search: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvvouinput/getBankAccList/json"
    });
}
