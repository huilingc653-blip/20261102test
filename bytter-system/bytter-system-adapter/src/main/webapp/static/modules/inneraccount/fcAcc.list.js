var netIdCallback;
bt.use(['form', 'datatable', 'corp', 'selectType', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.selectType.init();

    //资金池标识 0，非资金池 1，资金池
    var poolSign = $("#poolSign").val();
    if(poolSign == "1") {
        // 重新设置资金池
        var corpPoolUrl = basePath + "/inneraccount/fcacc?getUserCorpPools";
        bt.selectType.get("eqs_netId").attr("data-url", corpPoolUrl);

        var poolId = bt.selectType.get("eqs_netId").val();
        if(poolId && poolId!="") {
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/inneraccount/fcacc?getUserCorpPoolChilds&poolId=" + poolId;
        }
        bt.selectType.get("ins_corpId").attr("data-url", corpPoolChildUrl);

        /**
         * 资金池回调
         * @param data
         */
        netIdCallback = function (data) {
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/inneraccount/fcacc?getUserCorpPoolChilds&poolId=" + data.id;
            bt.selectType.get("ins_corpId").attr("data-url", corpPoolChildUrl);
        }
    }

    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    fcAccTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcacc/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:innerAccount:fcAcc:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    //资金池标识 0，非资金池 1，资金池
                    var poolSign = $("#poolSign").val();
                    if(poolSign == "1") {
                        addPool();
                    }else{
                        add();
                    }
                }
            }, {
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
                    if (bt.isResource('netbank:innerAccount:fcAcc:apply', full.corpId) || bt.isResource('netbank:innerAccount:fcAcc:pool:apply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;

                }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/inneraccount/fcacc/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcacc/add",
        end: function () {
            location.reload();
        }
    });
}

function addPool() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/inneraccount/fcacc/addPool");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcacc/addPool",
        end: function () {
            location.reload();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/inneraccount/fcacc/view/" + data.id + "/v");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcacc/view/" + data.id + "/v"
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/inneraccount/fcacc/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/inneraccount/fcacc/update?id=" + data.id,
            end: function () {
                location.reload();
            }
        });
    }
}


function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/inneraccount/fcacc/delete/" + data.id, function (result) {
            // location.reload();
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}