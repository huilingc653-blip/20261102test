var billTable = {}, dicts;
var idsValArray=[];
bt.use(['form', 'datatable', 'moneyType', 'btdate', 'corp', 'select', 'dateType', 'dicts','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    dicts = bt.dicts.init();
    bt.mulselect.init();
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
            'url': basePath + "/bill/accebill/abbillcollectionaccount/datatable"
        },
        buttons: [
            //到账确认、下载
            {
                text: bt.lang('common.button.arrivalConfirm'),
                hide: bt.isnotResource('bill:accebill:collectionaccount:confirm'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submit(data);
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
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {
                data: "signerok", render: function (data, type, full, meta) {
                    if (data == null || data == 'null') {
                        data = '';
                    }
                    if (full.status == 11 || full.status == 94) {//审批中
                        return full.signerok + '<input type="hidden" class="layui-input" value="' + data + '" name="signerok" id="' + full.aid + 'signerok" lay-verify=""></input>';
                    } else {
                        return '<input class="layui-input" value="' + data + '" name="signerok" id="' + full.aid + 'signerok" lay-verify=""></input>';
                    }
                }
            },
            {data: "enterStockName", bSortable: true},
            {data: "sendSignName", bSortable: true},
            {data:"newStmIssuofpprIndName",bSortable:true},
            {data:"blgrdbagcrclPrmtIndName",bSortable:true},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
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
            {data: "statusName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.aid+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
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

//托收到账
function submit(data) {
    var $ = bt.jquery;
    len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);

    for (i = 0; i < len; i++) {
        if (!$("#" + rowData[i].aid + "signerok").val()) {
            //你选择的第{0}条数据没有填写收款人!
            layer.alert(bt.lang('accebill.abbillcollectionaccount.message.signerok', [(i + 1)]), {icon: 7});
            return;
        }
    }

    parent.layer.confirm(bt.lang('common.message.confirm.submit.abbillcollectionaccount'), function () {//确定托收到账吗?
        var vids, vvids, aids, signeroks;
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
            if (signeroks) {
                signeroks = signeroks + "," + $("#" + rowData[i].aid + "signerok").val();
            } else {
                signeroks = $("#" + rowData[i].aid + "signerok").val();
            }
        }
        $.post(basePath + "/bill/accebill/abbillcollectionaccount/collectionApply", {
            "vids": vids,
            "vvids": vvids,
            "aids": aids,
            "signerok": signeroks,
            "businessDate": $("[name=businessdate]").val()
        }, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
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
//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);
}

