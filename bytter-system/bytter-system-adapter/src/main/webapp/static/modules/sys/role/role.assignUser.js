bt.use(['jquery', 'form', 'ztree'], function () {
    $ = bt.jquery, form = layui.form();
    var roleId = $('input[name="roleId"]').val();

    //当前是否显示下级单位的用户
    var displayChildUser = false;

    var preSelectUserList = [];
    var selectedUserList = [];
    //渲染准备选择用户
    var renderPreSelectUserList = function () {
        var html = '';
        if (preSelectUserList.length === 0) {
            //该网点、单位未分配用户
            html += '<div>'+bt.lang("sys.role.assign.unAssign")+'</div>';
            $("#pre-select-user").html(html);
            return;
        }
        for (var i = 0; i < preSelectUserList.length; i++) {
            var node = preSelectUserList[i];
            var checked = checkHasSelected(node);
            html += '<a href="javascript:void(0);" class="list-group-item ' + (checked ? "list-group-item-success" : "") + '" data-userid="' + node.id + '">';
            html += '<div class="select-user-name">(' + node.loginName + ')' + node.username + '</div>';
            html += '<div class="select-user-text">' + node.corpName + '</div>';
            if (checked) {
                html += '<span class="select-user-op-icon"><i class="fa fa-times" aria-hidden="true"></i></span>';
            } else {
                html += '<span class="select-user-op-icon"><i class="fa fa-plus" aria-hidden="true"></i></span>';
            }
            html += '</a>';
        }
        $("#pre-select-user").html(html);
    }

    //渲染被选中的用户
    var renderSelectUserList = function () {
        var html = '';
        for (var k in selectedUserList) {
            var node = selectedUserList[k];
            html += '<a href="javascript:void(0);" class="list-group-item" data-userid="' + node.id + '" data-select-userid="' + node.id + '">';
            html += '<div class="select-user-name">(' + node.loginName + ')' + node.username + '</div>';
            html += '<div class="select-user-text">' + node.corpName + '</div>';
            html += '<span class="select-user-op-icon"><i class="fa fa-times" aria-hidden="true"></i></span>';
            html += '</a>';
        }
        $("#selected-user").html(html);
    }

    //获取已选中的用户
    var getSelectedUser = function () {
        // $.get(basePath + "/sys/role/roleSelectedUser", {roleId: roleId}, function (nodes) {
        //     selectedUserList = nodes;
        //     renderSelectUserList();
        // }, "JSON");
        $.ajax({
            type: "GET",
            url: basePath + "/sys/role/roleSelectedUser",
            data: {roleId: roleId},
            dataType: "JSON",
            success: function (nodes) {
                selectedUserList = nodes;
                renderSelectUserList();
            },
            complete: function () {
                bt.loading.hide();
            }
        });
    };
    getSelectedUser();

    $("#selected-user").on('click', 'a', function () {
        var _that = $(this);
        var userId = _that.data("userid");
        removeByValue(selectedUserList, userId)
        renderPreSelectUserList();
        renderSelectUserList();
    });

    $("#pre-select-user").on('click', 'a', function () {
        var _that = $(this);
        var userId = _that.data("userid");
        if (_that.hasClass("list-group-item-success")) {
            removeByValue(selectedUserList, userId)
        } else {
            for (var k in preSelectUserList) {
                var user = preSelectUserList[k];
                if (user.id == userId) {
                    selectedUserList.push(user);
                    break;
                }
            }
        }
        renderPreSelectUserList();
        renderSelectUserList();
    });

    //备选用户全选
    $("#select-all").on('click', function () {
        if ($('.curSelectedNode').length < 1) {
            return;
        }
        // selectedUserList = [];
        for (var k in preSelectUserList) {
            var user = preSelectUserList[k];
            var selectUser = $("a[data-select-userid='" + user.id + "']");
            if(!(selectUser && selectUser.length)){
                selectedUserList.push(user);
            }
        }
        renderPreSelectUserList();
        renderSelectUserList();
    });

    function removeByValue(arr, val) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].id == val) {
                arr.splice(i, 1);
                break;
            }
        }
    }

    //已选用户全删
    $("#unselect-all").on('click', function () {
        selectedUserList = [];
        renderPreSelectUserList();
        renderSelectUserList();
    });

    function checkHasSelected(user) {
        for (var i = 0; i < selectedUserList.length; i++) {
            var sUser = selectedUserList[i];
            if (sUser.id === user.id) {
                return true;
            }
        }
        return false;
    }

    var nodeClick = function (event, treeId, treeNode) {
        var loading = parent.layer.load(1, {
            shade: [0.3, '#000'] //0.1透明度的白色背景
        });
        $('input[name="corpId"]').val(treeNode.id);
        $.ajax({
            url: basePath + "/sys/user/listByCorpId",
            async: false,
            dataType: "json",
            data: {corpId: treeNode.id},
            success: function (nodes) {
                setTimeout(function () {
                    parent.layer.closeAll('loading');
                }, 300)
                for (var k in nodes) {
                    nodes[k]['corpId'] = treeNode.id;
                    nodes[k]['corpName'] = treeNode.name;
                }
                preSelectUserList = nodes;
            }
        });
        if (displayChildUser) {
            var children = treeNode.children;
            if (children) {
                getPreUser(children, preSelectUserList);
            }
        }
        renderPreSelectUserList();
    }

    var setting = {
        data: {simpleData: {enable: true, idKey: "id", pIdKey: "pId"}},
        callback: {
            onClick: nodeClick
        }
    };
    var ztree;
    bt.loading.show();
    $.ajax({
        type: "GET",
        url: basePath + "/sys/corp/tree",
        dataType: "JSON",
        success: function (nodes) {
            for (var k in nodes) {
                nodes[k]['name'] = "(" + nodes[k]['code'] + ") " + nodes[k]['name'];
            }
            ztree = $.fn.zTree.init($("#corpTree"), setting, nodes);
            ztree.expandAll(true);
            setTimeout(function () {
                bt.loading.hide();
            }, 500);
        },
        error: function () {
            setTimeout(function () {
                bt.loading.hide();
            }, 500);
        }
    });

    //显示下级单位用户，这里用到递归去多次请求节点的用户。
    $('#display-child').on('click', function () {
        if ($('.curSelectedNode').length < 1) {
            layer.alert(bt.lang("common.select.corpNet"), {icon: 2});
            return;
        }
        var loading = parent.layer.load(1, {
            shade: [0.3, '#000'] //0.3透明度的白色背景
        });
        setTimeout(function () {
            parent.layer.close(loading);
        }, 800);
        var nodes = ztree.getSelectedNodes();
        if (displayChildUser) {
            displayChildUser = false;
            nodeClick(event, nodes[0].tId, nodes[0]);
            $('#display-child').text(bt.lang('sys.role.assignUser.notDisplayChildrenCorpUser'));
        } else {
            if (nodes.length > 0) {
                var children = nodes[0].children;
                if (children) {
                    getPreUser(children, preSelectUserList);
                    renderPreSelectUserList();
                }
            }
            displayChildUser = true;
            $('#display-child').text(bt.lang('sys.role.assignUser.displayChildrenCorpUser'));
        }
    });


    //递归获取网点用户。
    function getPreUser(node, preUser) {
        //转换成数组对象,当node.length=1时，
        var nodeArray = [];
        node instanceof Array ? nodeArray = Array.from(node) : nodeArray.push(node);
        for (var i = 0; i < nodeArray.length; i++) {
            var tmpNode = ztree.getNodeByTId(nodeArray[i].tId);
            $.ajax({
                type: "GET",
                url: basePath + "/sys/user/listByCorpId",
                async: false, //这里要把ajax请求设置成同步
                dataType: 'json',
                data: {corpId: tmpNode.id},
                success: function (nodes) {
                    //赋值网点名称到页面显示
                    if (nodes instanceof Array) {
                        if (nodes.length > 0) {
                            for (var k in nodes) {
                                nodes[k]['corpId'] = tmpNode.id;
                                nodes[k]['corpName'] = tmpNode.name;
                                preUser.push(nodes[k]);
                            }
                        }
                    }
                }
            });
            if (tmpNode.isParent) {
                var children = tmpNode.children;
                //子节点获取角色
                getPreUser(children, preUser);
            }
        }
    }


    $("#roleSumit").click(function () {
        var temp = [];
        for (var k in selectedUserList) {
            var user = selectedUserList[k];
            var obj = {
                roleId: roleId,
                corpId: user.corpId,
                userId: user.id
            }
            temp.push(obj);
        }
        var tempData = {
            data: temp,
            roleId: roleId
        }
        if (temp.length == 0) {
            /*parent.layer.alert('分配用户不能为空',{icon:2});*/
            parent.layer.confirm('分配用户为空，确定继续吗？', {icon: 3}, function () {
                bt.loading.show();
                $.ajax({
                    type: "POST",
                    url: basePath + "/sys/role/assignUser",
                    data: JSON.stringify(tempData),//将对象序列化成JSON字符串
                    dataType: "json",
                    contentType: 'application/json;charset=utf-8', //设置请求头信息
                    success: function (result) {
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    },
                    error: function () {
                        bt.loading.hide();
                    }
                });
            });
            return;
        }
        bt.loading.show();
        $.ajax({
            type: "POST",
            url: basePath + "/sys/role/assignUser",
            data: JSON.stringify(tempData),//将对象序列化成JSON字符串
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            },
            error: function () {
                bt.loading.hide();
            }
        });
    });

});
