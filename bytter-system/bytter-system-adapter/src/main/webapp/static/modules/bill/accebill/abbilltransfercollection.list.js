var billTable = {}, netBankAcc;
var idsValArray=new Array();
bt.use(['form', 'datatable', 'moneyType', 'btdate', 'corp', 'select', 'dateType', 'dicts', 'bank','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    dateFun = bt.dateType.getFun();
    bt.dicts.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank'
    });

    //票据单位初始化
    bt.select.init({
        elem: '#corpId',
        name: "eqs_corpId",
        valueField: "id",
        textField: "name",
        placeholder: {value: '', text: bt.lang('common.placeholder.select')},
        search: true,
        disabled: false,
        url: basePath + "/bill/accebill/abbilltransfercollection/getCorpIdByNetId?netId=" + $("#netId").val(),
        change: function (data, text) {
            /*corpCallback(data);*/
        }
    });
    //申请单位初始化
    bt.select.init({
        elem: '#applyCorpId',
        name: "eqs_applyCorpId",
        valueField: "id",
        textField: "name",
        placeholder: {value: '', text: bt.lang('common.placeholder.select')},
        search: true,
        disabled: false,
        url: basePath + "/bill/accebill/abbilltransfercollection/getCorpIdByNetId?netId=" + $("#netId").val(),
        change: function (data, text) {
            // applyCorpCallback(data)
        }
    });
    //业务单位初始化
    bt.select.init({
        elem: '#busCorpId',
        name: "eqs_busCorpId",
        valueField: "id",
        textField: "name",
        placeholder: {value: '', text: bt.lang('common.placeholder.select')},
        search: true,
        disabled: false,
        url: basePath + "/bill/accebill/abbilltransfercollection/getCorpIdByNetId?netId=" + $("#netId").val(),
        change: function (data, text) {
            /*corpCallback(data);*/
        }
    });
    //归集中心账号下拉框初始化
    bt.select.init({
        elem: '#netBankAcc',
        name: "netBankAcc",
        valueField: "bankAcc",
        textField: "bankAcc",
        placeholder: {value: '', text: bt.lang('common.placeholder.select')},
        search: true,
        disabled: false,
        required: true,
        url: basePath + "/bill/accebill/abbilltransfercollection/getNetBankAccByNetId?netId=" + $("#netId").val(),
        change: function (data, text) {
            netBankAcc = text;
        }
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
            'url': basePath + "/bill/accebill/abbilltransfercollection/datatable"
        },
        buttons: [
            //归集、下载
            {
                text: bt.lang('common.button.transfercollection'),
                hide: bt.isnotResource('bill:accebill:abbilltransfercollection:transfercollection'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    transferup(data);
                }
            },/*{
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	imports(data);
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
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    /* html+='<span class="text-explode">|</span>';*/
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
    dd.setValue({value: new Date().Format("yyyy-MM-dd")})

});

//归集
function transferup(data) {
    var $ = bt.jquery;
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    if (!netBankAcc) {
        parent.layer.alert(bt.lang('bill.accebill.abEmitBill.message.netBankAcc'));//请选择归集中心账号!
        return;
    }
    var rowData = data.splice(0, len);
    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abbilltransfercollection:transfercollection', rowData[i].corpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.transfercollection'), function () {//确定要归集吗?
        bt.loading.show();
        var vids, vvids, aids, netId;
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
        }
        $.post(basePath + "/bill/accebill/abbilltransfercollection/transfercollection",
            {
                "vids": vids, "vvids": vvids, "aids": aids, "netBankAcc": $("[name=netBankAcc]").val(),
                "businessDate": $("[name=businessdate]").val()
            }, function (result) {
                bt.loading.hide();
                parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }
            }, 'JSON');
    });
}

//中心回调函数
function netCallback(data) {
    location.replace(basePath + "/bill/accebill/abbilltransfercollection/list?netId=" + data.id);
}

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

