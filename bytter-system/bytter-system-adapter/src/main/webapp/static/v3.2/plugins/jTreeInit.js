$(function(){
    //初始化左边页面,现在需要在页面中调用jtree_init
    //initleftside();

    //调用jtree
   jtree_init();
    //
    $("#searchTreeBtn").bind("click",function(){
        if($("#searchTree").val()!=""){
            $("#searchTreeClose").show();
        }
        $('ul.tree').colExpAll({clickType:'search'});
    });
    // $("#searchTree").bind("keydown",function(e){
    //     if(e.keyCode==13){
    //         if($("#searchTree").val()!=""){$("#searchTreeClose").show();}else{$("#searchTreeClose").hide();};
    //         $('ul.tree').colExpAll({clickType:'search'})
    //     }
    // });

    $("#searchTree").bind("input propertychange",function(e){
        if ($(".layui-side-scroll").css("display") == 'none'){
            $(".menu-toggle-btn").trigger("click");
        }
        if($("#searchTree").val()!=""){$("#searchTreeClose").show();}else{$("#searchTreeClose").hide();};
        $('ul.tree').colExpAll({clickType:'search'})

    });

    $("#searchTreeClose").bind("click",function(){
        $("#searchTreeClose").hide();
        $("#searchTree").val("");
        $('ul.tree').colExpAll({clickType:'close'});
    });

})

function jtree_init(){
    var $p = $(document);
    $("ul.tree", $p).jTree();
    // $('ul.tree').colExpAll({clickType:'expAll'});
    // $('ul.tree').colExpAll({clickType:'close'});
    //添加收藏功能
    $('#sideMenuNav').on('click','.bt-menu-collect',function (event,data) {
        var icon = $(this);
        var menuId = ''
        // 新ui key
        var newUIKey = $('body').data('key')
        if(newUIKey){
            menuId = icon.parents('a').data("id");
        }else {
            menuId = icon.parent().parent().parent('li').find('a').data("id");
        }
        if (!menuId) {
            menuId = data;
            if (!menuId){
                return;
            }
        }
        $.get(basePath + '/sys/menuCollect/collect', {menuId: menuId}, function (result) {
            if (result.success) {
                if(newUIKey){
                    // 新版UI
                    if (icon.hasClass('byicon_collection')) {
                        icon.removeClass('byicon_collection').addClass('byicon_collection_full');
                        // 添加收藏成功
                        layer.msg(bt.lang('sys.index.addCollectMenu.success'));
                    } else {
                        icon.removeClass('byicon_collection_full').addClass('byicon_collection');
                        // 取消收藏成功
                        layer.msg(bt.lang('sys.index.deleteCollectMenu.failure'));
                    }
                }else {
                    // 旧版UI
                    if (icon.hasClass('icon-xingxing_kongxin')) {
                        icon.removeClass('icon-xingxing_kongxin').addClass('icon-xingxing');
                        // 添加收藏成功
                        layer.msg(bt.lang('sys.index.addCollectMenu.success'));
                    } else {
                        icon.removeClass('icon-xingxing').addClass('icon-xingxing_kongxin');
                        // 取消收藏成功
                        layer.msg(bt.lang('sys.index.deleteCollectMenu.failure'));
                    }
                }

                $.ajax({
                    type:"post",
                    url:basePath + "/sys/menuCollect/widget",
                    dataType: "html",
            		contentType: "text/html",
                    // data:JSON.stringify(data2),
                    success:function(result) {
                        $("#commonMenus").html(result);
                        var newUIkey = $('#indexBody').data('key')
                        if(newUIkey === 'newUI'){
                            bt.use(['element'],function () {
                                var $ = bt.jquery;
                                bt.element().init()
                                if(hoverCommonMenus){
                                    hoverCommonMenus()
                                }
                            })
                        }else{
                            $("#commonMenus").dad({
                                draggable:'.icon-caidan',
                                callback:function (elem) {
                                    var commonMenus = [];
                                    bt.each($("#commonMenus").children("div"),function () {
                                        if (!$(this).hasClass('dads-children-clone')){
                                            var obj = {},$menuElem = $(this).find(".content-body-menu");
                                            obj.id = $menuElem.attr('mc-id');
                                            obj.menuId = $menuElem.data('id');
                                            obj.sort = $(this).data('dad-position');
                                            commonMenus.push(obj)
                                        }
                                    })
                                    $.post(basePath + "/sys/menuCollect/dragSaveMenuWidget", {menuCollectList:commonMenus}, function (result) {
                                    }, 'JSON');
                                }
                            })
                        }
                    }
                });
            }
        }, 'JSON');
    });
    $('#sideMenuNav').on('click','.bt-nav-next',function (event,data) {
        $(this).parent().siblings('div').find('a').trigger('click');
    })
}

$.fn.extend({
    size:function(){
        return this.length;
    }
})

