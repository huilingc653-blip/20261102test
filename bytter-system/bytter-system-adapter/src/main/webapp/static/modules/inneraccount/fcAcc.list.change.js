var netIdCallback;
var moneyFun;
bt.use(['form', 'datatable', 'corp', 'selectType', 'bank', 'dicts', 'moneyType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    bt.moneyType.init();//金额
    moneyFun = bt.moneyType.getFun();

    //资金池标识 0，非资金池 1，资金池
    var poolSign = $("#poolSign").val();
    if(poolSign == "1") {
        // 重新设置资金池
        var corpPoolUrl = basePath + "/inneraccount/fcaccchange?getUserCorpPools";
        bt.selectType.get("eqs_netId").attr("data-url", corpPoolUrl);

        var poolId = bt.selectType.get("eqs_netId").val();
        if(poolId && poolId!="") {
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/inneraccount/fcaccchange?getUserCorpPoolChilds&poolId=" + poolId;
        }
        bt.selectType.get("ins_corpId").attr("data-url", corpPoolChildUrl);

        /**
         * 资金池回调
         * @param data
         */
        netIdCallback = function (data) {
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/inneraccount/fcaccchange?getUserCorpPoolChilds&poolId=" + data.id;
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
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcaccchange/datatable"
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
            {data: "netName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "subjectName", bSortable: true},
            {data: "accTypeName", bSortable: true},
            {data: "balanceWayName", bSortable: true},
            {data: "accStateName", bSortable: true},
            {data: "overdraftSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fcAcc.overdraftSign", full.overdraftSign);
                }
            },
            {data:"overdraftMoney", bSortable:true,className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(full.overdraftMoney);
                }},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:innerAccount:fcAcc:changeApply', full.corpId) || bt.isResource('netbank:innerAccount:fcAcc:pool:changeApply', full.corpId)) {
                        if (data.accState != 'N') {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="cancel(this)">' + bt.lang("common.button.cancel") + '</a><span class="text-explode">|</span>';
                        }
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="changeFcAcc(this)">' + bt.lang("common.button.change.fcacc") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/inneraccount/fcaccchange/view/" + data.id + "/" + data.accState + "/v");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcaccchange/view/" + data.id + "/" + data.accState + "/v"
    });
}

function changeFcAcc(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/inneraccount/fcaccchange/view/" + data.id + "/" + data.accState + "/change");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/inneraccount/fcaccchange/view/" + data.id + "/" + data.accState + "/change",
            end: function () {
                location.reload();
            }
        });
    }
}

function cancel(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.cancel'), function () {
        $.post(basePath + "/inneraccount/fcaccchange/cancel/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}