var billTable = {};
var idsValArray=[];
bt.use(['form', 'datatable', 'moneynum', 'corp', 'dicts', 'moneyType', 'btdate', 'dicts', 'element','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.mulselect.init();
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
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,

        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abReceiveBillLock/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.lock'),
                hide: bt.isnotResource('bill:accebill:abReceiveBillLock:lock'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchLock(data);
                }
            },
            {
                text: bt.lang('common.button.unLock'),
                hide: bt.isnotResource('bill:accebill:abReceiveBillLock:unLock'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchUnLock(data);
                }
            },
            /*{
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {
                    exports();
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
                data: "corpIdName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpIdName;
                }
            },
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data: "origNo", bSortable: true},
            {data: "payer", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "cnAccrecords", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "enterStockName", bSortable: true},
            {data: "sendSignName", bSortable: true},
            {data: "briefIdName", bSortable: true},
            {data: "lockSignName", bSortable: true},
            {
                data: "reason", render: function (data, type, full, meta) {
                    if (full.lockSign == 0) {//如果是处于锁定状态
                        return full.reason;
                    } else {
                        return '<input class="layui-input" placeholder="' + bt.lang('table.AbReceiveBillLock.lockReason') + '" name="lockReason" lay-verify="strlength" data-maxlength="100" id="reason_' + full.id + '"></input>';
                    }
                }
            },

            {data: "newStmIssuofpprIndName", bSortable: true},
            {data: "blgrdbagcrclPrmtIndName", bSortable: true},
            {data: "sbbllRngStrtSn", bSortable: true},
            {data: "sbbllRngEndSn", bSortable: true},
            {data: "blStdAmt", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return bt.money.format(full.blStdAmt);
                    }else {
                        return "";
                    }
                }},
            {data: "batchNo", bSortable: true},

            {
                data: "lockSign", bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = "";
                    if (data == "1") {
                        if (bt.isResource('bill:accebill:abReceiveBillLock:lock', full.corpId)) {
                            html = '<a class="" href="#" data-row="' + meta.row + '" onclick="lock(this)">' + bt.lang("common.button.lock") + '</a>';
                        }
                    } else {
                        if (bt.isResource('bill:accebill:abReceiveBillLock:unLock', full.corpId)) {
                            html = '<a class="" href="#" data-row="' + meta.row + '" onclick="unLock(this)">' + bt.lang("common.button.unLock") + '</a>';
                        }
                    }
                    if (html) {
                        html += '<span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        /*fnDrawCallback:function(){
            idsValArray=new Array();
            setSumMoney();
        },*/
        btClickRowCallback:function(data){
            bt.repeatLockSign(data);
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
                    colNum: '11',
                    dataName: 'billMoney'
                }
            ]
        }
    });
    var date = new Date();
    var currentTime = date.Format("yyyy-MM-dd");
    var dd = bt.btdate.init({
        elem: ".date-single",
        // labelname:bt.lang('table.abVoucherinfo.Business.date'),
        name: "date"
    });
    dd.setValue({value: currentTime});
});

bt.repeatLockSign = function (data) {
    var data = billTable.rows('.selected').data();
    var len = data.length;
    var isrepeat = false;
    var sign;
    var flag = true;
    if (len > 0) {
        var rowData = data.splice(0, len);
        for (i = 0; i < len; i++) {
            var lockSign = rowData[i].lockSign;
            if (sign) {
                if (sign != lockSign) {
                    flag = false;
                    break;
                }
            }
            sign = lockSign;
        }
        if (!flag) {
            layer.alert(bt.lang('common.message.sameBusiness'), {icon: 7});
        }
    }
}

/**
 * 批量锁定
 * @param data
 */
function batchLock(data) {
    if (!bt.form().verifyFun('#dataForm')) {
        return false;
    }
    var date = document.getElementById("date");
    var reason = $("#reason").val();
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    if (isNull(reason)) {
        $("#reason").attr("lay-verify", "required");
        if (!bt.form().verifyFun('#dataForm')) {
            $("#reason").attr("lay-verify", "strlength");
            return false;
        }
    }
    var rowData = data.splice(0, len);
    var ids, vids, vvids;
    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abReceiveBillLock:lock',
            rowData[i].corpId, (i + 1))) {
            return;
        }
        if (rowData[i].lockSign == '0') {
            parent.layer.alert(bt.lang("common.message.sameLock", [i + 1]));
            return;
        }
        if (vids) {
            vids = vids + "," + rowData[i].vid;
        } else {
            vids = rowData[i].vid;
        }
        if (vvids) {
            vvids = vvids + "," + rowData[i].vvid;
        } else {
            vvids = rowData[i].vvid;
        }
        if (ids) {
            ids = ids + "," + rowData[i].id;
        } else {
            ids = rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.lock'), function () {
        $.post(basePath + "/bill/accebill/abReceiveBillLock/batchLock", {
            "date": date.value,
            "reason": reason,
            "ids": ids,
            "vids": vids,
            "vvids": vvids
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

/**
 * 批量解锁
 * @param data
 */
function batchUnLock(data) {
    if (!bt.form().verifyFun('#dataForm')) {
        return false;
    }
    var date = document.getElementById("date");
    var reason = $("#reason").val();
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var ids, vids, vvids;
    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abReceiveBillLock:unLock',
            rowData[i].corpId, (i + 1))) {
            return;
        }
        if (rowData[i].lockSign == '1') {
            parent.layer.alert(bt.lang("common.message.sameUnLock", [i + 1]));
            return;
        }
        if (vids) {
            vids = vids + "," + rowData[i].vid;
        } else {
            vids = rowData[i].vid;
        }

        if (vvids) {
            vvids = vvids + "," + rowData[i].vvid;
        } else {
            vvids = rowData[i].vvid;
        }
        if (ids) {
            ids = ids + "," + rowData[i].id;
        } else {
            ids = rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.unLock'), function () {
        $.post(basePath + "/bill/accebill/abReceiveBillLock/batchUnLock", {
            "date": date.value,
            "reason": "",
            "ids": ids,
            "vids": vids,
            "vvids": vvids
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.id
    });
}

/**
 * 锁定
 */
function lock(el) {
    if (!bt.form().verifyFun('#dataForm')) {
        return false;
    }
    var date = document.getElementById("date");
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var reaName = "#reason_" + data.id;
    var reason = $(reaName).val();
    if (isNull(reason)) {
        $(reaName).attr("lay-verify", "required");
        if (!bt.form().verifyFun('#appTable')) {
            $(reaName).attr("lay-verify", "");
            return false;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.lock'), function () {
        $.post(basePath + "/bill/accebill/abReceiveBillLock/lock", {
            "date": date.value,
            "reason": reason,
            "ids": data.id,
            "vids": data.vid,
            "vvids": data.vvid
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

/**
 * 解锁
 */
function unLock(el) {
    if (!bt.form().verifyFun('#dataForm')) {
        return false;
    }
    var date = document.getElementById("date");
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.unLock'), function () {
        $.post(basePath + "/bill/accebill/abReceiveBillLock/unLock", {
            "date": date.value,
            "reason": "",
            "ids": data.id,
            "vids": data.vid,
            "vvids": data.vvid
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

/**
 * 判断是否null
 * @param data
 */
function isNull(data) {
    return (data == "" || data == undefined || data == null) ? true : false;
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
    bt.corp.init().get("corpId").setUrl2("accCorp",data);
}

