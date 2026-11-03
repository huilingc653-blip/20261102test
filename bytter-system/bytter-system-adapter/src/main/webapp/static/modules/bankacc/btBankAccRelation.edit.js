bt.use(['form', 'layedit', 'selectType', 'datatable', 'corp'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();

    //资金池标识 0，非资金池 1，资金池
    var poolSign = $("#poolSign").val();
    var poolId = $("#poolId").val();
    if(poolSign == "1") {
        // 重新设置资金池的子单位
        var corpPoolChildUrl = basePath + "/bankacc/btbankaccrelation?getUserCorpPoolChilds&poolId=" + poolId;
        bt.selectType.get("eqs_corpId").attr("data-url", corpPoolChildUrl);
    }

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
            'url': basePath + "/bankacc/btbankaccrelation/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            },
            {
                text: bt.lang('common.button.submit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if (len < 1) {
                        layer.msg(bt.lang('common.button.selectnum'));
                        return;
                    }
                    var ids = "";
                    var rowData = data.splice(0, len);
                    for (var i = 0; i < rowData.length; i++) {
                        if (ids == "") {
                            ids += rowData[i].id;
                        } else {
                            ids += "," + rowData[i].id;
                        }
                    }
                    var prentAccId = $("#id").val();
                    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
                        $.post(basePath + "/bankacc/btbankaccrelation/toAdd", {
                            ids: ids,
                            prentAccId: prentAccId,
                            curId: $("#curId").val(),
                            poolSign: $("#poolSign").val()
                        }, function (result) {
                            if (result.success) {
                                if (bt.navtab) {
                                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                                    bt.navtab.getParentDom().$("#search").trigger('click');
                                    bt.navtab.closeCurrent();
                                }
                            }
                        }, 'JSON');
                    });
                }
            }
        ]
        , columns: [
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: false},
            {data: "bankName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "attributeName", bSortable: true},
            {data: "bifName", bSortable: true},
            {data: "natureName", bSortable: true}
        ]
    });
});
