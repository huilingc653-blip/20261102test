
// 一级菜单点击事件，用于将二级三级菜单弹出来
var menuHoverTimeOut = null
var isClickOpen = 'is-click-open'; // 点击打开标识的className
var parentActive = 'parent-active'; // 一级菜单标识打开样式
var childOpen = 'child-open'

bt.use(['layer'], function () {
  var layer = bt.layer, $ = bt.jquery;
  var $menuLayer = $('#menuLayer')
  var $childMenu = $('#childMenu')
  // 一级菜单点击事件
  $('#sideMenuNav > li').on('click',function (e) {
    e.stopPropagation()
    // 添加一个点击打开菜单标识
    $menuLayer.addClass(isClickOpen)

    // 弹出二级菜单
    openMenu2Layer($(this))
    // 注册二级菜单的hover事件
    hoverMenuParentInMenu2()
  })
  // 一级菜单hover事件
  $('#sideMenuNav > li').hover(function (e) {
    // 清除弹框定时器
    clearMenuLayerTimeout()
    if(!$('#menuLayer').hasClass(isClickOpen)){
      openMenu2Layer($(this))
      // 注册二级菜单的hover事件
      hoverMenuParentInMenu2()
    }
  },function () {
    if(!$('#menuLayer').hasClass(isClickOpen)){
      menuHoverTimeOut = setTimeout(function () {
        $('#menuLayer').removeClass(childOpen).hide()
        updateTopParentMenuCss()
      },3000)
    }
  })

  //点击二三级菜单弹框事件
  $('#menuLayer').click(function (e) {
    $childMenu.removeClass(isClickOpen).removeClass('active').hide()
    $menuLayer.removeClass(childOpen).find('.active').removeClass('active')
    e.stopPropagation()
  })
  //二三级菜单弹框hover事件
  $menuLayer.hover(function (e) {
    e.stopPropagation()
    clearMenuLayerTimeout()
  },function (e) {
    e.stopPropagation()
    var $ele = $(e.toElement)
    if( !$ele.parents('.child-menu-layer')[0]
        && !$ele.find('.navtab-tips-text')[0]
        && !$ele.hasClass('navtab-tips-text')
        && !$ele.siblings('.navtab-tips-text')[0]){
      if(!$menuLayer.hasClass(isClickOpen) && !$ele.hasClass('child-menu-layer')){
        $childMenu.removeClass(isClickOpen).hide();
        $menuLayer.removeClass(childOpen).removeClass(isClickOpen).hide()
        updateTopParentMenuCss()
      }
    }
  })
  // 点击二三级弹框菜单的有父级的菜单
  $menuLayer.on('click','.menu-parent',function (e) {
    $childMenu.addClass(isClickOpen)
      if(!$(this).hasClass('active')){
        openMenu4Layer($(this))
      }
    e.stopPropagation()
  })
  //点击四五级菜单
  $childMenu.click(function (e) {
    e.stopPropagation()
  })

  //四五级菜单弹框hover事件
  $childMenu.hover(function (e) {
    e.stopPropagation()
    clearMenuLayerTimeout()
    // $('#menuLayer').show();

  },function (e) {
    var $ele = $(e.toElement)
    e.stopPropagation()
    if(!$ele.find('.navtab-tips-text')[0] && !$ele.hasClass('navtab-tips-text') && !$ele.siblings('.navtab-tips-text')[0]){
      if(!$childMenu.hasClass(isClickOpen)){
        $childMenu.hide();
        // 恢复滚动条
        $menuLayer.removeClass(childOpen)
        $menuLayer.find('.active').removeClass('active')
        if(!$(e.toElement).parents('.menu-layer')[0]){
          $menuLayer.removeClass(isClickOpen).hide()
          updateTopParentMenuCss()
        }
      }
    }
  })
  // 根据肤色判断tips的参数
  var tips,tipsBgc = '#fff';
  if($('body').hasClass('dark-skin')){
    tipsBgc = '#25262B'
  }

  // 菜单显示不全时，启动tips显示内容
  $menuLayer.on('mouseenter','.menu-name',function(){
      createMenuTips($(this))
      if(!$(this).parent('div').hasClass('menu-parent')){
        if(!$childMenu.hasClass(isClickOpen)){
          $childMenu.hide();
          $('.menu-layer').find('.active').removeClass('active')
        }
      }
  })
  // 关闭tips
  $menuLayer.on('mouseleave','.menu-name',function(){
    layer.close(tips)
  })
  // 显示tips
  $childMenu.on('mouseenter','.menu-name',function(){
      createMenuTips($(this))
  })
  // 关闭tips
  $childMenu.on('mouseleave','.menu-name',function(){
    layer.close(tips)
  })

  /**
   * 生成菜单显示的tips
   * */
  function createMenuTips(elem) {
    var title = elem.attr('name')
    if(title && title.length > 10){
      tips = layer.tips('<span class="navtab-tips-text">'+title+'</span>',elem.parent(),{tips:[3,tipsBgc]})
    }
  }

  /**
   * 二级菜单中注册hover事件
   */
  function hoverMenuParentInMenu2(){
    $menuLayer.find('.menu-parent .menu-name').unbind('mouseenter').unbind('mouseleave')
    $menuLayer.find('.menu-parent .menu-name').hover(function (e) {
      e.stopPropagation()
      if(!$('.child-menu-layer').hasClass(isClickOpen)){
        openMenu4Layer($(this).parent())
      }

    },function (e) {
      // 离开时触发
      if(!$('.child-menu-layer').hasClass(isClickOpen)){
        var $ele = $(e.relatedTarget)
        if(!$ele.hasClass('child-menu-layer')){
          if(!$ele.hasClass('child-menu-wrapper')){
            if(!$ele.parents('.child-menu-layer')[0]){
              $menuLayer.removeClass(childOpen).find('.active').removeClass('active')
              $childMenu.removeClass(isClickOpen).hide();
            }
          }
        }
      }
      e.stopPropagation()
    })
  }
  /**
   * 生成四五级弹框
   * */
  function openMenu4Layer(elem) {
      $('.child-menu-layer').hide();
      $('.menu-layer').find('.active').removeClass('active')
      elem.addClass('active')
      $childMenu.html('<div class="child-menu-wrapper">'+elem.find('> span > .menu-child').html()+'</div>')
      // menuLayerOffset($('#childMenu'), $(this).find('.byicon_15'), 6, 16)
      // menuLayer2Offset($('#childMenu'), $(this).find('.byicon_15'), 6, 4)
      menuLayer2Offset($childMenu, elem, 6, 4)
      $childMenu.show();
      $menuLayer.addClass(childOpen)
  }
  /**
   * 二级菜单打开方法
   * @param
   * elem jq对象
   * clickFlag 点击标识
   * */
  function openMenu2Layer(elem) {
// 如果本身已经展开了菜单，将不做处理
    if(!elem.hasClass(parentActive) || $menuLayer.css('display') !== 'block'){
      // 删除兄弟元素的打开样式
      elem.siblings().removeClass(parentActive)
      // 添加自身选中样式
      elem.addClass(parentActive)
      // 关闭查询窗口
      $('#searchResult').hide();
      // 关闭四五级菜单窗口
      $childMenu.removeClass(isClickOpen).hide()

      $menuLayer.removeClass(childOpen).hide()
      var childMenus = elem.find('> .menu-2').html()
      $('#menuLayer > .menu-wrapper').html(childMenus)
      $menuLayer.show()
      menuLayerOffset($menuLayer,elem, 1, 38)
      $('.menu-wrapper').scroll(function () {
        if($('.child-menu-layer')[0]){
          $('.child-menu-layer').hide();
        }
      })
    }
  }


  /**
   * 弹框菜单定位信息
   * @param
   * layerElem  弹框jq对象
   * follow 弹框要依附的元索
   * */
  function menuLayerOffset(layerElem, follow, topGap,leftGap){
    var layerArea = [ layerElem.outerWidth(),layerElem.outerHeight()]
    var goal = {
      width: follow.outerWidth(),
      height: follow.outerHeight(),
      top: follow.offset().top - topGap,
      left: follow.offset().left + follow.outerWidth() + leftGap
    }

    var winHeight = $(window).height()
    var winWidth = $(window).width()
    var heightGap = winHeight - goal.top - layerArea[1]
    var widthGap = winWidth - goal.left - layerArea[0]

    if( heightGap < 8 ){
      goal.top = goal.top - Math.abs(heightGap) - 8
    }
    if( widthGap < 8 ){
      goal.left = follow.parent().offset().left
      goal.top = goal.top + goal.height + 16
    }

    layerElem.css({
      left: goal.left,
      top: goal.top
    })
  }

  /**
   * 第二个弹框菜单定位信息
   * @param
   * layerElem  弹框jq对象
   * follow 弹框要依附的元索
   * */
  function menuLayer2Offset(layerElem, follow, topGap,leftGap){
    var layerArea = [ layerElem.outerWidth(),layerElem.outerHeight()]
    var goal = {
      width: follow.outerWidth(),
      height: follow.outerHeight(),
      // top: follow.offset().top - layerArea[1] + follow.outerHeight() + topGap,
      //默认朝下 4px为间距
      top:follow.offset().top + 26,
      left: follow.offset().left
    }
    var winWidth = $(window).width()
    var winHeight = $(window).height()
    var widthGap = winWidth - goal.left - layerArea[0]

    var cssObj = {
      left:goal.left,
      top: goal.top,
    }
    // 朝下时，如果top + 弹框高度 > 视口高度 - 20 就朝上
    if( (goal.top + layerArea[1]) > (winHeight - 20)){
      cssObj.top = follow.offset().top - layerArea[1]
      cssObj.paddingTop = '0'
      cssObj.paddingBottom = '4px'
    }else{
      cssObj.paddingBottom = '0'
      cssObj.paddingTop = '4px'
    }
    if( widthGap < 20 ){
      cssObj.left = winWidth - 20
    }

    layerElem.css(cssObj)
  }
  /**
   * 清除弹框定时器
   * */
  function clearMenuLayerTimeout() {
    if(menuHoverTimeOut){
      clearTimeout(menuHoverTimeOut)
      menuHoverTimeOut = null
    }
  }

})
// 更新顶级菜单选样式
function updateTopParentMenuCss() {
  var selectLi = $('#navtabContainer').find('>li.layui-this').find('.navtab-name')
  var topParentId = selectLi.data('topparentid')
  $('#sideMenuNav').find('.parent-active').removeClass('parent-active')
  $('.mini-parent-active').removeClass('mini-parent-active')
  // 页签字与菜单联动，即当前页面对应的一级菜单名字
  if(topParentId){
    $('#sideMenuNav').find('#menus'+topParentId).parent('li').addClass('parent-active')
    $('.layui-side-mini').find('[data-id="'+topParentId+'"]').parent('li').addClass('mini-parent-active')
  }
}
