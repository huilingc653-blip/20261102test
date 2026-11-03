var dicts;
var idsValArray=new Array();
bt.use(['form', 'datatable', 'moneyType', 'moneynum', 'btdate', 'corp', 'select', 'dicts', 'element','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.mulselect.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abbillinvenfetch/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('bill:accebill:abbillinvenfetch:fetch'),
                text: bt.lang('bill.accebill.update.fetch'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            },/*{
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {

                }
            },*/{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "billTypeName", bSortable: true},
            {data: "billFlagName", bSortable: true},
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data: "payer", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "origNo", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "payee", bSortable: true},
            {data: "cnAccrecords", bSortable: true},
            {data: "statusName", bSortable: true},
            {data: "enterStockName", bSortable: true},
            {data: "briefIdName", bSortable: true},
            {data: "billSourceName", bSortable: true},
            {data: "poolStatusName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    //html+='<span class="text-explode">|</span>';
                    // html += '<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.accId+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        /*fnDrawCallback:function(){
            idsValArray=new Array();
            setSumMoney();
        },*/
        btClickRowCallback:function(data){
            isNet();
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            idsValArray = new Array()
            setSumMoney();
        },
        fixedColumns: {
            rightColumns: 1
        },
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '10',
                    dataName: 'billMoney'
                }
            ]
        }
    });
    bt.btdate.init({
        elem: "#startDate",
        startname: "ged_startDate",
        endname: "led_startDate"
    });

    bt.btdate.init({
        elem: "#endDate",
        startname: "ged_endDate",
        endname: "led_endDate"
    });
});

bt.use(['btdate'], function () {
    var date = new Date();
    var currentTime = date.Format("yyyy-MM-dd");
    var dd = bt.btdate.init({
        elem: ".date-single",
        // labelname : bt.lang('table.abVoucherinfo.Business.date'),
        name: "businessdate",
        choose: function (data) {
//			console.log(data)
        }
    });
    dd.setValue({value: currentTime})
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vouId + "&viceId=" + data.viceId + "&accId=" + data.accId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vouId + "&viceId=" + data.viceId + "&accId=" + data.accId
    });
}

//选择所属库存刷新列表
function enterStockCall() {
    bt.loading.show();//显示加载中
    bt.datatable.refreshCurrentPage();
    bt.loading.hide(); //关闭加载中
}


/**
 * 进行批量提交操作
 * @param data 选择的数据
 */
function submitBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var vouIds, viceIds, accIds;
    var enterStock = dicts.get("abEnterStock").val();
    var businessdate = $("[name=businessdate]").val();
    if (!isNet()) {
        return;
    }
    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abbillinvenfetch:fetch', rowData[i].applyCorpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
        if (vouIds) {
            vouIds = vouIds + "," + rowData[i].vouId;
        } else {
            vouIds = rowData[i].vouId;
        }
        if (viceIds) {
            viceIds = viceIds + "," + rowData[i].viceId;
        } else {
            viceIds = rowData[i].viceId
        }
        if (accIds) {
            accIds = accIds + "," + rowData[i].accId;

        } else {
            accIds = rowData[i].accId;
        }
        if (enterStock == rowData[i].enterStock) {
            parent.layer.alert(bt.lang('common.message.confirm.enterstockequally'));
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchfetch'), function () {
        bt.jquery.ajax({
            type: "POST",
            url: basePath + "/bill/accebill/abbillinvenfetch/batchChangeEnterStock",
            data: JSON.stringify({
                "vouId": vouIds,
                "viceId": viceIds,
                "accId": accIds,
                "enterStock": enterStock,
                "businessdate": businessdate
            }),
            dataType: "json",
            async: false,
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            },
            error: function (res) {
            }
        });
    });

}

//获取选中的所有数据
function getSelectData() {
    var selectData;
    bt.use('datatable', function () {
        selectData = bt.datatable.getSelectData("#appTable");
    })
    return selectData;
}


//判断所选单位是否是网点
function isNet() {
    var selectData = getSelectData();
    if (!selectData.length) {
        return false;
    }
    var corpIds;
    var corpNames;
    for (var i = 0; i < selectData.length; i++) {
        if (corpIds) {
            corpIds = corpIds + "," + selectData[i].corpId;
            corpNames = corpNames + "," + selectData[i].corpName;
        } else {
            corpIds = selectData[i].corpId;
            corpNames = selectData[i].corpName;
        }
    }
    var listSysCorp;
    //获取所选数据的单位信息
    $.ajax({
        type: 'post',
        url: basePath + "/bill/accebill/abBillDiscountApply/listSysCorp",
        data: {corpIds: corpIds, corpNames: corpNames},
        async: false,
        success: function (result) {
            if (result.success) {
                listSysCorp = result.obj;
            } else {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }
        },
        dataType: 'json'
    });
    if (!listSysCorp.length) {           //无法获取单位信息,请检查数据!
        //layer.alert(bt.lang('bill.accbill.abbillDiscountApply.corp.isnotexist'),{icon:7});
        return false;
    }
    var enterStock = dicts.get("abEnterStock").val();
    for (var i = 0; i < listSysCorp.length; i++) {
        if ("0" == (listSysCorp[i].type + "") && (listSysCorp[i].parentId == null || listSysCorp[i].parentId == "") && enterStock == '11') {   //是顶级网点 且选择中心代管
            //所属单位为顶级网点的票据不允许变更库存到中心代管
            layer.alert(bt.lang('bill.accbill.abBillInvenTransfer.corp.isNetNot'), {icon: 7});
            bt.datatable.onCheckRow('#appTable', {
                key: 'corpId',
                value: listSysCorp[i].id,
                checked: false
            });
            return false;
        }
    }
    return true;
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].accId)<0){
                    idsValArray.push(datas[i].accId);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].accId);
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

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);

}