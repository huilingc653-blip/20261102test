var billTable = {};
var idsValArray=[];
bt.use(['form', 'select', 'datatable', 'dicts', 'btdate', 'moneyType', 'corp', 'element','mulselect','accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.mulselect.init();
    var accountTable = bt.accountTable.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abbillRecall/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('bill:accebill:abBillRecall:apply'),
                text: bt.lang('common.button.recallApply'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchOp(data, 'Apply');
                }
            }
        ]
        , columns: [
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
            {data: "bankAcc", bSortable: true},
            {data: "origNo", bSortable: true},
            {data: "billTypeName", bSortable: true},
            {data: "recallTypeName", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "payer", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "payee", bSortable: true},
            /*{data: "cnAccrecords", bSortable: true},*/
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "statusName", bSortable: true},
            {data: "batchNo", bSortable: true},
            {data: "newStmIssuofpprIndName", bSortable: true},
            {data: "blgrdbagcrclPrmtIndName", bSortable: true},
            {data: "sbbllRngStrtSn", bSortable: true},
            {data: "sbbllRngEndSn", bSortable: true},
            {data:"blStdAmt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return bt.money.format(full.blStdAmt);
                    }else {
                        return "";
                    }
                }
            },
            {data:"blRmtrUnnIdNo",bSortable:true},
            /* {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                 var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="billInfo(this)">'+bt.lang("common.button.billinfo")+'</a>&nbsp;|&nbsp;\
                 <a class="" href="#" data-row="'+meta.row+'" onclick="logInfo(this)">'+bt.lang("common.button.logInfo")+'</a>';
                 return html;
             }}*/
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        /*fnDrawCallback:function(){
            idsValArray=new Array();
            setSumMoney();
        },*/
        btClickRowCallback:function(data){
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            idsValArray = []
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
                    colNum: '9',
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
    var moneyType = bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
});

/**
 * 进行批量操作
 * @param data 选择的数据
 * @param type Send发送，Repulse打回
 */
function batchOp(data, type) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var showTitle = "";
    var sendUrl = "";
    showTitle = bt.lang('common.message.confirm.batch' + type);
    sendUrl = "/bill/accebill/abbillRecall/batch" + type;
    var rowData = data.splice(0, len);
    var ids, recallTypes;
    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abBillRecall:apply', rowData[i].applyCorpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
        if (ids) {
            ids = ids + "," + rowData[i].id;
        } else {
            ids = rowData[i].id;
        }
        if (recallTypes) {
            recallTypes = recallTypes + "," + rowData[i].recallType;
        } else {
            recallTypes = rowData[i].recallType;
        }
    }
    //证书签名
    var signDataMsg = signData("通用撤回提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(showTitle, function () {
        var sendDate = {"ids": ids, "recallTypes": recallTypes};
        bt.jquery.ajax({
            type: "POST",
            url: basePath + sendUrl,
            data: JSON.stringify(sendDate),//将对象序列化成JSON字符串
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

function logInfo(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var ids = "";
    if (data.id) {
        if (ids) {
            ids += ",";
        }
        ids += data.id;
    }
    if (data.vvid) {
        if (ids) {
            ids += ",";
        }
        ids += data.vvid;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abLog/abLogView?ids=" + ids,
        end: function () {
//        	billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}

/**
 * 进行票据信息查看
 * @param id 签收表ID
 * @param type front正面，back背面
 */
var billInfo = function (el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var recId = data.recId;
    var origNo = data.origNo;
    parent.layer.open({
        title: bt.lang('common.button.billinfo'),
        type: 2,
        area: ['850px', '730px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abvoucherinfo/billEcdsInfoView?recId=" + recId + "&origNo=" + origNo
    });
}

function view(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if (data.type == 0) {
        emitView(data);
    } else {
        receiveView(data);
    }
}

function emitView(data) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abemitbill/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbill/view/" + data.id
    });
}

function receiveView(data) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?viceId=" + data.vvid + "&accId=" + data.id);
        return;
    }
    layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?viceId=" + data.vvid + "&accId=" + data.id
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

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);
}

/**
 * 选择账户回调
 * @param data
 */
function accountCallBack (data) {
    $("#likes_bankAcc").val(data.bankAcc);
    //selectType.get("eqs_curId").val(data.curId);
}

function bankTypeCallBack(data){
    if(!data){
        return;
    }
    var bankTypeIdStr = "";
    for(var i = 0; i < data.length; i++){
        if(bankTypeIdStr == ""){
            bankTypeIdStr = data[i].id;
        }else {
            bankTypeIdStr = bankTypeIdStr + "," + data[i].id;
        }
    }
    bt.accountTable.get("likes_bankAcc").reload({
        urldata: {
            bankTypeId: bankTypeIdStr,
        }
    });
}