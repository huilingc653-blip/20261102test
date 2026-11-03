(function (doc, win) {

    $(".bt-select-popup input").focus(function(){
        document.activeElement.blur();
    });
    //点击浮框的阴影位置，将会关闭弹框
    $('.popup-block .ui-actionsheet').off('click').on('click',function(){
        $(this).removeClass('show');
        var id = $(this).data('link');
        $('#'+id).parent().removeClass('active');
    })
    $('.popup-block .bt-popup-container').off('click').on('click',function(){
        return false
    })
    $('.popup-block .bt-popup-bottom').off('click').on('click',function(){
        return false
    })

    $('.popup-block').on('click','.bt-card-item',function(){
        return false
    })

})(document, window);
