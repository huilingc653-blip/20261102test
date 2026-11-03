var corps,defaultSelectedIds;
bt.use(['form', 'datatable','corp'], function () {
    $ = bt.jquery, form = bt.form();
    corps = bt.corp.init();
    var userIds = $("input[name='userIds']").val();
    defaultSelectedIds = userIds.split(",") || [];
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        query: "search",
        checkbox: true,
        jumpPageSelect: true,//开启跨页选择
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/sys/user/cashierDatatable"
        },
        ignoreButtons:['colvis','output','print'],
        buttons: [
            {
                text: bt.lang('选择'),
                action: function (e, dt, node, config) {
                    batchSelect(e, dt, node, config);
                }
            }
        ],
        //order: [[1, 'asc']],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "loginName", bSortable: false, className: "center"},
            {data: "username", bSortable: false}
        ],
        btDrawCallback:function(setting){
            if(defaultSelectedIds.length){
                bt.datatable.onCheckRow("appTable", {
                    "key": "id",
                    "value": defaultSelectedIds,
                    "checked": true
                });
            }
        },
        btClickRowCallback:function (data) {
            if(data){
                if(data[0].isSelect){
                    if(defaultSelectedIds.indexOf(data[0].id) == -1){
                        defaultSelectedIds.push(data[0].id)
                    }
                }else{
                    if(defaultSelectedIds.indexOf(data[0].id) > -1){
                        defaultSelectedIds.splice(defaultSelectedIds.indexOf(data[0].id),1);
                    }
                }
            }
        }
    });

    function batchSelect(e, dt, node, confi) {
        var corpId = corps.get("cashierCorpId").val();
        if (isEmpty(corpId)){
            layer.alert(bt.lang('请选择单位'), {icon: 7});
            return;
        }
        if (!defaultSelectedIds.length) {
            layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
            return;
        }
        var selectedIds = "";
        for (var i = 0; i < defaultSelectedIds.length; i++) {
            if(selectedIds){
                selectedIds += "," + defaultSelectedIds[i]
            }else{
                selectedIds += defaultSelectedIds[i]
            }
        }
        var id = $("#id").val();
        $.post(basePath + "/netbank/corpCashier/save", {corpId: corpId,userIds: selectedIds,id: id}, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParentTable();
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
    }


    function isEmpty(stringData) {
        if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
            return true;
        } else {
            return false;
        }
    }


});
