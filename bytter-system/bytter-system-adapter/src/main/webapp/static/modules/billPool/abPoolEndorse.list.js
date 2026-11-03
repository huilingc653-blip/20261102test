var billTable={},corp,moneyType,moneyFun;
var idsValArray=new Array();
bt.use(['form','datatable','moneyType','billPool','btdate','billPoolCorp','select','dateType','corp','dicts','selectType','mulselect'],function(){
    $ = bt.jquery,form = bt.form();
    bt.billPoolCorp.init();
    bt.dicts.init();
    bt.billPool.init();
    bt.corp.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.mulselect.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[6,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/billPool/endorse/datatable"
        },
        buttons: [
            {
                text: bt.lang("billPool.endorse.btn"),
                action: function (e, dt, node, config) {
                    endorse(dt.rows('.selected').data());
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),//'列隐藏',
            }
        ]
        ,columns: [
            {data:"corpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data:"origNo",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "billFlag", bSortable: true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_BILL_FLAG",full.billFlag);
                }
            },
            {data: "billType", bSortable: true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("AB_BILL_TYPE",full.billType);
                }
            },
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"payer",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"bpName",bSortable:true},
            {data:"inoutTypeName",bSortable:true},
            {
                data: "payNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.payNo != "" && full.payNo != null) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewNisBillhead(this)">' + full.payNo + '</a>';
                    }
                    return html;
                }
            },
            {data:"statusName",bSortable:true},
            {data:"optStatusName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {

                var html='';
                //交易失败的可重新发送或中止
                if(data.optStatus==-1){
                    html = html+'<a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,1)">'+bt.lang("billPool.sign.continue")+'</a>';
                    html = html+' | <a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,0)">'+bt.lang("billPool.sign.stop")+'</a>  | ';
                }
                html = html+'<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                return html;
            }}
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
        fixedColumns:{
            rightColumns:3
        },
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'3',
                    dataName:'billMoney'
                }
            ]
        }
    });

});

//查看详细
function view(el){
    var data =  billTable.row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/billPool/endorse/view?id="+ data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '550px'],
        maxmin: true,
        content: basePath + "/billPool/endorse/view?id=" + data.id,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

//操作 0终止  1继续
function operation(el,opt){
    var data =  billTable.row(layui.jquery(el).data("row")).data();
    var msg ="";
    if(opt==0){
        msg ="billPool.sign.isStop"
    }else{
        msg ="billPool.sign.isContinue"
    }

    parent.layer.confirm(bt.lang(msg),function(){
        $.post(basePath + "/billPool/endorse/operation",{"pdId":data.id,"opt":opt}, function(result){
            bt.loading.hide();
            if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
            }
        },'JSON');
    });
}


//背书转让
function endorse(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    //拼装数据
    var rowData = data.splice(0,len);
    var ids;
    var totalMoney=0.00;
    var billFlag=10;
    for(i=0;i<len;i++){
        //兴业银行纸票不支持池内背书交易(识别到数据进行提示不支持做交易)
        if(rowData[i].bifCode == '9015' && rowData[i].billFlag == 10){
            layer.alert(bt.lang('billPool.endorse.isNotEndorse'), {icon: 7});
            return false;
        }
        if(ids){
            ids = ids + "," + rowData[i].id;
        }else{
            ids = rowData[i].id;
        }
        if(rowData[i].billFlag == 11){
            billFlag = 11;
        }
        totalMoney = moneyFun.add(totalMoney, rowData[i].billMoney);
    }
    parent.layer.open({
        type: 2,
        area: ['900px', '530px'],
        maxmin: true,
        content: basePath + "/billPool/endorse/batchEndorse?id=" + ids+"&billMoney="+totalMoney+"&length="+len+"&billFlag="+billFlag,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].id)<0){
                    idsValArray.push(datas[i].id);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].id);
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

//付款单查看
function viewNisBillhead(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/view/" + data.payId
    });
}