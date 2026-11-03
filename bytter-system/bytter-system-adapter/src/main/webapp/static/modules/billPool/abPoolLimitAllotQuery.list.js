var billTable = {};
var form,$,billPool,billPoolCorp,moneyType;
var idsValArray=new Array();
bt.use(['form','billPool', 'datatable', 'btdate', 'select', 'dateType','mulselect', 'dicts','billPoolCorp','moneyType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.billPoolCorp.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    moneyType = bt.moneyType.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/billpool/limitAllot/queryDatatable"
        },
        buttons: [
            //新增、导出
             {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "corpName", bSortable: true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data: "bpName", bSortable: true},
            {data: "poolMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(!full.poolMoney){
                        return "";
                    }
                return bt.money.format(full.poolMoney);
            }},
            {data: "lockMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.lockMoney){
                        return bt.money.format(full.lockMoney);
                    }else{
                        return "";
                    }
                }},
            {data: "validMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.validMoney){
                        return bt.money.format(full.validMoney);
                    }else{
                        return "";
                    }
                }},
            /*{data: "applyMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.applyMoney);
                }},*/
            {data: "adjustType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("pool_limit_adjust_type", full.adjustType);
                }
            },
            {data: "overAmt", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(!full.overAmt){
                        return "";
                    }
                    return bt.money.format(full.overAmt);
                }},
            {data: "downAmt", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(!full.downAmt){
                        return "";
                    }
                    return bt.money.format(full.downAmt);
                }},
            {data: "finalMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(!full.finalMoney){
                        return "";
                    }
                    return bt.money.format(full.finalMoney);
                }},
            {data: "createTime", bSortable: true},
            {data: "rmk", bSortable: true},
            {data: "parentName", bSortable: true},
            {data: "bustypeLevel", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html ='';
                    if(full.lockMoney){
                        html = html+ '<a class="" href="#" data-row="' + meta.row + '" onclick="detail(this)">' + bt.lang("common.button.view") + '</a>';
                    }
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
        fixedColumns: {
            rightColumns: 1
        },
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                // {
                //     colNum:'2',
                //     dataName:'poolMoney'
                // },{
                //     colNum:'3',
                //     dataName:'lockMoney'
                // },{
                //     colNum:'4',
                //     dataName:'validMoney'
                // },{
                //     colNum:'5',
                //     dataName:'applyMoney'
                // },
                {
                    colNum:'7',
                    dataName:'overAmt'
                },{
                    colNum:'8',
                    dataName:'downAmt'
                }
            ]
        }
    });

    $("#manual").click(function(){
        var bean = new Object();
        var bpId = billPool.get("billPool").val();
        if(bpId==null || bpId=="" || bpId==" "){
            layer.alert(bt.lang('billPool.Sign.bdIdIsNotNull'), {icon: 7});
            return false;
        }else{
            bean.bpId = bpId;
        }
        if($('#appDateStart').val()==null || $('#appDateStart').val()==""
            || $('#appDateEnd').val()==null || $('#appDateEnd').val()==""){
            parent.layer.msg(bt.lang("billPool.Limit.createTime.isNotNull"), {icon: 2});
            return false;
        }else{
            bean.operDateStart = $('#appDateStart').val();
            bean.operDateEnd = $('#appDateEnd').val();
        }
        bean.corpId = billPoolCorp.get('corpId').val();
        bean.excType = '05';
        bt.loading.show();//显示加载中
        $.post(basePath+"/billpool/queryExc/manualExc",bean, function(result){
            bt.loading.hide(); //关闭加载中
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();//重刷表格
            }
        },'JSON');
    });
});


//明细查看
function detail(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/billpool/limitAllot/viewLimitAllot/"+data.id,
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
