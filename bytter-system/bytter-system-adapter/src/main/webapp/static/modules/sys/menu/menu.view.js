
bt.use(['form', 'layedit', 'laydate','jquery','selectTree'], function(){
    var form = bt.form(),$  = bt.jquery;

    bt.selectTree.init({
        url:basePath + "/sys/menu/tree",
        elem:".menu-parentId",
        name:'parentId',
        selected:$("#parentId").val(),
        currentNode:$("#currentNode").val()
    });


    //自定义验证规则
    form.verify({
        sortVerify:function(value){
            var pattern = /^-?\d{0,5}$/;
            if(value && !pattern.test(value)){
                // 排序值为正整数，并且不能大于5位数！
                return bt.lang('sys.menu.sort.show');
            }
        },
        name: function(value){
            if(value.length < 2){
                // 菜单名称至少得2个字符！
                return bt.lang('sys.menu.name.minLength');
            }
            if(value.length>30){
                // 菜单名称不能大于30个字符！
                return bt.lang('sys.menu.name.maxLength');
            }
        },
        code:function(value){
            if(value && value.length > 30){
                // 代码编号不能大于30个字符！
                return bt.lang('sys.menu.codeNumber.maxLength');
            }
        },
        url:function(value){
            if(value && value.length > 255){
                // URL不能大于255个字符！
                return 'URL不能大于255个字符！';
            }
        },
        icon:function(value){
            if(value && value.length > 50){
                // 图标不能大于50个字符！
                return '图标不能大于50个字符！';
            }
        },
        description:function(value){
            if(value && value.length > 200){
                // 菜单描述不能大于200个字符！
                return '菜单描述不能大于200个字符！';
            }
        }
    });

    //监听提交
    form.on('submit(menuSubmit)', function(data){
        if(typeof(data.field.status) == "undefined"){
            data.field.status = 0;
        }else{
            data.field.status = 1;
        }
        if(typeof(data.field.isModule)=="undefined"){
            data.field.isModule = 0;
        }else{
            data.field.isModule = 1;
        }
        if(typeof(data.field.isExtend)=="undefined"){
            data.field.isExtend = 0;
        }else{
            data.field.isExtend = 1;
        }
        var action = $('input[name="id"]').val()?"update":"save";
        $.post(basePath + "/sys/menu/"+action,data.field,function(result){
            if(result.success){
                parent.layer.closeAll();
                window.top.refreshParentFlag = true; // 刷新父页面标识
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });
});
