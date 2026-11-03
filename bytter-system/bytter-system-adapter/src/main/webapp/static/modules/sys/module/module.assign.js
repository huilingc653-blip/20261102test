
bt.use(['form','jquery','ztree','ztreeCheck','jqColor'], function(){
    var form = bt.form();
    var $  = bt.jquery,layer=parent?parent.layer:layer;
    var setting = {
        view: {showIcon: false, dblClickExpand: false,fontCss: getFont,nameIsHTML: true},
        check: {enable: true, nocheck: false,chkStyle : "checkbox", chkboxType: {"Y": "ps", "N": "ps"}},
        data:{simpleData: {enable: true,idKey: "id",pIdKey: "parentId"}}
    }
    function addTreeContainer(){
        var treeHtml = '';
        for(var i=0;i<menus.length;i++){
            var menu = menus[i];
            treeHtml += '<li class="box"><ul id="menuTree_'+menu.id+'" class="ztree resources-tree"></ul></li>';
        }
        $(".treeContainer").html(treeHtml).css("width",325*menus.length);
    }

    function addTreePositionButton(){
        var treeHtml = '';
        for(var i=0;i<menus.length;i++){
            var menu = menus[i];
            treeHtml += '<button id="#menuTree_'+menu.id+'" class="scroll-btn layui-btn layui-btn-primary layui-btn-small">'+menu.name+'</button>';
        }
        $(".treePositionButton").html(treeHtml);
    }

    function getAllNodes(){
        var nodesArr = [];
        for(var i=0;i<menus.length;i++){
            var menu = menus[i];
            var zTree = $.fn.zTree.getZTreeObj("menuTree_"+menu.id);
            var nodes = zTree.getCheckedNodes(true);
            for(var j=0;j<nodes.length;j++){
                var node = nodes[j];
                nodesArr.push(node);
            }
        }
        return nodesArr;
    }

    var animateFlag = true;
    $(document).on('click',".scroll-btn",function(){
        var that = $(this);
        if(!animateFlag){
            return;
        }
        animateFlag = false;
        var index = that.index();
        var liContainer = $("ul"+that.attr("id")).parents("li");
        liContainer.css("background","#ff9");
        $(".menuContent").animate({scrollLeft: 320*index+'px' }, 500,function(){
            animateFlag = true;
            //渐变恢复容器的颜色
            liContainer.animate({backgroundColor:"#fff"},2000);
        });
    });


    function getFont(treeId, node) {
        return node.font ? node.font : {};
    }
    var menus;
    var treeArr = [];
    $.ajax({url:basePath+"/sys/menu/json",method: 'GET',async:true,dataType:'json', success: function (data) {
        menus = data;
        addTreePositionButton();
        addTreeContainer();
        var bodyHeight = $('body').innerHeight();
        var fieldHeight = $(".layui-elem-field").height();
        var treePositionButtonHeight = $(".treePositionButton").height();
        var addHeight = $(".add").outerHeight();
        $('.menuContent').height(bodyHeight - fieldHeight-treePositionButtonHeight-addHeight-50);

        for(var i=0;i<menus.length;i++){
            var menu = menus[i];
            $.get(basePath+"/sys/menu/treeAndResources",{id:menu.id,type:"menu"},function(nodes){
                //将资源项标识为蓝色
                var temp = [];
                var parentNode;
                for(var j=0;j<nodes.length;j++){
                    var node = nodes[j];
                    if(!node.parent){
                        parentNode = node;
                    }
                    if(!node.isMenu){
                        node.font = {'color':'#9966FF'}
                    }
                    temp.push(node);
                }
                temp.sort(function(a,b){
                    return a.sort - b.sort;
                });
                var ztree = $.fn.zTree.init($("#menuTree_"+parentNode.id), setting, temp);
                ztree.expandAll(true);
                //设置显示状态
                var nodes = ztree.transformToArray(ztree.getNodes());
                var hiddenModuleMenuIds = $('input[name="hiddenModuleMenuIds"]').val();
                for(var j=0;j<nodes.length;j++){
                    var node = nodes[j];
                    if(hiddenModuleMenuIds.indexOf(node.id)>=0){
                        ztree.checkNode(node, true, false);
                    }
                }
            },"JSON");
        }
    }});

    $('.add').click(function(){
        var nodes = getAllNodes();
        var menuIds = "";
        for (var i=0, l=nodes.length; i<l; i++) {
            if(nodes[i].isMenu){
                menuIds += nodes[i].id + ",";
            }
        }
        if (menuIds.length > 0 ) menuIds = menuIds.substring(0, menuIds.length-1);
        $.post(basePath + "/sys/module/assign",{
            id:$('input[name="moduleId"]').val(),
            menuIds:menuIds
        },function(result){
            layer.alert(result.msg, {icon: result.success?1:2});
        },"JSON");
    });

    $(".close").click(function () {
        layer.confirm(bt.lang('sys.module.curWindow.close'), {icon:3},function (i) {
            layer.close(i);
            bt.navtab.closeCurrent();
        });
    });
});
