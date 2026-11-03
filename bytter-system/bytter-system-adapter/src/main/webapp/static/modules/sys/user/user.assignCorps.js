bt.use(['jquery', 'form', 'ztree', 'ztreeCheck', 'ztreeEdit', 'ztreeHide', 'selectType'], function () {
    var layer = parent.layer ? parent.layer : bt.layer,
        $ = bt.jquery;
    var form = bt.form()
    var userId = $('#userId').val();
    var currentCorpId = $('#corpId').val();
    var currentCorpIdAlways = $('#corpId').val();
    var baseModule = $('#baseModule').val();
    var selectModuleId =baseModule;
    loadSelectData(baseModule);

    form.on('select(organtypeSelect)', function(data){
        selectModuleId = data.value;
        loadSelectData(data.value);
    });

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
    var corpIds;
    CorpTree.prototype.init = function (options) {

        $.ajaxSettings.async = false;
        // $.get(basePath + "/sys/user/getResourceCorpIds?userId=" + userId, function (allCorpIds) {
        //     //获取当前用户拥有的权限资源单位列表
        //     corpIds = allCorpIds;
        // }, "JSON");
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
                if (node.children && node.children.length > 0) {
                    node.children = beforeInit(node.children);
                }
            }
            return nodes;
        };
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
        // bt.loading.show();
        // menuTree.init();
        // $("#corpSearch").val("");
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

    CorpTree.prototype.corpSearch = function () {
        var that = this;
        var searchSelectTree = throttle(function (treeList) {
            // 显示所有节点
            $('#' + treeList).find('.layui-hide').removeClass('layui-hide');
            that.obj.showNodes(that.hiddenTreeNodes.hiddenNodes);
            //获取不符合条件的叶子结点
            that.hiddenTreeNodes.hiddenNodes =that.obj.getNodesByFilter(filterFunc_corp);
            that.obj.hideNodes(that.hiddenTreeNodes.hiddenNodes);
        });
        $('#corpSearch').on('input propertychange', function () {
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
                chkboxType: {"Y": "", "N": "" }
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
        $.post(basePath + "/sys/corp/getCorpByOrgId/", {moduleId: ''}, function (result) {
            result = that.filter(result);
            that.obj = $.fn.zTree.init($("#menuTree"), setting, result);

            //根据是否设置勾选节点
            var nodes = that.obj.transformToArray(that.obj.getNodes());
            for (var i = 0; i < nodes.length; i++) {
                var node = nodes[i];
                if(node.level == '0') {
                    nodes[i].nocheck = true
                    nodes[i].chkDisabled = true
                }
                if (node.isChecked) {
                    that.obj.checkNode(node, true, true);
                }
            }

            //只展开到二级菜单，加快页面加载速度。如果展开全部，页面加载时间比较长。
            expandTreeMenu(that, true, that.obj.getNodes(), 1);

            that.checkRoleSelect(nodes);

            // 校验是否全选，目的是为了设置全选或者取消全选文本
            that.checkNodeAllChecked();

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
    var onCheck = function(event){
        var nodes = menuTree.obj.transformToArray(menuTree.obj.getNodes());
        menuTree.checkRoleSelect(nodes);
        // 校验是否全选，目的是为了设置全选或者取消全选文本
        menuTree.checkNodeAllChecked();
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
                // flag &= this.checkContainer(resourceIds, role.resource[m]);
                flag = this.checkContainer(resourceIds, role.resource[m]);
                if(!flag){
                    break
                }
            }
            if (flag) {
                $("a[role-id='" + role.id + "']").find(".chk").removeClass("checkbox_false").addClass("checkbox_true");
            }else{
                $("a[role-id='" + role.id + "']").find(".chk").removeClass("checkbox_true").addClass("checkbox_false");
            }
        }
    };
    // 校验是否全选，目的是为了设置全选或者取消全选文本
    MenuTree.prototype.checkNodeAllChecked = function(){
        var nodes = menuTree.obj.transformToArray(menuTree.obj.getNodes());
        var isAllChecked = true;
        for (var i = 0; i < nodes.length; i++) {
            if (!nodes[i].checked) {
                // 如果有一个没有选，就不是全选
                if(isAllChecked){
                    isAllChecked = false;
                    break
                }
            }
        }
        // 初始化全选/取消全选内容
        var checkAllText = !isAllChecked ? bt.lang('common.check.all'):bt.lang('common.check.unAll');
        $("#checkedAll").text(checkAllText);
    }

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


    /**
     * 查找子结点，如果匹配模糊查询的关键字则返回false，否则返回true
     */
    function searchChildren(pNode,keyword){
        var unMatch=true;
        for(var i = 0;i < pNode.length;i++){
            if(pNode[i].name.indexOf(keyword)!=-1){
                return false
            }
            if(pNode[i].isParent && pNode[i].children){
                unMatch = JSON.stringify(pNode[i].children).indexOf(keyword) === -1 || searchChildren(pNode[i].children,keyword)
            }
            if(!unMatch){
                return false
            }


        }
        return unMatch;
    };

    //查找不符合条件的结点
    //返回true表示需要隐藏，返回false表示不需要隐藏。
    function filterFunc(node){
        var keyword = $.trim($('#menuSearch').val());
        $("#" + node.tId).removeClass('search_result');

        if(!keyword){
            return false;
        }
        if(node.name =='内部流水资金计划匹配'){
            console.log(node.name);
        }

        //如果当前结点或其子节点匹配模糊查询的关键字，则该结点不隐藏
        if(node.name.indexOf(keyword)!=-1){
            $("#" + node.tId).addClass('search_result')
            return false;
        }

        if(node.isParent && node.children){
            // 如果父级有模糊匹配,则子级全部显示
            if($("#" + node.tId).parents().hasClass('search_result')){
                return false;
            }
            return JSON.stringify(node.children).indexOf(keyword) === -1 || searchChildren(node.children,keyword);
        }else{
            // 如果末级的父级有模糊匹配,则末级全部显示
            if($("#" + node.parentTId).hasClass('search_result') || $("#" + node.parentTId).parents().hasClass('search_result')){
                return false;
            }
        }
        return  false
    };


    //查找不符合条件的叶子节点
    function filterFunc_corp(node) {
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

        if (isSearch) {
            if (len) {
                $("#" + node.tId).addClass('search_result')
            } else {
                $("#" + node.tId).removeClass('search_result');
            }
            return false;
        }

        if (node.isParent) {
            if (len && isSearch) {
                $("#" + node.tId).addClass('search_result');//设置检索
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
    var corpTreeObj = null;

    //初始化单位网点树
    var menuTree = new MenuTree();
    menuTree.init();

    function beforeInit(nodes) {
        for (var k in nodes) {
            var node = nodes[k];
            node.corpName = node.name;
            node.name = '(' + node.code + ') ' + node.name;
            if (node.children && node.children.length > 0) {
                node.children = beforeInit(node.children);
            }
        }
        return nodes;
    }

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

    var setting = {
        data: {
            simpleData: {
                enable: true,
            }
        },
        callback: {
            beforeClick: corpTree.zTreeBeforeClick,
            onClick: corpTree.zTreeOnClick
        },
        view: {
            fontCss: getFont,
            nameIsHTML: true,
            addHoverDom: corpTree.addHoverDom,
            removeHoverDom: corpTree.removeHoverDom,
        }
    };


    //是否选中全部节点
    var checkedAllText = bt.lang('common.check.all');
    var uncheckedAllText = bt.lang('common.check.unAll')
    $("#checkedAll").click(function () {
        // 如果相等，就是要数据全选，否则就是取消全选
        if($(this).text() == checkedAllText){
            $(this).text(uncheckedAllText)
        }else{
            $(this).text(checkedAllText)
        }
        menuTree.obj.checkAllNodes($(this).text() != checkedAllText);
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
        // corpTree.obj.expandAll(false);
        corpTreeObj.expandAll(false);
    });
    $("#corpExpandAll").click(function () {
        // corpTree.obj.expandAll(true);
        corpTreeObj.expandAll(true);
    });

    corpTree.corpSearch();
    menuTree.menuSearch();

    $('#sumitData').click(function () {
        var checkedData = menuTree.getCheckedData();

        var sData = {
            // userId: userId,
            parentId: currentCorpId,
            // moduleId: selectModuleId,
            corpIds: checkedData.resources
        };
        bt.loading.show();
        $.post(basePath + '/sys/corp/transfer', sData, function (data) {
            bt.loading.hide();
            parent.layer.alert(data.msg, {icon: data.success ? 1 : 2});
            if (data.success) {
               // corpTree.init();
                location.reload();
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

    function loadSelectData(id){
        $.post(basePath + "/sys/corp/getCorpByOrgId/",{moduleId:id}, function (nodes) {   //获取树形的单位列表
            nodes = beforeInit(nodes);
            corpTreeObj = $.fn.zTree.init($("#corpTree"), setting, nodes);
            corpTreeObj.expandAll(true);

            corpTree.obj = corpTreeObj;
            var nodesArray = corpTreeObj.transformToArray(corpTreeObj.getNodes());
            for (var i = 0; i < nodesArray.length; i++) {
                var treeNode = nodesArray[i];
                if (treeNode.id == currentCorpId) {
                    corpTreeObj.selectNode(treeNode);
                }
            }
        }, "JSON");

        // $.get(basePath + "/sys/user/menuAndResource", {userId: userId, corpId: currentCorpId, moduleId: selectModuleId}, function (result) {
        //     menuTree.init();
        // }, "JSON");
    }
});
