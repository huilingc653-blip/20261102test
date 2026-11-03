
bt.use(['jquery','form','ztree'], function(){
    $ = bt.jquery,form = layui.form();
    var sourceUser = $('input[name="sourceUser"]').val();

    var preSelectUserList = [];
    var selectedUserList = [];
    //渲染准备选择用户
    var renderPreSelectUserList = function(){
        var html = '';
        if(preSelectUserList.length===0){
            html +='<div>该网点、单位未分配用户</div>';
            $("#pre-select-user").html(html);
            return;
        }
        for(var i=0;i<preSelectUserList.length;i++ ){
            var node = preSelectUserList[i];
            if(node.id === sourceUser){
                continue;
            }
            var checked = checkHasSelected(node);
            html += '<a href="javascript:void(0);" class="list-group-item '+(checked?"list-group-item-success":"")+'" data-userid="'+node.id+'">';
            html += '<div class="select-user-name">'+node.username+'</div>';
            html += '<div class="select-user-text">'+node.corpName+'</div>';
            if(checked){
                html += '<span class="select-user-op-icon"><i class="fa fa-times" aria-hidden="true"></i></span>';
            }else{
                html += '<span class="select-user-op-icon"><i class="fa fa-plus" aria-hidden="true"></i></span>';
            }
            html += '</a>';
        }
        $("#pre-select-user").html(html);
    }

    //渲染被选中的用户
    var renderSelectUserList = function(){
        var html = '';
        for(var k in selectedUserList){
            var node = selectedUserList[k];
            html += '<a href="javascript:void(0);" class="list-group-item" data-userid="'+node.id+'">';
            html += '<div class="select-user-name">'+node.username+'</div>';
            html += '<div class="select-user-text">'+node.corpName+'</div>';
            html += '<span class="select-user-op-icon"><i class="fa fa-times" aria-hidden="true"></i></span>';
            html += '</a>';
        }
        $("#selected-user").html(html);
    }


    $("#selected-user").on('click','a',function(){
        var _that = $(this);
        var userId = _that.data("userid");
        removeByValue(selectedUserList,userId)
        renderPreSelectUserList();
        renderSelectUserList();
    });

    $("#pre-select-user").on('click','a',function(){
        var _that = $(this);
        var userId = _that.data("userid");
        if(_that.hasClass("list-group-item-success")){
            removeByValue(selectedUserList,userId)
        }else{
            for(var k in preSelectUserList){
                var user = preSelectUserList[k];
                if(user.id == userId){
                    selectedUserList.push(user);
                    break;
                }
            }
        }
        renderPreSelectUserList();
        renderSelectUserList();
    });

    function removeByValue(arr, val) {
        for(var i=0; i<arr.length; i++) {
            if(arr[i].id == val) {
                arr.splice(i, 1);
                break;
            }
        }
    }

    function checkHasSelected(user){
        for(var i=0;i<selectedUserList.length;i++){
            var sUser = selectedUserList[i];
            if(sUser.id === user.id){
                return true;
            }
        }
        return false;
    }

    var nodeClick = function (event, treeId, treeNode) {
        var loading = parent.layer.load(1, {
            shade: [0.3,'#000'] //0.1透明度的白色背景
        });
        $('input[name="corpId"]').val(treeNode.id);
        $.get(basePath + "/sys/user/listByCorpId",{corpId:treeNode.id},function(nodes){
            setTimeout(function(){
                parent.layer.closeAll('loading');
            },300);
            for(var k in nodes){
                nodes[k]['corpId'] = treeNode.id;
                nodes[k]['corpName'] = treeNode.name;
            }
            preSelectUserList = nodes;
            renderPreSelectUserList();
        },"JSON");
    }
    var setting = {
        data:{simpleData: {enable: true,idKey: "id",pIdKey: "pId"}},
        callback: {
            onClick: nodeClick
        }
    }

    $.get(basePath + "/sys/corp/tree",function(nodes){
        for(var k in nodes){
            nodes[k]['name'] = "(" + nodes[k]['code'] + ") " + nodes[k]['name'];
        }
        var ztree = $.fn.zTree.init($("#corpTree"), setting, nodes);
        ztree.expandAll(true);
    },"JSON");

    $("#roleSubmit").click(function(){
        var temp = [];
        for(var k in selectedUserList){
            var user = selectedUserList[k];
            temp.push(user.id);
        }
        if(temp.length === 0){
            parent.layer.alert(bt.lang('common.button.selectnum'),{icon:2});
            return;
        }
        bt.loading.show();
        $.post(basePath + "/sys/user/copyRoles",{ids:temp,userId:sourceUser}, function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });

});
