var form,$,billPool,billPoolCorp,datatable,moneyType,moneyFun;
var idsValArray=new Array();
bt.use(['form','billPool','billPoolCorp','dateType','datatable','mulselect','dicts','moneyType'], function () {
    $ = bt.jquery; form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/billpool/collection/datatable"
        },
        buttons: [
            {
                text: bt.lang('abBillBankAutoDtl.autoPay'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    collection(data);
                }
            }
        ]
        ,columns: [
            {data:"corpCodeAndName",bSortable:true},
            {data:"pbCodeAndName",bSortable:true},
            {data:"origNo",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"billFlagName",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"inoutTypeName",bSortable:true},
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
            {data:"payer",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"statusName",bSortable:true},
            {data:"optStatusName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    if(full.optStatus == '-1') {
                        html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="continueRemind(this)">' + bt.lang("billPool.sign.continue") + '</a><span class="text-explode">|</span>';
                        html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="abortDtl(this)">' + bt.lang("billPool.sign.stop") + '</a><span class="text-explode">|</span>';
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


// 提示付款
function collection(data){
    var len = data.length;
    var param;
    //获取选中数据的票据金额
    var selectAllMoney = 0;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    var dtlIds;
    for(i=0;i<len;i++){
        if(dtlIds){
            dtlIds=dtlIds+","+rowData[i].dtlId;
        }else{
            dtlIds=rowData[i].dtlId;
        }
        selectAllMoney = moneyFun.add(selectAllMoney, rowData[i].billMoney);
    }
    param = "billMoney="+selectAllMoney+"&length="+len+"&dtlIds="+dtlIds;
    parent.layer.open({
        title:bt.lang('abBillBankAutoDtl.autoPay'),
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billpool/collection/add?"+param,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}


//提示付款查看页面
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
        content: basePath + "/billpool/collection/view"+param,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

// 继续
function continueRemind(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();

    parent.layer.confirm(bt.lang('billPool.sign.isContinue'),function(){
        bt.loading.show();
        $.post(basePath + "/billpool/collection/continueRemind",{"aid":data.aid}, function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}

// 终止
function abortDtl(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('billPool.sign.isStop'),function(){
        bt.loading.show();
        $.post(basePath + "/billpool/collection/abort",{"aid":data.aid}, function(result){
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
