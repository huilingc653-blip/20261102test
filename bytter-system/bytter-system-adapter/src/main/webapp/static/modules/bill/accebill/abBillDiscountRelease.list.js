var corp, dicts, moneyType, dateType;
var idsValArray=[];
bt.use(['form', 'datatable', 'moneyType', 'dateType', 'corp', 'select', 'dicts','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.corp.init();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
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
            'url': basePath + "/bill/accebill/abBillDiscountRelease/datatable"
        },
        buttons: [
            {
                text: bt.lang('bill.accebill.update.release'),
                hide: bt.isnotResource('bill:accebill:abBillDiscountRelease:apply'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            },
            {
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
            {data: "cnAccrecords", bSortable: true},
            {data: "origNo", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "discountDate", bSortable: true},
            {data: "bankName", bSortable: true},
            {
                data: "occupancyMoney", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.occupancyMoney);
                }
            },
            {data:"newStmIssuofpprIndName",bSortable:true},
            {data:"blgrdbagcrclPrmtIndName",bSortable:true},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
            {data:"blStdAmt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return full.blStdAmt;
                    }else {
                        return "";
                    }
                }
            },
            {data:"blRmtrUnnIdNo",bSortable:true},
            {data: "releaseStateName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    if (data.releaseState == 1) {//待确认
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.sure") + '</a>';
                    }
                    return html;
                }
            }
        ],
        /*fnDrawCallback:function(){
            idsValArray=new Array();
            setSumMoney();
        },*/
        btClickRowCallback:function(data){
            if (data && data.length && data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    //数据选中后，收款人必填
                    if (data[i].isSelect) {
                        $('#' + data[i].accId + 'Signerok').attr("lay-verify", "required|strlength");
                    } else {
                        $('#' + data[i].accId + 'Signerok').attr("lay-verify", "");
                    }
                }
            }
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
                },
                {
                    colNum: '16',
                    dataName: 'realMoney'
                }
            ]
        }
    });
});

//确认更新
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var isCredit = $('#isCredit').val();
    var isDisCountCredit = $('#isDisCountCredit').val();
    if (isCredit != "1" || isDisCountCredit != "1") {
        parent.layer.alert(bt.lang('common.message.isCredit.notoperate'));
        return;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
        $.post(basePath + "/bill/accebill/abBillDiscountRelease/sure?vvid=" + data.viceId + "&id=" + data.accId, function (result) {
            if (result.success) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                bt.datatable.refreshCurrentPage();
            } else {
                parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success ? 1 : 2});
            }
        }, 'JSON');
    });
}

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

//批量提交
function submitBatch(data) {
    var len = data.length;
    var rowData = data.splice(0, len);
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var isCredit = $('#isCredit').val();
    var isDisCountCredit = $('#isDisCountCredit').val();
    if (isCredit != "1" || isDisCountCredit != "1") {
        parent.layer.alert(bt.lang('common.message.isCredit.notoperate'));
        return;
    }
    //批量提交权限控制
    for (var i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abBillDiscountRelease:apply', rowData[i].corpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
    }
    if (!bt.form().verifyFun('#dataForm')) {
        return;
    }
    if (!bt.form().verifyFun('#appTable')) {
        return;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
        url = basePath + "/bill/accebill/abBillDiscountRelease/batchSubmit";
        var vouIds, viceIds, accIds;
        for (var i = 0; i < len; i++) {
            if (accIds) {
                accIds = accIds + "," + rowData[i].accId;
                viceIds = viceIds + "," + rowData[i].viceId;
            } else {
                accIds = rowData[i].accId;
                viceIds = rowData[i].viceId;
            }
        }

        $.post(url, {
            "accIds": accIds,
            "viceIds": viceIds
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');

    })
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].vouId)<0){
                    idsValArray.push(datas[i].vouId);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].vouId);
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
