bt.use(['form', 'jquery', 'ztree', 'ztreeCheck', 'jqColor'], function () {
    var form = bt.form();
    var $ = bt.jquery, layer = parent ? parent.layer : layer;
    var setting = {
        view: {showIcon: false, dblClickExpand: false, fontCss: getFont, nameIsHTML: true},
        check: {enable: true, nocheck: false, chkStyle: "checkbox", chkboxType: {"Y": "ps", "N": "ps"}},
        data: {simpleData: {enable: true, idKey: "id", pIdKey: "parentId"}}
    };

    function addTreeContainer() {
        var treeHtml = '';
        for (var i = 0; i < menus.length; i++) {
            var menu = menus[i];
            treeHtml += '<li class="box"><ul id="menuTree_' + menu.id + '" class="ztree resources-tree"></ul></li>';
        }
        // $(".treeContainer").html(treeHtml).css("width", 325 * menus.length);
        $(".treeContainer").html(treeHtml).css("width", 365 * menus.length);
    }

    function addTreePositionButton() {
        var treeHtml = '';
        for (var i = 0; i < menus.length; i++) {
            var menu = menus[i];
            treeHtml += '<div id="#menuTree_' + menu.id + '" class="scroll-btn layui-btn layui-btn-primary layui-btn-small">' + menu.name + '</div>';
        }
        $(".treePositionButton").html(treeHtml);
    }

    function getAllNodes() {
        var nodesArr = [];
        for (var i = 0; i < menus.length; i++) {
            var menu = menus[i];
            var zTree = $.fn.zTree.getZTreeObj("menuTree_" + menu.id);
            if (zTree) {
                var nodes = zTree.getCheckedNodes(true);
                for (var j = 0; j < nodes.length; j++) {
                    var node = nodes[j];
                    nodesArr.push(node);
                }
            }
        }
        return nodesArr;
    }

    var webSkin = bt.getSysSkin()
    var targetLiBackgroundColor = ''
    if(webSkin === 'dark-skin'){
        targetLiBackgroundColor = "#1B1C20"
    }else{
        targetLiBackgroundColor = "#fff"
    }
    var animateFlag = true;
    $(document).on('click', ".scroll-btn", function () {
        var that = $(this);
        if (!animateFlag) {
            return;
        }

        animateFlag = false;
        var index = that.index();
        var liContainer = $("ul" + that.attr("id")).parents("li");
        if(webSkin === 'dark-skin'){
            liContainer.css("backgroundColor", "#2C2D31");
        }else{
            liContainer.css("backgroundColor", "#EBEDF1");
        }

        $(".menuContent").animate({scrollLeft: 320 * index + 'px'}, 500, function () {
            animateFlag = true;
            //渐变恢复容器的颜色
            liContainer.animate({backgroundColor: targetLiBackgroundColor}, 2000);
        });
    });


    function getFont(treeId, node) {
        return node.font ? node.font : {};
    }

    function closeLoading() {
        setTimeout(function () {
            bt.loading.hide();
        }, 1500);
    }

    var menus;
    var treeArr = [];
    bt.loading.show();
    $.ajax({
        url: basePath + "/sys/menu/json",
        method: 'GET',
        async: true,
        dataType: 'json',
        data: {type: "bytter"},
        success: function (data) {
            menus = data;
            addTreePositionButton();
            addTreeContainer();
            var bodyHeight = $('body').innerHeight();
            var fieldHeight = $(".layui-elem-field").height();
            var treePositionButtonHeight = $(".treePositionButton").height();
            var addHeight = $(".add").outerHeight();
            $('.menuContent').height(bodyHeight - fieldHeight - treePositionButtonHeight - addHeight - 50);
            for (var i = 0; i < menus.length; i++) {
                var menu = menus[i];
                setTreeNode(menu);
            }
            setTimeout(function () {
                bt.loading.hide();
            }, 2000);
        },
        error: function () {
            closeLoading();
        }
    });

    function setTreeNode(menu){
        $.ajax({
            url: basePath + "/sys/menu/treeAndResources",
            type: "GET",
            data: {id: menu.id},
            async: true,
            dataType: "JSON",
            success: function (nodes) {
                if (nodes.length == 0) {
                    return;
                }
                var temp = [];
                var parentNode;
                for (var j = 0; j < nodes.length; j++) {
                    var node = nodes[j];
                    if (!node.parent) {
                        parentNode = node;
                    }
                    if (!node.isMenu) {
                        //将资源项标识为红色
                        /// node.font = {'color': '#9966FF'}
                        if(bt.getSysSkin() === 'dark-skin'){
                            node.font = {'color': '#526DD9'}
                        }else{
                            node.font = {'color': '#4E6DEC'}
                        }
                    }
                    temp.push(node);
                }
                temp.sort(function (a, b) {
                    return a.sort - b.sort;
                });
                var ztree = $.fn.zTree.init($("#menuTree_" + menu.id), setting, temp);
                ztree.expandAll(true);
                //只展开到二级菜单，加快页面加载速度。如果展开全部，页面加载时间比较长。
                // expandTreeMenu(ztree, true, ztree.getNodes(), 1);
                //设置显示状态
                var nodes = ztree.transformToArray(ztree.getNodes());
                var hiddenMenuIds = $('input[name="hiddenMenuIds"]').val();
                var hiddenResourcesIds = $('input[name="hiddenResourcesIds"]').val();
                for (var j = 0; j < nodes.length; j++) {
                    var node = nodes[j];
                    if (hiddenMenuIds.indexOf(node.id) >= 0 || hiddenResourcesIds.indexOf(node.id) >= 0) {
                        if (node.children) {
                            ztree.checkNode(node, true, false);
                        } else {
                            ztree.checkNode(node, true, true);
                        }
                    }
                }
            }
        });
    }

    $('.add').click(function () {
        var nodes = getAllNodes();
        var menuIds = "";
        var resourcesIds = "";
        for (var i = 0, l = nodes.length; i < l; i++) {
            if (nodes[i].isMenu) {
                menuIds += nodes[i].id + ",";
            } else {
                resourcesIds += nodes[i].id + ",";
            }
        }
        if (menuIds.length > 0) menuIds = menuIds.substring(0, menuIds.length - 1);
        if (resourcesIds.length > 0) resourcesIds = resourcesIds.substring(0, resourcesIds.length - 1);
        bt.loading.show();
        $.post(basePath + "/sys/role/assign", {
            id: $('input[name="roleId"]').val(),
            menuIds: menuIds,
            resourcesIds: resourcesIds
        }, function (result) {
            bt.loading.hide();
            layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, "JSON");
    });

    $(".close").click(function () {
        layer.confirm(bt.lang('sys.role.curWindow.close'), {icon: 3}, function (i) {
            layer.close(i);
            bt.navtab.closeCurrent();
        });
    });

});
