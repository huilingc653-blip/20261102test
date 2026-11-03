var billTable = {};
bt.use(['form', 'datatable', 'btdate', 'select', 'dateType', 'conType', 'dicts', 'selectType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.conType.init();
    bt.selectType.init();

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'asc'],[7, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/contract/signed/datatable"
        },
        buttons: [
            {
                text: bt.lang('contract.signed.batch'),
                hide: bt.isnotResource('contract:signed:manage:opt'),
                action: function (e, dt, node, config) {
                    batchSigned(dt.rows('.selected').data());
                }
            },
            {
                text: bt.lang('contract.refused.batch'),
                hide: bt.isnotResource('contract:signed:manage:opt'),
                action: function (e, dt, node, config) {
                    batchRefused(dt.rows('.selected').data());
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },
            {data: "conName", bSortable: true},
            {data: "typeName", bSortable: true},
            {data: "conNo", bSortable: true},
            {
                data: "signTypeName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("CON_SIGN_TYPE", full.signType);
                }
            },
            {data: "draftDate", bSortable: true},
            {data: "beginDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {
                data: "statusName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("CON_SIGN_STATUS", full.status);
                }
            },
            {data: "backMsg", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    if (data.status == 0 || data.status == -1) {//审批打回、待签署
//                           if(bt.isResource('con:op',full.corpId)){
//						   if(data.status==0||data.status==-1){//审批打回、待签署
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="signed(this)">' + bt.lang("contract.signed") + '</a>';
//						   }
//						   if(bt.isResource('con:op',full.corpId)){
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="refused(this)">' + bt.lang("contract.refused") + '</a>';
//						   }
                    }
                    return html;
                }
            }
        ],
        fixedColumns: {
            rightColumns: 2
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
});

//同意签署跳转
function signed(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var rid = data.rid;
    parent.layer.confirm(bt.lang('contract.message.confirm.signed'), function () {
        $.post(basePath + "/contract/signed/sigOpt", {"id": rid, "opt": "signed"}, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

//拒绝数据
function refused(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '249px'],
        maxmin: true,
        content: basePath + "/contract/signed/retreatPage/" + data.rid,
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });

}

//查看详细
function view(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.rid;

    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'), basePath + "/contract/conCommon/conView?recordId=" + aid);
        return;
    }

    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/contract/conCommon/conView?recordId=" + aid
    });

}


//拒绝签收
function batchRefused(data) {
    var len = data.length;
    if (!data.length) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    if ($("input[name='reason']").val() == "") {
        layer.alert(bt.lang('contract.reason.notnull'), {icon: 7});
        return;
    }
    //校验数据权限
    var rowData = data.splice(0, data.length);
    parent.layer.confirm(bt.lang('contract.message.confirm.refused'), function () {
        var ids;
        for (i = 0; i < len; i++) {
            if (ids) {
                ids = ids + "," + rowData[i].rid;
            } else {
                ids = rowData[i].rid;
            }
        }
        $.post(basePath + "/contract/signed/batchOpt", {
            "ids": ids,
            "opt": "refused",
            "reason": $("input[name='reason']").val()
        }, function (result) {
            if (result.success) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                bt.datatable.refreshCurrentPage();
            } else {
                parent.layer.alert(bt.lang("contract.method.error"), {icon: result.success ? 1 : 2});
            }
        }, 'JSON');
    });
}


//同意签署
function batchSigned(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
//校验数据权限
    var rowData = data.splice(0, len);

    parent.layer.confirm(bt.lang('contract.message.confirm.signed'), function () {
        var ids;
        for (i = 0; i < len; i++) {
            if (ids) {
                ids = ids + "," + rowData[i].rid;
            } else {
                ids = rowData[i].rid;
            }
        }
        $.post(basePath + "/contract/signed/batchOpt", {"ids": ids, "opt": "signed"}, function (result) {
            if (result.success) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                bt.datatable.refreshCurrentPage();
            } else {
                parent.layer.alert(bt.lang("contract.method.error"), {icon: result.success ? 1 : 2});
            }
        }, 'JSON');
    });
}
