var netIdCallback;
bt.use(['form', 'datatable', 'corp', 'selectType', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();

    //资金池标识 0，非资金池 1，资金池
    var poolSign = $("#poolSign").val();
    if(poolSign == "1") {
        // 重新设置资金池
        var corpPoolUrl = basePath + "/inneraccount/fcacccancel?getUserCorpPools";
        bt.selectType.get("eqs_netId").attr("data-url", corpPoolUrl);

        var poolId = bt.selectType.get("eqs_netId").val();
        if(poolId && poolId!="") {
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/inneraccount/fcacccancel?getUserCorpPoolChilds&poolId=" + poolId;
        }
        bt.selectType.get("ins_corpId").attr("data-url", corpPoolChildUrl);

        /**
         * 资金池回调
         * @param data
         */
        netIdCallback = function (data) {
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/inneraccount/fcacccancel?getUserCorpPoolChilds&poolId=" + data.id;
            bt.selectType.get("ins_corpId").attr("data-url", corpPoolChildUrl);
        }
    }

    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcacccancel/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "blnctrAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "netName", bSortable: true,
                render: function (data, type, full, meta) {
                    if(full.poolSign=="1"){
                        return full.netPoolName;
                    }else{
                        return full.netName;
                    }
                }
            },
            {data: "corpName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "subjectName", bSortable: true},
            {data: "accTypeName", bSortable: true},
            {data: "balanceWayName", bSortable: true},
            {data: "accStateName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:innerAccount:fcAcc:cancelApply', full.corpId) || bt.isResource('netbank:innerAccount:fcAcc:pool:cancelApply', full.corpId)) {
                        if (data.accState != 'N') {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="cancel(this)">' + bt.lang("common.button.cancel") + '</a><span class="text-explode">|</span>';
                        }
                        if (data.accState == 'U' || data.accState == 'Z') {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="cancelEdit(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        } else {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="cancelFcAcc(this)">' + bt.lang("common.button.cancellation") + '</a><span class="text-explode">|</span>';
                        }
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

/**
 * 查看
 * @param el
 */
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcacc/view/" + data.id + "/v"
    });
}

function cancelFcAcc(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        //验证是否可以注销
        var obj = new Object();
        if (validFcAcc(data.id, obj)) {
            if (obj.fl) { //加载记账分录
                bt.navtab.addNavtab(bt.lang('common.button.cancellation'), basePath + "/inneraccount/fcacc/view/" + data.id + "/cancelFL");
            } else {
                bt.navtab.addNavtab(bt.lang('common.button.cancellation'), basePath + "/inneraccount/fcacc/view/" + data.id + "/cancel");
            }
            return;
        }
    } else {
        //验证是否可以注销
        if (validFcAcc(data.id)) {
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: basePath + "/inneraccount/fcacccancel/update?id=" + data.id,
                end: function () {
                    location.reload();
                }
            });
        }
    }
}

function cancelEdit(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        //验证是否可以注销
        var obj = new Object();
        if (validFcAcc(data.id, obj)) {
            if (obj.fl) {//加载记账分录
                bt.navtab.addNavtab(bt.lang('common.button.cancellation'), basePath + "/inneraccount/fcacc/view/" + data.id + "/cancelEditFL");
            } else {
                bt.navtab.addNavtab(bt.lang('common.button.cancellation'), basePath + "/inneraccount/fcacc/view/" + data.id + "/cancelEdit");
            }
            return;
        }
    }
}

/**
 * 验证是否可以注销
 * @param id
 */
function validFcAcc(id, obj) {
    var param = {
        id: id,
        date: (new Date()).getTime()
    };
    var flag = false;
    $.ajax({
        url: basePath + "/inneraccount/fcacccancel/validFcAcc",
        method: 'GET',
        data: param,
        async: false,
        dataType: 'json',
        success: function (data) {
            if (data) {
                flag = Boolean(data.success);
                if (data.attributes != null) {
                    var isProcess = data.attributes.isProcess;
                    if (isProcess && isProcess == "1") {
                        obj.fl = isProcess;
                    }
                }
                if (!flag) {
                    parent.layer.alert( bt.lang(data.msg , data.params),{icon: data.success ? 1 : 2});
                }
            }
        }
    });
    return flag;

}

function cancel(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.cancel'), function () {
        $.post(basePath + "/inneraccount/fcacccancel/cancel/" + data.id, function (result) {
            // location.reload();
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}