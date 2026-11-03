var form,$,billPool,billPoolCorp,datatable,moneyType,dateObj;
var idsValArray=new Array();
bt.use(['form','billPool','billPoolCorp','dateType','datatable','mulselect','dicts','moneyType'], function () {
    $ = bt.jquery; form = bt.form();
    dateObj =bt.dateType.init();
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
        checkbox:false,
        order: [[2,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/billPool/bailBook/datatable"
        },
        buttons: []
        ,columns: [
            {data:"corpCodeAndName",bSortable:true},
            {data:"bailAcc",bSortable:true},
            {data:"transDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(full.transDate, "yyyy-MM-dd hh:mm:ss");
                    }
                    return '';
                }
            },
            {data:"money",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }},
            {data:"type",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_POOL_BAIL_TRANS_TYPE", full.type);
                }},
            {data:"oppAcc",bSortable:true},
            {data:"oppAccName",bSortable:true},
            {data:"pbCodeAndName",bSortable:true},
            {data:"createTime",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd hh:mm:ss");
                    }
                    return '';
                }
             },
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
            sumList:[
                {
                    colNum:'4',
                    dataName:'money'
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
        if($('#transDateStart').val()==null || $('#transDateStart').val()==""
            || $('#transDateEnd').val()==null || $('#transDateEnd').val()==""){
            parent.layer.msg(bt.lang("billPool.transDate.isNotNull"), {icon: 2});
            return false;
        }else{
            //交易日期区间不能超过3个月
            if(bt.date.getBetweenMonth($('#transDateStart').val(),$('#transDateEnd').val())>3){
                layer.alert(bt.lang("billPool.transDate.monthNotPass"),{icon:7});
                return false;
            }
            bean.operDateStart = $('#transDateStart').val();
            bean.operDateEnd = $('#transDateEnd').val();
        }
        bean.corpId = billPoolCorp.get('corpId').val();
        bean.excType = '04';
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


//保证金详情页面
function dtlView(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var param='';
    if(data.id){
        param = "?id="+data.id;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/billPool/bailBook/view"+param,
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

