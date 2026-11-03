bt.use(['form', 'datatable', 'moneyType','btImport'], function () {
    $ = bt.jquery, form = bt.form();

    importExcel = function (corpIds) {
        up = bt.btImport.init({
            code: "40000002",
            width: "100%",
            height: "100%",
            headRows: 1,
            titleRows: 0,
            success: function (head,data,select) {
                $.ajax({
                    type:"POST",
                    url: basePath + "/sys/externalCorp/saveImport?corpId="+corpIds,
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(up.getSelectData()),
                    success:function(result){
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                        if(!bt.navtab){
                            parent.layer?parent.layer.closeAll():layer.closeAll();
                        }else{
                            bt.navtab.closeCurrent();
                        }
                    }
                });
            }
        });
    }

    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        order: [[1,'asc']],
        ajax: {
            'url': basePath + "/sys/corp/datatable"
        },
        checkbox: true,
        buttons: [
            {
                text: bt.lang("common.button.confirm"),
                action: function (e, dt, node, config) {
                    //获取选中行数据
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if(!len){
                        layer.msg(bt.lang('common.message.confirm.selectData'));
                        return;
                    }
                    var rowData = data.splice(0, len);
                    var ids = [];
                    for (var i = 0; i < rowData.length; i++) {
                        ids.push(rowData[i].id);
                    }
                    importExcel(ids);
                }
            }
        ],
        columns: [
            {data: "code", bSortable: true},
            {
                data: "name", bSortable: true,
                render: function (data, type, full, meta) {
                    return "(" + full.code + ")" + full.name;
                }
            }
        ]
    });
});