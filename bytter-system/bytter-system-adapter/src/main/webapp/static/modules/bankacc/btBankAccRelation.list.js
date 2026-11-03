var corp;
var netIdCallback;
bt.use(['form',  'selectType', 'corp', 'treeTable'], function () {
    $ = bt.jquery, form = bt.form();
    // bt.treeTable.init({elem: $("#bankacc-table"), column: 0});
    corp = bt.corp.init();

    //资金池标识 0，非资金池 1，资金池
    var poolSign = $("#poolSign").val();
    if(poolSign == "1") {
        // 重新设置资金池
    //    var corpPoolUrl = basePath + "/bankacc/btbankaccrelation?getUserCorpPools";
     //   bt.selectType.get("netId").attr("data-url", corpPoolUrl);

        var poolId = $("#netIdSel").val();
        if(poolId && poolId!="") {
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/bankacc/btbankaccrelation?getUserCorpPoolChilds&poolId=" + poolId;
            $('[data-name="corpId"]').data('url',corpPoolChildUrl)
        }
       // bt.selectType.get("corpId").attr("data-url", corpPoolChildUrl);

        /**
         * 资金池回调
         * @param data
         */
        netIdCallback = function (data) {
            bt.selectType.get("corpId").attr("data-defaultValue", "");
            // 重新设置资金池的子单位
            var corpPoolChildUrl = basePath + "/bankacc/btbankaccrelation?getUserCorpPoolChilds&poolId=" + data.id;
            bt.selectType.get("corpId").attr("data-url", corpPoolChildUrl);
        }
    }

    var selectType = bt.selectType.init();

    $("#search").click(function () {
        var action = basePath + "/bankacc/btbankaccrelation/listBycorpId";
        var netId = "";
        var poolSign = $("#poolSign").val();
        if(poolSign == "1") {
            netId = selectType.get("netId").val();
        }else{
            netId = corp.get("netId").val();
        }
        if(netId != "") {
            $.post(action,$("#searchForm").serialize(),function(result){
                if(result.success){
                    var list = result.obj.treeList, html = ''
                    if(list && list.length){
                        for (var i = 0; i < list.length; i++) {
                            var tree = list[i]
                            html += '<tr data-tt-id="'+tree.id+'" class="ttt" data-tt-parent-id="'+tree.parentId+'">'
                            html +=  '<td width="50%">' + '(' + tree.code +')' + tree.name + '</td>'
                            html +=  '<td width="30%">' + '</td>'
                            html +=  '<td width="10%">' + '</td>'
                            html +=  '<td width="10%">' + '</td>'
                            html += '</tr>'
                            if(tree.accList && tree.accList.length) {
                                for (var j = 0; j < tree.accList.length; j++) {
                                    var acc = tree.accList[j]
                                    html += '<tr data-tt-id="' + tree.id + '_' + acc.id + '" class="ttt" data-tt-parent-id="'+tree.id+'">'
                                    html += '<td width="50%">' + '(' + acc.bankAcc + ')' + acc.accName + '</td>'
                                    html += '<td width="30%">' + acc.bankName + '</td>'
                                    html += '<td width="10%">' + acc.curName + '</td>'
                                    html += '<td width="10%">'
                                    //         <!-- 资金池银行账号父子关系维护 -->
                                    if ($("#poolSign").val() == "1") {
                                        if (tree.poolSign == "1") {
                                            html += '<a class="" href="#" onclick="updateForPool(\'' + acc.id + '\',\'' + acc.curId + '\',\'' + tree.id + '\')">' + bt.lang("common.button.addchild") + '</a>';
                                            html += '<span class="text-explode">|</span>'
                                        }
                                    } else {
                                        html += '<a href="#" data-row="" onclick="update(\'' + acc.id + '\',\'' + acc.curId + '\')">'+  bt.lang("common.button.addchild")+'</a>';
                                        html += '<span class="text-explode">|</span>'
                                    }
                                    html += '<a href="javascript:;" onclick="view(\'' + acc.id + '\',\'' + acc.curId + '\')">'+  bt.lang("common.button.view")+'</a>';
                                    html += '</td></tr>'
                                    if(acc.childList && acc.childList.length) {
                                        for (var k = 0; k < acc.childList.length; k++) {
                                            var childacc = acc.childList[k]
                                            //     #foreach($childacc in $acc.childList)
                                            html += '<tr data-tt-id="' + tree.id + '_' + acc.id + '_' + childacc.id + '" class="ttt" data-tt-parent-id="'+tree.id+ '_' + childacc.prentAccId +'">'

                                            html += '<td width="50%">' + '(' + childacc.bankAcc + ')' + childacc.accName + '</td>'
                                            html += '<td width="30%">' + childacc.bankName + '</td>'
                                            html += '<td width="10%">' + childacc.curName + '</td>'
                                            html += '<td width="10%">'
                                            if ($("#applyCodes").val() || $("#poolCodes").val()) {
                                                html += '<a href="#" onclick="deleteColumn(\'' + childacc.id + '\',\'' + acc.curId + '\')">'+  bt.lang("common.button.deletechild")+'</a>';
                                                html += '<span class="text-explode">|</span>';
                                                //         #end
                                            }
                                            html += '<a href="#" onclick="view(\'' + childacc.id + '\',\'' + acc.curId + '\')">'+  bt.lang("common.button.view")+'</a>';
                                            html += '</td></tr>'
                                        }
                                    }
                                }
                            }
                        }
                    }

                    $("#bankacc-table").removeData("treetable")
                    var frag = document.createDocumentFragment();
                    $(frag).append($("#bankacc-table"))
                    $(frag).find('#bankacc-table tbody').html(html)
                    bt.treeTable.init({elem: $(frag).find("#bankacc-table"), column: 0});
                    $("#tree-form").html(frag)


                    //$('#bankacc-table tbody').html(html)
                   // $("#bankacc-table").removeData("treetable")
                    // bt.treeTable.init({elem: $("#bankacc-table"), column: 0});
                }
            },'JSON');
            return false;
        }
    });
    bt.searchResetCallback = function (data, dom) {
        var poolSign = $("#poolSign").val();
        if(poolSign == "1") {
            selectType.get("netId").val("");
            selectType.get("corpId").val("");
        }else {
            corp.get("netId").val(null);
            corp.get("corpId").val(null);
        }
        selectType.get("curId").val("");
    };

    bt.send = function () {
        var action = basePath + "/bankacc/btbankaccrelation/listBycorpId";
        $("#searchForm").attr("action", action);
        $("#searchForm").submit();
    }
});

/**
 * 查看
 * @param el
 */
function view(id, curId) {
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccrelation/view/" + id + "/" + curId
    });
}

/**
 * 增加子账号
 * @param id
 * @param curId
 */
function update(id, curId) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/btbankaccrelation/update?id=" + id + "&curId=" + curId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccrelation/update?id=" + id + "&curId=" + curId,
        end: function () {
            location.reload();
        }
    });
}

/**
 * 增加子账号
 * @param id
 * @param curId
 */
function updateForPool(id, curId, poolId) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/btbankaccrelation/updateForPool?id=" + id + "&curId=" + curId+ "&poolId=" + poolId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccrelation/updateForPool?id=" + id + "&curId=" + curId+ "&poolId=" + poolId,
        end: function () {
            location.reload();
        }
    });
}

/**
 * 删除子账户
 * @param el
 */
function deleteColumn(id, curId) {
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bankacc/btbankaccrelation/delete/" + id + "/" + curId, function (result) {
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
