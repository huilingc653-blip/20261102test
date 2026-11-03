var form,$,billPool,billPoolCorp,datatable,dateObj;
var idsValArray=new Array();
bt.use(['form','billPool','billPoolCorp','dateType','datatable','mulselect','dicts'], function () {
    $ = bt.jquery; form = bt.form();
    dateObj = bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();

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
            'url': basePath+"/billPool/dtlBook/datatableInoutQuery"
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
            {data:"billType",bSortable:false,className:"center",
                render:function(data,type,full,meta){
                    return bt.dict("AB_BILL_TYPE",full.billType);
                }},
            {data: "billFlag", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_FLAG",full.billFlag);
                }},
            {data:"briefId",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_POOL_BRIEF_ID",full.briefId);
                }
            },
            {data:"inoutType",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_POOL_INOUT_TYPE",full.inoutType);
                }
            },
            {data:"origNo",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"payer",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"poolName",bSortable:true,
                render: function ( data, type, full, meta ) {
                	var reVal=full.poolName;
                	if(full.poolNo){
                		reVal='('+full.poolNo+')'+reVal;
                	}
                    return reVal;
                }
            },
            {data:"updateTime",bSortable:true},
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
                    colNum:'7',
                    dataName:'billMoney'
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
        if($('#dateStart').val()==null || $('#dateStart').val()==""
            || $('#dateEnd').val()==null || $('#dateEnd').val()==""){
            parent.layer.msg(bt.lang("billPool.query.intoDataIsNotNull"), {icon: 2});
            return false;
        }else{
            bean.operDateStart = $('#dateStart').val();
            bean.operDateEnd = $('#dateEnd').val();
        }
        bean.corpId = billPoolCorp.get('corpId').val();
        bean.excType = '02';
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

//设置入池结束时间
function setCreateTimeEnd(){
    if($('#dateStart').val()!=null && $('#dateStart').val()!=''){
        var dateString = dateObj.getFun().addMonth($('#dateStart').val(),3);
        var month = dateString.getMonth()+1;
        dateObj.get("createTimeEnd").attr("data-max",dateString.getFullYear()+"-"+month+"-"+dateString.getDate());
    }
}

//详情页面
function dtlView(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/billPool/dtlBook/viewInoutInfo?id="+data.id,
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
