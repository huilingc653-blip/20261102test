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
            'url': basePath+"/ttpay/refundOrderApply/dataTableApprove"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
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
            {data:"serialNo",bSortable:true},
            {data:"trandNo",bSortable:true},
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
            {data:"currency",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    if(!data){
                        data='CNY';
                    }
                    return bt.dict("TTP_WX_CURRENCY",data);
                }},
            {data:"rmk",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    if($("#flowStatus").val()=='todo'){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
                    }
                    if($("#flowStatus").val() == 'pass'){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="dtlView(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }else {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }
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

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var param = "dtlId="+data.dtlId+"&payType="+data.payType+"&taskId=" + data.taskId;
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/ttpay/refundOrderApply/viewApprove?"+param );
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillendorse/viewApprove?"+param
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
