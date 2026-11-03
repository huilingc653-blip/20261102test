var billTable = {};
var idsValArray=[];
bt.use(['form', 'select', 'datatable', 'dicts', 'users', 'btdate', 'moneyType', 'corp', 'accebillInfo', 'accebillView', 'element', 'bank','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.mulselect.init();
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
            'url': basePath + "/bill/accebill/abbillSearchforManage/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('bill:accebill:abBillSearchforManage:apply'),
                text: bt.lang('common.button.payOffApply'),
                hide: bt.isnotResource('bill:accebill:abBillSearchforManage:apply'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchOp(data, 'Apply');
                }
            }
        ]
        , columns: [
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    if(full.applyCorpName==null || full.applyCorpName==""){
                        return '';
                    }
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
            {data: "origNo", bSortable: true},
            {data: "billTypeName", bSortable: true},
            {data: "appldt", bSortable: true, render: function (data, type, full, meta) {
                    return (full.appldt ? bt.date.format(full.appldt,'yyyy-MM-dd') : "");
                }
            },
            {
                data: "transMoney", className: 'textRight', width: 120, bSortable: true,
                render: function (data, type, full, meta) {
                    var reInfo = '<input id="' + (full.id || '') + 'Rcrsokmoney" name="' + (full.id || '') + 'Rcrsokmoney" value="' + (full.reqamt || '') + '" lay-verify="required" ' +
                        'class="layui-input bt-biz-moneyType trans-money-input" data-key="'+full.id+'" type="text" placeholder="' + bt.lang("bill.accebill.consentpayMoney") + '" data-biz-name="rcrsokmoney"  />';
                    if(full.newStmIssuofpprInd != 1 || full.blgrdbagcrclPrmtInd != 1){
                        reInfo = '<input id="' + (full.id || '') + 'Rcrsokmoney" name="' + (full.id || '') + 'Rcrsokmoney" value="' + (full.reqamt || '') + '" lay-verify="required" ' +
                            'class="layui-input bt-biz-moneyType trans-money-input" readonly="readonly" data-key="'+full.id+'" type="text" placeholder="' + bt.lang("bill.accebill.consentpayMoney") + '" data-biz-name="rcrsokmoney"  />';
                    }
                    return reInfo;
                }
            },
            {
                data: "reqamt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.reqamt);
                }
            },
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "payer", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "payee", bSortable: true},
            {data: "cnAccrecords", bSortable: true},
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "rcrsTpName", bSortable: true},
            {data: "rcrsrsncdName", bSortable: true},
            {data: "reqNm", bSortable: true},
            {data: "recNm", bSortable: true},
            {data: "recAcctId", bSortable: true},
            {data: "statusName", bSortable: true},

            {data: "newStmIssuofpprInd", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict('AB_BILL_NEW_STM_ISSUOFPPR_IND',full.newStmIssuofpprInd);
                }},
            {data: "blgrdbagcrclPrmtInd", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict('AB_BILL_BLGRDBAGCRCL_PRMT_IND',full.blgrdbagcrclPrmtInd);
                }},
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
            {data: "socCode", bSortable: true},


            /*{data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html ="";
            	if(bt.isResource('bill:accebill:abBillSearchforManage:apply',full.corpId)){
            		html+='<a class=""  href="#" data-row="'+meta.row+'" onclick="billInfo(this)">'+bt.lang("common.button.billinfo")+'</a>&nbsp;|&nbsp;';
            	}
            	html+='<a class="" href="#" data-row="'+meta.row+'" onclick="logInfo(this)">'+bt.lang("common.button.logInfo")+'</a>';
                return html;
            }}*/
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.lastid+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
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
            bt.moneyType.init("rcrsokmoney");
            idsValArray = []
            setSumMoney();
        },
        fixedColumns: {
            rightColumns: 1
        },
        summary: {
            pageSum: true,
            selectSum: true,
            outerData: true,
            sumList: [
                {
                    colNum: '8',
                    dataName: 'transMoney'
                },
                {
                    colNum: '9',
                    dataName: 'reqamt'
                },
                {
                    colNum: '10',
                    dataName: 'billMoney'
                }
            ]
        }
    });
    bt.btdate.init({
        elem: "#appldt",
        startname: "ged_appldt",
        endname: "led_appldt"
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

    var accebillInfo = bt.accebillInfo.init();
    var accebillView = bt.accebillView.init();
    bt.funUserReset = function () {
        bt.otheruser.get("userName").reset();
    }
    var idsP = {"id": "6e5d59b43b3742868d82f459010b4937,6e5d59b43b3742868d82f459010b4937", "billDate": "2017-10-22"};
    bt.accebillInfoParams = function () {
        accebillInfo.set(idsP);
        return true;
    }
    bt.accebillViewParams = function () {
        accebillView.set(idsP);
        return true;
    }
    bt.accebillInfoCallback = function (datas, dom) {
        if (datas.data) {
            var ids = "";
            for (var i = 0; i < datas.data.length; i++) {
                if (ids) {
                    ids = ids + ","
                }
                ids = ids + datas.data[i].id;
            }
            idsP.id = ids;
        }
        //console.log(datas);
    }
    var moneyType = bt.moneyType.init();
    bt.dicts.init();
    bt.otheruser = bt.users.init();
    bt.corp.init();
    bt.bank.init({elem: '.bank'});
});

// 表格输入框离开事件，更新表格汇总功能
var timeId = null
$("#appTable").on("blur",".trans-money-input",function () {
    var that = this
    if(timeId){
        clearTimeout(timeId);
        timeId = null
    }
    timeId = setTimeout(function () {
        var key = $(that).data("key");
        var tableData = $("#appTable").DataTable().rows().data()
        if(key && tableData && tableData.length){
            for (var i = 0; i < tableData.length; i++) {
                if(tableData[i].id === key){
                    tableData[i].transMoney = $("#"+key+"Rcrsokmoney").val();
                    break
                }
            }
        }
        bt.datatable.updateSummary("#appTable");
    },50)

})

/**
 * 进行批量操作
 * @param data 选择的数据
 * @param type Send发送，Repulse打回
 */
function batchOp(data, type) {
    // if (!bt.form().verifyFun('#dataForm')) {
    //     return false;
    // }
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var showTitle = "";
    var sendUrl = "";
    showTitle = bt.lang('common.message.confirm.batch' + type);
    sendUrl = "/bill/accebill/abbillSearchforManage/batch" + type;
    var rowData = data.splice(0, len);
    var ids, rcrsokmoneys, chackOrigNos="";

    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abBillSearchforManage:apply', rowData[i].corpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
        var rcrsokmoney = $('#' + rowData[i].id + 'Rcrsokmoney').val();
        if(!rcrsokmoney){
            layer.alert('票据'+rowData[i].origNo+'的同意清偿金额不能为空', {icon: 7});
            return;
        }
        var billMoney = bt.money.toNumber(rowData[i].billMoney);
        var rcrsokMoney1 = bt.money.toNumber($('#' + rowData[i].id + 'Rcrsokmoney').val());
        if (bt.money.sub(billMoney, rcrsokMoney1) < 0){
            if (chackOrigNos){
                chackOrigNos = chackOrigNos + "," +rowData[i].origNo;
            } else {
                chackOrigNos  = rowData[i].origNo;
            }
        }
        if (ids) {
            ids = ids + "," + rowData[i].id;
            rcrsokmoneys = rcrsokmoneys + ";" + rcrsokmoney;
        } else {
            ids = rowData[i].id;
            rcrsokmoneys = rcrsokmoney;
        }
    }

    if (chackOrigNos.length > 0){
        layer.alert("所选的票据包号{"+chackOrigNos+"}的清偿金额不能大于票据金额！");
        return false;
    }
    //证书签名
    var signDataMsg = signData("追索清偿提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(showTitle, function () {
        var sendDate = {"ids": ids, "rcrsokmoneys": rcrsokmoneys};
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
    var recId = data.id;
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
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?viceId=" + data.vvid + "&accId=" + data.id + "&briefId=9022");
        return;
    }
    layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?viceId=" + data.vvid + "&accId=" + data.id + "&briefId=9022"
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

