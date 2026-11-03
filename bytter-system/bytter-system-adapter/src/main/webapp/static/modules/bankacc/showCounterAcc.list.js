bt.use(['form', 'datatable', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        mulselect: false,
        checkbox: true,
        order: [1, 'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/btbankaccmaintain/dataTableForCounterAcc?corpId=" + $("#corpId").val()+"&id="+$("#id").val()
        },
        columns: [
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
            {data: "blnctrAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "accOpenTime", bSortable: true}
        ],
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            },
            {
                text: bt.lang('common.button.submit'),
                action: function (e, dt, node, config) {
                    submit(e, dt, node, config);
                }
            }
        ]
    });

    /**
     * 提交
     * @param e
     * @param dt
     * @param node
     * @param config
     */
    submit = function (e, dt, node, config) {
        bt.loading.show();
        var data = dt.rows('.selected').data();
        var len = data.length;
        if (len != 1) {
            bt.loading.hide();
            layer.alert(bt.lang('config.fcs.relationAccount.selectOne'), {icon: 7});
            return false;
        }
        var blnctrAcc = "";
        var rowData = data.splice(0, len);
        var blnctrAcc = rowData[0].blnctrAcc;
        var data = {
            id: $("#id").val(),
            blnctrAcc: blnctrAcc,
            flag: $("#flag").val(),
            curId: $("#curId").val()
        };

        var result = false;
        if($("#flag").val() == "1"){
            $.ajax({
                url: basePath + "/bankacc/btbankaccmaintain?checkUnionnetOrUnionacc",
                method: 'POST',
                async: false,
                data: {blnctrAcc: blnctrAcc},
                dataType: 'json',
                success: function (data) {
                    result = data.obj;
                }
            });
        }

        if (result) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('inner.account.message.not.unionnet.or.unionacc', [blnctrAcc]), function (index) {
                bt.loading.show();
                submitForm(data);
            });
        } else {
            submitForm(data);
        }
        return false;
    }

    function submitForm(data) {
        bt.loading.hide();
        $.post(basePath + "/bankacc/btbankaccmaintain/toAdd", data, function (result) {
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.refreshParent();
                    bt.navtab.closeCurrent();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
    }
});
