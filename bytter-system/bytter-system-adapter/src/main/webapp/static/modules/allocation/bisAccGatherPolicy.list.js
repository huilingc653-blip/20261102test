bt.use(['form', 'treeTable'], function () {
    $ = bt.jquery, form = bt.form();
    var tableContainer = $("#accTable");
    if(tableContainer.length){
        var frag = document.createDocumentFragment();
        $(frag).append(tableContainer)
        bt.treeTable.init({elem: $(frag).find("#accTable"), column: 0});
        $("#treeDiv").html(frag)
    }

    $("#search").click(function () {
        var action = basePath + "/allocation/bisaccgatherpolicy/queryList";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    });


    /**
     * 增加父子账号
     * @param gatherPolicyId
     * @param curId
     */
    addParentAcc = function (gatherPolicyId, curId) {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/allocation/bisaccgatherpolicy/listParentBankAcc/" + gatherPolicyId + "/" + curId,
            end: function () {
                location.reload();
            }
        });
    }

    /**
     * 增加子账号
     * @param gatherPolicyId
     * @param prentAccId
     * @param curId
     */
    addBankAcc = function (gatherPolicyId, prentAccId, curId) {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/allocation/bisaccgatherpolicy/listBankAcc/" + gatherPolicyId + "/" + prentAccId + "/" + curId,
            end: function () {
                location.reload();
            }
        });
    }

    /**
     * 删除子账号
     * @param bankAccId
     * @param prentAccId
     * @param gatherPolicyId
     */
    delBankAcc = function (bankAccId, prentAccId, gatherPolicyId) {
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            parent.bt.loading.show();
            $.post(basePath + "/allocation/bisaccgatherpolicy/delete", {
                bankAccId: bankAccId,
                prentAccId: prentAccId,
                gatherPolicyId: gatherPolicyId
            }, function (result) {
                location.reload();
                parent.bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }

    /**
     * 查看调拨策略详情
     * @param id
     */
    viewGatherPolicy = function (id) {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/allocation/bisgatherpolicy/view/" + id
        });
    }

    /**
     * 查看银行账号详情
     * @param id
     */
    viewBankAcc = function (id) {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/btbankacchandle/view/" + id
        });
    }


});


/**
 * 重置按钮
 */
function resetButton(){
    $("#gatherType").find("option:selected").removeAttr('selected');
    $("#gatherPolicyCode").attr("value","");
    $("#parentAccName").attr("value","");
}
