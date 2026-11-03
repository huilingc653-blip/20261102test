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
        order: [[1,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/billPool/limitInfo/datatableQuery"
        },
        buttons: [
        ]
        ,columns: [
            {data:"corpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                	var reVal=full.corpName;
                	if(full.corpName){
                		reVal='('+full.corpCode+')'+reVal;
                	}
                    return reVal;
                }
            },
            {data:"pledgeMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"bailMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"poolMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"lowRiskMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"inLowRiskMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"lockMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"usedLowRiskMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"usedInLowRiskMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"validMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"validLowRiskMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"validInLowRiskMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"poolName",bSortable:true,
                render: function ( data, type, full, meta ) {
                	var reVal=full.poolName;
                	if(full.poolNo){
                		reVal='('+full.poolNo+')'+reVal;
                	}
                    return reVal;
                }
            },
            {data:"createTime",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='<a class="layui-btn" href="#" data-row="'+meta.row+'" onclick="dtlView(this)">'+bt.lang("common.button.view")+'</a>';
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
                         colNum:'2',
                         dataName:'pledgeMoney'
                     }, {
                         colNum:'3',
                         dataName:'bailMoney'
                     }, {
                         colNum:'4',
                         dataName:'poolMoney'
                     }, {
                         colNum:'5',
                         dataName:'lowRiskMoney'
                     }, {
                         colNum:'6',
                         dataName:'inLowRiskMoney'
                     }, {
                         colNum:'7',
                         dataName:'lockMoney'
                     }, {
                         colNum:'8',
                         dataName:'usedLowRiskMoney'
                     }, {
                         colNum:'9',
                         dataName:'usedInLowRiskMoney'
                     }, {
                         colNum:'10',
                         dataName:'validMoney'
                     }, {
                        colNum:'11',
                        dataName:'validLowRiskMoney'
                     }, {
                        colNum:'12',
                        dataName:'validInLowRiskMoney'
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
        bean.corpId = billPoolCorp.get('corpId').val();
        bean.excType = '01';
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

//详情页面
function dtlView(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/billPool/limitInfo/view?id="+data.id,
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

