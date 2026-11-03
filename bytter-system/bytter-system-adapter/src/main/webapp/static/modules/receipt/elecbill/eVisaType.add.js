bt.use(['form','layedit','selectTree','select','corp','upload','dicts','selectType'], function(){
    var $ = bt.jquery,form = bt.form();
    var tt = bt.selectType.init();
    bt.billType = function(data){
        var btDom = data.elem;//得到select原始DOM对象
        var value = data.value;//得到被选中的value值
        var options = btDom.options;//得到下拉框所有的option的节点对象  注意：得到的options是一个对象数组
        var index = btDom.selectedIndex;//得到当前选中的option的索引
        var text = options[index].text;//得到当前选中的option的文本内容
        $("#billName").val(text);
    };
    var dicts = bt.dicts.init()
    bt.corp.init();
    var upload = layui.upload;
    layui.upload({
  	  url: basePath+'/attachment/sysattachment/upload'
  	  ,success: function(res, input){
  		  if(typeof res.success == 'boolean' && !res.success){
  			  layer.alert(bt.lang(res.msg));
  		  }else{
  		    var imgWidth =  $("#picWidth").val();
  		    var imgHeight = $("#picHeight").val();
  			$("#imger").attr("src", basePath+'/download/attachment?id='+res[0].id);
  			var html = "width:" + imgWidth + "px;" + "height:" + imgHeight + "px;";
  			$("#imger").attr("style", html);
  			$("#dir").attr("value",res[0].path);
  			$("#fileName").attr("value",res[0].id);
  		  }
  	  }
  });  
    //下拉树
    $.each($("div[type='selecttree']"),function(i,item){
        var filter = {"enable":true};
        if($(item).attr("dictField")){
            filter.idKey =  $(item).attr("dictField");
        }
        if($(item).attr("dictText")){
            filter.pIdKey =  $(item).attr("dictText");
        }
        bt.selectTree.init({
            url: $(item).attr("fieldHref"),
            elem:"[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            selected: $(item).attr("defaultVal"),
            setting:{
                data:{
                    simpleData: filter
                }
            }
        });
    });

    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/evisatype/evisatype/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(bt.navtab) {
                    bt.navtab.refreshCurrent();
                    bt.navtab.refreshParent();
                }else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });
});
