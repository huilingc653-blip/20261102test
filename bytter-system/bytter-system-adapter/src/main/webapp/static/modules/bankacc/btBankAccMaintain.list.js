var netIdCallback;
bt.use(['form', 'datatable', 'corp', 'selectType', 'dicts', 'bank'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.corp.init();
    var selectType = bt.selectType.init();
    //资金池标识 0，非资金池 1，资金池
    var poolSign = $("#poolSign").val();
    if(poolSign == "1") {
        // 重新设置资金池
        var corpPoolUrl = basePath + "/bankacc/btbankaccmaintain?getUserCorpPools";
        bt.selectType.get("netId").attr("data-url", corpPoolUrl);

        var poolId = bt.selectType.get("netId").val();
        if(poolId && poolId!="") {
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/bankacc/btbankaccmaintain?getUserCorpPoolChilds&poolId=" + poolId;
        }
        bt.selectType.get("ins_corpId").attr("data-url", corpPoolChildUrl);

        /**
         * 资金池回调
         * @param data
         */
        netIdCallback = function (data) {
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/bankacc/btbankaccmaintain?getUserCorpPoolChilds&poolId=" + data.id;
            bt.selectType.get("ins_corpId").attr("data-url", corpPoolChildUrl);
        }
    }

    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [1, 'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/btbankaccmaintain/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "bankAcc", bSortable: true},
            {data: "counterAcc", bSortable: true},
            {
                data: "superCurrentAcc", bSortable: true, className: 'center', visible: setSuperCurrentAccVisible(),
                render: function (data, type, full, meta) {

                    return full.superCurrentAcc;
                }
            },
            {data: "curName", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "regDate", bSortable: true},
            {data: "natureName", bSortable: true},
            {data: "attributeName", bSortable: true},
            {data:"accTypeName",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("btBankAcc.accType",full.accType);
                }
            },
            {data: "bankCode", bSortable: true},
            {data:"isOnlineStr",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("sf_yn",full.isOnline);
                }
            },
            {data:"validSignStr",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("sf_yn",full.validSign);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';

                    if (bt.isResource('netbank:bankAcc:btBankAccMaintain:apply', full.corpId) || bt.isResource('netbank:bankAcc:btBankAccMaintain:pool:apply', full.corpId)) {
                        if (data.counterAcc == "" || data.counterAcc == null) {
                            html = '<a class="" href="#" data-row="' + meta.row + '" onclick="showCounterAccList(this)">' + bt.lang("table.btBankAcc.relationAccount");
                        } else {
                            html = '<a class="" href="#" data-row="' + meta.row + '" onclick="delCoounterAcc(this)">' + bt.lang("table.btBankAcc.relationDel");
                        }
                        if(poolSign != "1") {
                            if (data.superCurrentAcc == "" || data.superCurrentAcc == null) {
                                html += '</br><a class="" href="#" data-row="' + meta.row + '" onclick="showSuperCounterAccList(this)">' + bt.lang("table.btBankAcc.relationSuperAccount") + '</a>';
                            } else {
                                html += '</br><a class="" href="#" data-row="' + meta.row + '" onclick="delSuperCoounterAcc(this)">' + bt.lang("table.btBankAcc.relationSuperDel") + '</a>';
                            }
                        }
                    }
                    return html;
                }
            },
        ]
    });
});

/**
 * 关联内部账号
 * @param el
 */
function showCounterAccList(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var poolSign = $("#poolSign").val();
    var url = basePath + "/bankacc/btbankaccmaintain/showCounterAccList?corpId=" + data.corpId + "&flag=0&id=" + data.id + "&curId=" + data.curId + "&poolSign=" + poolSign;
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), url);
            return;
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: url,
        end: function () {
            location.reload();
        }
    });
}

function showSuperCounterAccList(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var url = basePath + "/bankacc/btbankaccmaintain/showCounterAccList?corpId=" + data.corpId + "&flag=1&id=" + data.id + "&curId=" + data.curId
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), url);
            return;
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: url,
        end: function () {
            location.reload();
        }
    });
}

/**
 * 取消关联内部账号
 * @param el
 */
function delCoounterAcc(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.cancel'), function () {
        $.post(basePath + "/bankacc/btbankaccmaintain/toDel?curId=" + data.curId + "&flag=0&id=" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function delSuperCoounterAcc(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.cancel'), function () {
        $.post(basePath + "/bankacc/btbankaccmaintain/toDel?curId=" + data.curId + "&flag=1&id=" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function setSuperCurrentAccVisible(){
    var poolSign = $("#poolSign").val();
    if (poolSign == "1") {
        return false;
    } else {
        return true;
    }
}