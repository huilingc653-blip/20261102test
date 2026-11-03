
bt.use(['jquery','layer'], function(){
    var layer = bt.layer;
    $('.add-btn,.edit-btn').click(function(){
        var that = $(this);
        var id = that.data('id');
        if(!id){id = "";}
        var param = that.hasClass("add-btn")?'parentId':'id';
        top.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/sys/region/view?"+ param +"=" + id,
            end:function(){
                location.reload();
            }
        });
    });

    $('.delete-btn').click(function(){
        var that = $(this);
        var id = that.data('id');
        // 删除后将不可恢复，是否继续？
        top.layer.confirm(bt.lang('sys.region.deleteInfo'),{icon:3},function(){
            $.get(basePath + "/sys/region/delete/" + id, function(result){
                top.layer.alert(result.msg, {icon: result.success?1:2});
                if(result.success){
                    location.reload();
                }
            },'JSON');
        });
    });

    $('.view-btn').click(function () {
        var that = $(this);
        var id = that.data('id');
        if (!id) {
            id = "";
        }
        var onlyViewFlag = "1";
        top.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/sys/region/view?id=" + id + "&onlyViewFlag=" + onlyViewFlag,
        });
    });

});
