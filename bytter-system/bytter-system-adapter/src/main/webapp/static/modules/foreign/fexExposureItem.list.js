bt.use(['form', 'selectType', 'dicts','treeTable'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.dicts.init();
    if($('#appTable').find('tbody tr').length){
        var tableContainer = $("#appTable");
        if(tableContainer.length){
            var frag = document.createDocumentFragment();
            $(frag).append(tableContainer)
            bt.treeTable.init({elem: $(frag).find("#appTable"), column: 0});
            $("#tree-box").html(frag)
        }
    }else{
        var html = '<tr><td colspan="'+$('#appTable').find('tr th').length+'" class="empty-table-td" style="height: 400px;"><img src="'+ basePath +'/static/v3.2/images/pic_blank.png" class="pic-bank"><div class="empty-text">'+bt.lang("common.msg.temporarily_no_data")+'</div></td></tr>>'
        $('#appTable').find('tbody').html(html)
        $('#appTable').parent().show()
        $('#maingrid1').parent().hide()
    }

    $("#search").click(function () {
        var action = basePath + "/foreign/fexExposureItem/queryList";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    });
    resetSearch = function () {
        $("#itemCode").val("");
        $("#itemName").val("");
        bt.dicts.get('showItem').val("0");
        return false;
    };

    /**
     * 添加子科目
     * @param id
     */
    add = function () {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexExposureItem/add",
            end: function () {
            }
        });
    };

    /**
     * 添加子科目
     * @param id
     */
    addChild = function (id) {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexExposureItem/update/?parentId=" + id,
            end: function () {
            }
        });
    };

    /**
     * 编辑
     * @param id
     */
    update = function (id) {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexExposureItem/update/?id=" + id,
            end: function () {
            }
        });
    };

    /**
     * 删除
     * @param id
     */
    del = function (id) {
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/foreign/fexExposureItem/delete/" + id, function (result) {
                if(result.success){
                    location.reload();
                }
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    };
});
