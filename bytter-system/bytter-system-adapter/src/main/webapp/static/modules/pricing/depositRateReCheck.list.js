bt.use(['form','datatable','dicts','dateType','date','rateType','selectType','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[5,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/pricing/depositRate/interestRateReCheck/dataTable"
        },
        buttons: [
            {
                text: bt.lang('common.button.batchadopt'),// 批量通过
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if(len<1){
                        layer.msg(bt.lang('common.message.confirm.selectData'));
                        return;
                    }
                    var ids="";
                    var rowData = data.splice(0,len);
                    for(var i=0;i<rowData.length;i++){
                        if (ids == "") {
                            ids += rowData[i].id;
                        } else {
                            ids += "," + rowData[i].id;
                        }
                    }
                    var action = "/pricing/depositRate/interestRateReCheck/checkBatch?ids=" + ids+"&handleType=2";
                    parent.layer.confirm(bt.lang('common.message.confirm.batchCheck'), function () {
                        $.post(basePath + action,data.field,function(result){
                            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                            if(result.success){
                                if(bt.navtab){
                                    bt.navtab.refreshCurrentTable();
                                }
                            }
                        },'JSON');
                    })
                }
            },
            {
                text: bt.lang('common.button.batchBack'),//批量打回
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if(len<1){
                        layer.msg(bt.lang('common.message.confirm.selectData'));
                        return;
                    }
                    var ids="";
                    var rowData = data.splice(0,len);
                    for(var i=0;i<rowData.length;i++){
                        if (ids == "") {
                            ids += rowData[i].id;
                        } else {
                            ids += "," + rowData[i].id;
                        }
                    }
                    var action = "/pricing/depositRate/interestRateReCheck/checkBatch?ids=" + ids+"&handleType=-1";
                    parent.layer.confirm(bt.lang('common.message.confirm.batchBack'), function () {
                        $.post(basePath + action,data.field,function(result){
                            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                            if(result.success){
                                if(bt.navtab){
                                    bt.navtab.refreshCurrentTable();
                                }
                            }
                        },'JSON');
                    });
                }
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"rateTypeCode",className:'center',bSortable:true},
            {data:"rateTypeName",className:'center',bSortable:true},
            {data:"depositClass",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("pricing.deposit.type",full.depositClass);
                }
            },
            {data:"depositAccSign",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(full.depositAccSign==null){
                        return bt.dict("pricing.depAccSign",2);//存量数据，默认为资金池内部账户
                    }else{
                        return bt.dict("pricing.depAccSign",full.depositAccSign);
                    }
                }
            },
            {data:"curName",className:'center',bSortable:true},
            {data:"timeOfTakeEffect",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.date.format(full.timeOfTakeEffect, "yyyy-MM-dd");
                }
            },
            {data:"referenceRateType",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(full.referenceRateType==null){
                        return bt.dict("reference.RateType",0);//基准利率
                    }else{
                        return bt.dict("reference.RateType",full.referenceRateType);
                    }
                }
            },
            {data:"rate",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.rateType.getFun().format(full.rate);
                }
            },
            {
                data: "rateUpLimit", className: 'center', render: function (data, type, full, meta) {
                    return bt.moneyType.getFun().format(full.rateUpLimit, {decimal: 2});
                }
            },
            {data:"rateDownLimit",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.moneyType.getFun().format(full.rateDownLimit, {decimal: 2});
                }
            },
            {data:"createBy",className:'center',bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="reCheck(this)">'+bt.lang("common.button.check")+'</a>';
                    return html;
                }}
        ],
    });
});
function reCheck(el){//复核页面
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.check'),basePath + "/pricing/depositRate/interestRateReCheck/reCheck?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/pricing/depositRate/interestRateReCheck/reCheck?id=" + data.id,
            end:function(){
            	bt.navtab.refreshCurrentTable();
            }
        });
    }
}