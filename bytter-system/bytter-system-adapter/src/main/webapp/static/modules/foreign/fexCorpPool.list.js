var corp;
var poolIdCallback;
bt.use(['form', 'datatable', 'selectType', 'corp', 'treeTable', 'treeGrid'], function () {
    $ = bt.jquery, form = bt.form();
    var tableContainer = $("#bankacc-table");
    if(tableContainer.length){
        var frag = document.createDocumentFragment();
        $(frag).append(tableContainer)
        bt.treeTable.init({elem: $(frag).find("#bankacc-table"), column: 0});
        $("#tree-form").html(frag)
    }
    corp = bt.corp.init();

    var selectType = bt.selectType.init();

    /**
     * 资金池回调
     * @param data
     */
    poolIdCallback = function (data) {
        bt.selectType.get("corpId").attr("data-defaultValue", "");
        // 重新设置资金池的子单位
        var corpPoolChildUrl = basePath + "/foreign/fexcorppool/getUserCorpPoolChilds?poolId=" + data.id;
        bt.selectType.get("corpId").attr("data-url", corpPoolChildUrl);
    }

    $("#search").click(function () {
        var action = basePath + "/foreign/fexcorppool/listBycorpId";
        var poolId = selectType.get("poolId").val();
        var curId = selectType.get("curId");
        if(poolId != "") {
            $("#searchForm").attr("action", action);
            $("#searchForm").submit();
        }
    });
    bt.searchResetCallback = function (data, dom) {
        selectType.get("poolId").val("");
        selectType.get("corpId").val("");
        selectType.get("curId").val("");
    };

    bt.send = function () {
        var action = basePath + "/foreign/fexcorppool/listBycorpId";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    }

});

/**
 * 新增外汇资金池主体
 */
function addPool() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexcorppool/addPool");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorppool/addPool",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

/**
 * 修改外汇资金池主体
 * @param id
 * @param curId
 */
function updatePool(id) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexcorppool/updatePool?id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorppool/updatePool?id=" + id,
        end: function () {
            location.reload();
        }
    });
}

/**
 * 查看外汇资金池主体
 * @param el
 */
function viewPool(id) {
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorppool/viewPool/" + id
    });
}

/**
 * 新增主账号
 */
function addPoolAcc(id) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexcorppool/addPoolAcc?id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorppool/addPoolAcc?id=" + id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

/**
 * 修改主账号
 */
function updatePoolAcc(id) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexcorppool/updatePoolAcc?id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorppool/updatePoolAcc?id=" + id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

/**
 * 查看主账号信息
 * @param el
 */
function viewPoolAcc(id) {
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorppool/viewPoolAcc/" + id
    });
}

/**
 * 新增成员账号
 */
function addPoolAccChild(id) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexcorppool/addPoolAccChild?id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorppool/addPoolAccChild?id=" + id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

/**
 * 查看成员账号
 */
function viewPoolChildAcc(id,childId) {
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorppool/viewPoolChildAcc/" + id+"/" + childId
    });
}

/**
 *删除子账户
 * @param id
 * @param poolSign 资金池主体标识 1是0否
 * @param parentSign 主账号标识 1是0否
 */
function del(id, poolSign, parentSign) {
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/foreign/fexcorppool/delete/" + id + "/" + poolSign + "/" + parentSign, function (result) {
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
