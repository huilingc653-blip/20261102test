var billTable = {};
var corp, isCounterpartEndorse, isRepeatEndorse;
var up;
var fromObj;
var externalCorpBankAcc = {};
var idsValArray=new Array();
bt.use(['form', 'radioType', 'datatable', 'selectTable', 'dicts', 'bank', 'corp', 'mulselect', 'btdate', 'jquery', 'bankInfo', 'moneyType', 'dateType', 'externalCorpBankAcc', 'element', 'accountTable', 'btImport', 'externalCorp', 'accebillInfo', 'accebillView'], function () {
    $ = bt.jquery, form = bt.form();
    fromObj = form;
    bt.bank.init({elem: '.bank'})
    corp = bt.corp.init();
    bt.moneyType.init();
    bt.bankInfo.init();
    bt.dateType.init();
    bt.mulselect.init();
    bt.accountTable.init({urldata: {accountType: '2,3'}});
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
    billTable = bt.datatable.render({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc'],[4, 'desc'],[10, 'desc']],
        mulselect: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/endorseRequest/datatable"
        },
        buttons: [
            {
                text: bt.lang('table.endorseRequest.endorseRequest'),
                hide: bt.isnotResource('bill:accebill:endorseRequest:apply'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    billEndorse(data);
                }
            }
        ]
        , columns: [
            {data: "serialNo", bSortable: true},
            {data: "billTypeName", bSortable: true},
            {data: "billFlagName", bSortable: true},
            {data: "enterStockName", bSortable: true},
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
            {data: "corpIdName", bSortable: true},
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
            {data: "briefIdName", bSortable: true},
            {data: "sendSignName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_BUSSTATUS", data);
                }
            },
            {data: "optStatusName", bSortable: true},
            {data: "rmk", bSortable: true},
            {
                data: "attachmentNum",
                bSortable: false,
                className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="attachmentView(this)">' + bt.lang("bill.accebill.attachment.num", [(full.attachmentNum || 0)]) + '</a>';
                    return html;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    return '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                }
            }
        ],
        btClickRowCallback: function (data) {
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            idsValArray = new Array()
            setSumMoney();
        },
        fixedColumns: {
            rightColumns: 2
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
    var dicts = bt.dicts.init();
});


function billEndorse(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = [];
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        ids.push(rowData[i].id)
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        $.post(basePath + "/bill/accebill/endorseRequest/endorseRequest", {ids: ids.join(',')}, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                location.reload();
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

//获取选中的所有数据
function getSelectData() {
    var selectData;
    bt.use('datatable', function () {
        selectData = bt.datatable.getSelectData("#appTable");
    })
    return selectData;
}


function attachmentView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var isNotOp = data.status != 11 ? '0' : '1';
    var params = {
        "id": data.id,
        "attachmentId": (data.attachmentId || null),
        "vvid": (data.vvid || null),
        "briefId": '010',
        "isNotOp": isNotOp
    };
    parent.layer.open({
        type: 2,
        area: ['820px', '400px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillattachment/view?" + $.param(params),
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}



var accBillIdsValArray = new Array();

// 查找指定的元素在数组中的位置
Array.prototype.indexOf = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) {
            return i;
        }
    }
    return -1;
}

var objArrayRemove = function (obj, index) {
    if (index > -1) {
        obj.splice(index, 1);
    }
};

var deleteSelectBill = function (id) {
    opAccBillIdsValArray(id, true);
    $("#billAccids").val(accBillIdsValArray.join(","));
    billTable.draw();
}

var opAccBillIdsValArray = function (accBillid, isDelete) {
    if (!isDelete) {
        if (accBillIdsValArray.indexOf(accBillid) < 0) {
            accBillIdsValArray.push(accBillid);
        }
    } else {
        var index = accBillIdsValArray.indexOf(accBillid);
        if (index > -1) {
            objArrayRemove(accBillIdsValArray, index);
        }
    }
    $("#ids").val(accBillIdsValArray.join(","));
};


// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        if ($("#budgetSel").val() != "1") {  // 不挂接资金计划
            for (i = 0; i < datas.length; i++) {
                if (datas[i].isSelect) {
                    if (idsValArray.indexOf(datas[i].id) < 0) {
                        idsValArray.push(datas[i].id);
                    }
                } else {
                    var index = idsValArray.indexOf(datas[i].id);
                    if (index > -1) {
                        objArrayRemove(idsValArray, index);
                    }
                }
            }
        } else {
            if(datas[0].isSelect) {
                idsValArray[0] = datas[0].id
            } else {
                objArrayRemove(idsValArray,0);
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
    //bt.corp.init().get("corpIdQuery").setUrl2("accCorp",data);
}


