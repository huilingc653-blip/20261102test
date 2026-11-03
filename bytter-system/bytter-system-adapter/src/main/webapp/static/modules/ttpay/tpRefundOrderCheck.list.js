var billTable,selectTree,$,form,dicts,corp;
bt.use(['form','corp','datatable','dicts','selectTree','mulselect','moneyType','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    selectTree=bt.selectTree.init({
        url:basePath+"/ttpay/merchant/treeData",
        elem:".merchantNo-selectTree-checkbox",
        valueField:'merchantNo',
        name: 'ins_mchId',
        checkType:'check',
        placeholder:bt.lang("ttpay.merchantNo")
    });

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[9,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ttpay/refundOrderCheck/checkDatatable"
        },
        buttons: [
            {
                text: bt.lang('ttpay.refund.batchSure'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchCheck(data);
                }
            },
            {
                text: bt.lang('common.button.batchBack'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchBack(data);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"retSource",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("ttpay.refund.retSource", data);
                }
            },
            {data:"payTypeName",bSortable:true},
            {data:"backNo",bSortable:true},
            {data:"corpName",bSortable:true,
                render:function(data, type, full, meta){
                    var corp=full.corpName;
                    if(full.corpCode){
                        corp="("+full.corpCode+")"+corp
                    }
                    return corp;
                }
            },
            {data:"mchId",bSortable:true},
            {data:"trandNo",bSortable:true},
            {data:"shopName",bSortable:true},
            {data:"custName",bSortable:true},
            {data:"backDate",bSortable:true,className:'center'},
            {data:"trandMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"backMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"refundableAmount",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"status",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("TTPAY_REFUND_CHECKSTATUS",data);
                }
            },
            {data:"currency",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                if(!data){
                    data='CNY';
                }
                    return bt.dict("TTP_WX_CURRENCY",data);
                }},
            {data:"rmk",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='<a class="layui-btn" href="#" data-row="'+meta.row+'" onclick="checkPass(this)">'+bt.lang("common.button.sure")+'</a>';
                    html+='<span class="text-explode">|</span><a class="layui-btn" href="#" data-row="'+meta.row+'" onclick="refundBack(this)">'+bt.lang("common.button.back")+'</a>';
                    html+='<span class="text-explode">|</span><a class="layui-btn" href="#" data-row="'+meta.row+'" onclick="dtlView(\''+full.id+'\',\''+full.payType+'\',\''+full.dtlId+'\')">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
        ],
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '10',
                    dataName: 'trandMoney'
                },
                {
                    colNum: '11',
                    dataName: 'backMoney'
                },
                {
                    colNum: '12',
                    dataName: 'refundableAmount'
                }
            ]
        }
    });
    dicts=bt.dicts.init();
    corp=bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();
});

// 批量确认
function batchCheck(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    parent.layer.confirm(bt.lang('ttpay.refund.confirm.batchSure'),function(){
        bt.loading.show();
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        checkMethod(ids);
    });
};

// 单条退款确认通过
function checkPass(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('ttpay.refund.confirm.sure'),function(){
        checkMethod(data.id);
    });
};

// 确认通过公共方法
function checkMethod(ids) {
        $.post(basePath + "/ttpay/refundOrderCheck/batchCheckOrBack",{"ids":ids,"opType":"check"}, function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
};

// 批量打回
function batchBack(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    parent.layer.confirm(bt.lang('common.message.confirm.batchBack'),function(){
        bt.loading.show();
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        $.post(basePath + "/ttpay/refundOrderCheck/batchCheckOrBack",{"ids":ids,"opType":"back"}, function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
};


//退款打回编辑页面
function refundBack(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        title:bt.lang('common.button.back'),
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/refundOrderCheck/refundBack?id="+data.id,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

// 退款详情页面
function dtlView(id, payType, dtlId){
    var param = "id="+id+"&payType="+payType+"&dtlId="+dtlId;
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/ttpay/refundOrderCheck/view?"+param);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/refundOrderCheck/view?"+param,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}


