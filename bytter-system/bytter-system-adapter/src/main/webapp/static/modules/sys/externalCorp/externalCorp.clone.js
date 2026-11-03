bt.use(['form', 'datatable', 'moneyType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/sys/corp/datatable"
        },
        order: [[1,'asc']],
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
                    cloneExternalCorp(ids);
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
    
    function cloneExternalCorp(corpIds) {
        var externalIds = $("#externalCorps").val();
        parent.layer.confirm(bt.lang("sys.externalCorp.cloneExternal.confirm"),function(){
            $.post(basePath + "/sys/externalCorp/cloneExternals", {corpIds:corpIds,externalIds:externalIds}, function(result){
                if(result.success){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                    if (bt.navtab) {
                        bt.navtab.refreshCurrent();
                        bt.navtab.refreshParent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            },'JSON');
        });
    }
});