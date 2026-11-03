var netIdCallback;
bt.use(['form', 'datatable', 'corp', 'selectType', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();

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
        checkbox: false,
        order: [[2, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcaccchange/checkDataTable"
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
                    if (bt.isResource('netbank:innerAccount:fcAcc:changeApply', full.corpId) || bt.isResource('netbank:innerAccount:fcAcc:pool:changeApply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="check(this)">' + bt.lang("common.button.check") + '</a><span class="text-explode">|</span>';
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
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcaccchange/view/" + data.id + "/" + data.accState + "/v"
    });
}

function check(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.check.data'), basePath + "/inneraccount/fcaccchange/view/" + data.id + "/" + data.accState + "/check");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcaccchange/view/" + data.id + "/" + data.accState + "/check",
        end: function () {
            location.reload();
        }
    });
}