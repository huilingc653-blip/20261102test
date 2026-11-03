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
        order: [[7,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ttpay/refundOrderApply/applyDatatable"
        },
        buttons: []
        ,columns: [
            {data:"corpName",bSortable:true,
                render:function(data, type, full, meta){
                    var corp=full.corpName;
                    if(full.corpCode){
                        corp="("+full.corpCode+")"+corp
                    }
                    return corp;
                }
            },
            {data:"payTypeName",bSortable:true},
            {data:"mchId",bSortable:true},
            {data:"serialNo",bSortable:true},
            {data:"trandNo",bSortable:true},
            {data:"payAcc",bSortable:true},
            {data:"trandTime",bSortable:true},
            {data:"refundedMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"trandMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"appStatusName",bSortable:true,className:'center'},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html='';
                if(full.appStatus == '0'){
                    html= '<a class="layui-btn" href="#" data-row="' + meta.row + '" onclick="addRefund(this)">' + bt.lang("ttpay.refundManage.refund") + '</a><span class="text-explode">|</span>';
                }
                    html+='<a class="layui-btn" href="#" data-row="'+meta.row+'" onclick="dtlView(this)">'+bt.lang("common.button.view")+'</a>';
                return html;
                }
            }
        ],
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'8',
                    dataName:'refundedMoney'
                },
                {
                    colNum:'9',
                    dataName:'trandMoney'
                }
            ]
        }
    });
    dicts=bt.dicts.init();
    corp=bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();
});

//退款申请编辑页面
function addRefund(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var param = "id="+data.id+"&payType="+data.payType+"&trandMoney="+data.trandMoney+"&corpId="+data.corpId+"&refundedMoney="+data.refundedMoney;
    parent.layer.open({
        title:bt.lang('ttpay.refundManage.refund'),
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/refundOrderApply/refundApplyMoney?"+param,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

//退款详情页面
function dtlView(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var param = "id="+data.id+"&payType="+data.payType;
    if(data.refundId){
        param+="&refundId="+data.refundId;
    }
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/ttpay/refundOrderApply/view?"+param);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/refundOrderApply/view?"+param,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}


