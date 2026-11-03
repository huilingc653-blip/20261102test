var dicts;
$("#fontId").hide();
bt.use(['form','jquery','selectType','dicts'],function(){
    $ = bt.jquery,form = bt.form();
	dicts = bt.dicts.init();//字典
	selectTypes=bt.selectType.init();
	parameters=new Object();
  //监听提交
    form.on('submit(submit)', function (data) {
      bt.loading.show();
        var action = "/accountReceivable/docManage/changeOrInvalidated";
        $.post(basePath + action,data.field, function (result) {
            bt.loading.hide();
            result.msg = bt.lang("submitted.successfully.pending.approval");
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2},function (){
            if (result.success) {
                  closeForms();
              }
            });

            return false;
        }, 'JSON');
        return false;
    });

});

bt.reasonTitleCallback = function(data){
        if("3" == data.value){
            $("#reasonContent").attr("lay-verify","required");
            $("#fontId").show();
            form.render();
        } else{
            $("#reasonContent").removeAttr('lay-verify');
            $("#fontId").hide();
        }
}

/**
 * 点击返回
 */
var closeForms = function () {
    parent.layer ? parent.layer.closeAll() : layer.closeAll();
};

var toChange = function (){
    var id = $('#id').val();
    var reasonTitle = dicts.get("reasonTitle").val();
    var reasonContent = $('#reasonContent').val();
    if(!reasonTitle || '' == reasonTitle.trim()){
        parent.layer.alert("调整原因不能为空！", {icon: 2});
        return false;
    }
    if(reasonTitle == 3 && '' == reasonContent.trim()){
        parent.layer.alert("说明不能为空！", {icon: 2});
        return false;
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('应收单变更'), basePath + "/accountReceivable/docManage/change?id=" + id + '&reasonTitle=' + reasonTitle + '&reasonContent=' + reasonContent);
        closeForms();
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/accountReceivable/docManage/change?id=" + id + '&reasonTitle=' + reasonTitle + '&reasonContent=' + reasonContent,
        end: function () {
            closeForms();
        }
    });
}



