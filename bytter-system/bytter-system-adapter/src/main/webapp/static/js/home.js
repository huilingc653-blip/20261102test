
if(parent.bt.navtab){
bt.navtab = parent.bt.navtab;
}

bt.use(['element','jquery','layer','navtab','util'], function(){
	'use strict'
  	var element = bt.element(); //导航的hover效果、二级菜单等功能，需要依赖element模块
  	var $ = bt.jquery;
  	var layer = bt.layer;
  	  
  	//iframe自适应
    $(window).on('resize', function () {
        var $content = $('.admin-nav-card .layui-tab-content');
        $content.height($(this).height() - 119);
        $content.find('iframe').each(function () {
            $(this).height($content.height());
        });
    }).resize();

    //获取页面高度

  	$('.left-nav').find('li').mouseenter(function(){
  	  	var title = $(this).data("title");
  	  	layer.tips(title,$(this).find('i'));
  	})

  	$('.left-nav').on('click','li',function(e){
        e.stopPropagation();
        var url = $(this).data("url");
        var type = $(this).data("type");
        $.ajax({
            type: type,
            url: url,
            dataType: 'json',
            success: function(result, status, xhr) {
                var html = getNavLeftHtml(result);
                $("#navbarLeft").html(html);
            },
        
        });
  		$(this).siblings().removeClass('active');
  		$('.side-box').show();
  	})

    function getNavLeftHtml(result){
        var titleHtml = '<div class="side-top"><span data-id="'+result.id+'">'+result.title+'</span></div>';
        var navBoxHtml ='',html='';
        var data = result.data;
        // console.log(data[0].navData[0].sonData)
        // console.log(data[0].navData[0])
        if(data){
            //生成主菜单
            navBoxHtml += '<ul class="navbarbox" lay-filter="navLeft">';
            bt.each(data,function(k,v){
                navBoxHtml += '<li class="navbar-item '; 
                if(k == 0){
                    navBoxHtml += 'navbar-itemed active" '
                }else{
                    navBoxHtml += '" '
                }
                navBoxHtml += 'data-id = "'+v.navParentId+'"';
                navBoxHtml += '">';
                navBoxHtml += '<a href="javascript:void(0)">'+v.navParentTitle+'<i class="layui-icon">&#xe625;</i></a>';
                
                var navData = v.navData;
                if(navData){
                //     //生成主菜单的内容
                    navBoxHtml += '<dl class="navbar-child">'
                    bt.each(navData,function(j,d){

                        navBoxHtml += '<dd ';
                        if(j == 0){
                            navBoxHtml += 'class="navbar-this"';
                        }
                        navBoxHtml += ' data-url="'+d.parentUrl+'" data-id ="'+d.navParentId+'" ';
                        // console.log(d[j])
                        navBoxHtml += '><a href="javascript:void(0)">'+d.parentTitle;
                        navBoxHtml += '<i class="fa fa-angle-right fa-2x"></i></a></dd>';
                    })
                    navBoxHtml += '</dl>'
                }
                navBoxHtml += '</li>'
            })
            navBoxHtml += '</ul>'

        }

        html += titleHtml + navBoxHtml; 
        getNavChildHtml(data);
        return html;   
    }

    $(document).on("click",function(){
        console.log(111)
        $(".side-box").hide()
    })

    function getNavChildHtml(data){
        var data = data[0];
        var breadHtml ='',childHtml ='';
        breadHtml += '<div class="item-child"><div class="child-title"><span class="layui-breadcrumb active">';
        breadHtml += '<a href="javascript:void(0)">'+data.navParentTitle+'</a>';
        var navData = data.navData;
        if(navData){
            bt.each(navData,function(i,v){
                var sonData = v.sonData;
                if(sonData){
                    breadHtml += '<a href="javascript:void(0)">'+v.parentTitle+'</a>';
                    breadHtml += '<a><cite>'+sonData.navSonTitle+'</cite></a>';
                    breadHtml += '</span></div>';
                    bt.each(sonData,function(j,d){
                        if(typeof d ==="object"){
                           childHtml += '<span ><a href="javascript:void(0)"  href-url="'+d.sonUrl+'" data-type="tabAdd" data-id ="'+d.navsonId+'">'+d.sonTitle+'</a></span>' 
                        }
                    })
                    breadHtml += childHtml;
                    breadHtml += '</div>';
                    $(".navbarbox-item-child").html(breadHtml);
                    element.init()
                }
            })
        }
    }


  	//设置主菜单点击时的展开
  	$('.navbar-parent').on('click','.navbar-item > a',function(e){
        e.stopPropagation();
        console.log(333)
  		var $parent =$(this).parent();
  		$parent.siblings().removeClass('navbar-itemed');
  		$parent.toggleClass('navbar-itemed');
  		if($parent.hasClass('navbar-single')){
  			$parent.siblings().removeClass('active');
  			$parent.addClass('active');
  		}
  	})
  	//设置子菜的选中样式
    $('.navbar-parent').on('click','.navbar-item >.navbar-child dd',function(e){
        e.stopPropagation();
        $(this).parents('.navbar-item').siblings().find('dd').removeClass('navbar-this');
        $(this).parents('.navbar-item').siblings().removeClass('active');
        $(this).parents('.navbar-item').addClass('active');
        $(this).siblings().removeClass('navbar-this');
        $(this).addClass('navbar-this');
    })

    $(".navbar-search").on("input propertychange paste click",function(e){
        e.stopPropagation();
        var searchTxt = $(this).val();
        var $item = $(".navbarbox-item-child");
        console.log($item.find(".item-child > span"))
            $item.find(".item-child > span")
                .hide()
                .filter(":contains('"+searchTxt+"')")
                .show();
    })
    $(".side-top").on("click",function(e){
        e.stopPropagation();
    })

     //初始化tab
    bt.navtab.init();

    var side = $('.navbarbox-item-child');
    var body = $('.my-body');

    // 监听控制content高度
    function init(){
        // 起始判断收缩还是展开
        // if(localStorage.log == 0){
        //     navHide(1);
        // }else{
        //     navShow(1);
        // }
        // 选项卡高度
        var cardTitleHeight = $(document).find(".layui-tab[lay-filter='card'] ul.layui-tab-title").height();
        // 需要减去的高度
        var height = $(window).height() - $('.nav-header').height() - cardTitleHeight;
        // 设置高度
        $(document).find(".layui-tab[lay-filter='card'] div.layui-tab-content").height( height - 2 );
        // $(document).find(".left-nav-part").height( height - 2 );
        // $(document).find('.left-nav-part').height(height + cardTitleHeight);
        $(document).find('.my-body').height(height + cardTitleHeight-2);
    }

    // 自适应
    $(window).on('resize', function() {
        if($(this).width() > 1024){
            if(localStorage.log == 0){
                localStorage.log = 1;
                navShow();
            }
        }else{
            if(localStorage.log == 1){
                localStorage.log = 0;
                navHide();
            }
        }
        init();
    });

    // 初始化
    init();
    // bt.navtab.addNavtab('测试'，'http://www.baidu.com')
    // var navtabs = $(".item-child > span > a");
    // $(".item-child > span").on("click","a",function(e){
    //     e.stopPropagation();
    //     bt.navtab.addNavtabWithId($(this).text(),$(this).data("url"),$(this).attr("id"));

    // })
   
});