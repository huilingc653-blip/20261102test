var billTable = {};
var idsValArray=[];
bt.use(['form', 'datatable', 'corp', 'dicts', 'moneyType', 'btdate', 'element','mulselect'], function () {

    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
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
            'url': basePath + "/bill/accebill/abbilldelete/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.delete'),
                hide: bt.isnotResource('bill:accebill:delete:confirm'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBill(data);
                }
            }/*,
		{
		    text: bt.lang('common.button.exports'),
		    action: function ( e, dt, node, config ) {
		    	exports();
		    }
		}*/
        ]
        , columns: [
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
            // {data: "billTypeName", bSortable: true},
            {data: "billType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_TYPE", full.billType);
                }
            },
            // {data: "billFlagName", bSortable: true},
            {data: "billFlag", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_FLAG", full.billFlag);
                }
            },
            // {data: "insideSingName", bSortable: true},
            {data: "insideSing", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_OPENWAY", full.insideSing);
                }
            },
            {data: "payer", bSortable: true},
            {data: "acceptor", bSortable: true},
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
            // {data: "sendSignName", bSortable: true},
            {data: "sendSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_SEND_SIGN", full.sendSign);
                }
            },
            // {data: "statusName", bSortable: true},
            {data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var status = full.status;
                    if (status == -1) {
                        return "打回" ;
                    };
                    if (status == 11) {
                        return  "审批中" ;
                    };
                    if (status == 95) {
                       return "可删除" ;
                    };
                    //return bt.dict("AB_BILL_DELETE_STATUS", full.status);
                }
            },
            {data: "batchNo", bSortable: true},
            // {data: "newStmIssuofpprIndName", bSortable: true},
            {data: "newStmIssuofpprInd", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_NEW_STM_ISSUOFPPR_IND", full.newStmIssuofpprInd);
                }
            },
            // {data: "blgrdbagcrclPrmtIndName", bSortable: true},
            {data: "blgrdbagcrclPrmtInd", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_BLGRDBAGCRCL_PRMT_IND", full.blgrdbagcrclPrmtInd);
                }
            },
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
            {data: "blRmtrUnnIdNo", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
       /* fnDrawCallback:function(){
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
                    colNum: '11',
                    dataName: 'billMoney'
                }
            ]
        }
    });
});

function deleteBill(data) {
    var date = document.getElementById("date");
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var ids, vids, vvids, rmk;
    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:delete:confirm',
            rowData[i].applyCorpId)) {
            layer.alert(bt
                .lang('common.message.dataResourceCheck', [(i + 1)]), {
                icon: 7
            });
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
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        bt.loading.show();
        $.post(basePath + "/bill/accebill/abbilldelete/deleteConfirm", {
            "ids": ids,
            "vids": vids,
            "vvids": vvids
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
            bt.loading.hide();
        }, 'JSON');
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
        content: basePath + "/bill/accebill/abreceivebill/view?viceId=" + data.vvid + "&accId=" + data.id,
    });
}

function exports(el) {
    parent.layer.alert(bt.lang("common.message.indevelopment"));
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
