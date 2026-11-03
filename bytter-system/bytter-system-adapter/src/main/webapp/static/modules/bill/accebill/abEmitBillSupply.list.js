var billTable = {};
var idsValArray = [];
bt.use(['form', 'datatable', 'moneyType', 'btdate', 'corp', 'select', 'dateType', 'btImport', 'dicts','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
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
            'url': basePath + "/bill/accebill/abEmitBillSupply/datatable"
        },
        buttons: [
            //补录,对内票据默认入库
            {
                text: bt.lang('button.batch.collection'),
                hide: bt.isnotResource('bill:accebill:abemitbillsupply:makeup'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    makeupBatch(data);
                }
            }, {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    importExcel();
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
            {data: "appNo", bSortable: true},
            {data: "billTypeName", bSortable: true},
            {data: "billFlagName", bSortable: true},
            {data: "insideSingName", bSortable: true},
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
            {data: "sendSignName", bSortable: true},
            {data: "emitTypeName", bSortable: true},
            {data: "emitGuandisSignName", bSortable: true},
            {data: "statusName", bSortable: true},
            {data: "briefIdName", bSortable: true},
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
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    if (bt.isResource('bill:accebill:abemitbillsupply:makeup', full.applyCorpId) && full.status != 11) {
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="makeup(this)">' + bt.lang("common.button.makeup") + '</a>';
                    }
                    //html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.aid+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        /*fnDrawCallback: function () {
            idsValArray = new Array();
            setSumMoney();
        },*/
        btClickRowCallback: function (data) {
            opIdsValArray(data);
        },
        btDrawCallback: function (setting) {
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
                    colNum: '12',
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
    //导入初始化
    importExcel = function () {
        up = bt.btImport.init({
            code: "10086101",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            autoCloseWindown:false,
            success: function (head, data, select) {
                bt.loading.show();
                $.ajax({
                    type: "POST",
                    url: basePath + "/bill/accebill/abEmitBillSupply/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(select),
                    //重写success方法
                    success: function (result) {
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        if (result.success) {
                            up.closeWindown();
                            bt.datatable.refreshCurrentPage();
                        }
                    },
                    error:function(){
                        bt.loading.hide();
                    }
                });
            }
        });
    }

});

////根据是否协议出票加载票据承兑协议编号查询条件
function agreeBillFlgTypeCallback(data){
    if(data.value == '1'){
        $(".accptncagrmtnbClass").show();
    }else {
        $("#agreeBillFlg").val("");
        $(".accptncagrmtnbClass").hide();
    }
}

//补录
function makeup(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.aid;
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.makeup'), basePath + "/bill/accebill/abEmitBillSupply/makeup/" + aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abEmitBillSupply/makeup/" + aid,
        end: function () {
//        	billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}

function view(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.aid;
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abemitbill/view/" + aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbill/view/" + aid
    });
}

//批量补录完成
function makeupBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    for (var i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abemitbillsupply:makeup', rowData[i].applyCorpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchMakeup'), function () {
        var vids, vvids, aids;
        for (var i = 0; i < len; i++) {
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
        $.post(basePath + "/bill/accebill/abEmitBillSupply/batchMakeupandstorage", {
            "vids": vids,
            "vvids": vvids,
            "aids": aids
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

function attachmentView(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var isNotOp = data.status != 11 ? '0' : '1';
    var params = {
        "id": data.aid,
        "attachmentId": (data.attachmentId || null),
        "vvid": (data.vvid || null),
        "briefId": (data.briefId || null),
        "isNotOp": isNotOp
    };
    parent.layer.open({
        type: 2,
        area: ['820px', '400px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillattachment/view?" + $.param(params),
        end: function () {
            billTable.draw();
        }
    });
}

// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        for (i = 0; i < datas.length; i++) {
            if (datas[i].isSelect) {
                if (idsValArray.indexOf(datas[i].aid) < 0) {
                    idsValArray.push(datas[i].aid);
                }
            } else {
                var index = idsValArray.indexOf(datas[i].aid);
                if (index > -1) {
                    objArrayRemove(idsValArray, index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function () {
    var showHTML = bt.lang("sys.common.isSelectSize", [idsValArray.length]);
    if ($("#sumMoneyBillSpan").text() != '') {
        $("#sumMoneyBillSpan").text(showHTML);
    } else {
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span>');
    }
}

var objArrayRemove = function (obj, index) {
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