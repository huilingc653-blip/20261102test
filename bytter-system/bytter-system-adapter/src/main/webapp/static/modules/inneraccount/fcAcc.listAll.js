var netIdCallback;
bt.use(['form', 'datatable', 'corp', 'selectType', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
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
            'url': basePath + "/inneraccount/fcacc/dataTableAll"
        },
        buttons: []
        , columns: [
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
            {data: "accTypeName", bSortable: true},
            {data: "accStateName", bSortable: true},
            {data: "accOpenTime", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/inneraccount/fcacc/view/" + data.id + "/ALL");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/inneraccount/fcacc/view/" + data.id + "/v"
        });
    }
}
