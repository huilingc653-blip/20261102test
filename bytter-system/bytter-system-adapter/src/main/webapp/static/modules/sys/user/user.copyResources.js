bt.use(['jquery', 'form', 'ztree', 'ztreeCheck', 'ztreeEdit','ztreeHide'], function () {
    var layer = (parent && parent.layer) ? parent.layer : bt.layer,
        $ = bt.jquery;
    var userId = $('#userId').val();
    var sourceCorpId = $("#sourceCorpId").val();
    var userBelongCorp = $("#userBelongCorp").val();
    var searchResultList = '';
    var corpTreeList = [];
    //网点单位树构造器
    var CorpTree = function () {
        this.obj = null;
        this.nodeList = [];
        this.timer;
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
    CorpTree.prototype.init = function (options) {
        bt.loading.show();
        var corpIds;
        $.ajaxSettings.async = false;
        $.get(basePath + "/sys/user/getResourceCorpIds?userId=" + userId, function (allCorpIds) {
            corpIds = allCorpIds;
        }, "JSON");
        $.ajaxSettings.async = true;
        var that = this;
        var setting = {
            data: {
                simpleData: {
                    enable: true
                }
            },
            check: {
                enable: true,
                chkStyle: "checkbox",
                chkboxType: {"Y": "s", "N": "s"}
            },
            callback: {
                beforeClick: that.zTreeBeforeClick,
                onClick: this.onClick
            },
            view: {
                fontCss: getFont,
                nameIsHTML: true,
                addHoverDom: that.addHoverDom,
                removeHoverDom: that.removeHoverDom
            }
        };

        function getFont(treeId, node) {
            for (var i in corpIds) {
                if (node.id === corpIds[i] && node.id !== userBelongCorp) {
                    return node.font = {'color': 'blue'};
                }
                if (userBelongCorp === node.id) {
                    return node.font = {'color': 'red'};
                }
            }
        }

        $.get(basePath + "/sys/corp/tree", function (nodes) {
            corpTreeList = [];
            nodes = that.filter(nodes);
            that.obj = $.fn.zTree.init($("#corpTree"), setting, nodes);
            that.obj.expandAll(true);
            bt.loading.hide();
            var nodesArray = corpTree.obj.transformToArray(corpTree.obj.getNodes());
            for (var i = 0; i < nodesArray.length; i++) {
                var treeNode = nodesArray[i];
                if (treeNode.id === sourceCorpId) {
                    corpTree.obj.setChkDisabled(treeNode, true);
                }
            }
        }, "JSON");
    };

    CorpTree.prototype.filter = function (childNodes) {
        if (!childNodes) {
            return null
        }
        for (var i = 0, l = childNodes.length; i < l; i++) {
            var node = childNodes[i]
            childNodes[i].name = '(' + childNodes[i].code + ') ' + childNodes[i].name.replace(/\.n/g, '.');
            childNodes[i].fixedName = childNodes[i].name;
            corpTreeList.push({
                name:node.name,
                id:node.id,
                parentIds:node.parentIds
            });

            if (childNodes[i].children && childNodes[i].children.length ){
                childNodes[i].children = this.filter(childNodes[i].children)
            }
        }
        return childNodes;
    };
    CorpTree.prototype.zTreeOnAsyncSuccess = function (event, treeId, treeNode, msg) {
        if (!treeNode) {
            CorpTree.prototype.expandNodes(corpTree.obj.getNodes());
        } else {
            CorpTree.prototype.expandNodes(treeNode.children);
        }
    };
    CorpTree.prototype.zTreeBeforeClick = function (event, treeId, treeNode) {
    };
    //新增点击文字可以选中功能
    CorpTree.prototype.onClick = function( event, treeId, treeNode){
        var zTree = corpTree.obj;
        zTree.checkNode(treeNode, !treeNode.checked, false);
    }

    CorpTree.prototype.expandNodes = function (nodes) {
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
        var temp = [];
        var nodes = corpTree.obj.getCheckedNodes(true);
        for (var k in nodes) {
            var node = nodes[k];
            temp.push(node.id);
        }
        return temp;
    };
    CorpTree.prototype.zTreeOnNodeCreated = function (event, treeId, treeNode) {
        if (treeNode.id === sourceCorpId) {
            corpTree.obj.setChkDisabled(treeNode, true);
        }
    };
    CorpTree.prototype.searchNode = function () {
        var input = $.trim($(this).val());
        clearTimeout(corpTree.timer);
        corpTree.timer = setTimeout(function () {
            corpTree.nodeList = corpTree.obj.getNodesByParamFuzzy('fixedName', input);
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
                corpTree.nodeList[i].name = name.replace(inputValue, '<b style="background:#ffff00">' + inputValue + '</b>');
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
                    $('#corpTree').find('.layui-hide').removeClass('layui-hide');
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

    //初始化单位网点树
    var corpTree = new CorpTree();
    corpTree.init();
    corpTree.corpSearch()

    //是否全部选中
    var corpSelectAllFlag = false;
    $("#corpSelectAll").click(function () {
        if(!corpSelectAllFlag){
            corpSelectAllFlag = true;
            corpTree.obj.checkAllNodes(corpSelectAllFlag);
        }else{
            corpSelectAllFlag = false;
            corpTree.obj.checkAllNodes(corpSelectAllFlag);
        }
    });

    $("#corpCollapseAll").click(function () {
        corpTree.obj.expandAll(false);
    });

    $("#corpExpandAll").click(function () {
        corpTree.obj.expandAll(true);
    });

    //$('#corpSearch').bind("propertychange", corpTree.searchNode).bind("input", corpTree.searchNode);



    $('#sumitData').click(function () {
        var corps = corpTree.getCheckedData();
        if (corps.length === 0) {
            layer.alert(bt.lang('sys.accountGrant.corpUnSelect'), {icon: 2});
            return;
        }
        var sData = {
            userId: userId,
            sourceCorpId: sourceCorpId,
            corps: corps,
        };

        if (corps.length >= 50) {

            layer.confirm(bt.lang('sys.assignResource.submitLargeDataHint'), {}, function (index) {
                bt.loading.show();

                $.post(basePath + '/sys/user/copyResources', sData, function (data) {
                    bt.loading.hide();
                    if (data.success) {
                        //layer.closeAll();
                        var indexTemp = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(indexTemp);
                    }
                    layer.alert(data.msg, {icon: data.success ? 1 : 2});

                }, 'JSON');

            });
        } else {
            bt.loading.show();
            $.post(basePath + '/sys/user/copyResources', sData, function (data) {
                bt.loading.hide();
                if (data.success) {
                    layer.close(layer.index);
                }
                layer.alert(data.msg, {icon: data.success ? 1 : 2});

            }, 'JSON');
        }


    });
});
