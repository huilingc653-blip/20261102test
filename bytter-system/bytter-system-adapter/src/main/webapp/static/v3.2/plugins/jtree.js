// 依赖jquery,uitool里面的扩展工具

(function($){
    $.extend($.fn, {
        jTree:function(options) {
            var op = $.extend({checkFn:null, selected:"selected", exp:"expandable", coll:"collapsable", firstExp:"first_expandable",
                firstColl:"first_collapsable", lastExp:"last_expandable", lastColl:"last_collapsable", folderExp:"folder_expandable",
                folderColl:"folder_collapsable", endExp:"end_expandable", endColl:"end_collapsable",file:"file",ck:"checked",
                unck:"unchecked"}, options);
            return this.each(function(){
                var $this = $(this);
                var $thisclone=$this.clone();
                $thisclone.attr("id",$this.attr("id")+"_clone").removeClass().hide().insertAfter($this);
                var cnum = $this.children().length;
                $(">li", $this).each(function(){
                    var $li = $(this);
                    var first = $li.prev()[0]?false:true;
                    var last = $li.next()[0]?false:true;
                    $li.genTree({
                        icon:$this.hasClass("treeFolder"),
                        ckbox:$this.hasClass("treeCheck"),
                        options: op,
                        level: 0,
                        exp:(cnum>1?(first?op.firstExp:(last?op.lastExp:op.exp)):op.endExp),
                        coll:(cnum>1?(first?op.firstColl:(last?op.lastColl:op.coll)):op.endColl),
                        showSub:(!$this.hasClass("collapse") && ($this.hasClass("expand") || (cnum>1?(first?true:false):true))),
                        isLast:(cnum>1?(last?true:false):true),
                        elem:op.elem
                    });
                });
                clearTimeout(temptree);
                var temptree = setTimeout(function(){
                    if($this.hasClass("treeCheck")){
                        var checkFn = eval($this.attr("oncheck"));
                        if(checkFn && $.isFunction(checkFn)) {
                            $("div.ckbox", $this).each(function(){
                                var ckbox = $(this);
                                ckbox.click(function(){
                                    var checked = $(ckbox).hasClass("checked");
                                    var items = [];
                                    if(checked){
                                        var tnode = $(ckbox).parent().parent();
                                        var boxes = $("input", tnode);
                                        if(boxes.size() > 1) {
                                            $(boxes).each(function(){
                                                items[items.length] = {name:$(this).attr("name"), value:$(this).val(), text:$(this).attr("text")};
                                            });
                                        } else {
                                            items = {name:boxes.attr("name"), value:boxes.val(), text:boxes.attr("text")};
                                        }
                                    }
                                    checkFn({checked:checked, items:items});
                                });
                            });
                        }
                    }
                    $("a", $this).click(function(event){
                        $("div." + op.selected).removeClass(op.selected);
                        var parent = $(this).parent().addClass(op.selected);
                        $(".ckbox",parent).trigger("click");
                        if(event.target.className && event.target.className.indexOf('bt-menu-collect') == -1 ){
                            event.stopPropagation();
                        }
                        $(document).trigger("click");
                        if(event.target.className && event.target.className.indexOf('bt-menu-collect') == -1 ){
                            if (!$(this).attr("target")) return false;
                        }

                    });
                },1);
            });
        },
        subTree:function(op, level) {
            return this.each(function(){
                $(">li", this).each(function(){
                    var $this = $(this);
                    var isLast = ($this.next()[0]?false:true);
                    $this.genTree({
                        icon:op.icon,
                        ckbox:op.ckbox,
                        exp:isLast?op.options.lastExp:op.options.exp,
                        coll:isLast?op.options.lastColl:op.options.coll,
                        options:op.options,
                        level:level,
                        space:isLast?null:op.space,
                        showSub:op.showSub,
                        isLast:isLast
                    });

                });
            });
        },
        colExpAll:function(options){
            var op = $.extend({Highlight:true,clickType:'colExp',colBtn:"collapseBtn",expBtn:"expandBtn",input:"searchTree",searchBtn:"searchTreeBtn",closeBtn:"searchTreeClose",checkFn:null, selected:"selected", exp:"expandable", coll:"collapsable", firstExp:"first_expandable", firstColl:"first_collapsable", lastExp:"last_expandable", lastColl:"last_collapsable", folderExp:"folder_expandable", folderColl:"folder_collapsable", endExp:"end_expandable", endColl:"end_collapsable",file:"file",ck:"checked", unck:"unchecked"}, options);
            return this.each(function(){
                var $this = $(this);
                var filterVal=$("#"+op.input).val();
                var filterArea=$("#"+$this.attr("id")+"_clone"),
                    filterAreaHtml=filterArea.html();
                var filterHtml=null;
                /*查找树上是否有包含输入框的值，过滤不不含的选项[规则，移除当前a的父LI，如果a的同级后节点是UL则不移除]*/
                var tempArea=filterArea.clone();
                addFilterHtml(tempArea);
                filterHtml=tempArea.html();
                switch(op.clickType){
                    case"search":
                        //op.colExp=true;
                        if ($.trim(filterVal) ==''){
                            op.colExp=false;
                        } else {
                            op.colExp=true;
                        }
                        break;
                    case"colAll":
                        op.colExp=true;
                        break;

                    case"close":
                        op.colExp=true;
                        filterHtml=filterAreaHtml;
                        break;
                    case"expAll":

                        op.colExp=false;
                        break;
                    default:
                        break;
                }


                function addFilterHtml(ul){
                    var $li=ul.children("li");
                    $li.each(function(){
                        var $tli=$(this);
                        if($tli.children("a").text().indexOf(filterVal)==-1){
                        // if($tli.children("a").text().indexOf(filterVal)==-1&&$tli.children("a").attr("autobypy").indexOf(filterVal)==-1){
                            if($tli.children("ul").length>0){
                                var tul=$tli.children("ul");
                                addFilterHtml(tul);
                            }
                            else{$tli.remove();}
                        }else{
                            if(op.Highlight){
                                var menuNameElem = $tli.children("a").find('.menu-name');
                                if($tli.children("a").text().indexOf(filterVal)!=-1){
                                    menuNameElem.html(menuNameElem.text().replace(filterVal,"<b class='color-red'>"+filterVal+"</b>"));
                                    // $tli.children("a").html($tli.children("a").text().replace(filterVal,"<b class='color-red'>"+filterVal+"</b>"));
                                }else{
                                    // var temptext=$tli.children("a").text().slice($tli.children("a").attr("autobypy").indexOf(filterVal),$tli.children("a").attr("autobypy").indexOf(filterVal)+filterVal.length);
                                    // $tli.children("a").html($tli.children("a").text().replace(temptext,"<b class='color-red'>"+temptext+"</b>"))
                                    // menuNameElem.html(menuNameElem.text().replace(temptext,"<b class='color-red'>"+temptext+"</b>"))
                                }
                                if($tli.children("ul").length>0){
                                    var tul=$tli.children("ul");
                                    addFilterHtml(tul);
                                }
                            }
                        }
                    });
                    if(ul.children("li").length==0){
                        if (ul.parent("li").attr('level') == '0' && ul.parent('li').find('.color-red').length ){

                        }else {
                             // ul.parent("li").remove();
                            if (ul.parent('li').find('.color-red').length){ //20200713根据测试的要求 ，如果搜索结果无子菜单时，也让它显示出来
                                ul.remove();
                            }else {
                                ul.parent("li").remove()
                            }
                        }

                    };
                };

                $this.empty().html(filterHtml);
                var cnum = $this.children().length;
                $(">li", $this).each(function(){
                    var $li = $(this);
                    var first = $li.prev()[0]?false:true;
                    var last = $li.next()[0]?false:true;
                    $li.genTree({
                        icon:$this.hasClass("treeFolder"),
                        ckbox:$this.hasClass("treeCheck"),
                        options: op,
                        level: 0,
                        exp:(cnum>1?(first?op.firstExp:(last?op.lastExp:op.exp)):op.endExp),
                        coll:(cnum>1?(first?op.firstColl:(last?op.lastColl:op.coll)):op.endColl),
                        showSub:op.colExp,
                        isLast:(cnum>1?(last?true:false):true)
                    });
                    $("a[target=navTab]", $li).each(function(){
                        var thisGroupA=$(this);
                        thisGroupA.click(function(event){
                            var $thisa = $(this);
                            var title = $thisa.attr("title") || $thisa.text();
                            var tabid = $thisa.attr("rel") || "_blank";
                            var fresh = eval($thisa.attr("fresh") || "false");
                            var external = eval($thisa.attr("external") || "false");
                            var url = unescape($thisa.attr("href")).replaceTmById($(event.target).parents(".unitBox:first"));
                            navTab.openTab(tabid, url,{title:title, fresh:fresh, external:external});
                            stopDefault(event);
                        });
                    });

                });
                delete tempArea;

            });
        },
        genTree:function(options) {
            var op = $.extend({icon:options.icon,ckbox:options.ckbox,exp:"", coll:"", showSub:false, level:0, options:null, isLast:false}, options);
            op.elem ="sideMenuNav";
            return this.each(function(){
                var node = $(this);
                var tree = $(">ul", node);
                var parent = node.parent().prev();
                var checked = 'unchecked';
                if(op.ckbox) {
                    if($(">.checked",parent).size() > 0) checked = 'checked';
                }
                if (tree.size()>0) {
                    node.children(":first").wrap("<div></div>");
                    $(">li:last",tree).addClass("last"+op.level);
                    $(">div", node).prepend("<div class='" + (op.showSub ? op.coll : op.exp) + "'></div>"+(op.ckbox ?"<div class='ckbox " + checked + "'></div>":"")+(op.icon?"<div class='"+ (op.showSub ? op.options.folderColl : op.options.folderExp) +"'></div>":""));
                    op.showSub ? tree.show() : tree.hide();
                    $(">div>div,>div>a", node).click(function(){//$(">div>div:first
                        var $fnode = $(">li:first",tree);
                        if($fnode.children(":first").isTag('a')) tree.subTree(op, op.level + 1);
                        var $this = $(this);
                        var isA = $this.isTag('a')||$this.isTag('div');
                        var $this = isA?$(">div>div", node).eq(op.level):$this;
                        var fontElem = $(this).parent().siblings('.bt-nav-more');
                        if (!fontElem.length){
                            fontElem = $(this).find('.bt-nav-more');
                        }
                        if (tree.is(":hidden")) {
                            $this.removeClass(op.exp).addClass(op.coll);
                            fontElem.find('.font_family').addClass('icon-xiangxiazhankai_zuodaohang').removeClass('icon-zhankai_zuodaohang')
                            if (op.icon) {
                                $(">div>div:last", node).removeClass(op.options.folderExp).addClass(op.options.folderColl);
                            }
                            $(this).addClass('parent-active');
                        }else{
                            fontElem.find('.font_family').removeClass('icon-xiangxiazhankai_zuodaohang').addClass('icon-zhankai_zuodaohang')
                            $this.removeClass(op.coll).addClass(op.exp);

                            if (op.icon) {
                                $(">div>div:last", node).removeClass(op.options.folderColl).addClass(op.options.folderExp);
                            }
                            $(this).removeClass('parent-active');
                        }
                        //(tree.is(":hidden"))?tree.slideDown("fast"):tree.slideUp("fast");//(tree.is(":hidden"))?tree.slideDown("fast"):(isA?"":tree.slideUp("fast"));
                        // (tree.is(":hidden"))?tree.show():tree.hide();

                        if (tree.is(":hidden")){
                            tree.addClass('tree-show');
                            var showNodes = tree.parent('li').siblings('li').find('.parent-active');
                            tree.parent('li').siblings('li').find('.tree-show').removeClass('tree-show');
                            showNodes.removeClass('parent-active');
                            showNodes.parent('div').find('.bt-nav-more').find('.font_family')
                                .removeClass('icon-xiangxiazhankai_zuodaohang').addClass('icon-zhankai_zuodaohang')

                        } else {
                            tree.removeClass('tree-show')
                        }

                        return false;
                    });

                    addSpace(op.level, node);
                    if(op.showSub) tree.subTree(op, op.level + 1);
                } else {
                    node.children().wrap("<div></div>");
                    $(">div", node).prepend("<div class='node'></div>"+(op.ckbox?"<div class='ckbox "+checked+"'></div>":"")+(op.icon?"<div class='file'></div>":""));
                    addSpace(op.level, node);
                    if(op.isLast)$(node).addClass("last");
                }
                //注册添加选项卡事件
                tree.find('[data-type="tabAdd"]').off("click").click(function(e){
                    if(e.target.className && e.target.className.indexOf('bt-menu-collect') !== -1 ){
                        return;
                    }
                    var elem = $(this);
                    var title   = elem.find('.menu-name').text().trim();                              // 导航栏text
                    var src     = elem.attr('href-url');      // 导航栏跳转URL
                    var name    = elem.attr('name');      // name属性
                    var top     = true;//新增加top参数，代表选项卡为顶级选项卡，无父级
                    if(src == "#"){
                        return;
                    }
                    title = title?title:name;
                    var id = elem.data('id');
                    var contact = '?';
                    if(src.indexOf("?")>-1){
                        contact = '&';
                    }
                    src = src + contact + "menuId=" + id;

                    if($('body').data('key') === 'newUI'){
                        // 如果是首次新增页签时，需要调整高度
                        if(!$('#menusTabCard').hasClass('is-add-menu')){
                            $('#menusTabCard').addClass('is-add-menu')
                            var height = $(window).height() - $('.layout-header').height() - 32
                            $('#tab-content').height(height);
                            //设置首页工作台常用菜单父容器的高度
                           // var cardHeight = height - $('#tabsHeader').height() - 20

                           // $('#tabsContent').height(cardHeight)

                        }
                    }


                    /**
                     * 判断 #navtabContainer 中是否存在 li 的 url 属性（去除查询参数后）与给定 url 的路径部分相等
                     * @param {string} targetUrl - 要对比的完整 URL 或路径，例如 "/sys/loginLog/index?menuId=123"
                     * @returns {boolean} 是否存在匹配项
                     */
                    function isUrlMatchCurrentPath(targetUrl) {
                        // 提取目标 URL 的路径部分（去掉 ? 及之后的内容）
                        var targetPath = targetUrl ? targetUrl.split('?')[0] : '';

                        var matched = false;
                        $('#navtabContainer>li').each(function() {
                            var url = $(this).attr('url');
                            if (url) {
                                var pathOnly = url.split('?')[0];
                                if (pathOnly === targetPath) {
                                    matched = true;
                                    return false; // 退出 each 循环
                                }
                            }
                        });

                        return matched;
                    }

                    //if(!isUrlMatchCurrentPath(src)){
                        //解决: 如果第二层iframe有相同name时,获取三级iframe对象会出现问题
                        bt.navtab.addNavtab(title,src,name+parseInt(Math.random()*1000000, 10),top);//新增加top参数，代表菜单为顶级菜单，无父级
                    //}


                    if (elem.find('.color-red')[0]){
                        var id = elem.data("id");
                        $("#searchTree").val("");
                        $('ul.tree').colExpAll({clickType:'search'})
                        var targetMenu = $("#sideMenuNav").find('[data-id="'+id+'"]').addClass("active");
                        targetMenu.parents('ul').parent("li").find(">a,>div>a").trigger("click");
                    } else {
                        $("#sideMenuNav .active").removeClass("active");
                        $("#sideMenuNav .parent-active").removeClass("parent-active");
                        elem.addClass("active")
                        $("#sideMenuNav .tree-show").siblings('div').find('a').addClass("parent-active");
                    }
                })

                if (op.ckbox) node._check(op);
                $(">div",node).mouseover(function(){
                    $(this).addClass("hover");
                }).mouseout(function(){
                    $(this).removeClass("hover");
                });
                if($.browser.msie)
                    $(">div",node).click(function(){
                        $("a", this).trigger("click");
                        return false;
                    });
            });
            function addSpace(level,node) {
                if (level > 0) {
                    var parent = node.parent().parent();
                    var space = !parent.next()[0]?"indent":"line";
                    var plist = "<div class='" + space + "'></div>";
                    if (level > 1) {
                        var next = $(">div>div", parent).filter(":first");
                        var prev = "";
                        while(level > 1){
                            prev = prev + "<div class='" + next.attr("class") + "'></div>";
                            next = next.next();
                            level--;
                        }
                        plist = prev + plist;
                    }
                    $(">div", node).prepend(plist);
                }
            }
        },
        _check:function(op) {
            var node = $(this);
            var ckbox = $(">div>.ckbox", node);
            var $input = node.find("a");
            var tname = $input.attr("tname"), tvalue = $input.attr("tvalue");
            var attrs = "text='"+$input.text()+"' ";
            if (tname) attrs += "name='"+tname+"' ";
            if (tvalue) attrs += "value='"+tvalue+"' ";

            ckbox.append("<input type='checkbox' style='display:none;' " + attrs + "/>").click(function(){
                var cked = ckbox.hasClass("checked");
                var aClass = cked?"unchecked":"checked";
                var rClass = cked?"checked":"unchecked";
                ckbox.removeClass(rClass).removeClass(!cked?"indeterminate":"").addClass(aClass);
                $("input", ckbox).attr("checked", !cked);
                $(">ul", node).find("li").each(function(){
                    var box = $("div.ckbox", this);
                    box.removeClass(rClass).removeClass(!cked?"indeterminate":"").addClass(aClass)
                        .find("input").attr("checked", !cked);
                });
                $(node)._checkParent();
                return false;
            });
            var cAttr = $input.attr("checked") || false;
            if (cAttr) {
                ckbox.find("input").attr("checked", true);
                ckbox.removeClass("unchecked").addClass("checked");
                $(node)._checkParent();
            }
        },
        _checkParent:function(){
            if($(this).parent().hasClass("tree")) return;
            var parent = $(this).parent().parent();
            var stree = $(">ul", parent);
            var ckbox = stree.find(">li>a").size()+stree.find("div.ckbox").size();
            var ckboxed = stree.find("div.checked").size();
            var aClass = (ckboxed==ckbox?"checked":(ckboxed!=0?"indeterminate":"unchecked"));
            var rClass = (ckboxed==ckbox?"indeterminate":(ckboxed!=0?"checked":"indeterminate"));
            $(">div>.ckbox", parent).removeClass("unchecked").removeClass("checked").removeClass(rClass).addClass(aClass);
            parent._checkParent();
        }
    });
})(jQuery);
