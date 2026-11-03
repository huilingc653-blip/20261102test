var billTable = {};
var idsValArray=new Array();
bt.use(['form', 'select', 'datatable', 'dicts', 'btdate', 'moneyType', 'mulselect', 'corp', 'element', 'bank'], function () {
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
            'url': basePath + "/bill/accebill/abbillSignSupply/datatable"
        },
        buttons: [
             {
                hide: bt.isnotResource('bill:accebill:abBillSignSupply:submit'),
                text: bt.lang('common.button.makeup'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSign(data);
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
            {data: "billTypeName", bSortable: true},
            {data: "signerType", bSortable: true},
            {data: "origNo", bSortable: true},
            {data: "bankendrsmtmkName", bSortable: true},
            {data: "endorsementCount", bSortable: true, render: function (data, type, full, meta) {
                    if (data == null) {
                        data = 0;
                    }
                    var html = '<a class="" href="#" data-row="' + meta.row + '" ' +
                        'onclick="endorsementCountDtl(\'' + full.id + '\')">' + data + '</a>';
                    return html;
                }
            },
            {data: "bussdt", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "payer", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "cnAccrecords", bSortable: true},
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "payerbankName", bSortable: true},
            {data: "acceptorbankName", bSortable: true},
            {data: "reqNm", bSortable: true},
            {data: "recNm", bSortable: true},
            {data: "recAcctId", bSortable: true},
            {data: "recBankTypeName", bSortable: true},
            {data: "sttlmmkName", bSortable: true},
            {data: "statusName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    return '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                }
            }
        ],
        btClickRowCallback:function(data){
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
                    colNum: '11',
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
    bt.btdate.init({
        elem: "#bussdt",
        startname: "ged_bussdt",
        endname: "led_bussdt"
    });
    var moneyType = bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
    bt.bank.init({elem: '.bank'});
});

/**
 * 进行批量补录操作
 * @param data 数据
 */
function batchSign(data) {
    if (!bt.form().verifyFun('#dataForm')) {
        return false;
    }
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }

    var rowData = data.splice(0, len);
    var ids = [], vvids = [], recids = [];
    for (i = 0; i < len; i++) {
        ids.push(rowData[i].id)
        recids.push(rowData[i].recid)
        vvids.push(rowData[i].vvid)
    }

    var signData = JSON.stringify({"ids": ids.join(','),"recids": recids.join(','),"vvids": vvids.join(','),
        "busCorpIdSupply": bt.corp.get("busCorpIdSupply").val(), "busPersonnelSupply": $("#busPersonnelSupply").val()});

    parent.layer.confirm(bt.lang('common.message.confirm.signSupply'), function () {
        bt.loading.show();
        bt.jquery.ajax({
            type: "POST",
            url: basePath + "/bill/accebill/abbillSignSupply/signSupply",
            data: signData,//将对象序列化成JSON字符串
            dataType: "json",
            async: false,
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            },
            error: function (res) {
                bt.loading.hide();
            }
        });
    });
}

// 日志信息
function logInfo(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['550px', '330px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abLog/abLogView?ids=" + data.vvid,
        end: function () {
//        	billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}

// 背书转让详情页
function endorsementCountDtl(id) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abbillQuery/endorsementCountDtl?recid=" + id);
        return;
    }
}

// 业务详细
function businessDtlView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.businessDtl') + '-' + data.origNo, basePath + "/bill/accebill/abbillreceivebiz/businessDtlView?id=" + data.id + "&msgtype=" + data.msgtype);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillreceivebiz/businessDtlView?id=" + data.id + "&msgtype=" + data.msgtype,
        end: function () {
//        	billTable.draw();
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
    if($("#sumMoneyBillSpan").text()!='') {
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

// 详情页
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

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);
}


