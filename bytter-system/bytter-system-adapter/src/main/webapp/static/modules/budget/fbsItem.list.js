bt.use(['form', 'toLigerTree', 'selectType', 'dicts'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.dicts.init();
    bt.toLigerTree.init({
        elem: '#appTable',
        newElem: '#maingrid1',
        rownumbers: false,//序号列数，默为无， 1代表左边第一列为序号列
        isDoubleHeader: false,//是否有双表头
        allowHideColumn: false,
        resize:false,
        fixedLeftColumns: 2,
    });
    $("#search").click(function () {
        var action = basePath + "/budget/fbsitem/queryList";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    });
    $("#reset").click(function () {
        $("#itemCode").val("");
        $("#itemName").val("");
        return false;
    });

    /**
     * 添加子科目
     * @param id
     */
    add = function () {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/budget/fbsitem/add",
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
            content: basePath + "/budget/fbsitem/view/?parentId=" + id,
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
            content: basePath + "/budget/fbsitem/view/?id=" + id,
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
            $.post(basePath + "/budget/fbsitem/delete/" + id, function (result) {
                if(result.success){
                    location.reload();
                }
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    };

});
