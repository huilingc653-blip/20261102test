+function ($) {
    'use strict';
    $('[data-toggle="i18n"]').each(function(){
	    var $this = $(this),keyId = $this.data("key-id");
	    if(!keyId){return;}
	    var lang = bt&&bt.lang('sys.lang.language');
	    $this.text(lang||"多语言");
	    $this.addClass("layui-btn layui-btn-primary layui-btn-small");
	});
    $(document).on('click','[data-toggle="i18n"]' , function (e) {
        var $this   = $(this);
        if ($this.is('a')) e.preventDefault();
        var keyId = $this.data("key-id");
        var layer = parent && parent.layer || layer;
        if(!keyId){layer.alert("尚未配置[data-key-id]属性！", {icon: 2});return;}
        var type = $this.data("type")==="base"?"0":"1";
        layer.open({type: 2, area: ['830px', '530px'], content: basePath + "/sys/i18n/view?keyId="+keyId+"&type="+type});
    });
}(jQuery);
var i18nInit=function(){
	$('[data-toggle="i18n"]').each(function(){
	    var $this = $(this),keyId = $this.data("key-id");
	    if(!keyId){return;}
	    var lang = bt&&bt.lang('sys.lang.language');
	    $this.text(lang||"多语言");
	    $this.addClass("layui-btn layui-btn-primary layui-btn-small");
	});
};