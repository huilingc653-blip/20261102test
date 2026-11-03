var form,$,billPool,billPoolCorp,datatable,moneyType;
var idsValArray=new Array();
bt.use(['form','billPool','billPoolCorp','dateType','datatable','mulselect','dicts','moneyType'], function () {
    $ = bt.jquery; form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    moneyType = bt.moneyType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[2,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/billpool/emitApply/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    addEmit();
                }
            },
            {
                text: bt.lang('common.button.delete'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchDelete(data);
                }
            },
            {
                text: bt.lang('common.button.submit'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            }
        ]
        ,columns: [
            {data:"corpCodeAndName",bSortable:true},
            {data:"appNo",bSortable:true},
            {data:"pbCodeAndName",bSortable:true},
            {data:"origNo",bSortable:true},
            {data: "billType", bSortable: true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_BILL_TYPE",full.billType);
                }
            },
            {data: "billFlag", bSortable: true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_BILL_FLAG",full.billFlag);
                }
            },
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"payeracc",bSortable:true},
            {data: "banendrsmtmk",bSortable: true, className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_BANEM",full.banendrsmtmk);
                }
            },
            {data:"startDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }
            },
            {data:"endDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }},
            {data:"payee",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"statusName",bSortable:true},
            {data:"optStatusName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    if((full.status == '0' || full.status == '-1' || full.optStatus == '-1') && full.appNo.indexOf('PF')) {
                        html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="updateInfo(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    }
                    html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="dtlView(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        fnDrawCallback:function(){
            idsValArray=new Array();
            setSumMoney();
        },
        btClickRowCallback:function(data){
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            if(idsValArray.length>0) {
                bt.datatable.onCheckRow("appTable", {
                    "key": "id",
                    "value": idsValArray,
                    "checked": true
                });
            }
            var showHTML=bt.lang("sys.common.isSelectSize",[idsValArray.length]);
            $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">'+showHTML+'</span>');
        },
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'4',
                    dataName:'billMoney'
                }
            ]
        }
    });
});

// 新增界面跳转
function addEmit(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/billpool/emitApply/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billpool/emitApply/add",
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

// 修改界面跳转
function updateInfo(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/billpool/emitApply/update?id="+data.dtlId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billpool/emitApply/update?id="+data.dtlId,
        end:function(){
//        	billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}

//出票申请查看页面
function dtlView(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var param;
    if(data.dtlId){
        param = "?id="+data.dtlId;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/billpool/emitApply/view"+param,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

// 批量提交出票申请
function submitBatch(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'),function(){
        bt.loading.show();
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].aid;
            }else{
                ids=rowData[i].aid;
            }
        }
        $.post(basePath + "/billpool/emitApply/batchSubmit",{"ids":ids}, function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}

// 批量删除出票申请
function batchDelete(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'),function(){//确定批量删除?
        bt.loading.show();
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].dtlId;
            }else{
                ids=rowData[i].dtlId;
            }
        }
        $.post(basePath + "/billpool/emitApply/batchDelete",{"ids":ids}, function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].dtlId)<0){
                    idsValArray.push(datas[i].dtlId);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].dtlId);
                if(index > -1){
                    objArrayRemove(idsValArray,index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function (){
    var showHTML=bt.lang("sys.common.isSelectSize",[idsValArray.length]);
    if($("#sumMoneyBillSpan").text()!=''){
        $("#sumMoneyBillSpan").text(showHTML);
    }else{
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">'+showHTML+'</span>');
    }
}

var objArrayRemove = function(obj,index){
    if (index > -1) {
        obj.splice(index, 1);
    }
};
