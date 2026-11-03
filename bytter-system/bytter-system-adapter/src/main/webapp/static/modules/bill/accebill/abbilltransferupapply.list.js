var billTable = {};
var idsValArray=new Array();
bt.use(['form', 'datatable', 'moneyType', 'btdate', 'corp', 'select', 'dateType', 'dicts', 'bank','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.mulselect.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank'
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
            'url': basePath + "/bill/accebill/abbilltransferupapply/datatable"
        },
        buttons: [
            //上划、下载
            {
                text: bt.lang('common.button.transferup'),
                hide: bt.isnotResource('bill:accebill:abbilltransferupapply:transferup'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    transferup(data);
                }
            },/*{
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	exports(data);
                }
            },*/
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
            {data:"newStmIssuofpprInd",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict('AB_BILL_NEW_STM_ISSUOFPPR_IND',full.newStmIssuofpprInd);
                }},
            {data:"blgrdbagcrclPrmtInd",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict('AB_BILL_BLGRDBAGCRCL_PRMT_IND',full.blgrdbagcrclPrmtInd);
                }},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},

            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "enterStockName", bSortable: true},
            {data: "statusName", bSortable: true},
            {
                data: "transMoney", bSortable: true, className: 'textRight', width: 120,
                render: function (data, type, full, meta) {
                    if (full.newStmIssuofpprInd === '1' && full.blgrdbagcrclPrmtInd === '1') {
                        return '<input lay-verify="required" style="width: 100px" class="layui-input bt-input-inline bt-biz-moneyType trans-money-input" readonly="readonly" ' +
                            'value="'+bt.money.format(full.transMoney)+'" type="text" data-decimal="2" data-min="0.01" data-max="' + full.transMoney + '"' +
                            'data-id="transMoney_'+full.id+'" data-key="'+full.id+'" data-name="transMoney_'+full.id+'" data-biz-name="transMoney_'+full.id+'" />';
                    }
                    return bt.money.format(full.transMoney);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html+='<span class="text-explode">|</span>';
                    // html += '<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.aid+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        /*fnDrawCallback:function(){
            idsValArray=new Array();
            setSumMoney();
        },*/
        btClickRowCallback:function(data){
            if(data[0].newStmIssuofpprInd === '1' && data[0].blgrdbagcrclPrmtInd === '1'){
                // 新票交易金额字段赋值
                data[0].transMoney = $("#transMoney_" + data[0].id).val();
            }
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            bt.moneyType.init();
            idsValArray = []
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
                },
                {
                    colNum: '20',
                    dataName: 'transMoney'
                }
            ]
        }
    });
    //格式化数字
    $.each($("input[decimal]"), function (i, item) {
        if ($(item).attr("decimal") != "") {
            bt.moneynum.init({elem: $(item), decimal: $(item).attr('decimal') * 1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
    //时间
    $.each($("div[type='date']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    var dd = bt.btdate.init({
        elem: ".date-single",
        // labelname:bt.lang('table.abVoucherinfo.Business.date'),
        name: "businessdate"
    });
    dd.setValue({value: new Date().Format("yyyy-MM-dd")});
    //init();
});

//上划
function transferup(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abbilltransferupapply:transferup', rowData[i].corpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.transferup'), function () {//确定要上划吗?
        bt.loading.show();
        var vids, vvids, aids,transMoneys;
        for (i = 0; i < len; i++) {
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
            if (aids) {
                aids = aids + "," + rowData[i].aid;
            } else {
                aids = rowData[i].aid;
            }
            if (transMoneys) {
                transMoneys = transMoneys + "," + rowData[i].transMoney;
            } else {
                transMoneys = rowData[i].transMoney;
            }
        }
        $.post(basePath + "/bill/accebill/abbilltransferupapply/transferup", {
            "vids": vids,
            "vvids": vvids,
            "aids": aids,
            "transMoneys":transMoneys,
            "businessDate": $("[name=businessdate]").val()
        }, function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

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
                    tableData[i].transMoney = $("#transMoney_"+key).val();
                    break
                }
            }
        }
        bt.datatable.updateSummary("#appTable");
    },50)

})

function view(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.aid
    });
}

/*
function init(){
	var $ = bt.jquery,form = bt.form();
	//持票单位账号下拉框初始化
    bt.select.init({
        elem:"[field='bankAcc']",
        name:"eqs_bankAcc",
        valueField:"bankAcc",
        textField:"bankAcc",
        placeholder:{value:'',text:bt.lang('common.placeholder.select')},
        search: true,
        disabled: false,
        url:basePath+"/bankacc/btbankacc/json"
    });

}*/

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].aid)<0){
                    idsValArray.push(datas[i].aid);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].aid);
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
