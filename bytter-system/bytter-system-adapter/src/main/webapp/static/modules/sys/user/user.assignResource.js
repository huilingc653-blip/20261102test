bt.use(['jquery', 'form', 'ztree', 'ztreeCheck', 'ztreeEdit', 'ztreeHide'], function () {
    var layer = parent.layer ? parent.layer : bt.layer,
        $ = bt.jquery;
    var userId = $('#userId').val();
    var currentCorpId = $('#corpId').val();
    var currentCorpIdAlways = $('#corpId').val();
    var searchResultList = '';
    var corpTreeList = [];

    //网点单位树构造器
    var CorpTree = function () {
        this.obj = null;
        this.nodeList = [];
        this.hiddenTreeNodes = {
            treeList: 'corpTree',
            hiddenNodes: []
        };//用于存储被隐藏的结点

        this.param = {
            treeContent: '',
            treeList: 'corpTree',
            treeInput: 'corpSearch',
            treeHiddenInput: ''
        }
    };
    bt.loading.show();
    CorpTree.prototype.init = function (options) {
        var corpIds;
        $.ajaxSettings.async = false;
        $.get(basePath + "/sys/user/getResourceCorpIds?userId=" + userId, function (allCorpIds) {
            //获取当前用户拥有的权限资源单位列表
            corpIds = allCorpIds;
        }, "JSON");
        $.ajaxSettings.async = true;
        var that = this;
        var setting = {
            data: {
                simpleData: {
                    enable: true,
                }
            },
            callback: {
                beforeClick: that.zTreeBeforeClick,
                onClick: that.zTreeOnClick
            },
            view: {
                fontCss: getFont,
                nameIsHTML: true,
                addHoverDom: that.addHoverDom,
                removeHoverDom: that.removeHoverDom,
            }
        };

        function getFont(treeId, node) {
            for (var i in corpIds) {
                if (node.id == corpIds[i] && node.id != currentCorpIdAlways) {
                    return node.font = {'color': 'blue'};
                }
            }
            if (currentCorpIdAlways == node.id) {
                return node.font = {'color': 'red'};
            }
        }

        function beforeInit(nodes) {
            for (var k in nodes) {
                var node = nodes[k];
                node.corpName = node.name;
                node.name = '(' + node.code + ') ' + node.name;
                corpTreeList.push({
                    name:node.name,
                    id:node.id,
                    parentIds:node.parentIds
                });
                if (node.children && node.children.length > 0) {
                    node.children = beforeInit(node.children);
                }
            }
            return nodes;
        };

        $.get(basePath + "/sys/corp/treeAll", function (nodes) {   //获取树形的单位列表
            corpTreeList = [];
             nodes = beforeInit(nodes);
            that.obj = $.fn.zTree.init($("#corpTree"), setting, nodes);
            that.obj.expandAll(true);
            var nodesArray = corpTree.obj.transformToArray(corpTree.obj.getNodes());
            for (var i = 0; i < nodesArray.length; i++) {
                var treeNode = nodesArray[i];
                if (treeNode.id == currentCorpId) {
                    corpTree.obj.selectNode(treeNode);
                }
            }
        }, "JSON");
    };

    CorpTree.prototype.zTreeBeforeClick = function (treeId, treeNode, clickFlag) {
        var zTree = $.fn.zTree.getZTreeObj("corpTree");
        var nodes = zTree.getCheckedNodes(true);
        if (!nodes || !nodes.length) {
            return true;
        }
        if (treeNode.id === nodes[0].id) {
            return false;
        }
    };
    CorpTree.prototype.zTreeOnClick = function (event, treeId, treeNode) {
        currentCorpId = treeNode.id;
        bt.loading.show();
        menuTree.init();
        $("#menuSearch").val("");
    };
    CorpTree.prototype.filter = function (childNodes) {
        if (!childNodes) {
            return null
        }
        for (var i = 0, l = childNodes.length; i < l; i++) {
            childNodes[i].name = '(' + childNodes[i].code + ') ' + childNodes[i].name.replace(/\.n/g, '.');
            childNodes[i].fixedName = childNodes[i].name;
        }
        return childNodes;
    };
    CorpTree.prototype.expandNodes = function (nodes) { //展开节点
        if (!nodes) {
            return;
        }
        var zTree = corpTree.obj;
        for (var i = 0, l = nodes.length; i < l; i++) {
            zTree.expandNode(nodes[i], true, false, false);

            if (nodes[i].isParent && nodes[i].zAsync) {
                CorpTree.prototype.expandNodes(nodes[i].children);
            }
        }
    };
    CorpTree.prototype.getCheckedData = function () {
        var nodes = corpTree.obj.getCheckedNodes(true);
    };

    CorpTree.prototype.searchNode = function () {
        var _this = $(this);
        clearTimeout(corpTree.timer);
        corpTree.timer = setTimeout(function () {
            var input = _this.val();
            corpTree.nodeList = corpTree.obj.getNodesByParamFuzzy("name", input, null);
            var tempNodeList = corpTree.nodeList;
            if (corpTree.nodeList.length === 0 || input === "") {
                corpTree.nodeList = corpTree.obj.transformToArray(corpTree.obj.getNodes());
                corpTree.updateNodes(false, input);
                return;
            }
            corpTree.updateNodes(true, input);
        }, 200);

    };
    CorpTree.prototype.updateNodes = function (highlight, inputValue) {
        for (var i = 0, l = corpTree.nodeList.length; i < l; i++) {
            var name = corpTree.nodeList[i].fixedName;
            if (!highlight) {
                corpTree.obj.expandNode(corpTree.nodeList[i].getParentNode(), true, false, false);
                corpTree.nodeList[i].name = name;
            } else {
                corpTree.nodeList[i].name = name.replace(inputValue, '<b style="background: #ffff00">' + inputValue + '</b>');
            }
            corpTree.obj.updateNode(corpTree.nodeList[i]);
        }
    };
    CorpTree.prototype.addHoverDom = function (treeId, treeNode) {
        var aObj = $("#" + treeNode.tId + "_a");
        if ($("#copyBtn_" + treeNode.id).length > 0) return;
        var editStr = '<i id="copyBtn_' + treeNode.id + '" class="fa fa-files-o" aria-hidden="true" title="copy" style="margin-left:10px;font-size: 15px;"></i>';
        aObj.append(editStr);
        var btn = $("#copyBtn_" + treeNode.id);
        if (btn) btn.bind("click", function () {
            parent.layer.open({
                type: 2,
                area: ['550px', '650px'],
                maxmin: false,
                content: basePath + "/sys/user/copyResources?corpId=" + treeNode.id + "&userId=" + userId + "&userBelongCorp="+currentCorpId,
            });
            return false;
        });
    };
    CorpTree.prototype.removeHoverDom = function (treeId, treeNode) {
        $("#copyBtn_" + treeNode.id).unbind().remove();
    };

    CorpTree.prototype.corpSearch = function () {
        var that = this;
        var searchSelectTree = throttle(function (treeList) {
            // 显示所有节点
            $('#' + treeList).find('.layui-hide').removeClass('layui-hide');
            that.obj.showNodes(that.hiddenTreeNodes.hiddenNodes);
            // 获取搜索结果清单数据
            getSearchResultList()
            //获取不符合条件的叶子结点
            that.hiddenTreeNodes.hiddenNodes = that.obj.getNodesByFilter(filterFuncByCorp);
            that.obj.hideNodes(that.hiddenTreeNodes.hiddenNodes);
        });
        $('#corpSearch').on('input propertychange', function () {
            $("#corpTree").removeClass('layui-hide');
            //执行搜索下拉树回调
            searchSelectTree(that.hiddenTreeNodes.treeList);
        })
            .on('focus', function () {
                //显示上次搜索后背隐藏的结点
                if (!$(this).val().length) { // 0.1.18 输入框有内容时，不显示树所有内容
                    $('#' + that.param.treeList).find('.layui-hide').removeClass('layui-hide');
                    that.obj.showNodes(that.hiddenTreeNodes.hiddenNodes);
                    that.hiddenTreeNodes.hiddenNodes = [];
                    $(this).siblings('.select-tree').find('.search_result').removeClass('search_result');
                }
                $(this).siblings('a').find('.bt-selectTree-close').addClass('bt-selectTree-hover');
            })
            .on('blur', function () { //丢去焦点时
                $(this).siblings('a').find('.bt-selectTree-close').removeClass('bt-selectTree-hover');
            })
    }

    //菜单构造器
    var MenuTree = function () {
        this.obj = null;
        this.nodeList = [];
        this.hiddenTreeNodes = {
            treeList: 'menuTree',
            hiddenNodes: []
        };
        this.roles = [];
        this.param = {
            treeContent: '',
            treeList: 'menuTree',
            treeInput: 'menuSearch',
            treeHiddenInput: ''
        }

    };
    MenuTree.prototype.init = function (options) {  //初始化树形菜单，每次点击单位都初始化一次
        //bt.loading.show();
        var that = this;
        var setting = {
            data: {
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "parentId"
                }
            },
            check: {
                enable: true,
                chkStyle: "checkbox",
                chkboxType: {"Y": "ps", "N": "ps"}
            },
            callback: {
                beforeClick: that.zTreeBeforeClick,
                onNodeCreated: that.zTreeOnNodeCreated,
                onCheck:onCheck
            },
            view: {
                nameIsHTML: true,
                autoCancelSelected: false
            }
        };
        $.ajax({
            url: basePath + "/sys/user/listUserRoleIncludeResource",
            method: 'GET',
            async: true,
            data: {userId: userId,"_btv":new Date().getTime()},
            dataType: 'json',
            success: function (result) {
                var html = "";
                for (var i = 0; i < result.length; i++) {
                    var resource = result[i];
                    html += "<a href=\"javascript:void(0);\" style='display: flex;align-items: center;' role-id='" + resource.id + "'>\n" +
                        "<span class=\"button chk checkbox_false\"></span>" +
                        '<span title="'+resource.name+'" class="node_name text-ellipsis">' + resource.name + "</span>\n";
                    html += "</a>"
                }
                $("#roleContainer").html(html);
                that.roles = result;
            }
        });
        $.get(basePath + "/sys/user/menuAndResource", {userId: userId, corpId: currentCorpId}, function (result) {
            result = that.filter(result);
            that.obj = $.fn.zTree.init($("#menuTree"), setting, result);

            //根据是否设置勾选节点
            var nodes = that.obj.transformToArray(that.obj.getNodes());
            for (var i = 0; i < nodes.length; i++) {
                var node = nodes[i];
                if (node.isChecked) {
                    that.obj.checkNode(node, true, true);
                }
            }
            //只展开到二级菜单，加快页面加载速度。如果展开全部，页面加载时间比较长。
            expandTreeMenu(that, true, that.obj.getNodes(), 1);

            that.checkRoleSelect(nodes);

            bt.loading.hide();
        }, "JSON");

    };

    MenuTree.prototype.checkContainer = function (items, value) {
        for (var i = 0; i < items.length; i++) {
            if (items[i] === value) {
                return true;
            }
        }
        return false;
    }

    var onCheck = function(event){
        var nodes = menuTree.obj.transformToArray(menuTree.obj.getNodes());
        menuTree.checkRoleSelect(nodes);
    }

    // 获取角色选中的资源ids
    MenuTree.prototype.getRoleCheckedResourceIds = function (elems) {
        var ids = []
        if(!elems.length){
            return  ids
        }
        for (var i = 0; i < elems.length; i++) {
            if($(elems[i]).find(".chk").hasClass("checkbox_true")){
                //获取角色ID
                var roleId = $(elems[i]).attr("role-id");
                //根据角色ID提取出角色的资源值
                for (var n = 0; n < menuTree.roles.length; n++) {
                    if (menuTree.roles[n].id === roleId) {
                        ids = ids.concat(menuTree.roles[n].resource)
                        break;
                    }
                }
            }
        }
        return ids;
    }

    MenuTree.prototype.checkRoleSelect = function (nodes) {
        var resourceIds = [];
        for (var i = 0; i < nodes.length; i++) {
            var node = nodes[i];
            if (node.isMenu || !node.checked) {
                continue;
            }
            resourceIds.push(node.id);
        }
        for (var n = 0; n < menuTree.roles.length; n++) {
            var role = menuTree.roles[n];
            var flag = true;
            for (var m = 0; m < role.resource.length; m++) {
                flag &= this.checkContainer(resourceIds, role.resource[m]);
            }
            if (flag) {
                $("a[role-id='" + role.id + "']").find(".chk").removeClass("checkbox_false").addClass("checkbox_true");
            }else{
                $("a[role-id='" + role.id + "']").find(".chk").removeClass("checkbox_true").addClass("checkbox_false");
            }
        }
    };

    MenuTree.prototype.zTreeOnNodeCreated = function (event, treeId, treeNode) {
        if (!treeNode.isMenu) {
            $('#' + treeNode.tId).addClass('resource-node');
        }
    };

    MenuTree.prototype.filter = function (childNodes) {
        if (!childNodes) {
            return null
        }
        for (var i = 0, l = childNodes.length; i < l; i++) {
            childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
            childNodes[i].fixedName = childNodes[i].name;
        }
        return childNodes;
    };

    MenuTree.prototype.getCheckedData = function () { //获取菜单资源的选中情况
        var temp = {
            resources: [],
            menus: []
        };
        var nodes = menuTree.obj.getCheckedNodes(true);
        for (var k in nodes) {
            var node = nodes[k];
            if (node.isMenu) {
                temp.menus.push(node.id);
            } else {
                temp.resources.push(node.id);
            }
        }
        return temp;
    };

    MenuTree.prototype.searchNode = function () {
        var input = $(this).val();
        clearTimeout(menuTree.timer);
        menuTree.timer = setTimeout(function () {
            menuTree.nodeList = menuTree.obj.getNodesByParamFuzzy('name', input);
            if (menuTree.nodeList.length === 0 || input === "") {
                menuTree.nodeList = menuTree.obj.transformToArray(menuTree.obj.getNodes());
                menuTree.updateNodes(false, input);
                return;
            }
            menuTree.updateNodes(true, input);
        }, 200)

    };

    MenuTree.prototype.updateNodes = function (highlight, inputValue) {
        for (var i = 0, l = menuTree.nodeList.length; i < l; i++) {
            var name = menuTree.nodeList[i].fixedName;
            if (!highlight) {
                menuTree.obj.expandNode(menuTree.nodeList[i].getParentNode(), true, false, false);
                menuTree.nodeList[i].name = name;
            } else {
                menuTree.nodeList[i].name = name.replace(inputValue, '<b style="background: #ffff00">' + inputValue + '</b>');
            }
            menuTree.obj.updateNode(menuTree.nodeList[i]);
        }
    };

    MenuTree.prototype.menuSearch = function () {
        var that = this;
        var searchSelectTree = throttle(function (treeList) {
            // 显示所有节点
            $('#' + treeList).find('.layui-hide').removeClass('layui-hide');
            that.obj.showNodes(that.hiddenTreeNodes.hiddenNodes);

            //获取不符合条件的叶子结点
            that.hiddenTreeNodes.hiddenNodes = that.obj.getNodesByFilter(filterFunc);
            that.obj.hideNodes(that.hiddenTreeNodes.hiddenNodes);
        });

        $('#menuSearch').on('input propertychange', function () {
            //执行搜索下拉树回调
            searchSelectTree(that.hiddenTreeNodes.treeList);
        })
            .on('focus', function () {
                //显示上次搜索后背隐藏的结点
                if (!$(this).val().length) { // 0.1.18 输入框有内容时，不显示树所有内容
                    $('#' + that.param.treeList).find('.layui-hide').removeClass('layui-hide');
                    that.obj.showNodes(that.hiddenTreeNodes.hiddenNodes);
                    that.hiddenTreeNodes.hiddenNodes = [];
                    $(this).siblings('.select-tree').find('.search_result').removeClass('search_result');
                }
                $(this).siblings('a').find('.bt-selectTree-close').addClass('bt-selectTree-hover');
            })
            .on('blur', function () {
                $(this).siblings('a').find('.bt-selectTree-close').removeClass('bt-selectTree-hover');
            })
    }
    // 字符串转数组
    var toArray = function (str) {
        var arr = str.split(" ");
        for (var i = 0; i < arr.length; i++) {
            if (arr[i] === "" || typeof (arr[i]) == "undefined") {
                arr.splice(i, 1);
                i = i - 1;
            }
        }
        return arr;
    }

    //　节流函数
    function throttle(fn, threshhold) {
        var timeout
        var start = new Date;
        var threshhold = threshhold || 0
        return function () {
            var context = this, args = arguments, curr = new Date() - 0
            clearTimeout(timeout)//总是干掉事件回调
            if (curr - start >= threshhold) {
                fn.apply(context, args) //只执行一部分方法，这些方法是在某个时间段内执行一次
                start = curr
            }
        }
    }

    function getSearchResultList() {
        searchResultList = '';
        var _keywords= $.trim( $('#'+ corpTree.param.treeInput).val());
        var textArr = toArray( _keywords );
        var len = textArr.length;
        if(len){
            for (var j = 0; j < corpTreeList.length; j++) {
                var node = corpTreeList[j]
                var isResult = false // 是否找到了结果
                for(var i = 0;i< len;i++){
                    if( node.name.indexOf( textArr[i] ) !=-1 ){
                        isResult = true
                        break;
                    }
                }

                if(isResult){
                    if(searchResultList.indexOf(node.id) === -1){
                        searchResultList += node.id
                    }
                    var parentIds = node.parentIds && node.parentIds.split(',') || ''
                    if(parentIds && parentIds.length){
                        for (var i = 0; i < parentIds.length; i++) {
                            var pid = parentIds[i]
                            if(searchResultList.indexOf(pid) === -1){
                                searchResultList += pid
                            }
                        }
                    }

                }
            }
        }
    }

    // 单位树的数据方法
    function filterFuncByCorp(node) {
        if(!searchResultList){
            $("#" + node.tId).removeClass('search_result');
            var _keywords= $.trim( $('#'+ corpTree.param.treeInput).val());
            // 如果有值，说明未匹配到数据
            if(_keywords){
                $("#"+corpTree.param.treeList).addClass('layui-hide');
            }
            return false
        }
        var isSearch = searchResultList.indexOf(node.id) ===-1?false:true
        var len = searchResultList.length;
        if( node.isParent){
            if( len && isSearch ){
                $("#" + node.tId).addClass('search_result');//设置检索
                return false
            }else{
                $("#" + node.tId).removeClass('search_result');
            }
            return true;
        }
        if( isSearch ){
            if( len ){
                $("#" + node.tId).addClass('search_result')
            }else{
                $("#" + node.tId).removeClass('search_result');
            }
            return false;
        }

        $("#" + node.tId).removeClass('search_result');
        return true
    }

    //查找不符合条件的叶子节点
    function filterFunc(node) {
        var id = $("#" + node.tId).parents(".ztree").data("input");
        var _keywords = $.trim($('#' + id).val())
        var textArr = toArray(_keywords);
        var isSearch = true, len = textArr.length;
        if (len) {
            for (var i = 0; i < len; i++) {
                if (node.name.indexOf(textArr[i]) === -1) {
                    isSearch = false;
                    break;
                }
            }
        }
        if (node.isParent) {
            if (len && isSearch) {
                $("#" + node.tId).addClass('search_result');//设置检索
            } else {
                $("#" + node.tId).removeClass('search_result');
            }
            return false;
        }
        if (isSearch) {
            if (len) {
                $("#" + node.tId).addClass('search_result')
            } else {
                $("#" + node.tId).removeClass('search_result');
            }
            return false;
        }

        $("#" + node.tId).removeClass('search_result');
        return true;
    };

    //初始化单位网点树
    var corpTree = new CorpTree();
    corpTree.init();

    //初始化单位网点树
    var menuTree = new MenuTree();
    menuTree.init();

    //是否选中全部节点
    var checkedAllFlag = false;
    $("#checkedAll").click(function () {
        if (!checkedAllFlag) {
            checkedAllFlag = true;
            menuTree.obj.checkAllNodes(checkedAllFlag);
        } else {
            checkedAllFlag = false;
            menuTree.obj.checkAllNodes(checkedAllFlag);
        }
    });

    $('#roleContainer').on('click', 'a', function () {
        var _that = $(this).find(".chk");
        var checked;
        //点击时设置角色复选框的样式
        if (_that.hasClass("checkbox_false")) {
            _that.addClass("checkbox_true")
            _that.removeClass("checkbox_false")
            checked = true;
        } else {
            _that.addClass("checkbox_false")
            _that.removeClass("checkbox_true")
            checked = false;
        }
        var resourceIds;
        //获取角色ID
        var roleId = $(this).attr("role-id");
        //根据角色ID提取出角色的资源值
        for (var n = 0; n < menuTree.roles.length; n++) {
            if (menuTree.roles[n].id === roleId) {
                resourceIds = menuTree.roles[n].resource;
                break;
            }
        }
        //获取菜单资源树中的节点
        var nodes = menuTree.obj.transformToArray(menuTree.obj.getNodes());
        for (var i = 0; i < nodes.length; i++) {
            var node = nodes[i];
            //如果不是菜单，继续下次循环
            if (node.isMenu) {
                continue;
            }
            //检查当前资源是否在角色资源中
            if (menuTree.checkContainer(resourceIds, node.id)) {
                if(!checked){
                    // 获取角色选中的资源ids
                    var checkedIds = menuTree.getRoleCheckedResourceIds($(this).siblings('a'));
                    if(checkedIds && checkedIds.length){
                        if(checkedIds.indexOf(node.id) === -1){
                            //取消资源选中状态
                            menuTree.obj.checkNode(node, checked, true);
                        }
                    }else {
                        //设置资源选中或非选中状态
                        menuTree.obj.checkNode(node, checked, true);
                    }
                }else{
                    //设置资源选中或非选中状态
                    menuTree.obj.checkNode(node, checked, true);
                }
            }
        }
    });

    $("#menuCollapseAll").click(function () {
        //收起菜单资源
        menuTree.obj.expandAll(false);
    });
    $("#menuExpandAll").click(function () {
        //展开全部菜单资源
        menuTree.obj.expandAll(true);
    });
    $("#corpCollapseAll").click(function () {
        corpTree.obj.expandAll(false);
    });
    $("#corpExpandAll").click(function () {
        corpTree.obj.expandAll(true);
    });

    corpTree.corpSearch();
    menuTree.menuSearch();

    $('#sumitData').click(function () {
        var checkedData = menuTree.getCheckedData();

        var sData = {
            userId: userId,
            corpId: currentCorpId,
            resources: checkedData.resources
        };
        bt.loading.show();
        $.post(basePath + '/sys/user/assignResource', sData, function (data) {
            bt.loading.hide();
            parent.layer.alert(data.msg, {icon: data.success ? 1 : 2});
            if (data.success) {
                corpTree.init();
                // 刷新资源缓存
                // 获取用户单位资源权限
                $.get(basePath + "/resource", function (json) {
                    if (json) {
                        // 清空已经加载资源的菜单
                        window.top.bt.accessedMenuIds = {}
                        try {
                            store.remove('bytter_resource');//清除 liuyj 2018-06-05
                            // 重置首页的缓存
                            window.top.bt.userCorpResource = json;
                            store.set('bytter_resource', json);
                        } catch (error) {
                            //报错则表示store存储的数据大小超出了限制
                            window.top.bt.userCorpResource = json;
                        }
                    }
                }, 'JSON');
            }
        }, 'JSON');
    });

    /**
     * ztree树菜单控制展开到任意几级节点
     * @param zTreeObj
     * @param isRootNode
     * @param childNodes
     * @param level
     */
    function expandTreeMenu(zTreeObj, isRootNode, childNodes, level) {
        if (isRootNode) {
            var rootNodes = zTreeObj.obj.getNodes();
            expandTreeMenu(zTreeObj, false, rootNodes, level);//递归
        } else {

            if (childNodes && childNodes.length && childNodes.length > 0) {
                if (level < childNodes[0].level) {
                    return;
                }
                var length = childNodes.length;
                for (var i = 0; i < length; i++) {
                    zTreeObj.obj.expandNode(childNodes[i], true, false, false, true);
                    expandTreeMenu(zTreeObj, false, childNodes[i].children, level);//递归
                }
            }
        }
    }

    //清除搜索框内容
    $('.bt-selectTree-close').on('click', function () {
        $(this).parent().siblings('input').val('').trigger('focus')
        $(this).parent().parent().siblings('ul').find('.search_result').removeClass('search_result')
    })
});