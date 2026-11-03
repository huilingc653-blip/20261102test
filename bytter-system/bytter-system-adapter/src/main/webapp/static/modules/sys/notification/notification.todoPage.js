
bt.use(['jquery'], function(){
    var $ = bt.jquery;
    $(".category-link a").click(function(){
        var url = basePath + $(this).data("url");
        var contact = '?';
        if(url.indexOf("?")>-1){
            contact = '&';
        }
        url = url + contact + "menuId=" + $(this).data("id");
        if(!url){
            return;
        }
        var title = $(this).text();
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('sys.notification.todo')+'-'+title,url);
        }
    });

});
